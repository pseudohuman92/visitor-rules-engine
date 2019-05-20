import React from "react";
import { connect } from "react-redux";

import { mapDispatchToProps } from "../Redux/Store";
import { withFirebase } from "../Firebase";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import { Tooltip } from "@material-ui/core";

const mapStateToProps = state => {
  return {
    playerName: state.username,
    playerScrapyard: state.extendedGameState.game.player.scrapyard,
    opponentUserId: state.extendedGameState.game.opponent.userId,
    opponentName: state.extendedGameState.opponentUsername,
    opponentScrapyard: state.extendedGameState.game.opponent.scrapyard
  };
};

class PlayerDisplay extends React.Component {
  componentDidMount() {
    const {
      isPlayer,
      opponentName,
      opponentUserId,
      firebase,
      updateExtendedGameState
    } = this.props;
    if (!isPlayer && opponentName === "") {
      firebase.setOpponentUsername(opponentUserId, updateExtendedGameState);
    }
  }

  render() {
    const {
      isPlayer,
      playerName,
      opponentName,
      playerScrapyard,
      opponentScrapyard,
      updateExtendedGameState
    } = this.props;

    const name = isPlayer ? playerName : opponentName;
    const scrapyard = isPlayer ? playerScrapyard : opponentScrapyard;

    let showScrapyard = event => {
      updateExtendedGameState({
        dialog: {
          open: true,
          title: `${name}'s Scrapyard`,
          cards: scrapyard
        }
      });
    };

    return (
      <Tooltip title={scrapyard.length} placement="top">
        <img
          src={[process.env.PUBLIC_URL + "/img/Scrapyard.png"]}
          style={{
            maxWidth: "100%",
            maxHeight: "100%",
            transform: "rotate(" + (isPlayer ? 0 : 180) + "deg)"
          }}
          onClick={showScrapyard}
          alt=""
        />
      </Tooltip>
    );
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withHandlers(withFirebase(PlayerDisplay)));

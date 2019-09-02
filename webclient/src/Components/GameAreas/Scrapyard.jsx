import React from "react";
import { connect } from "react-redux";
import TextOnImage from "../Primitives/TextOnImage";

import { mapDispatchToProps } from "../Redux/Store";
import { withFirebase } from "../Firebase";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import { CardDisplay } from "../Card/CardDisplay";
import TextOnComponent from "../Primitives/TextOnComponent";

const mapStateToProps = state => {
  return {
    playerName: state.username,
    playerScrapyard: state.extendedGameState.game.player.scrapyard,
    opponentUserId: state.extendedGameState.game.opponent.userId,
    opponentName: state.extendedGameState.opponentUsername,
    opponentScrapyard: state.extendedGameState.game.opponent.scrapyard
  };
};

class Scrapyard extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hover: false };
  }

  toggleHover = () => {
    this.setState((state, props) => ({
      hover: !state.hover
    }));
  };

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
      updateExtendedGameState,
      style
    } = this.props;

    const { hover } = this.state;
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
    const hasCard = scrapyard.length > 0;

    return (
      <div
        onClick={showScrapyard}
        onMouseEnter={this.toggleHover}
        onMouseLeave={this.toggleHover}
        style={{width:"100%", ...style }}
      >
        {hasCard ? (
          <TextOnComponent
            text={hover ? scrapyard.length : ""}
            component={<CardDisplay {...scrapyard[scrapyard.length - 1]} />}
            compStyle={{ transform: "rotate(" + (isPlayer ? 0 : 180) + "deg)" }}
            scale={3}
          />
        ) : (
          <TextOnImage
            text={hover ? scrapyard.length : ""}
            src={process.env.PUBLIC_URL + "/img/Scrapyard.png"}
            imgStyle={{ transform: "rotate(" + (isPlayer ? 0 : 180) + "deg)" }}
            scale={3}
          />
        )}
      </div>
    );
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withHandlers(withFirebase(Scrapyard)));

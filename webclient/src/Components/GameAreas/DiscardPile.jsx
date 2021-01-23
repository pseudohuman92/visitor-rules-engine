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
    windowDimensions: state.windowDimensions,
    playerName: state.profile.username,
    playerDiscardPile: state.extendedGameState.game.player.discardPile,
    opponentUserId: state.extendedGameState.game.opponent.username,
    opponentName: state.extendedGameState.opponentUsername,
    opponentDiscardPile: state.extendedGameState.game.opponent.discardPile
  };
};

class DiscardPile extends React.Component {
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
      windowDimensions,
      isPlayer,
      playerName,
      opponentName,
      playerDiscardPile,
      opponentDiscardPile,
      updateExtendedGameState,
      style
    } = this.props;

    const {width } = windowDimensions;
    const scrapWidth = width / 10;
    const scrapHeight = scrapWidth * (88/63);
    const { hover } = this.state;
    const name = isPlayer ? playerName : opponentName;
    const discardPile = isPlayer ? playerDiscardPile : opponentDiscardPile;

    let showDiscardPile = event => {
      updateExtendedGameState({
        dialogData: {
          open: true,
          title: `${name}'s DiscardPile`,
          cards: discardPile
        }
      });
    };
    const hasCard = discardPile.length > 0;

    return (
      <div
        onClick={showDiscardPile}
        onMouseEnter={this.toggleHover}
        onMouseLeave={this.toggleHover}
        style={{...style, width: scrapWidth, height: scrapHeight}}
      >
        {hasCard ? (
          <TextOnComponent
            text={hover ? (discardPile.length?discardPile.length:0) : ""}
            component={
              <CardDisplay
                cardData={discardPile[discardPile.length - 1]}
                windowDimensions={windowDimensions}
                scale={0.75}
              />
            }
            compStyle={{ transform: "rotate(" + (isPlayer ? 0 : 180) + "deg)" }}
            scale={3}
            windowDimensions={windowDimensions}
          />
        ) : (
          <TextOnImage
            text={hover ? (discardPile.length?discardPile.length:0) : ""}
            src={process.env.PUBLIC_URL + "/img/discard-pile-grunge-white.png"}
            imgStyle={{ transform: "rotate(" + (isPlayer ? 0 : 180) + "deg)" }}
            scale={3}
            windowDimensions={windowDimensions}
          />
        )}
      </div>
    );
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withHandlers(withFirebase(DiscardPile)));

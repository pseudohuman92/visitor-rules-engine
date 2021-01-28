import React from "react";
import { Component } from "react";
import { connect } from "react-redux";
import TextOnImage from "../Primitives/TextOnImage";
import {cardBackURL} from "../Helpers/URLS";

const mapStateToProps = state => {
  return {
    windowDimensions: state.windowDimensions,
    playerDeckSize: state.extendedGameState.game.player.deckSize,
    opponentDeckSize: state.extendedGameState.game.opponent.deckSize
  };
};

class Deck extends Component {
  constructor(props) {
    super(props);
    this.state = { hover: false };
  }

  toggleHover = () => {
    this.setState((state, props) => ({
      hover: !state.hover
    }));
  };

  render() {
    const { playerDeckSize, opponentDeckSize, isPlayer, style, windowDimensions } = this.props;

    const { width } = windowDimensions;
    const deckWidth = width / 10;
    const deckHeight = deckWidth * (88/63);

    const { hover } = this.state;
    const deckSize = isPlayer ? playerDeckSize : opponentDeckSize;
    return (
      <div
        onMouseEnter={this.toggleHover}
        onMouseLeave={this.toggleHover}
        style={{ ...style, width: deckWidth, height: deckHeight}}
      >
        <TextOnImage
          text={hover ? (deckSize?deckSize:0) : ""}
          src={cardBackURL}
          imgStyle={{ transform: "rotate(" + (isPlayer ? 0 : 180) + "deg)" }}
          scale = {3}
          windowDimensions={windowDimensions}
        />
      </div>
    );
  }
}

export default connect(mapStateToProps)(Deck);

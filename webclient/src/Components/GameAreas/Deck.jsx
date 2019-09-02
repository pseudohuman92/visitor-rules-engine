import React from "react";
import { Component } from "react";
import { connect } from "react-redux";
import TextOnImage from "../Primitives/TextOnImage";

const mapStateToProps = state => {
  return {
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
    const { playerDeckSize, opponentDeckSize, isPlayer, style } = this.props;
    const { hover } = this.state;
    const deckSize = isPlayer ? playerDeckSize : opponentDeckSize;
    return (
      <div
        onMouseEnter={this.toggleHover}
        onMouseLeave={this.toggleHover}
        style={{ height: "100%", ...style }}
      >
        <TextOnImage
          text={hover ? deckSize : ""}
          src={process.env.PUBLIC_URL + "/img/CardBack.png"}
          imgStyle={{ transform: "rotate(" + (isPlayer ? 0 : 180) + "deg)" }}
          scale = {3}
        />
      </div>
    );
  }
}

export default connect(mapStateToProps)(Deck);

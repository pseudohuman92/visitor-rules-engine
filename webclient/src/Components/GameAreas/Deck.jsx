import React from "react";
import { Component } from "react";
import { connect } from "react-redux";
import { Tooltip } from "@material-ui/core";

const mapStateToProps = state => {
  return {
    playerDeckSize: state.extendedGameState.game.player.deckSize,
    opponentDeckSize: state.extendedGameState.game.opponent.deckSize
  };
};

class Deck extends Component {
  render() {
    const { playerDeckSize, opponentDeckSize, isPlayer } = this.props;
    const deckSize = isPlayer ? playerDeckSize : opponentDeckSize;
    return (
      <Tooltip title={deckSize} placement="top">
        <img
          src={[process.env.PUBLIC_URL + "/img/CardBack.png"]}
          style={{
            maxWidth: "100%",
            maxHeight: "100%",
            transform: "rotate(" + (isPlayer ? 0 : 180) + "deg)"
          }}
          alt=""
        />
      </Tooltip>
    );
  }
}

export default connect(mapStateToProps)(Deck);

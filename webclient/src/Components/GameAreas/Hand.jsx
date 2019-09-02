import React from "react";
import { Component } from "react";
import { connect } from "react-redux";

import PlayingCard from "../Card/PlayingCard";
import Fanner from "../Primitives/Fanner";

const mapStateToProps = state => {
  return {
    hand: state.extendedGameState.game.player.hand,
    opponentHandSize: state.extendedGameState.game.opponent.handSize
  };
};

class Hand extends Component {
  render() {
    const { hand, opponentHandSize, isPlayer } = this.props;
    return (
      <Fanner isPlayer={isPlayer} angle={15} style={{ width: "100%" }} maxNumItems={7}>
        {isPlayer
          ? hand.map(card => (
              <PlayingCard key={card.id} {...card} play={true} />
            ))
          : Array(opponentHandSize)
              .fill(null)
              .map((card, i) => (
                <img
                  key={i}
                  src={[process.env.PUBLIC_URL + "/img/CardBack.png"]}
                  style={{
                    maxWidth: "100%"
                  }}
                  alt=""
                />
              ))}
      </Fanner>
    );
  }
}

export default connect(mapStateToProps)(Hand);

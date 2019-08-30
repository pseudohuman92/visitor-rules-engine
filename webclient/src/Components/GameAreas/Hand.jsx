import React from "react";
import { Component } from "react";
import { connect } from "react-redux";

import PlayingCard from "../Card/PlayingCard";
import Fanner from "../Primitives/Fanner";

const mapStateToProps = state => {
  return {
    hand: state.extendedGameState.game.player.hand
  };
};

class Hand extends Component {
  render() {
    const { hand } = this.props;
    return (
      <Fanner
        isPlayer={true}
        angle={15}
        style={{ width: "100%" }}
        maxNumItems={7}
      >
        {hand.map(card => (
          <PlayingCard
            key={card.id}
            {...card}
            play={true}
          />
        ))}
      </Fanner>
    );
  }
}

export default connect(mapStateToProps)(Hand);

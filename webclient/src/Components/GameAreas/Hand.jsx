import React from "react";
import { Component } from "react";
import { connect } from "react-redux";
import { withSize } from "react-sizeme";

import PlayingCard from "../Card/PlayingCard";
import Fanner from "../Primitives/Fanner";
import ComponentStack from "../Primitives/ComponentStack";

const mapStateToProps = state => {
  return {
    hand: state.extendedGameState.game.player.hand
  };
};

class Hand extends Component {
  render() {
    const { hand, size } = this.props;
    const width = Math.min(size.width / hand.length, size.width / 5);
    return (
      <Fanner
        angle={15}
        elevation={size.height}
        width={size.width}
        style={{ width: "100%" }}
      >
        {hand.map(card => (
          <PlayingCard
            key={card.id}
            {...card}
            play={true}
            style={{
              width: width
            }}
          />
        ))}
      </Fanner>
    );
  }
}

export default connect(mapStateToProps)(withSize()(Hand));

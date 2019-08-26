import React from "react";
import { Component } from "react";
import { connect } from "react-redux";
import { withSize } from "react-sizeme";

import "../../css/Utils.css";
import Fanner from "../Primitives/ReverseFanner";

const mapStateToProps = state => {
  return {
    opponentHandSize: state.extendedGameState.game.opponent.handSize
  };
};

class OpponentHand extends Component {
  render() {
    const { opponentHandSize, size } = this.props;
    const width = Math.min(size.width / opponentHandSize, size.width / 5);
    return (
      <Fanner angle={-15} maxNumItems={7}>
        {Array(opponentHandSize)
          .fill(null)
          .map((card, i) => (
            <img
              key={i}
              src={[process.env.PUBLIC_URL + "/img/CardBack.png"]}
              style={{
                maxWidth: width,
                maxHeight: "100%"
              }}
              alt=""
            />
          ))}
      </Fanner>
    );
  }
}

export default connect(mapStateToProps)(withSize()(OpponentHand));

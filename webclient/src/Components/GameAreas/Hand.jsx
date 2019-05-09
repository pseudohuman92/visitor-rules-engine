import React from "react";
import { Component } from "react";
import GridList from "@material-ui/core/GridList";
import GridListTile from "@material-ui/core/GridListTile";
import { connect } from "react-redux";

import PlayingCard from "../Card/PlayingCard";
import "../../css/Board.css";
import "../../css/Utils.css";

const mapStateToProps = state => {
  return {
    hand: state.extendedGameState.game.player.hand
  };
};

class Hand extends Component {
  render() {
    return (
      <GridList
        cols={7.25}
        className="hand"
        style={{ flexWrap: "nowrap" }}
        cellHeight="auto"
      >
        {this.props.hand.map(card => (
          <GridListTile
            key={card.id}
            style={{ maxWidth: "100%", maxHeight: "100%" }}
          >
            <PlayingCard {...card} />
          </GridListTile>
        ))}
      </GridList>
    );
  }
}

export default connect(mapStateToProps)(Hand);

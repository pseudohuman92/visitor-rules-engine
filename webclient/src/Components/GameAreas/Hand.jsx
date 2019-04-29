import React from "react";
import { Component } from "react";

import GridList from "@material-ui/core/GridList";
import GridListTile from "@material-ui/core/GridListTile";

import PlayingCard from "../Card/PlayingCard";
import "../../css/Board.css";
import "../../css/Utils.css";

class Hand extends Component {
  render() {
    const {
      playableCards,
      selectableCards,
      selectedCards,
      studyableCards
    } = this.props;
    return (
      <GridList
        cols={7.25}
        className="hand"
        style={{ flexWrap: "nowrap" }}
        cellHeight="auto"
      >
        {this.props.cards.map(card => (
          <GridListTile
            key={card.id}
            style={{ maxWidth: "100%", maxHeight: "100%" }}
          >
            <PlayingCard
              playable={playableCards.includes(card.id)}
              activatable={false}
              selectable={selectableCards.includes(card.id)}
              selected={selectedCards.includes(card.id)}
              studyable={studyableCards.includes(card.id)}
              {...card}
            />
          </GridListTile>
        ))}
      </GridList>
    );
  }
}

export default Hand;

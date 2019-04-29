import React from "react";
import GridList from "@material-ui/core/GridList";
import GridListTile from "@material-ui/core/GridListTile";
import PlayingCard from "../Card/PlayingCard";
import "../../css/Stack.css";
import "../../css/Utils.css";

export default class Stack extends React.Component {
  render() {
    const {
      cards,
      selectedCards,
      selectableCards,
      targets,
      updateTargets
    } = this.props;

    return (
      <GridList
        cols={1}
        className="stack"
        cellHeight="auto"
        style={{ margin: -12 }}
      >
        {cards.slice(0).map(card => (
          <GridListTile key={card.id}>
            <PlayingCard
              {...card}
              small={true}
              activatable={false}
              playable={false}
              selectable={selectableCards.includes(card.id)}
              selected={selectedCards.includes(card.id)}
              studyable={false}
              targetd={targets.includes(card.id)}
              updateTargets={updateTargets}
            />
          </GridListTile>
        ))}
      </GridList>
    );
  }
}

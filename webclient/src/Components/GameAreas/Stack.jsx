import React from "react";
import GridList from "@material-ui/core/GridList";
import GridListTile from "@material-ui/core/GridListTile";
import { connect } from "react-redux";

import PlayingCard from "../Card/PlayingCard";
import "../../css/Stack.css";
import "../../css/Utils.css";

const mapStateToProps = state => {
  return { stack: state.extendedGameState.game.stack };
};

class Stack extends React.Component {
  render() {
    const { stack } = this.props;

    return (
      <GridList
        cols={1}
        className="stack"
        cellHeight="auto"
        style={{ margin: -12 }}
      >
        {stack.slice(0).map(card => (
          <GridListTile key={card.id}>
            <PlayingCard
              {...card}
              small={true}
            />
          </GridListTile>
        ))}
      </GridList>
    );
  }
}

export default connect(mapStateToProps)(Stack);
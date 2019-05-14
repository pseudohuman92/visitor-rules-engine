import React, {Component} from "react";
import { DropTarget } from "react-dnd";
import GridList from "@material-ui/core/GridList";
import GridListTile from "@material-ui/core/GridListTile";
import { connect } from "react-redux";

import PlayingCard from "../Card/PlayingCard";
import { ItemTypes } from "../Helpers/Constants";

import "../../css/Board.css";
import "../../css/Utils.css";

const boardSideTarget = {
  drop(props, monitor) {
    if (monitor.didDrop()) {
      return;
    }

    return { targetType: ItemTypes.FIELD, id: props.id };
  },

  canDrop(props, monitor) {
    const item = monitor.getItem();
    return item.sourceType === ItemTypes.CARD && item.playable;
  }
};

const mapStateToProps = state => {
  return {
    playerCards: state.extendedGameState.game.player.play,
    opponentCards: state.extendedGameState.game.opponent.play
  };
};

class BoardSide extends Component {
  render() {
    const {
      isPlayer,
      connectDropTarget,
      playerCards,
      opponentCards
    } = this.props;

    return connectDropTarget(
      <div style={{ height: "100%" }}>
        <GridList
          cols={6.25}
          className="board-side"
          style={{ flexWrap: "nowrap" }}
          cellHeight="auto"
        >
          {(isPlayer ? playerCards : opponentCards).map(card => (
            <GridListTile
              key={card.id}
              style={{ maxWidth: "100%", maxHeight: "100%" }}
            >
              <PlayingCard {...card} />
            </GridListTile>
          ))}
        </GridList>
      </div>
    );
  }
}

BoardSide = DropTarget(ItemTypes.CARD, boardSideTarget, (connect, monitor) => ({
  connectDropTarget: connect.dropTarget(),
  isOver: monitor.isOver(),
  isOverCurrent: monitor.isOver({ shallow: true }),
  canDrop: monitor.canDrop()
}))(BoardSide);

export default connect(mapStateToProps)(BoardSide);

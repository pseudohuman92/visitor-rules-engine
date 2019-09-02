import React, {Component} from "react";
import { DropTarget } from "react-dnd";
import { connect } from "react-redux";

import PlayingCard from "../Card/PlayingCard";
import { ItemTypes } from "../Helpers/Constants";

import "../../css/Utils.css";
import { withSize } from "react-sizeme";

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
    const {width} = this.props.size;
    const cards = isPlayer ? playerCards : opponentCards;

    return connectDropTarget(
      <div style={{ height: "100%", display: "flex", justifyContent: "center", alignItems: isPlayer?"flex-end":"flex-start"}}>
          {cards.map((card, i) => (
            <div
              key={i}
              style={{width: Math.min(width / cards.length, width / 10), margin:"1%"}}
            >
              <PlayingCard square {...card} play/>
            </div>
          ))}
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

export default connect(mapStateToProps)(withSize()(BoardSide));

import React from "react";
import { DragSource, DropTarget } from "react-dnd";
import { connect } from "react-redux";

import CardDisplay from "./CardDisplay";
import { ItemTypes } from "../Constants/Constants";
import { cardSource, cardTarget } from "./PlayingCardDnd";

import proto from "../../protojs/compiled.js";
import { mapDispatchToProps } from "../Redux/Store";
import { withHandlers } from "../MessageHandlers/HandlerContext";


const mapStateToProps = state => {
  return {
    playableCards: state.extendedGameState.game.canPlay,
    studyableCards : state.extendedGameState.game.canStudy,
    activatableCards : state.extendedGameState.game.canStudy,
    selectedCards: state.extendedGameState.selectedCards,
    selectableCards: state.extendedGameState.selectableCards,
    displayTargets: state.extendedGameState.targets
  };
};

export class PlayingCard extends React.Component {

  onMouseEnter = event => {
    this.props.updateExtendedGameState({ targets: this.props.targets });
  };

  onMouseLeave = event => {
    this.props.updateExtendedGameState({ targets: [] });
  };

  render() {
    const {
      id,
      depleted,
      isOver,
      canDrop,
      isDragging,
      connectDragSource,
      selectableCards,
      selectedCards,
      displayTargets,
      activatableCards,
      playableCards,
      gameMessage
    } = this.props;

    const activatable = activatableCards.includes(id);
    const playable = playableCards.includes(id);
    const selectable = selectableCards.includes(id);
    const selected = selectedCards.includes(id);
    const targeted = displayTargets.includes(id);

    var opacity = 1,
      borderColor = "black";
    let clickHandler = undefined;
    if (isDragging) {
      opacity = 0.5;
      borderColor = "yellow";
    } else if (canDrop && isOver) {
      borderColor = "red";
    } else if (targeted) {
      borderColor = "yellow";
    } else if (selected) {
      borderColor = "magenta";
      clickHandler = event => {
        gameMessage.UnselectCard(id);
      };
    } else if (selectable) {
      clickHandler = event => {
        gameMessage.SelectCard(id);
      };
      borderColor = "green";
    } else if (activatable) {
      clickHandler = event => {
        gameMessage.ActivateCard(id);
      };
      borderColor = "blue";
    } else if (playable) {
      borderColor = "blue";
    } else if (depleted) {
      opacity = 0.5;
    }

    const counterMap = {};
    counterMap[proto.Counter.CHARGE] = "C";

    return connectDragSource(
      <div
        onClick={clickHandler}
        onMouseEnter={this.onMouseEnter}
        onMouseLeave={this.onMouseLeave}
      >
        <CardDisplay
          opacity={opacity}
          borderColor={borderColor}
          {...this.props}
        />
      </div>
    );
  }
}

PlayingCard = DragSource(ItemTypes.CARD, cardSource, (connect, monitor) => ({
  connectDragSource: connect.dragSource(),
  isDragging: monitor.isDragging()
}))(PlayingCard);

PlayingCard = DropTarget(ItemTypes.CARD, cardTarget, (connect, monitor) => ({
  connectDropTarget: connect.dropTarget(),
  isOver: monitor.isOver(),
  canDrop: monitor.canDrop()
}))(PlayingCard);

export default connect(mapStateToProps, mapDispatchToProps)(withHandlers (PlayingCard));

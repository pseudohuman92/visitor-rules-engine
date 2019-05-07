import React from "react";
import { Component } from "react";
import { DropTarget } from "react-dnd";
import { connect } from "react-redux";

import { ItemTypes, GamePhases } from "../Constants/Constants";
import "../../css/StudyArea.css";

const altarTarget = {
  drop(props, monitor) {
    return { targetType: ItemTypes.ALTAR };
  },

  canDrop(props, monitor) {
    const item = monitor.getItem();
    return item.sourceType === ItemTypes.CARD && item.studyable;
  }
};

const mapStateToProps = state => {
  return { phase: state.extendedGameState.phase, game: state.extendedGameState.game };
};

class Altar extends Component {
  render() {
    const { connectDropTarget, phase, game } = this.props;
    const hasStudyable =
      phase === GamePhases.UPDATE_GAME &&
      game.activePlayer === game.player.name &&
      game.canStudy.length > 0;

    const style = {};
    if (hasStudyable) {
      style.backgroundColor = "green";
      style.height = "100%";
    } else {
      style.backgroundColor = "red";
      style.height = "20%";
    }

    return connectDropTarget(
      <div style={{ height: "100%" }}>
        <section id="studyArea" className="study-area" />
      </div>
    );
  }
}

Altar = DropTarget(ItemTypes.CARD, altarTarget, (connect, monitor) => ({
  connectDropTarget: connect.dropTarget(),
  isOver: monitor.isOver(),
  canDrop: monitor.canDrop()
}))(Altar);

export default connect(mapStateToProps)(Altar);

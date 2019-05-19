import React from "react";
import { Component } from "react";
import { DropTarget } from "react-dnd";
import { connect } from "react-redux";

import { ItemTypes, GamePhases } from "../Helpers/Constants";
import "../../css/StudyArea.css";

const studyAreaTarget = {
  drop(props, monitor) {
    return { targetType: ItemTypes.ALTAR };
  },

  canDrop(props, monitor) {
    const item = monitor.getItem();
    return item.sourceType === ItemTypes.CARD && item.studyable;
  }
};

const mapStateToProps = state => {
  return {
    phase: state.extendedGameState.phase,
    game: state.extendedGameState.game,
  };
};

class StudyArea extends Component {
  
  render() {
    const { connectDropTarget, phase, game } = this.props;
    const hasStudyable =
      phase === GamePhases.UPDATE_GAME &&
      game.activePlayer === game.player.userId &&
      game.canStudy.length > 0;

    const style = {height: "100%"};
    if (hasStudyable) {
      style.backgroundColor = "#adea99";
    } else {
      style.backgroundColor = "red";
    }

    return connectDropTarget(
      <div style={{ height: "100%" }}>
        <section id="studyArea" className="study-area" style={style}/>
      </div>
    );
  }
}

StudyArea = DropTarget(ItemTypes.CARD, studyAreaTarget, (connect, monitor) => ({
  connectDropTarget: connect.dropTarget(),
  isOver: monitor.isOver(),
  canDrop: monitor.canDrop()
}))(StudyArea);

export default connect(mapStateToProps)(StudyArea);

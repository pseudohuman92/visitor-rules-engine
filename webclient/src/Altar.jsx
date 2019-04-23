import React from "react";
import { Component } from "react";
import { DropTarget } from "react-dnd";

import Paper from "@material-ui/core/Paper";

import { ItemTypes } from "./Constants.js";

const altarTarget = {
  drop(props, monitor) {
    return { targetType: ItemTypes.ALTAR };
  },

  canDrop(props, monitor) {
    const item = monitor.getItem();
    return item.sourceType === ItemTypes.CARD && item.studyable;
  }
};

class Altar extends Component {
  render() {
    const { connectDropTarget, canDrop, hasStudyable } = this.props;
    const style = {};
    if (hasStudyable) {
      style.backgroundColor = "green";
      style.height = "100%";
    } else {
      style.backgroundColor = "red";
      style.height = "20%";
    }
    /*
    if (isOver && canDrop) {
      style.border = "5px red solid";
    }
    */

    return connectDropTarget(
      <div style={{ height: "100%" }}>
        <Paper style={style} />
      </div>
    );
  }
}

Altar = DropTarget(ItemTypes.CARD, altarTarget, (connect, monitor) => ({
  connectDropTarget: connect.dropTarget(),
  isOver: monitor.isOver(),
  canDrop: monitor.canDrop()
}))(Altar);

export default Altar;

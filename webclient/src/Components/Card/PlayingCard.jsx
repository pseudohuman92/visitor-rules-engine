import React from "react";
import { DragSource, DropTarget } from "react-dnd";
import CardDisplay from "./CardDisplay";

import { ItemTypes, FieldIDs } from "../../Constants/Constants";
import {
  PlayCard,
  ActivateCard,
  UnselectCard,
  SelectCard,
  StudyCard
} from "../Game/Game";

import proto from "../../protojs/compiled.js";

const cardSource = {
  beginDrag(props) {
    return {
      sourceType: ItemTypes.CARD,
      playable: props.playable,
      studyable: props.studyable
    };
  },

  canDrag(props) {
    return props.playable || props.studyable;
  },

  endDrag(props, monitor) {
    if (!monitor.didDrop()) {
      return;
    }

    const targetProps = monitor.getDropResult();
    if (
      props.playable &&
      (targetProps.targetType === ItemTypes.FIELD &&
        targetProps.id === FieldIDs.MY_FIELD)
    ) {
      PlayCard(props.id);
    } else if (targetProps.targetType === ItemTypes.ALTAR) {
      StudyCard(props.id);
    }
  }
};

const cardTarget = {
  drop(props, monitor) {
    if (monitor.didDrop()) {
      return;
    }

    return { targetType: ItemTypes.CARD, id: props.id };
  }
};

export class PlayingCard extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      updateTargets: props.updateTargets,
      targets: props.targets
    };
  }

  componentDidUpdate(prevProps) {
    // Typical usage (don't forget to compare props):
    if (this.props.targets !== prevProps.targets) {
      this.setState({ targets: this.props.targets });
    }
  }

  onMouseEnter = event => {
    if (this.state.updateTargets) {
      this.state.updateTargets(this.state.targets);
    }
  };

  onMouseLeave = event => {
    if (this.state.updateTargets) {
      this.state.updateTargets([]);
    }
  };

  render() {
    const {
      id,
      depleted,
      activatable,
      playable,
      isOver,
      canDrop,
      isDragging,
      connectDragSource,
      selectable,
      selected,
      targeted
    } = this.props;

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
        UnselectCard(id);
      };
    } else if (selectable) {
      clickHandler = event => {
        SelectCard(id);
      };
      borderColor = "green";
    } else if (activatable) {
      clickHandler = event => {
        ActivateCard(id);
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
          onMouseLeave={this.onMouseLeave}>
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

export default PlayingCard;

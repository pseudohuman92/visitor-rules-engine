import React from 'react';

import {DragSource, DropTarget} from 'react-dnd';

import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';

import {ItemTypes} from './Constants.js';
import './PlayingCard.css';

const cardSource = {
  beginDrag(props) {
    return {};
  },

  endDrag(props, monitor) {
    if (!monitor.didDrop()) {
      return;
    }

    const targetProps = monitor.getDropResult();
    console.log(targetProps);
    alert(
      `You dragged card ${props.id} to ${targetProps.targetType} ${
        targetProps.props.id
      }`,
    );
  },
};

const cardTarget = {
  drop(props, monitor) {
    if (monitor.didDrop()) {
      return;
    }

    return {targetType: ItemTypes.CARD, props: props};
  },
};

export class PlayingCard extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      id: props.id,
      name: props.name,
      description: 'Some shit',
    };
  }

  render() {
    const {
      isOver,
      canDrop,
      isDragging,
      connectDragSource,
      connectDropTarget,
    } = this.props;

    var opacity = 1,
      border = 'none';
    if (isDragging) {
      opacity = 0.5;
      border = '5px blue solid';
    } else {
      if (canDrop && isOver) {
        border = '5px red solid';
      }
    }
    return connectDropTarget(
      connectDragSource(
        <div>
          <Card
            style={{
              opacity: opacity,
              border: border,
            }}>
            <CardHeader title={this.state.name} />
            <CardContent>{this.state.description}</CardContent>
          </Card>
        </div>,
      ),
    );
  }
}

PlayingCard = DragSource(ItemTypes.CARD, cardSource, (connect, monitor) => ({
  connectDragSource: connect.dragSource(),
  isDragging: monitor.isDragging(),
}))(PlayingCard);

PlayingCard = DropTarget(ItemTypes.CARD, cardTarget, (connect, monitor) => ({
  connectDropTarget: connect.dropTarget(),
  isOver: monitor.isOver(),
  canDrop: monitor.canDrop(),
}))(PlayingCard);

export default PlayingCard;

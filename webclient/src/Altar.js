import React from 'react';

import {DropTarget} from 'react-dnd';

import Paper from '@material-ui/core/Paper';

import {ItemTypes} from './Constants.js';
import './Altar.css';

const altarTarget = {
  drop(props, monitor) {
    return {targetType: ItemTypes.ALTAR};
  },
};

class Altar extends React.Component {
  render() {
    const {isOver, connectDropTarget} = this.props;
    const style = {};
    if (isOver) {
      style.border = '5px red solid';
    }

    return connectDropTarget(
      <div style={{height: '100%'}}>
        <Paper className="altar material-override" style={style} />
      </div>,
    );
  }
}

Altar = DropTarget(ItemTypes.CARD, altarTarget, (connect, monitor) => ({
  connectDropTarget: connect.dropTarget(),
  isOver: monitor.isOver(),
}))(Altar);

export default Altar;

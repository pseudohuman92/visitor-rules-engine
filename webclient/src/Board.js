import React from 'react';

import {DropTarget} from 'react-dnd';

import Grid from '@material-ui/core/Grid';

import PlayingCard from './PlayingCard.js';
import {ItemTypes} from './Constants.js';
import './Board.css';
import './Board.css';
import './Utils.css';

// From now on field is an alias for board side.
const fieldTarget = {
  drop(props, monitor) {
    if (monitor.didDrop()) {
      return;
    }

    return {targetType: ItemTypes.FIELD, props: props};
  },
};

class BoardSide extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      myField: props.myField,
      cards: props.cards,
    };
  }

  render() {
    const {isOver, isOverCurrent, canDrop, connectDropTarget} = this.props;

    var style = {};
    if (canDrop && isOverCurrent) {
      style.border = '5px red solid';
    }

    return connectDropTarget(
      <div style={{height: '100%'}}>
        <Grid container spacing={0} style={style} className="board-side">
          {this.state.cards.map(card => (
            <Grid item xs={1} key={card.id}>
              <PlayingCard {...card} />
            </Grid>
          ))}
        </Grid>
      </div>,
    );
  }
}

BoardSide = DropTarget(ItemTypes.CARD, fieldTarget, (connect, monitor) => ({
  connectDropTarget: connect.dropTarget(),
  isOver: monitor.isOver(),
  isOverCurrent: monitor.isOver({shallow: true}),
  canDrop: monitor.canDrop(),
}))(BoardSide);

class Hand extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      cards: props.cards,
    };
  }

  render() {
    return (
      <Grid container spacing={0} className="hand">
        {this.state.cards.map(card => (
          <Grid item xs={1} key={card.id}>
            <PlayingCard {...card} />
          </Grid>
        ))}
      </Grid>
    );
  }
}

export default class Board extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      myCards: props.myCards,
      garyCards: props.garyCards,
      hand: props.hand,
    };
  }

  render() {
    return (
      <Grid
        container
        direction="column"
        spacing={0}
        style={{
          margin: '-12px 0px',
          height: '100%',
        }}>
        <Grid item xs={4} className="grid-col-item no-max-width">
          <BoardSide id="gary" myField={false} cards={this.state.garyCards} />
        </Grid>
        <Grid item xs={4} className="grid-col-item no-max-width">
          <BoardSide id="me" myField={true} cards={this.state.myCards} />
        </Grid>
        <Grid item xs={4} className="grid-col-item no-max-width">
          <Hand cards={this.state.hand} />
        </Grid>
      </Grid>
    );
  }
}

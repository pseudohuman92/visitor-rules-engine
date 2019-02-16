import React from 'react';

import {DropTarget} from 'react-dnd';

import Grid from '@material-ui/core/Grid';

import PlayingCard from './PlayingCard.js';
import {ItemTypes, FieldIDs} from './Constants.js';
import './Board.css';
import './Board.css';
import './Utils.css';

// From now on field is an alias for board side.
const fieldTarget = {
  drop(props, monitor) {
    if (monitor.didDrop()) {
      return;
    }

    return {targetType: ItemTypes.FIELD, id: props.id};
  },

  canDrop(props, monitor) {
    return props.id !== FieldIDs.GARY_FIELD;
  },
};

class BoardSide extends React.Component {
  render() {
    const {
      id,
      cards,
      isOverCurrent,
      canDrop,
      connectDropTarget,
      amActive,
    } = this.props;
    const selectCandIDs = this.props.selectCands.map(card => card.id);

    var style = {};
    if (canDrop && isOverCurrent) {
      style.border = '5px red solid';
    }

    return connectDropTarget(
      <div style={{height: '100%'}}>
        <Grid container spacing={0} style={style} className="board-side">
          {cards.map(card => (
            <Grid item xs={1} key={card.id}>
              <PlayingCard
                playable={false}
                activatable={amActive}
                selectable={selectCandIDs.includes(card.id)}
                {...card}
              />
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
  render() {
    const amActive = this.props.amActive;
    const selectCandIDs = this.props.selectCands.map(card => card.id);
    return (
      <Grid container spacing={0} className="hand">
        {this.props.cards.map(card => (
          <Grid item xs={1} key={card.id}>
            <PlayingCard
              playable={amActive}
              activatable={false}
              selectable={selectCandIDs.includes(card.id)}
              {...card}
            />
          </Grid>
        ))}
      </Grid>
    );
  }
}

export default class Board extends React.Component {
  //constructor(props) {
  //  super(props);
  //  this.state = {
  //    myCards: props.myCards,
  //    garyCards: props.garyCards,
  //    hand: props.hand,
  //  };
  //}

  render() {
    const myPlayCards = this.props.game.player.play;
    const myHandCards = this.props.game.player.hand;
    const garyPlayCards = this.props.game.opponent.play;
    const selectCands = this.props.selectCands;
    const amActive =
      this.props.game.activePlayer === this.props.game.player.name;

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
          <BoardSide
            id={FieldIDs.GARY_FIELD}
            cards={garyPlayCards}
            selectCands={selectCands}
            amActive={false}
          />
        </Grid>
        <Grid item xs={4} className="grid-col-item no-max-width">
          <BoardSide
            id={FieldIDs.MY_FIELD}
            cards={myPlayCards}
            selectCands={selectCands}
            amActive={amActive}
          />
        </Grid>
        <Grid item xs={4} className="grid-col-item no-max-width">
          <Hand
            cards={myHandCards}
            selectCands={selectCands}
            amActive={amActive}
          />
        </Grid>
      </Grid>
    );
  }
}

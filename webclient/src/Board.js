import React from 'react';

import {DropTarget} from 'react-dnd';

import Grid from '@material-ui/core/Grid';
import GridList from '@material-ui/core/GridList';
import GridListTile from '@material-ui/core/GridListTile';

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
    const item = monitor.getItem();
    return (
      item.sourceType === ItemTypes.CARD &&
      item.playable &&
      props.id !== FieldIDs.GARY_FIELD
    );
  },
};

class BoardSide extends React.Component {
  render() {
    const {
      cards,
      isOverCurrent,
      canDrop,
      connectDropTarget,
      activatableCards,
      selectableCards,
      selectedCards,
    } = this.props;
    var style = {};
    if (canDrop && isOverCurrent) {
      style.border = '5px red solid';
    }

    return connectDropTarget(
      <div style={{height: '100%'}}>
        <GridList
          cols={6}
          className="board-side"
          style={{flexWrap: 'nowrap', ...style}}>
          {cards.map(card => (
            <GridListTile key={card.id} style={{height: '100%'}}>
              <PlayingCard
                playable={false}
                activatable={activatableCards.includes(card.id)}
                selectable={selectableCards.includes(card.id)}
                selected={selectedCards.includes(card.id)}
                studyable={false}
                {...card}
              />
            </GridListTile>
          ))}
        </GridList>
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
    const {
      playableCards,
      selectableCards,
      selectedCards,
      studyableCards,
    } = this.props;
    return (
      <GridList cols={6} className="hand" style={{flexWrap: 'nowrap'}}>
        {this.props.cards.map(card => (
          <GridListTile key={card.id} style={{height: '100%'}}>
            <PlayingCard
              playable={playableCards.includes(card.id)}
              activatable={false}
              selectable={selectableCards.includes(card.id)}
              selected={selectedCards.includes(card.id)}
              studyable={studyableCards.includes(card.id)}
              {...card}
            />
          </GridListTile>
        ))}
      </GridList>
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
    let activatableCards = this.props.game.canActivate;
    let playableCards = this.props.game.canPlay;
    let studyableCards = this.props.game.canStudy;
    const selectableCards = this.props.selectableCards;
    const selectedCards = this.props.selectedCards;
    const amActive =
      this.props.game.player.name === this.props.game.activePlayer;
    if (selectableCards.length > 0 || !amActive) {
      activatableCards = [];
      playableCards = [];
      studyableCards = [];
    }

    return (
      <Grid
        container
        spacing={0}
        style={{
          margin: '-12px 0px',
          height: '100%',
        }}>
        <Grid item xs={12} style={{height: '32%'}}>
          <BoardSide
            id={FieldIDs.GARY_FIELD}
            cards={garyPlayCards}
            selectableCards={selectableCards}
            selectedCards={selectedCards}
            activatableCards={[]}
          />
        </Grid>
        <Grid item xs={12} style={{height: '32%'}}>
          <BoardSide
            id={FieldIDs.MY_FIELD}
            cards={myPlayCards}
            selectableCards={selectableCards}
            selectedCards={selectedCards}
            activatableCards={activatableCards}
          />
        </Grid>
        <Grid item xs={12} style={{height: '32%'}}>
          <Hand
            cards={myHandCards}
            selectableCards={selectableCards}
            selectedCards={selectedCards}
            playableCards={playableCards}
            studyableCards={studyableCards}
          />
        </Grid>
      </Grid>
    );
  }
}

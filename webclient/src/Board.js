import React from 'react';
import Grid from '@material-ui/core/Grid';
import PlayingCard from './PlayingCard.js';
import './Board.css';
import './Board.css';
import './Utils.css';

export class BoardSide extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      cards: props.cards,
    };
  }

  render() {
    return (
      <Grid container spacing={0}>
        {this.state.cards.map(card => (
          <Grid item xs={1} key={card.id}>
            <PlayingCard {...card} />
          </Grid>
        ))}
      </Grid>
    );
  }
}

export class Hand extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      cards: props.cards,
    };
  }

  render() {
    return (
      <Grid container spacing={8} style={{padding: 16}}>
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
        spacing={24}
        style={{
          padding: '0px 12px',
          height: '100%',
        }}>
        <Grid item xs={4} className="grid-col-item no-max-width board-side">
          <BoardSide cards={this.state.garyCards} />
        </Grid>
        <Grid item xs={4} className="grid-col-item no-max-width board-side">
          <BoardSide cards={this.state.myCards} />
        </Grid>
        <Grid item xs={4} className="grid-col-item no-max-width hand">
          <Hand cards={this.state.hand} />
        </Grid>
      </Grid>
    );
  }
}

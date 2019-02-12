import React from 'react';
import Grid from '@material-ui/core/Grid';
import PlayingCard from './PlayingCard.js';
import './Board.css';

export default class BoardSide extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      player: props.player,
      cards: props.cards,
    };
  }

  render() {
    return (
      <div className="BoardSide">
        <Grid
          container
          spacing={24}
          style={{
            padding: 24,
          }}>
          {this.state.cards.map(card => (
            <Grid item xs={1} key={card.id}>
              <PlayingCard {...card} />
            </Grid>
          ))}
        </Grid>
      </div>
    );
  }
}

export class Board extends React.Component {
  render() {
    return <div className="Card" />;
  }
}

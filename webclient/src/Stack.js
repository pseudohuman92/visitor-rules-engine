import React from 'react';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import PlayingCard from './PlayingCard.js';
import './Stack.css';

export default class Stack extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      cards: props.cards,
    };
  }

  render() {
    return (
      <div className="Stack">
        <Grid
          container
          spacing={24}
          style={{
            padding: 24,
          }}>
          {this.state.cards.map(card => (
            <Grid item key={card.id} xs={12}>
              <PlayingCard {...card} />
            </Grid>
          ))}
        </Grid>
      </div>
    );
  }
}

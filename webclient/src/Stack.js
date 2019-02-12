import React from 'react';
import Grid from '@material-ui/core/Grid';
import PlayingCard from './PlayingCard.js';
import './Stack.css';
import './Utils.css';

export default class Stack extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      cards: props.cards,
    };
  }

  render() {
    return (
      <Grid
        container
        spacing={24}
        style={{
          padding: 0,
        }}
        className="stack"
        direction="column">
        {this.state.cards.map(card => (
          <Grid
            item
            key={card.id}
            xs={1}
            className="grid-col-item no-max-width">
            <PlayingCard {...card} />
          </Grid>
        ))}
      </Grid>
    );
  }
}

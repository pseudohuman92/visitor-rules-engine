import React from 'react';
import GridList from '@material-ui/core/GridList';
import GridListTile from '@material-ui/core/GridListTile';
import PlayingCard from './PlayingCard.js';
import './Stack.css';
import './Utils.css';

export default class Stack extends React.Component {
  render() {
    const {cards, selectedCards, selectableCards} = this.props;

    return (
      <GridList
        cols={1}
        className="stack"
        cellHeight="auto"
        style={{margin: -12}}>
        {cards
          .slice(0)
          .reverse()
          .map(card => (
            <GridListTile key={card.id}>
              <PlayingCard
                {...card}
                activatable={false}
                playable={false}
                selectable={selectableCards.includes(card.id)}
                selected={selectedCards.includes(card.id)}
                studyable={false}
              />
            </GridListTile>
          ))}
      </GridList>
    );
  }
}

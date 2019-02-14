import React, {Component} from 'react';

import HTML5Backend from 'react-dnd-html5-backend';
import {DragDropContextProvider} from 'react-dnd';

import Grid from '@material-ui/core/Grid';

import Board from './Board.js';
import Stack from './Stack.js';
import StateDisplay from './StateDisplay.js';
import {
  GamePhases,
  SetBasicGameInfo,
  RegisterUpdateViewHandler,
} from './Game.js';
import './App.css';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      game: {},
      phase: GamePhases.NOT_STARTED,
    };

    const cards = 'abcdefghijklmnopqrstuvwxyz'.split('').map(l => ({
      id: l,
      name: l,
      counters: [],
      depleted: false,
      marked: false,
      targets: [],
    }));
    const myHandCards = cards.slice(0, 3);
    const garyHandSize = 4;
    const myScrapCards = cards.slice(7, 9);
    const garyScrapCards = cards.slice(9, 10);
    const myVoidCards = cards.slice(10, 12);
    const garyVoidCards = [];
    const myPlayCards = cards.slice(12, 16);
    const garyPlayCards = cards.slice(16, 19);
    const stackCards = cards.slice(19, 23);

    const me = {
      id: 'me',
      name: 'me',
      deckSize: 45,
      energy: 3,
      maxEnergy: 7,
      play: myPlayCards,
      handSize: myHandCards.length,
      hand: myHandCards,
      scrapyard: myScrapCards,
      void: myVoidCards,
      knowledgePool: [
        {
          knowledgeType: 1,
          count: 3,
        },
        {
          knowledgeType: 3,
          count: 2,
        },
      ],
    };

    const gary = {
      id: 'gary',
      name: 'gary',
      deckSize: 39,
      energy: 2,
      maxEnergy: 12,
      play: garyPlayCards,
      handSize: garyHandSize,
      scrapyard: garyScrapCards,
      void: garyVoidCards,
      knowledgePool: [
        {
          knowledgeType: 1,
          count: 6,
        },
        {
          knowledgeType: 3,
          count: 2,
        },
        {
          knowledgeType: 4,
          count: 1,
        },
      ],
    };

    this.state.game = {
      id: 'best game',
      player: me,
      opponent: gary,
      turnPlayer: me.id,
      activePlayer: me.id,
      stackCards: stackCards,
    };

    SetBasicGameInfo('best game', me.id, gary.id);
  }

  updateView(gameState, phase) {
    if (phase) {
      this.setState({game: gameState, phase: phase});
    } else {
      this.setState({game: gameState});
    }
  }

  render() {
    return (
      <DragDropContextProvider backend={HTML5Backend}>
        <div className="App">
          <header className="App-header">
            <Grid
              container
              spacing={24}
              style={{
                padding: '12px 24px',
                height: '100vh',
              }}
              justify="space-between">
              <Grid item xs={2} className="display-col">
                <StateDisplay game={this.state.game} phase={this.state.phase} />
              </Grid>
              <Grid item xs={9} className="display-col">
                <Board game={this.state.game} phase={this.state.phase} />
              </Grid>
              <Grid item xs={1} className="display-col">
                <Stack cards={this.state.game.stackCards} />
              </Grid>
            </Grid>
          </header>
        </div>
      </DragDropContextProvider>
    );
  }
}

export default App;

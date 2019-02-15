import React, {Component} from 'react';

import Grid from '@material-ui/core/Grid';

import Board from './Board.js';
import Stack from './Stack.js';
import StateDisplay from './StateDisplay.js';
import ChooseDialog from './ChooseDialog.js';
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
      dialog: {title: '', cards: [], open: false},
      selectCands: [],
      selectCount: 0,
    };

    const me = {
      id: 'me',
      name: 'me',
      deckSize: 0,
      energy: 0,
      maxEnergy: 0,
      play: [],
      hand: [],
      scrapyard: [],
      void: [],
      knowledgePool: [],
    };

    const gary = {
      id: 'gary',
      name: 'gary',
      deckSize: 0,
      energy: 0,
      maxEnergy: 0,
      play: [],
      handSize: 0,
      hand: [],
      scrapyard: [],
      void: [],
      knowledgePool: [],
    };

    this.state.game = {
      id: 'best game',
      player: me,
      opponent: gary,
      turnPlayer: me.id,
      activePlayer: me.id,
      stack: [],
    };

    SetBasicGameInfo('best game', me.id, gary.id);
    RegisterUpdateViewHandler(this.updateView.bind(this));
  }

  setDummyData(state) {
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
      hand: myHandCards,
      scrapyard: myScrapCards,
      void: myVoidCards,
      knowledgePool: [
        {
          knowledge: 1,
          count: 3,
        },
        {
          knowledge: 3,
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
          knowledge: 1,
          count: 6,
        },
        {
          knowledge: 3,
          count: 2,
        },
        {
          knowledge: 4,
          count: 1,
        },
      ],
    };

    state.game = {
      id: 'best game',
      player: me,
      opponent: gary,
      turnPlayer: me.id,
      activePlayer: me.id,
      stack: stackCards,
    };
  }

  updateView(params, phase, dialog = null) {
    const game = params.game;
    const toUpdate = {game: game, phase: phase};
    if (this.state.selectCands.length !== 0) {
      // Reset select cands if we get another message.
      // TODO In the future, this reset should go under the ack of the
      // selection responses.
      toUpdate['selectCands'] = [];
      toUpdate['selectCount'] = 0;
    }
    if (dialog !== null) {
      toUpdate['dialog'] = dialog;
    }

    if (
      phase === GamePhases.SELECT_FROM_LIST ||
      phase === GamePhases.SELECT_FROM_SCRAPYARD ||
      phase === GamePhases.SELECT_FROM_VOID
    ) {
      toUpdate['dialog'] = {
        open: true,
        title: `Select ${params.selectionCount} from the following`,
        cards: params.candidates,
      };
      toUpdate['selectCands'] = params.candidates;
      toUpdate['selectCount'] = params.selectionCount;
    } else if (
      phase === GamePhases.SELECT_FROM_PLAY ||
      phase === GamePhases.SELECT_FROM_HAND
    ) {
      toUpdate['selectCands'] = params.candidates;
      toUpdate['selectCount'] = params.selectionCount;
    }
    this.setState(toUpdate);
  }

  updateDialog = (open, title, cards) => {
    this.setState({
      dialog: {
        open: open,
        title: title,
        cards: cards,
      },
    });
  };

  render() {
    const dialog = this.state.dialog;
    const selectCands = this.state.selectCands;
    const chooseDialog = (
      <ChooseDialog
        title={dialog.title}
        cards={dialog.cards}
        open={dialog.open}
        selectCands={selectCands}
        onClose={event => {
          this.setState({dialog: {...this.state.dialog, open: false}});
        }}
      />
    );

    return (
      <div className="App">
        <header className="App-header">
          {chooseDialog}
          <Grid
            container
            spacing={24}
            style={{
              padding: '12px 24px',
              height: '100vh',
            }}
            justify="space-between">
            <Grid item xs={2} className="display-col">
              <StateDisplay
                game={this.state.game}
                phase={this.state.phase}
                updateDialog={this.updateDialog}
              />
            </Grid>
            <Grid item xs={9} className="display-col">
              <Board
                game={this.state.game}
                phase={this.state.phase}
                selectCands={selectCands}
              />
            </Grid>
            <Grid item xs={1} className="display-col">
              <Stack cards={this.state.game.stack} />
            </Grid>
          </Grid>
        </header>
      </div>
    );
  }
}

export default App;

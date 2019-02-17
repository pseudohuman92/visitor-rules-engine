import React, {Component} from 'react';

import Grid from '@material-ui/core/Grid';

import proto from './protojs/compiled.js';

import Board from './Board.js';
import Stack from './Stack.js';
import Altar from './Altar.js';
import StateDisplay from './StateDisplay.js';
import ChooseDialog from './ChooseDialog.js';
import InfoEntryDialog from './InfoEntryDialog.js';
import LoadingDialog from './LoadingDialog.js';
import {
  Keep,
  Pass,
  GamePhases,
  SetGameInfo,
  SetBasicGameInfo,
  RegisterUpdateGameHandler,
  IsSelectCardPhase,
} from './Game.js';
import {ConnectProfile, RegisterUpdateViewHandler} from './Manage.js';
import {debug} from './Utils.js';
import './App.css';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      game: {},
      phase: GamePhases.NOT_STARTED,
      dialog: {title: '', cards: [], open: false},
      waiting: false,
      selectedCards: [],
      selectableCards: [],
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
    RegisterUpdateGameHandler(this.updateView);
    RegisterUpdateViewHandler(this.updateView);
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

  updateInfoUpdate = username => {
    SetGameInfo({me: username});
    ConnectProfile(username);
    this.setState({waiting: true});
  };

  updateView = (params, phase, selectedCards = null) => {
    const game = params.game;
    const toUpdate = {};

    if (phase === GamePhases.UPDATE_GAME || phase === GamePhases.NOT_STARTED) {
      toUpdate.game = params.game;
    }

    if (IsSelectCardPhase(phase) && selectedCards === null) {
      toUpdate.selectableCards = params.candidates.map(card => card.id);
    }

    if (this.state.waiting) {
      // XXX This will not be true in the future when we have server messages
      // as well.
      toUpdate.waiting = false;
    }

    if (phase === GamePhases.DONE_SELECT) {
      if (this.state.dialog.open) {
        toUpdate['dialog'] = {...this.state.dialog, open: false};
      }
      toUpdate.selectableCards = [];
      toUpdate.selectedCards = [];
    }

    if (selectedCards !== null) {
      toUpdate.selectedCards = selectedCards;
    } else {
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
      }
    }
    if (phase !== this.state.phase) {
      toUpdate.phase = phase;
    }

    debug('[toUpdate]', phase, toUpdate);
    this.setState(toUpdate);

    if (
      game.phase === proto.Phase.MULLIGAN &&
      game.activePlayer === game.player.name &&
      game.player.hand.length === 0
    ) {
      Keep();
    }

    if (
      phase === GamePhases.UPDATE_GAME &&
      game.activePlayer === game.player.name &&
      game.canStudy.length === 0 &&
      game.canActivate.length === 0 &&
      game.canPlay.length === 0
    ) {
      Pass();
    }
  };

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
    const {
      dialog,
      waiting,
      selectedCards,
      phase,
      game,
      selectableCards,
    } = this.state;
    const hasStudyable =
      phase === GamePhases.UPDATE_GAME &&
      game.activePlayer === game.player.name &&
      game.canStudy.length > 0;

    const chooseDialog = (
      <ChooseDialog
        title={dialog.title}
        cards={dialog.cards}
        open={dialog.open}
        selectedCards={selectedCards}
        selectableCards={selectableCards}
        onClose={event => {
          this.setState({dialog: {...dialog, open: false}});
        }}
      />
    );

    return (
      <div className="App">
        <header className="App-header">
          <LoadingDialog open={waiting} />
          <InfoEntryDialog open={true} onSubmit={this.updateInfoUpdate} />
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
                game={game}
                phase={phase}
                updateDialog={this.updateDialog}
              />
            </Grid>
            <Grid item xs={9} className="display-col">
              <Board
                game={game}
                phase={phase}
                selectedCards={selectedCards}
                selectableCards={selectableCards}
              />
            </Grid>
            <Grid item xs={1} className="display-col">
              <Grid
                container
                spacing={24}
                style={{
                  padding: 0,
                  height: '100%',
                }}
                direction="column">
                <Grid item xs={9} className="grid-col-item no-max-width">
                  <Stack
                    cards={game.stack}
                    selectedCards={selectedCards}
                    selectableCards={selectableCards}
                  />
                </Grid>
                <Grid item xs={3} className="grid-col-item no-max-width">
                  <Altar hasStudyable={hasStudyable} />
                </Grid>
              </Grid>
            </Grid>
          </Grid>
        </header>
      </div>
    );
  }
}

export default App;

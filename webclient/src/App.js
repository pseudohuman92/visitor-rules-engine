import React, {Component} from 'react';
import logo from './logo.svg';
import Grid from '@material-ui/core/Grid';
import './App.css';
import Board from './Board.js';
import Stack from './Stack.js';
import StateDisplay from './StateDisplay.js';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      cards: [],
      me: null,
      gary: null,
    };

    var card1 = {
      id: 'a',
      name: 'a',
      counters: [],
      depleted: false,
      marked: false,
      targets: [],
    };
    var card2 = {
      id: 'b',
      name: 'b',
      counters: [],
      depleted: false,
      marked: false,
      targets: [],
    };
    this.state.cards = this.state.cards.concat([card1, card2]);

    this.state.me = {
      id: 'me',
      name: 'me',
      deckSize: 45,
      energy: 3,
      maxEnergy: 7,
      hand: [],
      scrapyard: [],
      void: [],
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

    this.state.gary = this.state.me;
  }

  render() {
    return (
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
              <StateDisplay me={this.state.me} gary={this.state.gary} />
            </Grid>
            <Grid item xs={9} className="display-col">
              <Board
                myCards={this.state.cards}
                garyCards={this.state.cards}
                hand={this.state.me.hand}
              />
            </Grid>
            <Grid item xs={1} className="display-col">
              <Stack cards={this.state.cards} />
            </Grid>
          </Grid>
        </header>
      </div>
    );
  }
}

export default App;

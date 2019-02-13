import React, {Component} from 'react';

import HTML5Backend from 'react-dnd-html5-backend';
import {DragDropContextProvider} from 'react-dnd';

import Grid from '@material-ui/core/Grid';

import Board from './Board.js';
import Stack from './Stack.js';
import StateDisplay from './StateDisplay.js';
import {ServerWSURL} from './Constants.js';
import './App.css';

var cmessages = require('./proto/ClientMessages_pb.js');
var smessages = require('./proto/ServerMessages_pb.js');

class ProtoSocket {
  constructor(url, msgHandler) {
    this.socket = new WebSocket(ServerWSURL);

    this.socket.onmessage = event => {
      var msg = event.deserializeBinary;
    };
  }

  send(msgType, params) {
    var msg = new cmessages[msgType]();
    Object.keys(params).forEach(function(key) {
      const setName = 'set' + key.charAt(0).toUpperCase() + key.slice(1);
      msg[setName] = params[key];
    });

    var bytes = msg.serializeBinary();
    this.socket(bytes);
  }
}

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      cards: [],
      me: null,
      gary: null,
      socket: new WebSocket(ServerWSURL),
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
      </DragDropContextProvider>
    );
  }
}

export default App;

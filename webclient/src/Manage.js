// Handles logic and calls to app outside of games.

import {ProtoSocket} from './ProtoSocket.js';
import {ServerWSURL, ServerWSGameURL} from './Constants.js';
import {GamePhases, InitiateConnection} from './Game.js';

let updateViewHandler = null;
const protoSocket = new ProtoSocket(ServerWSURL, handleMsg);

function handleMsg(msgType, params) {
  const phase = GamePhases.NOT_STARTED;
  InitiateConnection(ServerWSGameURL);
  updateViewHandler(params, phase);
}

export function RegisterUpdateViewHandler(handler) {
  updateViewHandler = handler;
}

export function JoinTable(username, tableID) {
  protoSocket.send('JoinTable', {
    tableID: tableID,
    username: username,
    decklist: [],
  });
}

export function RegisterGameConnection(username, gameID) {
  protoSocket.send('RegisterGameConnection', {
    gameID: gameID,
    username: username,
  });
}

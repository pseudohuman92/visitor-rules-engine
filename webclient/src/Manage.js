// Handles logic and calls to app outside of games.

import {ProtoSocket} from './ProtoSocket.js';
import {GetProfileURL, GetGameURL} from './Utils.js';
import {GamePhases, InitiateConnection, SetGameInfo} from './Game.js';

let updateViewHandler = null;
let protoSocket = null;

function handleMsg(msgType, params) {
  if (msgType === 'newGame') {
    SetGameInfo({gameID: params.game.gameID, gary: params.game.opponent.name});
  }
  const phase = GamePhases.NOT_STARTED;
  InitiateConnection(GetGameURL(params.game.player.name, params.game.id));
  updateViewHandler(params, phase);
}

export function ConnectProfile(username) {
  protoSocket = new ProtoSocket(GetProfileURL(username), handleMsg);
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

import ProtoSocket from './ProtoSocket.js';
import {ServerWSURL} from './Constants.js';

export const GamePhases = {
  NOT_STARTED: 'NOT_STARTED',
  UPDATE_GAME: 'UPDATE_GAME',
  WIN: 'WIN',
  LOSE: 'LOSE',
  ORDER_CARDS: 'ORDER_CARDS',
  SELECT_FROM_LIST: 'SELECT_FROM_HAND',
  SELECT_FROM_PLAY: 'SELECT_FROM_HAND',
  SELECT_FROM_HAND: 'SELECT_FROM_HAND',
  SELECT_FROM_SCRAPYARD: 'SELECT_FROM_SCRAPYARD',
  SELECT_FROM_VOID: 'SELECT_FROM_VOID',
  SELECT_PLAYER: 'SELECT_PLAYER',
};

export class GameState {
  constructor() {
    this.protoSocket = new ProtoSocket(ServerWSURL, this.handleMsg.bind(this));
    // This is the handler which will be called whenever the game state
    // changes.
    this.updateViewHandler = undefined;
  }

  handleMsg(msgType, params) {
    // XXX Remember to update this with the protocol updates
    const phase = {
      UpdateGameState: GamePhases.UPDATE_GAME,
      Loss: GamePhases.LOSS,
      Win: GameState.WIN,
      OrderCards: GameState.ORDER_CARDS,
      SelectFromList: GameState.SELECT_FROM_LIST,
      SelectFromPlay: GameState.SELECT_FROM_PLAY,
      SelectFromHand: GameState.SELECT_FROM_HAND,
      SelectFromScrapyard: GameState.SELECT_FROM_SCRAPYARD,
      SelectFromVoid: GameState.SELECT_FROM_VOID,
      SelectPlayer: GameState.SELECT_PLAYER,
    }[msgType];
    this.updateViewHandler(params, phase);
  }

  send(msgType, params) {
    this.protoSocket.send(msgType, params);
  }
}

const gameState = new GameState();

export function SetBasicGameInfo(gameID, me, gary) {
  gameState.gameID = gameID;
  gameState.me = me;
  gameState.gary = gary;
}

export function RegisterUpdateViewHandler(updateViewHandler) {
  gameState.updateViewHandler = updateViewHandler;
}

export function Pass() {
  gameState.send('Pass', {gameID: gameState.gameID, username: gameState.me});
}

export function Mulligan() {
  gameState.send('Mulligan', {
    gameID: gameState.gameID,
    username: gameState.me,
  });
}

export function Keep() {
  gameState.send('Keep', {
    gameID: gameState.gameID,
    username: gameState.me,
  });
}

export function Concede() {
  gameState.send('Concede', {
    gameID: gameState.gameID,
    username: gameState.me,
  });
}

export function PlayCard(cardID) {
  gameState.send('PlayCard', {
    gameID: gameState.gameID,
    username: gameState.me,
    cardID: cardID,
  });
}

export function ActivateCard(cardID) {
  gameState.send('ActivateCard', {
    gameID: gameState.gameID,
    username: gameState.me,
    cardID: cardID,
  });
}

import ProtoSocket from './ProtoSocket.js';
import {ServerWSURL} from './Constants.js';

export const GamePhases = {
  NOT_STARTED: 'NotStarted',
  UPDATE_GAME: 'UpdateGame',
  WIN: 'Win',
  LOSE: 'Lose',
  ORDER_CARDS: 'OrderCards',
  SELECT_FROM_LIST: 'SelectFromList',
  SELECT_FROM_PLAY: 'SelectFromPlay',
  SELECT_FROM_HAND: 'SelectFromHand',
  SELECT_FROM_SCRAPYARD: 'SelectFromScrapyard',
  SELECT_FROM_VOID: 'SelectFromVoid',
  SELECT_PLAYER: 'SelectPlayer',
};

export class GameState {
  constructor() {
    this.protoSocket = new ProtoSocket(ServerWSURL, this.handleMsg);
    // This is the handler which will be called whenever the game state
    // changes.
    this.updateViewHandler = undefined;
    this.lastMsg = null;
    this.phase = null;
  }

  handleMsg = (msgType, params) => {
    // XXX Remember to update this with the protocol updates
    const phase = {
      UpdateGameState: GamePhases.UPDATE_GAME,
      Loss: GamePhases.LOSS,
      Win: GamePhases.WIN,
      OrderCards: GamePhases.ORDER_CARDS,
      SelectFromList: GamePhases.SELECT_FROM_LIST,
      SelectFromPlay: GamePhases.SELECT_FROM_PLAY,
      SelectFromHand: GamePhases.SELECT_FROM_HAND,
      SelectFromScrapyard: GamePhases.SELECT_FROM_SCRAPYARD,
      SelectFromVoid: GamePhases.SELECT_FROM_VOID,
      SelectPlayer: GamePhases.SELECT_PLAYER,
    }[msgType];
    this.lastMsg = params;
    this.phase = phase;
    this.selected = [];

    if (phase === GamePhases.SELECT_FROM_HAND) {
      params.candidates = params.game.player.hand;
    }
    this.updateViewHandler(params, phase);
  };

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

export function SelectCard(cardID) {
  let selectCount = gameState.lastMsg.selectionCount;
  gameState.selected.push(cardID);
  if (selectCount === 1) {
    gameState.send(gameState.phase + 'Response', {
      gameID: gameState.gameID,
      selectedCards: gameState.selected,
    });
    gameState.selected = [];
    // TODO This probably shouldn't update here but when we receive the ack
    // from the server.
    // XXX Also this is kind of a hack to call update game state when we didn't
    // really get a message to do so and game state hasn't changed, but it will
    // clear out candidates and stuff.
    gameState.updateViewHandler(
      {game: gameState.lastMsg.game},
      GamePhases.UPDATE_GAME,
      {open: false, title: '', cards: []},
    );
  } else {
    gameState.lastMsg.selectionCount--;
    gameState.lastMsg.candidates = gameState.lastMsg.candidates.filter(
      cand => cand.id !== cardID,
    );
    gameState.updateViewHandler(gameState.lastMsg, gameState.phase);
  }
}

import {GameProtoSocket} from './ProtoSocket.js';

const proto = require('./protojs/compiled.js');

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
  SELECT_FROM_STACK: 'SelectFromStack',
  SELECT_X_VALUE: 'SelectXValue',
  SELECT_PLAYER: 'SelectPlayer',
  DONE_SELECT: 'DoneSelect',
};

export function IsSelectCardPhase(phase) {
  return [
    GamePhases.SELECT_FROM_LIST,
    GamePhases.SELECT_FROM_PLAY,
    GamePhases.SELECT_FROM_HAND,
    GamePhases.SELECT_FROM_SCRAPYARD,
    GamePhases.SELECT_FROM_VOID,
    GamePhases.SELECT_FROM_STACK,
  ].includes(phase);
}

export class GameState {
  constructor() {
    this.protoSocket = null;
    // This is the handler which will be called whenever the game state
    // changes.
    this.updateViewHandler = null;
    this.lastMsg = null;
    this.phase = null;
    this.gameInfoInitialized = false;
  }

  handleMsg = (msgType, params) => {
    // XXX Remember to update this with the protocol updates
    let phase = {
      UpdateGameState: GamePhases.UPDATE_GAME,
      Loss: GamePhases.LOSS,
      Win: GamePhases.WIN,
      OrderCards: GamePhases.ORDER_CARDS,
      SelectFrom: GamePhases.SELECT_FROM_LIST,
      SelectXValue: GamePhases.SELECT_X_VALUE,
      SelectPlayer: GamePhases.SELECT_PLAYER,
    }[msgType];
    this.lastMsg = params;
    this.phase = phase;
    this.selectedCards = [];

    if (msgType === 'SelectFrom') {
      const selectPhase = {};
      selectPhase[proto.SelectFromType.LIST] = GamePhases.SELECT_FROM_LIST;
      selectPhase[proto.SelectFromType.HAND] = GamePhases.SELECT_FROM_HAND;
      selectPhase[proto.SelectFromType.PLAY] = GamePhases.SELECT_FROM_PLAY;
      selectPhase[proto.SelectFromType.SCRAPYARD] =
        GamePhases.SELECT_FROM_SCRAPYARD;
      selectPhase[proto.SelectFromType.VOID] = GamePhases.SELECT_FROM_VOID;
      selectPhase[proto.SelectFromType.STACK] = GamePhases.SELECT_FROM_STACK;
      phase = selectPhase[params.messageType];
    }

    if (!this.gameInfoInitialized) {
      SetGameInfo({
        gameID: params.game.id,
        me: params.game.player.name,
        gary: params.game.opponent.name,
      });
    }

    this.updateViewHandler(params, phase, null);
  };

  send(msgType, params) {
    this.protoSocket.send(msgType, params);
  }
}

const gameState = new GameState();

export function InitiateConnection(url) {
  gameState.protoSocket = new GameProtoSocket(url, gameState.handleMsg);
}

export function SetBasicGameInfo(gameID, me, gary) {
  gameState.gameID = gameID;
  gameState.me = me;
  gameState.gary = gary;
}

export function RegisterUpdateGameHandler(updateViewHandler) {
  gameState.updateViewHandler = updateViewHandler;
}

export function Pass() {
  gameState.send('Pass', {gameID: gameState.gameID, username: gameState.me});
}

export function SetGameInfo(newInfo) {
  Object.keys(newInfo).forEach(i => {
    gameState[i] = newInfo[i];
  });
  gameState.gameInfoInitialized = true;
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

export function StudyCard(cardID) {
  gameState.send('StudyCard', {
    gameID: gameState.gameID,
    username: gameState.me,
    cardID: cardID,
  });
}

export function SelectDone() {
  gameState.send('SelectFromResponse', {
    gameID: gameState.gameID,
    messageType: gameState.lastMsg.messageType,
    selectedCards: gameState.selectedCards,
  });
  gameState.selectedCards = [];
  gameState.phase = GamePhases.DONE_SELECT;

  gameState.updateViewHandler(
    gameState.lastMsg,
    gameState.phase,
    gameState.selectedCards,
  );
}

export function SelectCard(cardID) {
  const selectCount = gameState.lastMsg.selectionCount;
  gameState.selectedCards.push(cardID);
  if (gameState.selectedCards.length === selectCount) {
    SelectDone();
  } else {
    gameState.updateViewHandler(
      gameState.lastMsg,
      gameState.phase,
      gameState.selectedCards,
    );
  }
}

export function UnselectCard(cardID) {
  if (gameState.selectedCards.includes(cardID)) {
    gameState.selectedCards.splice(gameState.selectedCards.indexOf(cardID), 1);
    gameState.updateViewHandler(
      gameState.lastMsg,
      gameState.phase,
      gameState.selectedCards,
    );
  }
}

export function SelectXValue(xVal) {
  gameState.send('SelectXValueResponse', {
    gameID: gameState.gameID,
    selectedXValue: xVal,
  });
  gameState.updateViewHandler(gameState.lastMsg, GamePhases.DONE_SELECT, null);
}

export function SelectPlayer(playerName) {
  gameState.send('SelectPlayerResponse', {
    gameID: gameState.gameID,
    selectedPlayerName: playerName,
  });
  gameState.updateViewHandler(gameState.lastMsg, GamePhases.DONE_SELECT, null);
}

const WebSocket = require('ws');

const proto = require('../src/protojs/compiled.js');

const wss = new WebSocket.Server({port: 8080});

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

const gameState = {
  id: 'best game',
  player: me,
  opponent: gary,
  turnPlayer: me.id,
  activePlayer: me.id,
  stackCards: stackCards,
};

function otherPlayer(id) {
  return id === me.id ? gary.id : me.id;
}

function capitalize(s) {
  return s.charAt(0).toUpperCase() + s.slice(1);
}

function capitalizeOnlyFirst(s) {
  return s.charAt(0).toUpperCase() + s.slice(1).toLowerCase();
}

function decapitalize(s) {
  return s.charAt(0).toLowerCase() + s.slice(1);
}

//function serializeKnowledge(knowledge) {
//  const msg = new msgTypes.Knowledge();
//  msg.setKnowledge(knowledge.knowledge);
//  msg.setCount(knowledge.count);
//  return msg;
//}
//
//function serializeCard(card) {
//  const msg = new msgTypes.Card();
//  msg.setId(card.id);
//  msg.setName(card.name);
//  msg.setDepleted(false);
//  msg.setCountersList([]);
//  msg.setMarked(false);
//  msg.setTargetsList([]);
//  return msg;
//}
//
//function serializeOpponent(opponent) {
//  const msg = new msgTypes.Player();
//  msg.setId(opponent.id);
//  msg.setName(opponent.name);
//  msg.setDecksize(opponent.deckSize);
//  msg.setEnergy(opponent.energy);
//  msg.setMaxenergy(opponent.maxEnergy);
//  msg.setScrapyardList(opponent.scrapyard.map(c => serializeCard(c)));
//  msg.setVoidList(opponent.void.map(c => serializeCard(c)));
//  msg.setKnowledgepoolList(
//    opponent.knowledgePool.map(k => serializeKnowledge(k)),
//  );
//  return msg;
//}
//
//function serializePlayer(player) {
//  const msg = new msgTypes.Player();
//  msg.setId(player.id);
//  msg.setName(player.name);
//  msg.setDecksize(player.deckSize);
//  msg.setEnergy(player.energy);
//  msg.setMaxenergy(player.maxEnergy);
//  msg.setHandList(player.hand.map(c => serializeCard(c)));
//  msg.setScrapyardList(player.scrapyard.map(c => serializeCard(c)));
//  msg.setVoidList(player.void.map(c => serializeCard(c)));
//  msg.setKnowledgepoolList(
//    player.knowledgePool.map(k => serializeKnowledge(k)),
//  );
//  return msg;
//}
//
//function serializeGameState(gameState) {
//  const msg = new msgTypes.GameState();
//  msg.setId(gameState.id);
//  msg.setPlayer(serializePlayer(gameState.player));
//  msg.setOpponent(serializeOpponent(gameState.opponent));
//  msg.setTurnplayer(gameState.turnPlayer);
//  msg.setActiveplayer(gameState.activePlayer);
//  msg.setStackList(gameState.stackCards.map(c => serializeCard(c)));
//}

function send(ws, msgType, params) {
  const msgParams = {};
  msgParams[decapitalize(msgType)] = params;
  const msg = proto.ServerGameMessage.create(msgParams);
  const bytes = proto.ServerGameMessage.encode(msg).finish();

  //const msg = new smessages[msgType]();
  //Object.keys(params).forEach(function(key) {
  //  const setName = 'set' + capitalizeOnlyFirst(key);
  //  if (setName === 'setGame') {
  //    msg[setName](serializeGameState(params[key]));
  //  } else {
  //    msg[setName](params[key]);
  //  }
  //});

  //const setPayloadName = 'set' + capitalizeOnlyFirst(msgType);
  //const outerMsg = new smessages.ServerGameMessage();
  //outerMsg[setPayloadName](msg);
  //const bytes = outerMsg.serializeBinary();
  //console.log(bytes);
  console.log('SEND:', msg);
  ws.send(bytes);
}

function handlePlayCard(ws, params) {}
function handleActivateCard(ws, params) {}
function handleStudyCard(ws, params) {}
function handlePass(ws, params) {
  gameState.activePlayer = otherPlayer(gameState.turnPlayer);
  gameState.turnPlayer = otherPlayer(gameState.turnPlayer);
  gameState.stackCards = [];
}
function handleMulligan(ws, params) {
  send(ws, 'UpdateGameState', {game: gameState});
}
function handleKeep(ws, params) {
  send(ws, 'UpdateGameState', {game: gameState});
}
function handleConcede(ws, params) {}
function handleOrderCardsResponse(ws, params) {}
function handleSelectFromHandResponse(ws, params) {}
function handleSelectFromListResponse(ws, params) {}
function handleSelectFromPlayResponse(ws, params) {}
function handleSelectFromScrapyardResponse(ws, params) {}
function handleSelectFromVoidResponse(ws, params) {}
function handleSelectPlayerResponse(ws, params) {}

wss.on('connection', function connection(ws) {
  ws.on('message', function incoming(message) {
    const msg = proto.ClientGameMessage.decode(new Uint8Array(message));
    //const outerMsg = cmessages.ClientGameMessage.deserializeBinary(message);
    //const msg = jspb.Message.getField(outerMsg, outerMsg.getPayloadCase());
    //const msgType = outerMsg.getPayloadCase();
    //console.log(outerMsg.getPayloadCase(), msg);
    const handler = {
      playCard: handlePlayCard,
      activateCard: handleActivateCard,
      studyCard: handleStudyCard,
      pass: handlePass,
      mulligan: handleMulligan,
      keep: handleKeep,
      concede: handleConcede,
      orderCardsResponse: handleOrderCardsResponse,
      selectFromHandResponse: handleSelectFromHandResponse,
      selectFromListResponse: handleSelectFromListResponse,
      selectFromPlayResponse: handleSelectFromPlayResponse,
      selectFromScrapyardResponse: handleSelectFromScrapyardResponse,
      selectFromVoidResponse: handleSelectFromVoidResponse,
      selectPlayerResponse: handleSelectPlayerResponse,
    }[msg.payload];
    console.log('RECV:', msg[msg.payload]);
    handler(ws, msg[msg.payload]);
  });
});

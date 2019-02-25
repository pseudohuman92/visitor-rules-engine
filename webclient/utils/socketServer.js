const http = require('http');
const url = require('url');
const WebSocket = require('ws');

const proto = require('../src/protojs/compiled.js');

const server = http.createServer();
const gameSocket = new WebSocket.Server({noServer: true});
const manageSocket = new WebSocket.Server({noServer: true});

const cards = 'abcdefghijklmnopqrstuvwxyz'.split('').map(l => ({
  id: l,
  name: l,
  counters: [],
  depleted: false,
  marked: false,
  targets: [],
  cost: 4,
  knowledgeCost: [
    {
      knowledge: 2,
      count: 2,
    },
  ],
  counters: [{counter: 1, count: 5}],
  type: 'Action',
  description: 'The best card ever.',
}));
const myHandCards = cards.slice(0, 10);
const garyHandSize = 4;
const myScrapCards = cards.slice(7, 9);
const garyScrapCards = cards.slice(9, 10);
const myVoidCards = cards.slice(10, 12);
const garyVoidCards = [];
const myPlayCards = cards.slice(12, 16);
const garyPlayCards = cards.slice(16, 19);
const stackCards = cards.slice(10, 23);

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

const gameState = {
  id: 'best game',
  player: me,
  opponent: gary,
  turnPlayer: me.name,
  activePlayer: me.name,
  stack: stackCards,
  canActivate: [myPlayCards[0].id, myPlayCards[2].id],
  canPlay: [myHandCards[0].id, myHandCards[2].id],
  canStudy: [myHandCards[0].id, myHandCards[1].id],
  phase: 0,
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

function sendMsg(ws, msgType, params) {
  const msgParams = {};
  msgParams[decapitalize(msgType)] = params;
  const msg = proto.ServerMessage.create(msgParams);
  const bytes = proto.ServerMessage.encode(msg).finish();
  console.log('[sendMsg]', msg);
  ws.send(bytes);
}

function sendGameMsg(ws, msgType, params) {
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
  console.log('[sendGameMsg]', msg);
  ws.send(bytes);
}

function handlePlayCard(ws, params) {
  const playedCard = gameState.player.hand.find(
    card => card.id === params.cardID,
  );
  gameState.player.hand = gameState.player.hand.filter(
    card => card.id !== params.cardID,
  );
  gameState.player.play.push(playedCard);
  gameState.activePlayer = otherPlayer(gameState.activePlayer);
  sendGameMsg(ws, 'UpdateGameState', {game: gameState});
}
function handleActivateCard(ws, params) {
  const selectedCards = params.selectedCards;
  gameState.activePlayer = otherPlayer(gameState.activePlayer);
  sendGameMsg(ws, 'UpdateGameState', {game: gameState});
}
function handleStudyCard(ws, params) {}
function handlePass(ws, params) {
  gameState.activePlayer = otherPlayer(gameState.turnPlayer);
  gameState.turnPlayer = otherPlayer(gameState.turnPlayer);
  gameState.stackCards = [];
  sendGameMsg(ws, 'UpdateGameState', {game: gameState});
}
function handleMulligan(ws, params) {
  sendGameMsg(ws, 'UpdateGameState', {game: gameState});
}
function handleKeep(ws, params) {
  sendGameMsg(ws, 'UpdateGameState', {game: gameState});
  //sendGameMsg(ws, 'SelectFrom', {
  //  game: gameState,
  //  messageType: 4,
  //  selectionCount: 2,
  //  candidates: [],
  //  canSelected: [
  //    myPlayCards[0].id,
  //    myPlayCards[2].id,
  //    myPlayCards[3].id,
  //    garyPlayCards[0].id,
  //    garyPlayCards[2].id,
  //  ],
  //  upTo: true,
  //});
  //  sendGameMsg(ws, 'SelectFromHand', {
  //    game: gameState,
  //    selectionCount: 2,
  //    candidates: [myHandCards[0], myHandCards[1]],
  //  });
  //sendGameMsg(ws, 'SelectFromScrapyard', {
  //  game: gameState,
  //  selectionCount: 1,
  //  candidates: [myScrapCards[1]],
  //});
  //sendGameMsg(ws, 'SelectFromVoid', {
  //  game: gameState,
  //  selectionCount: 2,
  //  candidates: [myVoidCards[0], myVoidCards[1]],
  //});
  //sendGameMsg(ws, 'SelectFrom', {
  //  game: gameState,
  //  messageType: 1,
  //  selectionCount: 2,
  //  candidates: cards.slice(0, 4),
  //  canSelected: ['b', 'd'],
  //  upTo: true,
  //});
  //sendGameMsg(ws, 'SelectXValue', {
  //  game: gameState,
  //  maxXValue: 6,
  //});
  //sendGameMsg(ws, 'SelectPlayer', {
  //  game: gameState,
  //});
  sendGameMsg(ws, 'SelectFrom', {
    game: gameState,
    messageType: 7,
    selectionCount: 2,
    candidates: stackCards,
    canSelected: ['n', 'p', 's', 't', 'w'],
    upTo: true,
  });
}
function handleConcede(ws, params) {}
function handleOrderCardsResponse(ws, params) {}
function handleSelectFromResponse(ws, params) {}
function handleSelectXValueResponse(ws, params) {}
function handleSelectFromHandResponse(ws, params) {}
function handleSelectFromListResponse(ws, params) {}
function handleSelectFromPlayResponse(ws, params) {}
function handleSelectFromScrapyardResponse(ws, params) {}
function handleSelectFromVoidResponse(ws, params) {}
function handleSelectPlayerResponse(ws, params) {}

let gameConnection = null;

gameSocket.on('connection', function connection(ws) {
  gameConnection = ws;
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
      selectFromResponse: handleSelectFromResponse,
      selectPlayerResponse: handleSelectPlayerResponse,
      selectXValueResponse: handleSelectXValueResponse,
    }[msg.payload];
    console.log('[recvGameMsg]', msg[msg.payload]);
    handler(ws, msg[msg.payload]);
  });
});

manageSocket.on('connection', function connection(ws) {
  ws.on('message', function incoming(message) {
    const msg = proto.ClientMessage.decode(new Uint8Array(message));
    //const outerMsg = cmessages.ClientGameMessage.deserializeBinary(message);
    //const msg = jspb.Message.getField(outerMsg, outerMsg.getPayloadCase());
    //const msgType = outerMsg.getPayloadCase();
    //console.log(outerMsg.getPayloadCase(), msg);
    console.log('[recvMsg]:', msg[msg.payload]);
    gameState.player.name = msg[msg.payload].username;
    gameState.activePlayer = gameState.player.name;
    gameState.turnPlayer = gameState.player.name;
    sendMsg(ws, 'NewGame', {game: gameState});
  });
});

server.on('upgrade', function upgrade(request, socket, head) {
  const pathname = url.parse(request.url).pathname;
  console.log('connected to', pathname);
  if (pathname.startsWith('/profiles/')) {
    manageSocket.handleUpgrade(request, socket, head, function done(ws) {
      manageSocket.emit('connection', ws, request);
    });
  } else if (pathname.startsWith('/games/')) {
    gameSocket.handleUpgrade(request, socket, head, function done(ws) {
      gameSocket.emit('connection', ws, request);
    });
  } else {
    socket.destroy();
  }
});

server.listen(8080);

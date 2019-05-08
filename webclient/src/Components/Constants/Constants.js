import proto from "../../protojs/compiled.js";

export const GamePhases = {
  NOT_STARTED: "NotStarted",
  UPDATE_GAME: "UpdateGame",
  WIN: "Win",
  LOSE: "Lose",
  ORDER_CARDS: "OrderCards",
  SELECT_FROM_LIST: "SelectFromList",
  SELECT_FROM_PLAY: "SelectFromPlay",
  SELECT_FROM_HAND: "SelectFromHand",
  SELECT_FROM_SCRAPYARD: "SelectFromScrapyard",
  SELECT_FROM_VOID: "SelectFromVoid",
  SELECT_FROM_STACK: "SelectFromStack",
  SELECT_X_VALUE: "SelectXValue",
  DONE_SELECT: "DoneSelect"
};

export const ItemTypes = {
  CARD: "card",
  FIELD: "field",
  ALTAR: "altar"
};

export const FieldIDs = {
  MY_FIELD: "myField",
  GARY_FIELD: "garyField"
};

export const knowledgeMap = {};
knowledgeMap[proto.Knowledge.BLACK] = "B";
knowledgeMap[proto.Knowledge.GREEN] = "G";
knowledgeMap[proto.Knowledge.RED] = "R";
knowledgeMap[proto.Knowledge.BLUE] = "U";
knowledgeMap[proto.Knowledge.YELLOW] = "Y";
knowledgeMap["B"] = proto.Knowledge.BLACK;
knowledgeMap["U"] = proto.Knowledge.BLUE;
knowledgeMap["R"] = proto.Knowledge.RED;
knowledgeMap["G"] = proto.Knowledge.GREEN;
knowledgeMap["Y"] = proto.Knowledge.YELLOW;

export function getCardColor(knowledgeCost) {
  if (knowledgeCost.length === 0) {
    return "gray";
  }
  if (knowledgeCost.length > 1) {
    return "yellow";
  }

  var knowlString = toKnowledgeString(knowledgeCost);
  if (knowlString.startsWith("B")) {
    return "#666666";
  } else if (knowlString.startsWith("U")) {
    return "#0066ff";
  } else if (knowlString.startsWith("R")) {
    return "#ff1a1a";
  } else if (knowlString.startsWith("Y")) {
    return "#ffff00";
  } else if (knowlString.startsWith("G")) {
    return "green";
  } else {
    return "#e6e6e6";
  }
}

export function toKnowledgeString(knowledgeCost) {
  var str = "";

  for (var i = 0; i < knowledgeCost.length; i++) {
    for (var j = 0; j < knowledgeCost[i].count; j++) {
      str = str + knowledgeMap[knowledgeCost[i].knowledge];
    }
  }
  return str;
}

export function toKnowledgeCost(knowledgeString) {
  var cost = {};

  for (var i = 0; i < knowledgeString.length; i++) {
    var c = knowledgeString.charAt(i);
    if (c in knowledgeMap) {
      var v = knowledgeMap[c];
      if (v in cost) {
        cost[v] += 1;
      } else {
        cost[v] = 1;
      }
    }
  }

  var res = [];
  for (var k in cost) {
    res.push({ knowledge: parseInt(k), count: cost[k] });
  }
  return res;
}

const fontMap = {
  "0": "🄋",
  "1": "➀",
  "2": "➁",
  "3": "➂",
  "4": "➃",
  "5": "➄",
  "6": "➅",
  "7": "➆",
  "8": "➇",
  "9": "➈",
  X: "Ⓧ",
  U: "🟐",
  R: "🟐",
  B: "🟐",
  G: "🟐",
  Y: "🟐",
  "-": ""
};

export function toIconString(s) {
  var line = "";
  for (var i = 0; i < s.length; i++) {
    var c = s.charAt(i);
    if (c in fontMap) {
      line += fontMap[c];
    } else {
      line += "?";
    }
  }
  return line;
}

export function getIconColor(knowledgeCost) {
  if (knowledgeCost.length === 0) {
    return "gray";
  }
  var knowlString = toKnowledgeString(knowledgeCost);
  if (knowlString.startsWith("B")) {
    return "black";
  } else if (knowlString.startsWith("U")) {
    return "blue";
  } else if (knowlString.startsWith("R")) {
    return "red";
  } else if (knowlString.startsWith("G")) {
    return "green";
  } else if (knowlString.startsWith("Y")) {
    return "yellow";
  } else {
    return "beige";
  }
}

const initialPlayer = {
  id: "player",
  name: "player",
  deckSize: 0,
  energy: 0,
  maxEnergy: 0,
  play: [],
  hand: [],
  handSize: 0,
  scrapyard: [],
  void: [],
  knowledgePool: [],
  shield: 0,
  reflect: 0
};

const emptyGame = {
  id: "Empty Game",
  player: initialPlayer,
  opponent: initialPlayer,
  turnPlayer: initialPlayer.id,
  activePlayer: initialPlayer.id,
  stack: [],
  phase: 0,
  canPlay: [],
  canActivate: [],
  canStudy: []
};

const emptyGameState = {
  game: emptyGame,
  phase: GamePhases.NOT_STARTED,
  gameInitialized: false,
  dialog: { title: "", open: false },
  selectedCards: [],
  candidateCards: [],
  selectableCards: [],
  selectablePlayers: [],
  selectCountMax: 0,
  maxXValue: 0,
  upTo: false,
  targets: [],
  autoPass: false,
  win: false
};

export const initialState = {
  coins: 0,
  fullCollection: [],
  collection: [],
  dailyWins: 0,
  decks: {},
  dust: 0,
  username: "",
  packs: {},
  extendedGameState: emptyGameState
};

export function IsSelectCardPhase(phase) {
  return [
    GamePhases.SELECT_FROM_LIST,
    GamePhases.SELECT_FROM_PLAY,
    GamePhases.SELECT_FROM_HAND,
    GamePhases.SELECT_FROM_SCRAPYARD,
    GamePhases.SELECT_FROM_VOID,
    GamePhases.SELECT_FROM_STACK
  ].includes(phase);
}

export function toGamePhase(msgType, selectType) {
  switch (msgType) {
    case "SelectFrom":
      switch (selectType) {
        case proto.SelectFromType.LIST:
          return GamePhases.SELECT_FROM_LIST;
        case proto.SelectFromType.HAND:
          return GamePhases.SELECT_FROM_HAND;
        case proto.SelectFromType.PLAY:
          return GamePhases.SELECT_FROM_PLAY;
        case proto.SelectFromType.SCRAPYARD:
          return GamePhases.SELECT_FROM_SCRAPYARD;
        case proto.SelectFromType.VOID:
          return GamePhases.SELECT_FROM_VOID;
        case proto.SelectFromType.STACK:
          return GamePhases.SELECT_FROM_STACK;
        default:
          break;
      }
      break;
    case "UpdateGameState":
      return GamePhases.UPDATE_GAME;
    case "GameEnd":
      return GamePhases.GAME_END;
    case "OrderCards":
      return GamePhases.ORDER_CARDS;
    case "SelectXValue":
      return GamePhases.SELECT_X_VALUE;
    default:
      return GamePhases.NOT_STARTED;
  }
}

export function toSelectFromType(phase) {
  switch (phase) {
    case GamePhases.SELECT_FROM_LIST:
      return proto.SelectFromType.LIST;
    case GamePhases.SELECT_FROM_HAND:
      return proto.SelectFromType.HAND;
    case GamePhases.SELECT_FROM_PLAY:
      return proto.SelectFromType.PLAY;
    case GamePhases.SELECT_FROM_SCRAPYARD:
      return proto.SelectFromType.SCRAPYARD;
    case GamePhases.SELECT_FROM_VOID:
      return proto.SelectFromType.VOID;
    case GamePhases.SELECT_FROM_STACK:
      return proto.SelectFromType.STACK;
    default:
      return proto.SelectFromType.NONE;
  }
}

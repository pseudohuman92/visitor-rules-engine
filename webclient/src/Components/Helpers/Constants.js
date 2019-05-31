import proto from "../../protojs/compiled.js";
import { toKnowledgeCost } from "./Helpers"

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


export const fontMap = {
  U: "\u2738",
  R: "\u2738",
  B: "\u2738",
  G: "\u2738",
  Y: "\u2738",
  P: "\u2738",
  "-": ""
};

const initialPlayer = {
  id: "",
  userId: "",
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
  reflect: 0,
  health: 0
};

const initialGame = {
  id: "",
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

const initialExtendedGameState = {
  game: initialGame,
  phase: GamePhases.NOT_STARTED,
  gameInitialized: false,
  dialog: { title: "", open: false, cards: [] },
  selectedCards: [],
  selectableCards: [],
  selectablePlayers: [],
  selectCountMax: 0,
  maxXValue: 0,
  upTo: false,
  targets: [],
  autoPass: false,
  win: false,
  opponentUsername: "",
};

export const initialState = {
  authUser: {},
  coins: 0,
  collection: {},
  collectionId: "",
  dailyWins: 0,
  decks: {},
  dust: 0,
  username: "",
  packs: {},
  extendedGameState: initialExtendedGameState
};

function initializeFullCollection (){
  var result ={};
  fetch("/Cards.tsv")
  .then(r => r.text())
  .then(file => {
    file.split("\n").forEach(line => {
      let fields = line.split("\t");
      if (
        fields[0] !== "" &&
        !fields[0].startsWith("Code") &&
        !fields[0].startsWith("A")
      ) {
        let name = fields[1] !== ""?fields[1]:fields[0];
        result[name] = {
          name: name,
          type: fields[2],
          description: fields[4],
          health: fields[5],
          cost: fields[6],
          knowledgeCost: toKnowledgeCost(fields[7])
        };
      }
    });
  })
  return result;
};

export const fullCollection = initializeFullCollection();

export function toCollectionArray(collection) {
  var col = [];
  Object.keys(collection).forEach(function(key) {
    col.push({ count: collection[key], card: fullCollection[key] });
  });
  return col;
}

export function getNewUserCollection() {
  let newUserCollection = {};
  Object.keys(fullCollection).forEach(function(key) {
    newUserCollection[key] = 3;
  });
  return newUserCollection;
}
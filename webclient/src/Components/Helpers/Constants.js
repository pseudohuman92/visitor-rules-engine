import proto from "../../protojs/compiled.js";
import { toKnowledgeCost } from "./Helpers";

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
  DONE_SELECT: "DoneSelect",
  SELECT_ATTACKERS: "SelectAttackers"
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
  canAttack: [],
  attacking: [],
  selectCountMax: 0,
  maxXValue: 0,
  upTo: false,
  targets: [],
  autoPass: false,
  win: false,
  opponentUsername: ""
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

const initializeFullCollection = () => {
  var result = {};
  fetch("/Cards - Cards.json")
    .then(r => r.text())
    .then(file =>
      JSON.parse(file).forEach(card => {
        let name = card.Name !== "" ? card.Name : card.Code;
        if (
          name !== "" //&&
          //!card.Code.startsWith("Code") //&& !card.Code.startsWith("A")
        ) {
          result[name] = {
            name: name,
            type: card.Type,
            description: card.Effect,
            attack: card.Attack,
            health: card.Health,
            cost: card.Energy,
            knowledgeCost: toKnowledgeCost(card.Knowledge),
            shield: card.Shield,
            loyalty: card.Loyalty,
          };
        }
      })
    );
  return result;
};

export const fullCollection = initializeFullCollection();

function getNewUserCollection() {
  let newUserCollection = {};
  Object.keys(fullCollection).forEach(function(key) {
    newUserCollection[key] = 3;
  });
  return newUserCollection;
}

export const newUserCollection = getNewUserCollection();

export const keywords = {Activate: "This is Activate explanation.", Favor: "This is Favor explanation"};


export const craftCost = 1000;
export const salvageValue = 100;

export const packList = ["Set1"];
export const packCosts = {Set1: 1000};

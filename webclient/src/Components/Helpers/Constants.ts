import proto, {
  SelectFrom,
  GameState,
  Card,
  AttackerAssignment,
  Player
} from "../../protojs/compiled";
import { toKnowledgeCost } from "./Helpers";

export enum ClientPhase {
  NOT_STARTED = "NotStarted",
  UPDATE_GAME = "UpdateGame",
  WIN = "Win",
  LOSE = "Lose",
  ORDER_CARDS = "OrderCards",
  SELECT_FROM_LIST = "SelectFromList",
  SELECT_FROM_PLAY = "SelectFromPlay",
  SELECT_FROM_HAND = "SelectFromHand",
  SELECT_FROM_SCRAPYARD = "SelectFromScrapyard",
  SELECT_FROM_VOID = "SelectFromVoid",
  SELECT_FROM_STACK = "SelectFromStack",
  SELECT_X_VALUE = "SelectXValue",
  DONE_SELECT = "DoneSelect",
  SELECT_ATTACKERS = "SelectAttackers",
  ATTACK_PHASE = "AttackPhase"
}

export enum ItemTypes {
  CARD = "card",
  FIELD = "field",
  ALTAR = "altar"
}

export enum FieldIDs {
  MY_FIELD = "myField",
  GARY_FIELD = "garyField"
}

export const knowledgeMap: any = {};
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

export interface DialogData {
  //Data for modal dialogue screen
  title: string;
  open: boolean;
  cards: Card[];
}

export function initialDialogData(): DialogData {
  return {
    title: "",
    open: false,
    cards: []
  };
}

export interface AttackerAssignmentData {
  possibleAttackers: string[];
  attackerAssignments: AttackerAssignment[];
  currentAttacker: string;
  possibleAttackTargets: string[];
}

export function initialAttackerAssignmentData(): AttackerAssignmentData {
  return {
    possibleAttackers: [],
    attackerAssignments: [],
    currentAttacker: "",
    possibleAttackTargets: []
  };
}

export interface SelectionData {
  selectionCount: number;
  candidates: Card[];
  selectable: string[];
  upTo: boolean;
  selected: string[];
  maxXValue: number;
}

export function initialSelectionData(): SelectionData {
  return {
    selectionCount: 0,
    candidates: [],
    selectable: [],
    upTo: false,
    selected: [],
    maxXValue: 0
  };
}

export interface ExtendedGameState {
  opponentUsername: string;
  game: GameState; //GameState object. See proto files
  clientPhase: ClientPhase; //Client defined phases

  gameInitialized: boolean;
  possibleBlockers: string[];
  targets: string[];
  autoPass: boolean;
  win: boolean;

  dialogData: DialogData;
  selectionData: SelectionData;
  attackerAssignmentData: AttackerAssignmentData;
}

export function initialExtendedGameState(): ExtendedGameState {
  let game = GameState.create();
  game.player = Player.create();
  game.opponent = Player.create();
  return {
    opponentUsername: "",
    game: game, //GameState object. See proto files
    clientPhase: ClientPhase.NOT_STARTED, //Client defined phases

    gameInitialized: false,
    possibleBlockers: [],
    targets: [],
    autoPass: false,
    win: false,

    dialogData: initialDialogData(),
    attackerAssignmentData: initialAttackerAssignmentData(),
    selectionData: initialSelectionData()
  };
}

export interface Profile {
  coins: number;
  collection: any; //TODO: Lookup its type
  collectionId: string;
  dailyWins: number;
  decks: any; //TODO: Lookup its type
  dust: number;
  username: string;
  packs: any; //TODO: Lookup its type
}

export function initialProfile(): Profile {
  return {
    coins: 0,
    collection: {},
    collectionId: "",
    dailyWins: 0,
    decks: "",
    dust: 0,
    username: "",
    packs: {}
  };
}

export interface State {
  extendedGameState: ExtendedGameState;
  firebaseAuthData: any; // Object received from firebase
  profile: Profile;
}

export function initialState(): State {
  return {
    extendedGameState: initialExtendedGameState(),
    firebaseAuthData: {}, // Object received from firebase
    profile: initialProfile()
  };
}

const initializeFullCollection = () => {
  var result: any = {};
  fetch("/Visitor Cards - Cards.json")
    .then(r => r.text())
    .then(file =>
      JSON.parse(file).forEach((card: any) => {
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
            loyalty: card.Loyalty
          };
        }
      })
    );
  return result;
};

export const fullCollection = initializeFullCollection();

function getNewUserCollection() {
  let newUserCollection: any = {};
  Object.keys(fullCollection).forEach(function(key) {
    newUserCollection[key] = 3;
  });
  return newUserCollection;
}

export const newUserCollection = getNewUserCollection();

export const keywords = {
  Transform: "Transforms the card into another one.",
  Possess: "Acquire the control of a card.",
  Pay: "Trigger card's ability at the cost of additional energy.",
  Sacrifice:
    "Trigger card's ability at the cost of putting it into your scrapyard.",
  Delay: "Postpone an action or effect for written number of rounds",
  Loyalty: "TODO",
  Shield: "Prevent the damage dealt to target.",
  Reflect:
    "Prevent the determined damage and deal that much damage back to its source.",
  Donate: "Transfer the control of the card to another player",
  Health: "An asset or players total restitance to fatal damage",
  Damage: "Reductions to Player health, shields or Card health",
  Trigger: "Activates on conditional interactions",
  Activate: "Change a cards play state form Ready to Depleted",
  Discharge: "Remove specified number of charge counters.",
  Charge: "Add specified number of charge counters."
};

export const craftCost = 1000;
export const salvageValue = 100;

export const packList = ["Set1"];
export const packCosts = { Set1: 1000 };

export const isProduction : boolean = process.env.NODE_ENV === "production"; 
import proto, {
    AttackerAssignment,
    BlockerAssignment,
    Card,
    DamageAssignment,
    DraftState,
    GameState,
    Player
} from "../../protojs/compiled";
import {toKnowledgeCost} from "./Helpers";

export enum ClientPhase {
    NOT_STARTED = "NotStarted",
    UPDATE_GAME = "UpdateGame",
    WAITING = "Waiting",
    WIN = "Win",
    LOSE = "Lose",
    ORDER_CARDS = "OrderCards",
    SELECT_FROM_LIST = "SelectFromList",
    SELECT_FROM_PLAY = "SelectFromPlay",
    SELECT_FROM_HAND = "SelectFromHand",
    SELECT_FROM_DISCARD_PILE = "SelectFromDiscardPile",
    SELECT_FROM_VOID = "SelectFromVoid",
    SELECT_FROM_STACK = "SelectFromStack",
    SELECT_X_VALUE = "SelectXValue",
    SELECT_ATTACKERS = "SelectAttackers",
    ATTACK_PHASE = "AttackPhase",
    SELECT_BLOCKERS = "SelectBlockers",
    BLOCK_PHASE = "BlockPhase",
    ASSIGN_DAMAGE = "AssignDamage",
    SELECT_KNOWLEDGE = "SelectKnowledge",
    PICK_CARD = "PickCard",
}

export const knowledgeMap: any = {};
knowledgeMap[proto.Knowledge.PURPLE] = "P";
knowledgeMap[proto.Knowledge.GREEN] = "G";
knowledgeMap[proto.Knowledge.RED] = "R";
knowledgeMap[proto.Knowledge.BLUE] = "U";
knowledgeMap[proto.Knowledge.YELLOW] = "Y";
knowledgeMap["P"] = proto.Knowledge.PURPLE;
knowledgeMap["U"] = proto.Knowledge.BLUE;
knowledgeMap["R"] = proto.Knowledge.RED;
knowledgeMap["G"] = proto.Knowledge.GREEN;
knowledgeMap["Y"] = proto.Knowledge.YELLOW;

export const knowledgeNameMap: any = {};
knowledgeNameMap[proto.Knowledge.PURPLE] = "Purple";
knowledgeNameMap[proto.Knowledge.GREEN] = "Green";
knowledgeNameMap[proto.Knowledge.RED] = "Red";
knowledgeNameMap[proto.Knowledge.BLUE] = "Blue";
knowledgeNameMap[proto.Knowledge.YELLOW] = "Yellow";
knowledgeNameMap[proto.Knowledge.NONE] = "None";

export interface Dimension {
    width: number;
    height: number;
}

export function initialDimension(): Dimension {
    return {
        width: 0,
        height: 0
    };
}

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

export interface BlockerAssignmentData {
    possibleBlockers: string[];
    blockerAssignments: BlockerAssignment[];
    currentBlocker: string;
    possibleBlockTargets: string[];
}

export function initialBlockerAssignmentData(): BlockerAssignmentData {
    return {
        possibleBlockers: [],
        blockerAssignments: [],
        currentBlocker: "",
        possibleBlockTargets: []
    };
}

export interface DamageAssignmentData {
    damageSource: string;
    possibleTargets: string[];
    totalDamage: number;
    damageAssignments: DamageAssignment[];
    totalAssignedDamage: number;
    trample: boolean;
}

export function initialDamageAssignmentData(): DamageAssignmentData {
    return {
        damageSource: "",
        possibleTargets: [],
        totalDamage: 0,
        damageAssignments: [],
        totalAssignedDamage: 0,
        trample: false,
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
        maxXValue: 0,
    };
}

/*
export interface ArrowData {
  from: string;
  to: string;
}

export function initialArrowData(): ArrowData {
  return {
    from: "",
    to: "",
  };
}
*/

export interface ExtendedGameState {
    opponentUsername: string;
    game: GameState; //GameState object. See proto files
    draft: DraftState; //DraftState object. See proto files
    clientPhase: ClientPhase; //Client defined phases

    message: string;
    gameInitialized: boolean;
    autoPass: boolean;
    win: boolean;

    dialogData: DialogData;
    selectionData: SelectionData;
    attackerAssignmentData: AttackerAssignmentData;
    blockerAssignmentData: BlockerAssignmentData;
    damageAssignmentData: DamageAssignmentData;
}

export function initialExtendedGameState(): ExtendedGameState {
    let game = GameState.create();
    game.player = Player.create();
    game.opponent = Player.create();

    let draft = DraftState.create();
    game.player = Player.create();
    game.opponent = Player.create();
    return {
        opponentUsername: "",
        game: game, //GameState object. See proto files
        draft: draft,
        clientPhase: ClientPhase.NOT_STARTED, //Client defined phases

        message: "",
        gameInitialized: false,
        autoPass: false,
        win: false,

        dialogData: initialDialogData(),
        attackerAssignmentData: initialAttackerAssignmentData(),
        blockerAssignmentData: initialBlockerAssignmentData(),
        damageAssignmentData: initialDamageAssignmentData(),
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
    isAuthenticated: boolean;
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
        packs: {},
        isAuthenticated: false,
    };
}

export interface State {
    extendedGameState: ExtendedGameState;
    firebaseAuthData: any; // Object received from firebase
    profile: Profile;
    windowDimensions: Dimension;
}

export function initialState(): State {
    return {
        extendedGameState: initialExtendedGameState(),
        firebaseAuthData: {}, // Object received from firebase
        profile: initialProfile(),
        windowDimensions: initialDimension(),
    };
}

const initializeFullCollection = () => {
    const result: any = {};
    fetch("/Cards.json")
        .then(r => r.text())
        .then(file =>
            JSON.parse(file).forEach((card: any) => {
                let name = card.Name;
                if (
                    name !== "" //&&
                    //!card.Code.startsWith("Code") //&& !card.Code.startsWith("A")
                ) {
                    result[card.Set + "." + name] = {
                        set: card.Set,
                        name: name,
                        types: [card.Type],
                        subtypes: [card.Subtype],
                        description: card.Effect,
                        combat: {
                            attack: card.Attack,
                            health: card.Health,
                            shield: card.Shield,
                        },
                        cost: card.Energy,
                        knowledgeCost: toKnowledgeCost(card.Knowledge),
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
    Object.keys(fullCollection).forEach(function (key) {
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
        "Trigger card's ability at the cost of putting it into your discardPile.",
    Delay: "Postpone an action or effect for written number of rounds",
    Loyalty: "TODO",
    Shield: "Prevent the damage dealt to target.",
    Reflect:
        "Prevent the determined damage and deal that much damage back to its source.",
    Donate: "Transfer the control of the card to another player",
    Health: "An asset or players total resistance to fatal damage",
    Damage: "Reductions to Player health, shields or Card health",
    Trigger: "Activates on conditional interactions",
    Use: "Change a cards play state form Ready to Used",
    Discharge: "Remove specified number of charge counters.",
    Charge: "Add specified number of charge counters."
};

export const craftCost = 1000;
export const salvageValue = 100;

export const packList = ["Set1"];
export const packCosts = {Set1: 1000};

export const isProduction: boolean = process.env.NODE_ENV === "production";

export const specialCharacters = {
    /*
    "{1}": "①",
    "{2}": "②",
    "{3}": "③",
    "{4}": "④",
    "{5}": "⑤",
    "{6}": "⑥",
    "{7}": "⑦",
    "{8}": "⑧",
    "{9}": "⑨",
    "{0}": "⓪",
    "{U}": "Ⓤ",
    "{G}": "Ⓖ",
    "{P}": "Ⓟ",
    "{R}": "Ⓡ",
    "{Y}": "Ⓨ",
     */
    "{S}": "⧗", //Slow
    "{D}": "↷", //Depleted
    "{Loyalty}": "\uD83D\uDC64",
};

export const colorPalette =
    {
        "black": "#000000",
        "white": "#ffffff",

        //reds
        "light-red"  : "#c80808",
        "red" : "#9f0004",
        "dark-red" : "#6d0101",
        "dark-red-2" : "#510208",

        //blues
        "light-blue" : "#6f92a6",
        "blue"      : "#5e87a4",
        "dark-blue" : "#2c485e",

        //greens
        "light-green" : "#00ba73",
        "green" : "#0e5f4c",
        "dark-green" : "#005301",

        //yellows
        "light-yellow" : "#e6ea00",
        "yellow" : "#bd9a00",

        //purples
        "purple" : "#321637",
    }


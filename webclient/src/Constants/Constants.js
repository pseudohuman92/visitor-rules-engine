import proto from "../protojs/compiled.js";

export const ItemTypes = {
    CARD: 'card',
    FIELD: 'field',
    ALTAR: 'altar',
};

export const FieldIDs = {
    MY_FIELD: 'myField',
    GARY_FIELD: 'garyField',
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
};

export function toKnowledgeString(knowledgeCost) {
    var str = "";

    for (var i = 0; i < knowledgeCost.length; i++) {
        for (var j = 0; j < knowledgeCost[i].count; j++) {
            str = str + knowledgeMap[knowledgeCost[i].knowledge];
        }
    }
    return str;
};

export function toKnowledgeCost(knowledgeString) {
    var cost = {};

    for (var i = 0; i < knowledgeString.length; i++) {
        var c = knowledgeString.charAt(i);
        if (c in knowledgeMap) {
            var v = knowledgeMap[c];
            if (v in cost) {
                cost[v] += 1;
            } else {
                cost[v] = 1
            }
        }

    }

    var res = [];
    for (var k in cost) {
        res.push({ knowledge: parseInt(k), count: cost[k] });
    }
    return res;
};

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
    "X": "Ⓧ",
    "U": "🟐",
    "R": "🟐",
    "B": "🟐",
    "G": "🟐",
    "Y": "🟐",
    "-": ""
}

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
};

const player = {
    id: "player",
    name: "player",
    deckSize: 0,
    energy: 0,
    maxEnergy: 0,
    play: [],
    hand: [],
    scrapyard: [],
    void: [],
    knowledgePool: []
  };

  const opponent = {
    id: "opponent",
    name: "opponent",
    deckSize: 0,
    energy: 0,
    maxEnergy: 0,
    play: [],
    handSize: 0,
    hand: [],
    scrapyard: [],
    void: [],
    knowledgePool: []
  };

 export const emptyGame = {
    id: "Empty Game",
    player: player,
    opponent: opponent,
    turnPlayer: player.id,
    activePlayer: player.id,
    stack: [],
    phase: 0,
    autoPass: false,
    selectCountMax: 0
  };
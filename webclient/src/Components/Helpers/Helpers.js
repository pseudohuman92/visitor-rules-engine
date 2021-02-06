import proto from "../../protojs/compiled";
import { ServerName, PrintDebug } from "../../Config.js";
import {ClientPhase, knowledgeMap, fullCollection, specialCharacters} from "./Constants";

export function GetProfileURL(userId) {
  return `ws://${ServerName}/profiles/${userId}`;
}

export function GetGameURL(userId, gameID, gameType) {
  switch(gameType){
    case proto.GameType.BO1_CONSTRUCTED:
      return `ws://${ServerName}/games/${userId}/${gameID}`;
    case proto.GameType.P2_DRAFT:
      return `ws://${ServerName}/drafts/${userId}/${gameID}`;
  }
}

export function toDeck(decklist) {
  let deck = {}
  decklist.forEach(entry => {
    const sep_entry = entry.split(";");
    deck[sep_entry[1]] = parseInt(sep_entry[0]);
  })
  debugPrint(deck);
  return deck;
}

export function toDecklist(deck) {
  const decklist = [];
  Object.keys(deck).forEach(cardName => {
    let count = deck[cardName];
    if (count <= 3 && count > 0) {
      decklist.push("" + count + ";" + cardName);
    } else {
      return [];
    }
  });
  return decklist;
}

export function debugPrint() {
  if (PrintDebug) {
    console.log(...arguments);
  }
}

export function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

export function spliceToSubarrays(arr, len) {
  let res = [];
  for (let i = 0; i * len < arr.length; i++) {
    res.push(arr.slice(i * len, (i + 1) * len));
  }
  return res;
}

//Deep copy function
export function copy(o) {
  let output, v, key;
  output = Array.isArray(o) ? [] : {};
  for (key in o) {
    v = o[key];
    output[key] = typeof v === "object" ? copy(v) : v;
  }
  return output;
}

export function toIconString(s) {
  let line = "";
  for (let i = 0; i < s.length; i++) {
    const c = s.charAt(i);
    line += c;
  }
  return line;
}

export function getIconColor(knowledgeCost) {
  if (!knowledgeCost || knowledgeCost.length === 0) {
    return "gray";
  }
  const knowlString = toKnowledgeString(knowledgeCost);
  if (knowlString.startsWith("P")) {
    return "purple";
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

export function IsSelectCardPhase(clientPhase) {
  return [
    ClientPhase.SELECT_FROM_LIST,
    ClientPhase.SELECT_FROM_PLAY,
    ClientPhase.SELECT_FROM_HAND,
    ClientPhase.SELECT_FROM_DISCARD_PILE,
    ClientPhase.SELECT_FROM_VOID,
    ClientPhase.SELECT_FROM_STACK
  ].includes(clientPhase);
}

export function toClientPhase(msgType, selectType) {
  switch (msgType) {
    case "SelectFrom":
      switch (selectType) {
        case proto.SelectFromType.LIST:
          return ClientPhase.SELECT_FROM_LIST;
        case proto.SelectFromType.HAND:
          return ClientPhase.SELECT_FROM_HAND;
        case proto.SelectFromType.PLAY:
          return ClientPhase.SELECT_FROM_PLAY;
        case proto.SelectFromType.DISCARD_PILE:
          return ClientPhase.SELECT_FROM_DISCARD_PILE;
        case proto.SelectFromType.VOID:
          return ClientPhase.SELECT_FROM_VOID;
        case proto.SelectFromType.STACK:
          return ClientPhase.SELECT_FROM_STACK;
        default:
          break;
      }
      break;
    case "UpdateGameState":
      return ClientPhase.UPDATE_GAME;
    case "GameEnd":
      return ClientPhase.GAME_END;
    case "OrderCards":
      return ClientPhase.ORDER_CARDS;
    case "SelectXValue":
      return ClientPhase.SELECT_X_VALUE;
    case "SelectAttackers":  
      return ClientPhase.SELECT_ATTACKERS;
    case "SelectBlockers":  
      return ClientPhase.SELECT_BLOCKERS;
    case "AssignDamage":  
      return ClientPhase.ASSIGN_DAMAGE;
    case "SelectKnowledge":
      return ClientPhase.SELECT_KNOWLEDGE;
    case "PickCard":
      return ClientPhase.PICK_CARD;
    default:
      return ClientPhase.WAITING;
  }
}

export function toSelectFromType(clientPhase) {
  switch (clientPhase) {
    case ClientPhase.SELECT_FROM_LIST:
      return proto.SelectFromType.LIST;
    case ClientPhase.SELECT_FROM_HAND:
      return proto.SelectFromType.HAND;
    case ClientPhase.SELECT_FROM_PLAY:
      return proto.SelectFromType.PLAY;
    case ClientPhase.SELECT_FROM_DISCARD_PILE:
      return proto.SelectFromType.DISCARD_PILE;
    case ClientPhase.SELECT_FROM_VOID:
      return proto.SelectFromType.VOID;
    case ClientPhase.SELECT_FROM_STACK:
      return proto.SelectFromType.STACK;
    default:
      return proto.SelectFromType.NONE;
  }
}

export function getCardColor(knowledgeCost) {
  if (!knowledgeCost || knowledgeCost.length === 0) {
    return "darkgray";
  }
  if (knowledgeCost.length > 1) {
    return "orange";
  }

  const knowlString = toKnowledgeString(knowledgeCost);
  if (knowlString.startsWith("P")) {
    return "purple";
  } else if (knowlString.startsWith("U")) {
    return "royalblue";
  } else if (knowlString.startsWith("R")) {
    return "firebrick";
  } else if (knowlString.startsWith("Y")) {
    return "goldenrod";
  } else if (knowlString.startsWith("G")) {
    return "forestgreen";
  } else {
    return "#e6e6e6";
  }
}

export function toKnowledgeString(knowledgeCost) {
  let str = "";
  if (!knowledgeCost){
    return str
  }

  for (let i = 0; i < knowledgeCost.length; i++) {
    for (let j = 0; j < knowledgeCost[i].count; j++) {
      str = str + knowledgeMap[knowledgeCost[i].knowledge];
    }
  }
  return str;
}

export function toKnowledgeCost(knowledgeString) {
  const cost = {};

  for (let i = 0; i < knowledgeString.length; i++) {
    const c = knowledgeString.charAt(i);
    if (c in knowledgeMap) {
      const v = knowledgeMap[c];
      if (v in cost) {
        cost[v] += 1;
      } else {
        cost[v] = 1;
      }
    }
  }

  const res = [];
  for (let k in cost) {
    res.push({ knowledge: parseInt(k), count: cost[k] });
  }
  return res;
}

export function delayClick(onClick, onDoubleClick, delay) {
  let timeoutID = null;
  delay = delay || 200;
  return event => {
    if (!timeoutID) {
      timeoutID = setTimeout(() => {
        if (onClick) {
          onClick(event);
        }
        timeoutID = null;
      }, delay);
    } else {
      timeoutID = clearTimeout(timeoutID);
      onDoubleClick(event);
    }
  };
}

export function compareCardsByKnowledge (a, b){
  if (toKnowledgeString(a.knowledgeCost) > toKnowledgeString(b.knowledgeCost)){
    return 1;
  } else if (toKnowledgeString(a.knowledgeCost) < toKnowledgeString(b.knowledgeCost)){
    return -1;
  } else {
    if (a.cost > b.cost){
      return 1;
    } else if (a.cost < b.cost){
      return -1;
    } else {
      if (a.name > b.name){
        return 1;
      } else if (a.name < b.name){
        return -1;
      } else {
        return 0;
      }
    }
  }
}

export function toFullCards(collection) {
  const col = {};
  Object.keys(collection).forEach(key => {
    col[key] = fullCollection[key];
  });
  return col;
}

export function isObject(item) {
  return (item && typeof item === 'object' && !Array.isArray(item));
}

export function mergeDeep(target, source) {
  let output = Object.assign({}, target);
  if (isObject(target) && isObject(source)) {
    Object.keys(source).forEach(key => {
      if (isObject(source[key])) {
        if (!(key in target))
          Object.assign(output, { [key]: source[key] });
        else
          output[key] = mergeDeep(target[key], source[key]);
      } else {
        Object.assign(output, { [key]: source[key] });
      }
    });
  }
  return output;
}

function replaceAll(string, search, replace) {
  return string.split(search).join(replace);
}

export function replaceSpecialCharacters(inputString, name){
  for (const [key, value] of Object.entries(specialCharacters)) {
    inputString = replaceAll(inputString, key, value);
  }
  return replaceAll(inputString, "{~}", name);
}

export function organizeCards(cardList) {
  let organizedCards = [];
  for (let i = 0; i < cardList.length ; i++){
    let curr = cardList[i];
    console.log("Curr", curr);
    if (!curr.types.includes("Attachment")){
      let attachments = [];
      for (let j = 0; j < cardList.length; j++){
        let candidate = cardList[j];
        if (curr.attachments.includes(candidate.id)){
          attachments.push(candidate);
        }
      }
      organizedCards.push({card : curr, attachments: attachments})
      console.log("Organize Iter", organizedCards);
    }
  }
  console.log("Organize Result", organizedCards);
  return organizedCards;
}
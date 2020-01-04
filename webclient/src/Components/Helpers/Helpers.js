import proto from "../../protojs/compiled";
import { ServerName, PrintDebug } from "../../Config.js";
import {  ClientPhase, knowledgeMap, fullCollection } from "./Constants";

export function GetProfileURL(userId) {
  return `ws://${ServerName}/profiles/${userId}`;
}

export function GetGameURL(userId, gameID) {
  return `ws://${ServerName}/games/${gameID}/${userId}`;
}

export function debugPrint() {
  if (PrintDebug) {
    console.log(...arguments);
  }
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
  var output, v, key;
  output = Array.isArray(o) ? [] : {};
  for (key in o) {
    v = o[key];
    output[key] = typeof v === "object" ? copy(v) : v;
  }
  return output;
}

export function toIconString(s) {
  var line = "";
  for (var i = 0; i < s.length; i++) {
    var c = s.charAt(i);
    line += c;
  }
  return line;
}

export function getIconColor(knowledgeCost) {
  if (!knowledgeCost || knowledgeCost.length === 0) {
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
    return "goldenrod";
  } else {
    return "beige";
  }
}

export function IsSelectCardPhase(clientPhase) {
  return [
    ClientPhase.SELECT_FROM_LIST,
    ClientPhase.SELECT_FROM_PLAY,
    ClientPhase.SELECT_FROM_HAND,
    ClientPhase.SELECT_FROM_SCRAPYARD,
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
        case proto.SelectFromType.SCRAPYARD:
          return ClientPhase.SELECT_FROM_SCRAPYARD;
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
    default:
      return ClientPhase.NOT_STARTED;
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
    case ClientPhase.SELECT_FROM_SCRAPYARD:
      return proto.SelectFromType.SCRAPYARD;
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

  var knowlString = toKnowledgeString(knowledgeCost);
  if (knowlString.startsWith("B")) {
    return "dimgray";
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
  var str = "";
  if (!knowledgeCost){
    return str
  }

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

export function delayClick(onClick, onDoubleClick, delay) {
  var timeoutID = null;
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
  var col = {};
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
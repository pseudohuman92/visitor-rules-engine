import proto from "./protojs/compiled.js";

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

export function getCardColor(knowlString) {
    if (knowlString.startsWith("B")) {
      return "#666666";
    } else if (knowlString.startsWith("U")) {
      return "#0066ff";
    } else if (knowlString.startsWith("R")) {
      return "#ff1a1a";
    } else if (knowlString.startsWith("Y")) {
      return "#ffff00";
    } else {
      return "#e6e6e6";
    }
  };
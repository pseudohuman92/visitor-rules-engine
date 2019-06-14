import { UPDATE_STATE, UPDATE_GAME_STATE, UPDATE_EXTENDED_GAME_STATE } from "./Actions";
import { initialState } from "../Helpers/Constants";
import { debugPrint } from "../Helpers/Helpers";

function rootReducer(state = initialState, action) {
  debugPrint(action.payload);
  if (action.type === UPDATE_STATE) {
    return Object.assign({}, state, action.payload);
  } else if (action.type === UPDATE_EXTENDED_GAME_STATE) {
    let o = Object.assign({}, state);
    o.extendedGameState = Object.assign({}, o.extendedGameState, action.payload);
    return o;
  } else if (action.type === UPDATE_GAME_STATE) {
    let o = Object.assign({}, state);
    o.extendedGameState.game = Object.assign({}, o.extendedGameState.game, action.payload);
    return o;
  }
  return state;
}

export default rootReducer;
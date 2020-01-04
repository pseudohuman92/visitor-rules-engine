import { UPDATE_STATE, UPDATE_GAME_STATE, UPDATE_EXTENDED_GAME_STATE } from "./Actions";
import { debugPrint, mergeDeep } from "../Helpers/Helpers";
import { initialState } from "../Helpers/Constants";

function rootReducer(state = initialState(), action) {
  debugPrint(action);
  if (action.type === UPDATE_STATE) {
    let o = mergeDeep(state, action.payload);
    debugPrint("NEW_STATE: ", o);
    return o;
  } else if (action.type === UPDATE_EXTENDED_GAME_STATE) {
    let o = Object.assign({}, state);
    o.extendedGameState = mergeDeep(o.extendedGameState, action.payload);
    debugPrint("NEW_STATE", o);
    return o;
  } else if (action.type === UPDATE_GAME_STATE) {
    let o = Object.assign({}, state);
    o.extendedGameState.game = mergeDeep(o.extendedGameState.game, action.payload);
    debugPrint("NEW_STATE: ", o);
    return o;
  }
  debugPrint("INITIAL_STATE: ", state);
  return state;
}

export default rootReducer;
import { createStore } from "redux";
import rootReducer from "./Reducer";
import {
  updateState,
  updateGameState,
  updateExtendedGameState
} from "./Actions";

const store = createStore(rootReducer);

export function mapDispatchToProps(dispatch) {
  return {
    updateState: data => dispatch(updateState(data)),
    updateGameState: data => dispatch(updateGameState(data)),
    updateExtendedGameState: data => dispatch(updateExtendedGameState(data))
  };
}

export default store;

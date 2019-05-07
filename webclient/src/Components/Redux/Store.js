import { createStore } from "redux";
import rootReducer from "./Reducer";
import { updateState, updateGameState,updateExtendedGameState, updateHandlers } from "./Actions";
import { debug } from "../../Utils";

const store = createStore(rootReducer);

export function mapDispatchToProps(dispatch) {
    return {
      updateState: data => {
        debug ("Store Update: " + data);
        dispatch(updateState(data))
      },
      updateGameState: data => {
        debug ("Store Update: " + data);
        dispatch(updateGameState(data))
      },
      updateExtendedGameState: data => {
        debug ("Store Update: " + data);
        dispatch(updateExtendedGameState(data))
      }
    };
  }

export default store;
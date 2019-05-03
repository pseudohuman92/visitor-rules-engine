import { INITIALIZE } from "./Actions";

const initialState = {
    coins: 0,
    fullCollection : [],
    collection : [],
    dailyWins : 0,
    decks : {},
    dust : 0,
    username : "",
    packs : {},
    game : {},
};

function rootReducer(state = initialState, action) {
  if (action.type === INITIALIZE) {
    return Object.assign({}, state, action.payload);
  }
  return state;
}

export default rootReducer;
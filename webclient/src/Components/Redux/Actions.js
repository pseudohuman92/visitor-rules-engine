export const UPDATE_STATE = "UPDATE_STATE";
export const UPDATE_GAME_STATE = "UPDATE_GAME_STATE";
export const UPDATE_EXTENDED_GAME_STATE = "UPDATE_EXTENDED_GAME_STATE";

export function updateState(payload) {
  return { type: UPDATE_STATE, payload };
}

export function updateGameState(payload) {
  return { type: UPDATE_GAME_STATE, payload };
}

export function updateExtendedGameState(payload) {
  return { type: UPDATE_EXTENDED_GAME_STATE, payload };
}

import {ServerName} from './Constants.js';

export function GetProfileURL(username) {
  return `ws://${ServerName}/profiles/${username}`;
}

export function GetGameURL(username, gameID) {
  return `ws://${ServerName}/games/${gameID}/${username}`;
}

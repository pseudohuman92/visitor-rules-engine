import {ServerName} from './Config.js';

export function GetProfileURL(username) {
  return `ws://${ServerName}/profiles/${username}`;
}

export function GetGameURL(username, gameID) {
  return `ws://${ServerName}/games/${gameID}/${username}`;
}

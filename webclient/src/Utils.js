import {ServerName, PrintDebug} from './Config.js';

export function GetProfileURL(username) {
  return `ws://${ServerName}/profiles/${username}`;
}

export function GetGameURL(username, gameID) {
  return `ws://${ServerName}/games/${gameID}/${username}`;
}

export function debug() {
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

export function copy(o) {
  var output, v, key;
  output = Array.isArray(o) ? [] : {};
  for (key in o) {
    v = o[key];
    output[key] = typeof v === "object" ? copy(v) : v;
  }
  return output;
}
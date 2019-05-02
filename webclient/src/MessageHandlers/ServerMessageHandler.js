// Handles ServerMessage messages.

import { ProtoSocket } from '../protojs/ProtoSocket.js';
import { GetProfileURL, GetGameURL } from '../Utils.js';
import { GamePhases } from "../Constants/Constants";
import { InitiateConnection, SetGameInfo } from './ServerGameMessageHandler';

let updateViewHandler = null;
let protoSocket = null;
let playerName = null;

//Message handler for ServerMessage messages
function handleMsg(msgType, params) {
    let doUpdate = false;
    if (msgType === 'LoginResponse') {
        if (params.gameIdList.length > 0) {
            InitiateConnection(GetGameURL(playerName, params.gameIdList[0]));
        } else {
            JoinTable(playerName, 'best game');
        }
    } else if (msgType === 'NewGame') {
        SetGameInfo({ gameID: params.game.gameID, gary: params.game.opponent.name });
        InitiateConnection(GetGameURL(params.game.player.name, params.game.id));
        doUpdate = true;
    }

    if (doUpdate) {
        const phase = GamePhases.NOT_STARTED;
        updateViewHandler(params, phase);
    }
}

export function ConnectProfile(username) {
    playerName = username;
    protoSocket = new ProtoSocket(GetProfileURL(username), handleMsg);
}

export function RegisterUpdateViewHandler(handler) {
    updateViewHandler = handler;
}

export function JoinTable(username, tableID) {
    protoSocket.send('JoinTable', {
        tableID: tableID,
        username: username,
        decklist: [],
    });
}

export function RegisterGameConnection(username, gameID) {
    protoSocket.send('RegisterGameConnection', {
        gameID: gameID,
        username: username,
    });
}
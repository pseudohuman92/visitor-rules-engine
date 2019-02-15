import {ServerWSURL} from './Constants.js';

const proto = require('./protojs/compiled.js');

function capitalize(s) {
  return s.charAt(0).toUpperCase() + s.slice(1);
}

function decapitalize(s) {
  return s.charAt(0).toLowerCase() + s.slice(1);
}

export default class ProtoSocket {
  constructor(url, msgHandler) {
    this.socket = new WebSocket(url);
    this.socket.binaryType = 'arraybuffer';
    this.socket.onmessage = event => {
      const msg = proto.ServerGameMessage.decode(new Uint8Array(event.data));
      msgHandler(capitalize(msg.payload), msg[msg.payload]);
    };
  }

  send(msgType, params) {
    if (this.socket.readyState !== 1) {
      console.log('wtf not ready');
    }

    const msgParams = {};
    msgParams[decapitalize(msgType)] = params;
    const msg = proto.ClientGameMessage.create(msgParams);
    const bytes = proto.ClientGameMessage.encode(msg).finish();

    this.socket.send(bytes);
  }
}

export const protoSocket = new ProtoSocket(ServerWSURL, msg => {});

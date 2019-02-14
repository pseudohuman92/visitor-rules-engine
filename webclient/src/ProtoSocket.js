import {ServerWSURL} from './Constants.js';

const jspb = require('google-protobuf');

const cmessages = require('./proto/ClientGameMessages_pb.js');
const smessages = require('./proto/ServerGameMessages_pb.js');

function capitalizeOnlyFirst(s) {
  return s.charAt(0).toUpperCase() + s.slice(1).toLowerCase();
}

export default class ProtoSocket {
  constructor(url, msgHandler) {
    this.socket = new WebSocket(url);
    this.socket.onmessage = event => {
      console.log(event);
      const outerMsg = smessages.ServerGameMessage.deserializeBinary(event);
      const msg = jspb.Message.getField(outerMsg, outerMsg.getPayloadCase());
      console.log(msg);
      msgHandler(msg);
    };
  }

  send(msgType, params) {
    if (this.socket.readyState !== 1) {
      console.log('wtf not ready');
    }

    const msg = new cmessages[msgType]();
    Object.keys(params).forEach(function(key) {
      const setName = 'set' + capitalizeOnlyFirst(key);
      msg[setName](params[key]);
    });

    const setPayloadName = 'set' + capitalizeOnlyFirst(msgType);
    const outerMsg = new cmessages.ClientGameMessage();
    outerMsg[setPayloadName](msg);
    const bytes = outerMsg.serializeBinary();
    this.socket.send(bytes);
  }
}

export const protoSocket = new ProtoSocket(ServerWSURL, msg => {});

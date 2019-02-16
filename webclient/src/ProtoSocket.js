const proto = require('./protojs/compiled.js');

function capitalize(s) {
  return s.charAt(0).toUpperCase() + s.slice(1);
}

function decapitalize(s) {
  return s.charAt(0).toLowerCase() + s.slice(1);
}

export class GameProtoSocket {
  constructor(url, msgHandler) {
    this.socket = new WebSocket(url);
    this.socket.binaryType = 'arraybuffer';
    this.socket.onmessage = event => {
      const msg = proto.ServerGameMessage.decode(new Uint8Array(event.data));
      console.log('[recvGameMsg]', msg);
      msgHandler(capitalize(msg.payload), msg[msg.payload]);
    };
  }

  send = (msgType, params) => {
    // Sends client game messages
    if (this.socket.readyState !== 1) {
      console.log('wtf not ready');
    }

    const msgParams = {};
    msgParams[decapitalize(msgType)] = params;
    const msg = proto.ClientGameMessage.create(msgParams);
    const bytes = proto.ClientGameMessage.encode(msg).finish();

    console.log('[sendGameMsg]', msg);
    this.socket.send(bytes);
  };
}

export class ProtoSocket {
  constructor(url, msgHandler) {
    this.socket = new WebSocket(url);
    this.socket.binaryType = 'arraybuffer';
    this.socket.onmessage = event => {
      const msg = proto.ServerMessage.decode(new Uint8Array(event.data));
      console.log('[recvMsg]', msg);
      msgHandler(capitalize(msg.payload), msg[msg.payload]);
    };
  }

  send = (msgType, params) => {
    // Sends client game messages
    if (this.socket.readyState !== 1) {
      console.log('wtf not ready');
    }

    const msgParams = {};
    msgParams[decapitalize(msgType)] = params;
    const msg = proto.ClientMessage.create(msgParams);
    const bytes = proto.ClientMessage.encode(msg).finish();

    console.log('[sendMsg]', msg);
    this.socket.send(bytes);
  };
}

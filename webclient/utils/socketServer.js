const WebSocket = require('ws');
const jspb = require('google-protobuf');

const cmessages = require('../src/proto/ClientMessages_pb.js');
const smessages = require('../src/proto/ServerMessages_pb.js');

const wss = new WebSocket.Server({port: 8080});

wss.on('connection', function connection(ws) {
  ws.on('message', function incoming(message) {
    console.log('received: %s', message);
    const outerMsg = cmessages.ClientMessage.deserializeBinary(message);
    const msg = jspb.Message.getField(outerMsg, outerMsg.getPayloadCase());
    console.log(msg);
  });
});

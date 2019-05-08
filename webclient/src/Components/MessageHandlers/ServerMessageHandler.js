// Handles ServerMessage messages.
import { ProtoSocket } from "../../protojs/ProtoSocket.js";
import { GetProfileURL, debug } from "../../Utils.js";
import ServerGameMessageHandler from "./ServerGameMessageHandler";

export default class ServerMessageHandler {
  constructor(username, updateHandlers, updateExtendedGameState) {
    this.username = username;
    this.protoSocket = new ProtoSocket(GetProfileURL(this.username), this.handleMsg);
    this.updateHandlers = updateHandlers;
    this.updateExtendedGameState = updateExtendedGameState;
    this.gameId = "";
  }
  
  handleMsg = (msgType, params) => {
    debug("handleMsg is called");
    if (msgType === "LoginResponse") {
      if (params.gameId !== "") {
        this.gameId = params.gameId;
        this.updateHandlers({
          gameMessage: new ServerGameMessageHandler(
            this.username,
            this.gameId,
            this.updateExtendedGameState
          )
        });
      } else {
        debug("Send JoinTable");
        this.JoinTable("best game");
      }
    } else if (msgType === "NewGame") {
      this.gameId = params.game.id;
      this.updateExtendedGameState({
        gameInitialized: true,
        game: params.game
      });
      this.updateHandlers({
        gameMessage: new ServerGameMessageHandler(
          this.username,
          this.gameId,
          this.updateExtendedGameState
        )
      });
    }
  };

  JoinTable = tableID => {
    this.protoSocket.send("JoinTable", {
      tableID: tableID,
      username: this.username,
      decklist: []
    });
  };

  RegisterGameConnection = () => {
    this.protoSocket.send("RegisterGameConnection", {
      gameID: this.gameId,
      username: this.username
    });
  };
}

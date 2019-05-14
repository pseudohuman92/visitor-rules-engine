// Handles ServerMessage messages.
import { ProtoSocket } from "../../protojs/ProtoSocket.js";
import { GetProfileURL, debug } from "../Helpers/Helpers";
import ServerGameMessageHandler from "./ServerGameMessageHandler";

export default class ServerMessageHandler {
  constructor(userId, updateHandlers, updateExtendedGameState) {
    this.userId = userId;
    this.protoSocket = new ProtoSocket(GetProfileURL(this.userId), this.handleMsg);
    this.updateHandlers = updateHandlers;
    this.updateExtendedGameState = updateExtendedGameState;
    this.gameId = "";
  }
  
  handleMsg = (msgType, params) => {
    if (msgType === "LoginResponse") {
      if (params.gameId !== "") {
        this.gameId = params.gameId;
        this.updateHandlers({
          gameHandler: new ServerGameMessageHandler(
            this.userId,
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
        gameHandler: new ServerGameMessageHandler(
          this.userId,
          this.gameId,
          this.updateExtendedGameState
        )
      });
    }
  };

  JoinTable = tableID => {
    this.protoSocket.send("JoinTable", {
      tableID: tableID,
      username: this.userId,
      decklist: []
    });
  };

  RegisterGameConnection = () => {
    this.protoSocket.send("RegisterGameConnection", {
      gameID: this.gameId,
      username: this.userId
    });
  };
}

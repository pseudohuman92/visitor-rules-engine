// Handles ServerMessage messages.
import { ProtoSocket } from "../../protojs/ProtoSocket.js";
import { GetProfileURL } from "../Helpers/Helpers";
import ServerGameMessageHandler from "./ServerGameMessageHandler";

export default class ServerMessageHandler {
  constructor(userId, updateHandlers, updateExtendedGameState, callback) {
    this.userId = userId;
    this.protoSocket = new ProtoSocket(GetProfileURL(this.userId), this.handleMsg);
    this.updateHandlers = updateHandlers;
    this.updateExtendedGameState = updateExtendedGameState;
    this.gameId = "";
    this.callback = callback;
  }
  
  handleMsg = (msgType, params) => {
    if (msgType === "LoginResponse") {
      if (params.gameId !== "") {
        this.gameId = params.gameId;
        this.updateHandlers({
          gameHandler: new ServerGameMessageHandler(
            this.userId,
            this.gameId,
            this.updateExtendedGameState,
            true
          )
        });
        this.callback();
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
          this.updateExtendedGameState,
          false
        )
      });
    }
  };

  joinQueue = decklist => {
    this.protoSocket.send("JoinQueue", {
      decklist: decklist
    });
  };

  RegisterGameConnection = () => {
    this.protoSocket.send("RegisterGameConnection", {
      gameID: this.gameId,
    });
  };

  LoadGame = (filename) => {
    this.protoSocket.send("LoadGame", {
      filename: filename,
    });
  };
}

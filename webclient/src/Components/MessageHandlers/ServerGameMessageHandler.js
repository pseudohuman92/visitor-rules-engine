import { GameProtoSocket } from "../../protojs/ProtoSocket";
import {
  ClientPhase,
  SelectionData,
  ExtendedGameState,
  initialExtendedGameState,
  initialSelectionData,
  initialAttackerAssignmentData,
  initialDialogData
} from "../Helpers/Constants";
import { toClientPhase, toSelectFromType } from "../Helpers/Helpers";
import { GetGameURL } from "../Helpers/Helpers";
import { AttackerAssignment } from "../../protojs/compiled";

export default class ServerGameMessageHandler {

  constructor(
    userId,
    gameId,
    updateExtendedGameState,
    continueGame
  ) {
    this.userId = userId;
    this.updateExtendedGameState = updateExtendedGameState;
    this.gameId = gameId;
    this.continueGame = continueGame;
    this.protoSocket = new GameProtoSocket(
      GetGameURL(userId, gameId),
      this.handleMsg
    );
  }

  //This is a message handler for ServerGameMessage messages
  handleMsg = (msgType, params) => {
    // XXX Remember to update this with the protocol updates
    let newExtendedState = {};
    let selectionData = {};
    newExtendedState["clientPhase"] = toClientPhase(msgType, params.messageType);
    switch (msgType) {
      case "SelectFrom":
        selectionData["selectionCount"] = params.selectionCount;
        selectionData["candidates"] = params.candidates;
        selectionData["selectable"] = params.selectable;
        selectionData["upTo"] = params.upTo;
        if (
          newExtendedState["clientPhase"] === ClientPhase.SELECT_FROM_LIST ||
          newExtendedState["clientPhase"] === ClientPhase.SELECT_FROM_SCRAPYARD ||
          newExtendedState["clientPhase"] === ClientPhase.SELECT_FROM_VOID
        ) {
          newExtendedState["dialogData"] = {
            open: true,
            title: `Select ${params.selectionCount} from the following`,
            cards: params.candidates
          };
        }
        break;
      case "GameEnd":
        newExtendedState["win"] = params.win;
        break;
      case "OrderCards":
        newExtendedState["dialogData"] = {
          open: true,
          title: "Order following cards",
          cards: params.cardsToOrder
        };
        break;
      case "SelectXValue":
        selectionData["maxXValue"] = params.maxXValue;
        break;
      case "SelectAttackers":
        newExtendedState["attackerAssignmentData"] = {
          possibleAttackers : params.possibleAttackers
        };
        break;
      case "SelectBlockers":
        //newExtendedState["possibleBlockers"] = params.possibleBlockers;
        break;
      default:
        break;
    }
    newExtendedState["game"] = params.game;
    selectionData["selected"] = [];
    newExtendedState["selectionData"] = selectionData;
    if (this.continueGame) {
      newExtendedState["gameInitialized"] = true;
      this.continueGame = false;
    }

    this.updateExtendedGameState(newExtendedState);
  };

  send = (msgType, params) => {
    this.protoSocket.send(msgType, params);
  };

  Pass = () => {
    this.send("Pass", {});
  };

  Redraw = () => {
    this.send("Redraw", {});
  };

  Keep = () => {
    this.send("Keep", {});
  };

  Concede = () => {
    this.send("Concede", {});
  };

  PlayCard = (cardID) => {
    this.send("PlayCard", {
      cardID: cardID
    });
  };

  ActivateCard = (cardID) => {
    this.send("ActivateCard", {
      cardID: cardID
    });
  };

  StudyCard = (cardID) => {
    this.send("StudyCard", {
      cardID: cardID
    });
  };

  SelectDone = (clientPhase, selected) => {
    this.send("SelectFromResponse", {
      messageType: toSelectFromType(clientPhase),
      selected: selected
    });
    this.updateExtendedGameState({
      clientPhase: ClientPhase.DONE_SELECT,
      selectionData: initialSelectionData(),
      dialogData: initialDialogData()
    });
  };

  SelectXValue = (xVal) => {
    this.send("SelectXValueResponse", {
      selectedXValue: xVal
    });
    this.updateExtendedGameState({ clientPhase: ClientPhase.DONE_SELECT });
  };

  SelectAttackers = (attackers) => {
    this.send("SelectAttackersResponse", {
      attackers: attackers
    });
    this.updateExtendedGameState({
      clientPhase: ClientPhase.DONE_SELECT,
      attackerAssignmentData: initialAttackerAssignmentData()
    });
  };
}

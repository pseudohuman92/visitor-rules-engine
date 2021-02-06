import {GameProtoSocket} from "../../protojs/ProtoSocket";
import {
    ClientPhase,
    initialSelectionData,
    initialAttackerAssignmentData,
    initialBlockerAssignmentData,
    initialDamageAssignmentData,
    initialDialogData
} from "../Helpers/Constants";
import {sleep, toClientPhase, toSelectFromType} from "../Helpers/Helpers";
import {GetGameURL} from "../Helpers/Helpers";
import {Phase} from "../../protojs/compiled";

export default class ServerGameMessageHandler {

    constructor(
        userId,
        gameId,
        gameType,
        updateExtendedGameState,
        continueGame
    ) {
        this.userId = userId;
        this.updateExtendedGameState = updateExtendedGameState;
        this.gameId = gameId;
        this.continueGame = continueGame;
        this.protoSocket = new GameProtoSocket(
            GetGameURL(userId, gameId, gameType),
            this.handleMsg
        );
    }

    send = (msgType, params) => {
        this.protoSocket.send(msgType, params);
    };

    Pass = () => {
        this.send("Pass", {});
        this.updateExtendedGameState({
            message: "",
            clientPhase: ClientPhase.WAITING,
        });
    };

    Redraw = () => {
        this.send("Redraw", {});
        this.updateExtendedGameState({
            message: "",
            clientPhase: ClientPhase.WAITING,
        });
    };

    Keep = () => {
        this.send("Keep", {});
        this.updateExtendedGameState({
            message: "",
            clientPhase: ClientPhase.WAITING,
        });
    };

    Concede = () => {
        this.send("Concede", {});
        this.updateExtendedGameState({
            message: "",
            clientPhase: ClientPhase.WAITING,
        });
    };

    PlayCard = (cardID) => {
        this.send("PlayCard", {
            cardID: cardID
        });
        this.updateExtendedGameState({
            message: "",
            clientPhase: ClientPhase.WAITING,
        });
    };

    ActivateCard = (cardID) => {
        this.send("ActivateCard", {
            cardID: cardID
        });
        this.updateExtendedGameState({
            message: "",
            clientPhase: ClientPhase.WAITING,
        });
    };

    StudyCard = (cardID) => {
        this.send("StudyCard", {
            cardID: cardID
        });
        this.updateExtendedGameState({
            message: "",
            clientPhase: ClientPhase.WAITING,
        });
    };

    SelectDone = (clientPhase, selected) => {
        this.send("SelectFromResponse", {
            messageType: toSelectFromType(clientPhase),
            selected: selected
        });
        this.updateExtendedGameState({
            message: "",
            clientPhase: ClientPhase.WAITING,
            selectionData: initialSelectionData(),
            dialogData: initialDialogData()
        });
    };

    SelectXValue = (xVal) => {
        this.send("SelectXValueResponse", {
            selectedXValue: xVal
        });
        this.updateExtendedGameState({message: "", clientPhase: ClientPhase.WAITING});
    };

    SelectAttackers = (attackers) => {
        this.send("SelectAttackersResponse", {
            attackers: attackers
        });
        this.updateExtendedGameState({
            message: "",
            clientPhase: ClientPhase.WAITING,
            attackerAssignmentData: initialAttackerAssignmentData()
        });
    };

    SelectBlockers = blockers => {
        this.send("SelectBlockersResponse", {
            blockers: blockers
        });
        this.updateExtendedGameState({
            message: "",
            clientPhase: ClientPhase.WAITING,
            blockerAssignmentData: initialBlockerAssignmentData()
        });
    };

    AssignDamage = damageAssignments => {
        this.send("AssignDamageResponse", {
            damageAssignments: damageAssignments
        });
        this.updateExtendedGameState({
            message: "",
            clientPhase: ClientPhase.WAITING,
            damageAssignmentData: initialDamageAssignmentData()
        });
    };

    SelectKnowledge = selected => {
        this.send("SelectKnowledgeResponse", {
            selectedKnowledge: selected
        });
        this.updateExtendedGameState({
            message: "",
            clientPhase: ClientPhase.WAITING,
            dialogData: initialDialogData()
        });

    }

    PickCard = selected => {
        this.send("PickCardResponse", {
            pickedCard: selected
        });
        this.updateExtendedGameState({
            message: "",
            clientPhase: ClientPhase.WAITING,
            dialogData: initialDialogData()
        });
    }

    SaveGameState = filename => {
        this.send("SaveGameState", {
            filename: filename
        });
    };

    //This is a message handler for ServerGameMessage messages
    handleMsg = (msgType, params) => {
        // TODO: Remember to update this with the protocol updates
        let newExtendedState = {};
        let selectionData = {};
        newExtendedState["clientPhase"] = toClientPhase(msgType, params.messageType);
        newExtendedState["message"] = "";
        switch (msgType) {
            case "SelectFrom":
                selectionData["selectionCount"] = params.selectionCount;
                selectionData["candidates"] = params.candidates;
                selectionData["selectable"] = params.selectable;
                selectionData["upTo"] = params.upTo;
                newExtendedState["message"] = params.message;
                if (
                    newExtendedState["clientPhase"] === ClientPhase.SELECT_FROM_LIST ||
                    newExtendedState["clientPhase"] === ClientPhase.SELECT_FROM_DISCARD_PILE ||
                    newExtendedState["clientPhase"] === ClientPhase.SELECT_FROM_VOID
                ) {
                    newExtendedState["dialogData"] = {
                        open: true,
                        title: params.message,
                        cards: params.candidates
                    };
                }
                break;
            case "GameEnd":
                newExtendedState["clientPhase"] = params.win ? ClientPhase.WIN : ClientPhase.LOSE;
                newExtendedState["gameInitialized"] = false;
                break;
            case "OrderCards":
                newExtendedState["dialogData"] = {
                    open: true,
                    title: "Order following cards",
                    cards: params.cardsToOrder
                };
                break;
            case "SelectXValue":
                newExtendedState["message"] = "Select X value";
                selectionData["maxXValue"] = params.maxXValue;
                break;
            case "SelectAttackers":
                newExtendedState["message"] = "Select attackers.";
                newExtendedState["attackerAssignmentData"] = {
                    possibleAttackers: params.possibleAttackers
                };
                break;
            case "SelectBlockers":
                newExtendedState["message"] = "Select blockers.";
                newExtendedState["blockerAssignmentData"] = {
                    possibleBlockers: params.possibleBlockers
                };
                break;
            case "AssignDamage":
                newExtendedState["message"] = "Assign combat damage.";
                newExtendedState["damageAssignmentData"] = {
                    damageSource: params.damageSource,
                    possibleTargets: params.possibleTargets,
                    totalDamage: params.totalDamage,
                    trample: params.trample
                };
                break;
            case "SelectKnowledge":
                newExtendedState["dialogData"] = {
                    open: true,
                    title: "Select a knowledge type.",
                    cards: params.knowledgeList,
                };
                break;
            case "PickCard":
                newExtendedState["dialogData"] = {
                    open: true,
                    title: params.message,
                    cards: params.candidates,
                };
                break;
            default:
                let game = params.game;
                if (
                    game.phase !== Phase.REDRAW &&
                    game.activePlayer === game.player.id &&
                    game.canStudy.length === 0 &&
                    game.canActivate.length === 0 &&
                    game.canPlay.length === 0
                ) {
                    newExtendedState["clientPhase"] = ClientPhase.WAITING;
                    setTimeout(this.Pass, 500);
                }
                if (
                    game.phase === Phase.REDRAW &&
                    game.activePlayer === game.player.id &&
                    game.player.hand.length === 0
                ) {
                    this.Keep();
                }
                break;
        }
        if(params.game && params.game.player && !params.game.player.energy){
            params.game.player.energy = 0;
        }
        if(params.game && params.game.opponent && !params.game.opponent.energy){
            params.game.opponent.energy = 0;
        }
        newExtendedState["game"] = params.game;

        newExtendedState["draft"] = params.draft;
        selectionData["selected"] = [];
        newExtendedState["selectionData"] = selectionData;
        if (msgType !== "PickCard"){
            newExtendedState["gameInitialized"] = true;
        }
        if (this.continueGame) {
            newExtendedState["gameInitialized"] = true;
            this.continueGame = false;
        }

        this.updateExtendedGameState(newExtendedState);
    };
}

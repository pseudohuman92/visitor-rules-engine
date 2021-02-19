import React from "react";
import { connect } from "react-redux";
import { knowledgeMap, ClientPhase } from "../Helpers/Constants";

import "../../css/Utils.css";
import { mapDispatchToProps } from "../Redux/Store";
import { withFirebase } from "../Firebase";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import TextOnImage from "../Primitives/TextOnImage";
import Countdown, {zeroPad} from "react-countdown";
import {Tooltip} from "@material-ui/core";

const mapStateToProps = state => {
  return {
    windowDimensions: state.windowDimensions,

    playerId: state.extendedGameState.game.player.id,
    playerName: state.profile.username,
    playerHealth: state.extendedGameState.game.player.health,
    playerEnergy: state.extendedGameState.game.player.energy,
    playerMaxEnergy: state.extendedGameState.game.player.maxEnergy,
    playerKnowledgePool: state.extendedGameState.game.player.knowledgePool,
    playerTimeRemaining: state.extendedGameState.game.player.time,
    playerDeckColors: state.extendedGameState.game.player.deckColors,

    opponentId: state.extendedGameState.game.opponent.id,
    opponentUserId: state.extendedGameState.game.opponent.username,
    opponentName: state.extendedGameState.opponentUsername,
    opponentHealth: state.extendedGameState.game.opponent.health,
    opponentEnergy: state.extendedGameState.game.opponent.energy,
    opponentMaxEnergy: state.extendedGameState.game.opponent.maxEnergy,
    opponentKnowledgePool: state.extendedGameState.game.opponent.knowledgePool,
    opponentTimeRemaining: state.extendedGameState.game.opponent.time,
    opponentDeckColors: state.extendedGameState.game.opponent.deckColors,

    selected: state.extendedGameState.selectionData.selected,
    selectable: state.extendedGameState.selectionData.selectable,
    selectionCount: state.extendedGameState.selectionData.selectionCount,

    attackerAssignmentData: state.extendedGameState.attackerAssignmentData,

    clientPhase: state.extendedGameState.clientPhase,
    activePlayer: state.extendedGameState.game.activePlayer,
    playerUserId: state.extendedGameState.game.player.username
  };
};

class PlayerDisplay extends React.Component {
  componentDidMount() {
    const {
      isPlayer,
      opponentName,
      opponentUserId,
      firebase,
      updateExtendedGameState
    } = this.props;
    if (!isPlayer && opponentName === "") {
      firebase.setOpponentUsername(opponentUserId, updateExtendedGameState);
    }
  }

  select = event => {
    let { isPlayer, playerId, opponentId } = this.props;
    let id = isPlayer ? playerId : opponentId;
    let maxCount = this.props.selectionCount;
    let selected = [...this.props.selected];
    let clientPhase = this.props.clientPhase;

    if (selected.length < maxCount) {
      selected.push(id);
      this.props.updateExtendedGameState({
        selectionData: { selected: selected }
      });
    }
    if (selected.length === maxCount) {
      this.props.gameHandler.SelectDone(clientPhase, selected);
    }
  };

  unselect = event => {
    let { isPlayer, playerId, opponentId } = this.props;
    let id = isPlayer ? playerId : opponentId;
    let selected = [...this.props.selected];

    if (selected.includes(id)) {
      selected.splice(selected.indexOf(id), 1);
      this.props.updateExtendedGameState({
        selectionData: {
          selected: selected
        }
      });
    }
  };

  setAttacked = event => {
    let { isPlayer, playerId, opponentId } = this.props;
    let id = isPlayer ? playerId : opponentId;

    let attackerAssignments = [
      ...this.props.attackerAssignmentData.attackerAssignments
    ];
    let currentAttacker = this.props.attackerAssignmentData.currentAttacker;

    attackerAssignments.push({ attackerId: currentAttacker, attacksTo: id });
    this.props.updateExtendedGameState({
      attackerAssignmentData: {
        currentAttacker: "",
        possibleAttackTargets: [],
        attackerAssignments: attackerAssignments
      }
    });
  };

  render() {
    const {
      isPlayer,
      playerId,
      opponentId,
      playerName,
      opponentName,
      playerHealth,
      opponentHealth,
      selected,
      selectable,
      playerEnergy,
      opponentEnergy,
      playerMaxEnergy,
      opponentMaxEnergy,
      playerKnowledgePool,
      opponentKnowledgePool,
      playerTimeRemaining,
      opponentTimeRemaining,
      playerDeckColors,
      opponentDeckColors,
      activePlayer,
      playerUserId,
      opponentUserId,
      clientPhase,
      attackerAssignmentData,
      windowDimensions
    } = this.props;

    const windowWidth = windowDimensions.width;
    const windowHeight = windowDimensions.height;
    const width = windowWidth / 20;
    const heightRound = windowHeight/20;

    const id = isPlayer ? playerId : opponentId;
    // const userId = isPlayer ? playerUserId : opponentUserId;
    // const name = isPlayer ? playerName : opponentName;
    const health = isPlayer ? playerHealth : opponentHealth;
    const energy = isPlayer ? playerEnergy : opponentEnergy;
    const maxEnergy = isPlayer ? playerMaxEnergy : opponentMaxEnergy;
    const knowledgePool = isPlayer
      ? playerKnowledgePool
      : opponentKnowledgePool;

    const deckColors = isPlayer ? playerDeckColors : opponentDeckColors;

    const timeRemaining = isPlayer ? playerTimeRemaining : opponentTimeRemaining;

    const selectable_ = selectable.includes(id);
    const selected_ = selected.includes(id);

    const isActive = activePlayer === id;

    const canBeAttacked =
      clientPhase === ClientPhase.SELECT_ATTACKERS &&
      attackerAssignmentData.currentAttacker &&
      attackerAssignmentData.possibleAttackTargets.includes(id);

    let borderColor = undefined;
    let clickHandler = undefined;
    if (selectable_) {
      clickHandler = this.select;
      borderColor = "green";
    } else if (selected_) {
      borderColor = "blue";
      clickHandler = this.unselect;
    } else if (canBeAttacked) {
      clickHandler = this.setAttacked;
      borderColor = "green";
    }

    const borderStyle = activePlayer === id ?
        {
          backgroundImage: "url(" + process.env.PUBLIC_URL + "/img/player-display-grunge-red.png)",
          backgroundPosition: "center",
          backgroundSize: "100% 100%",
          backgroundRepeat: "no-repeat",
        } : {
          backgroundImage: "url(" + process.env.PUBLIC_URL + "/img/player-display-grunge-white.png)",
          backgroundPosition: "center",
          backgroundSize: "100% 100%",
          backgroundRepeat: "no-repeat",
        };

    return (
      <div
        style={{
          height: "100%",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          ...borderStyle,
        }}
      >
        <div
          style={{
            width: (width * 0.9) / 2,
            flexGrow: 9,
            height: heightRound,
            display: "flex",
            flexDirection: "row-reverse",
            position: "relative"
          }}
        >
          {maxEnergy &&
            Array(Math.max(maxEnergy, energy))
              .fill(null)
              .map((c, i) => (

                  <img
                    src={
                      process.env.PUBLIC_URL +
                      "/img/card-components/energy-black.png"
                    }
                    style={{
                      maxWidth: "100%",
                      maxHeight: "100%",
                      objectFit: "scale-down",
                      position: "absolute",
                      right: 5 * i + "%",
                      zIndex: maxEnergy - i,
                      opacity: energy ? (i >= energy ? 0.5 : 1) : 0.5
                    }}
                    alt=""
                  />
              ))}
        </div>
          <div
            className={id}
            onClick={clickHandler}
            style={{
              flexGrow: 2,
              height: heightRound,
              margin: "0 4% 0 4%",
            }}
          >

            <Tooltip placement={isPlayer? "top" : "bottom"}
                title={
              <div
                style={{
                  display: "flex",
                  justifyContent: "center"
                }}
            >
              {deckColors.map((k, i) => (
                  <img
                      key={i}
                      src={
                        process.env.PUBLIC_URL +
                        "/img/card-components/knowledge-" +
                        knowledgeMap[k.knowledge] +
                        ".png"
                      }
                      style={{
                        maxWidth: "20%",
                        maxHeight: "20%",
                        objectFit: "scale-down",
                      }}
                      alt=""
                  />
              ))}
            </div>} >
                <TextOnImage
                    style={{color: "white"}}
                  src={
                    process.env.PUBLIC_URL +
                    "/img/card-components/health-black.png"
                  }
                  text={health}
                  min={1}
                  max={25}
                  scale={2}
                  windowDimensions={windowDimensions}
                />
          </Tooltip>
          </div>
          <div
              className="timer"
              style={{
                color: "black",
                textAlign: "center"
              }}
          >
            {isActive ? <Countdown date={Date.now() + (timeRemaining)}
                                   onTick={delta =>
                                       this.props.updateExtendedGameState({game: (isPlayer? {player: {time: delta.total}} :
                                         {opponent: {time: delta.total}})})}
                                   renderer={props => <div>{zeroPad(props.minutes) + ":" + zeroPad(props.seconds)}</div>} />
            : ("" + zeroPad(Math.floor((timeRemaining / 1000) / 60))+":"+zeroPad((timeRemaining/1000) % 60)) }
          </div>
        <div
          style={{
            width: (width * 0.9) / 2,
            height: heightRound * 0.75,
            flexGrow: 9,
            display: "flex",
            alignSelf: "center"
          }}
        >
          {knowledgePool.map((k, i) => (
            <div
              key={i}
              style={{
                width: (width * 0.9) / 2,
                height: heightRound,
                position: "relative"
              }}
            >
              {Array(k.count)
                .fill(null)
                .map((c, j) => (
                  <img
                    key={j}
                    src={
                      process.env.PUBLIC_URL +
                      "/img/card-components/knowledge-" +
                      knowledgeMap[k.knowledge] +
                      ".png"
                    }
                    style={{
                      maxWidth: "100%",
                      maxHeight: "100%",
                      objectFit: "scale-down",
                      position: "absolute",
                      left: 50 * j + "%",
                      zIndex: k.count - j
                    }}
                    alt=""
                  />
                ))}
            </div>
          ))}
        </div>
      </div>
    );
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withHandlers(withFirebase(PlayerDisplay)));

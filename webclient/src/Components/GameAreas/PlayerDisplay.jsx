import React from "react";
import { connect } from "react-redux";
import { knowledgeMap, ClientPhase } from "../Helpers/Constants";

import "../../css/Utils.css";
import { mapDispatchToProps } from "../Redux/Store";
import { withFirebase } from "../Firebase";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import TextOnImage from "../Primitives/TextOnImage";
import { ArcherElement } from "react-archer";

const mapStateToProps = state => {
  return {
    windowDimensions: state.windowDimensions,

    playerId: state.extendedGameState.game.player.id,
    playerName: state.profile.username,
    playerHealth: state.extendedGameState.game.player.health,
    playerEnergy: state.extendedGameState.game.player.energy,
    playerMaxEnergy: state.extendedGameState.game.player.maxEnergy,
    playerKnowledgePool: state.extendedGameState.game.player.knowledgePool,

    opponentId: state.extendedGameState.game.opponent.id,
    opponentUserId: state.extendedGameState.game.opponent.userId,
    opponentName: state.extendedGameState.opponentUsername,
    opponentHealth: state.extendedGameState.game.opponent.health,
    opponentEnergy: state.extendedGameState.game.opponent.energy,
    opponentMaxEnergy: state.extendedGameState.game.opponent.maxEnergy,
    opponentKnowledgePool: state.extendedGameState.game.opponent.knowledgePool,

    selected: state.extendedGameState.selectionData.selected,
    selectable: state.extendedGameState.selectionData.selectable,
    selectionCount: state.extendedGameState.selectionData.selectionCount,

    attackerAssignmentData: state.extendedGameState.attackerAssignmentData,

    clientPhase: state.extendedGameState.clientPhase,
    activePlayer: state.extendedGameState.game.activePlayer,
    playerUserId: state.extendedGameState.game.player.userId
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
    const userId = isPlayer ? playerUserId : opponentUserId;
    const name = isPlayer ? playerName : opponentName;
    const health = isPlayer ? playerHealth : opponentHealth;
    const energy = isPlayer ? playerEnergy : opponentEnergy;
    const maxEnergy = isPlayer ? playerMaxEnergy : opponentMaxEnergy;
    const knowledgePool = isPlayer
      ? playerKnowledgePool
      : opponentKnowledgePool;

    const selectable_ = selectable.includes(id);
    const selected_ = selected.includes(id);

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

    return (
      <div
        style={{
          height: "100%",
          display: "flex",
          justifyContent: "center",
          alignItems: "center"
        }}
      >
        <div
          style={{
            width: (width * 0.9) / 2,
            flexGrow: 9,
            height: heightRound,
            display: "flex",
            flexDirection: "row-reverse"
          }}
        >
          {maxEnergy &&
            Array(maxEnergy)
              .fill(null)
              .map((c, i) => (
                <div
                  key={i}
                  style={{
                    height: heightRound * 0.75,
                    alignSelf: "center",
                    marginLeft: "1%"
                  }}
                >
                  <img
                    src={
                      process.env.PUBLIC_URL +
                      "/img/card-components/energy-display.png"
                    }
                    style={{
                      maxWidth: "100%",
                      maxHeight: "100%",
                      objectFit: "scale-down",
                      opacity: i >= energy ? 0.3 : 1
                    }}
                    alt=""
                  />
                </div>
              ))}
        </div>
        <ArcherElement id={id}>
          <div
            onClick={clickHandler}
            style={{
              flexGrow: 2,
              height: heightRound,
              margin: "0 4% 0 4%"
            }}
          >
            <TextOnImage
              style={{
                backgroundColor: borderColor
              }}
              src={
                process.env.PUBLIC_URL +
                "/img/card-components/health" +
                (activePlayer === userId ? "-active" : "") +
                ".png"
              }
              text={health}
              min={1}
              max={15}
              scale={2}
              windowDimensions={windowDimensions}
            />
          </div>
        </ArcherElement>
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
                height: heightRound * 0.75,
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
                      left: 30 * j + "%",
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

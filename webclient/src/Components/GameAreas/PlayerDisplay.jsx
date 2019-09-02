import React from "react";
import { connect } from "react-redux";
import { knowledgeMap } from "../Helpers/Constants";

import "../../css/Utils.css";
import { mapDispatchToProps } from "../Redux/Store";
import { withFirebase } from "../Firebase";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import TextOnImage from "../Primitives/TextOnImage";
import { withSize } from "react-sizeme";

const mapStateToProps = state => {
  return {
    playerId: state.extendedGameState.game.player.id,
    playerName: state.username,
    playerHealth: state.extendedGameState.game.player.health,
    opponentId: state.extendedGameState.game.opponent.id,
    opponentUserId: state.extendedGameState.game.opponent.userId,
    opponentName: state.extendedGameState.opponentUsername,
    opponentHealth: state.extendedGameState.game.opponent.health,
    selectedCards: state.extendedGameState.selectedCards,
    selectablePlayers: state.extendedGameState.selectablePlayers,
    displayTargets: state.extendedGameState.targets,
    phase: state.extendedGameState.phase,
    selectCountMax: state.extendedGameState.selectCountMax,
    playerEnergy: state.extendedGameState.game.player.energy,
    playerMaxEnergy: state.extendedGameState.game.player.maxEnergy,
    playerKnowledgePool: state.extendedGameState.game.player.knowledgePool,
    opponentEnergy: state.extendedGameState.game.opponent.energy,
    opponentMaxEnergy: state.extendedGameState.game.opponent.maxEnergy,
    opponentKnowledgePool: state.extendedGameState.game.opponent.knowledgePool,
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

  render() {
    const {
      isPlayer,
      playerId,
      opponentId,
      playerName,
      opponentName,
      playerHealth,
      opponentHealth,
      selectedCards,
      selectablePlayers,
      displayTargets,
      phase,
      selectCountMax,
      playerEnergy,
      opponentEnergy,
      playerMaxEnergy,
      opponentMaxEnergy,
      playerKnowledgePool,
      opponentKnowledgePool,
      activePlayer,
      playerUserId,
      opponentUserId
    } = this.props;

    const { width, height } = this.props.size;
    const heightRound = Math.floor(height);

    const id = isPlayer ? playerId : opponentId;
    const userId = isPlayer ? playerUserId : opponentUserId;
    const name = isPlayer ? playerName : opponentName;
    const health = isPlayer ? playerHealth : opponentHealth;
    const energy = isPlayer ? playerEnergy : opponentEnergy;
    const maxEnergy = isPlayer ? playerMaxEnergy : opponentMaxEnergy;
    const knowledgePool = isPlayer
      ? playerKnowledgePool
      : opponentKnowledgePool;

    const selectable = selectablePlayers.includes(id);
    const selected = selectedCards.includes(id);
    const targeted = displayTargets.includes(id);

    let select = event => {
      let maxCount = selectCountMax;
      let selected = [...selectedCards];
      if (selected.length < maxCount) {
        selected.push(id);
        this.props.updateExtendedGameState({ selectedCards: selected });
      }
      if (selected.length === maxCount) {
        this.props.gameHandler.SelectDone(phase, selected);
      }
    };

    let unselect = event => {
      let selected = [...selectedCards];

      if (selected.includes(id)) {
        selected.splice(selected.indexOf(id), 1);
        this.props.updateExtendedGameState({
          selectedCards: selected
        });
      }
    };

    console.log("ID:", userId, "AP:", activePlayer);

    let borderColor = undefined;
    let clickHandler = undefined;
    if (targeted) {
      borderColor = "yellow";
    } else if (selectable) {
      clickHandler = select;
      borderColor = "green";
    } else if (selected) {
      borderColor = "magenta";
      clickHandler = unselect;
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
          {Array(maxEnergy)
            .fill(null)
            .map((c, i) => (
              <div
                key={i}
                style={{
                  height: heightRound *0.75,
                  alignSelf: "center",
                  marginLeft:"1%"
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
            src={process.env.PUBLIC_URL + "/img/card-components/health"+(activePlayer === userId?"-active":"")+".png"}
            text={health}
            min={1}
            max={15}
            scale={2}
          />
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
                width: (width * 0.9) / 2 / 6,
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
                      left: 15 * j + "%",
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
)(withHandlers(withFirebase(withSize({ monitorHeight: true })(PlayerDisplay))));

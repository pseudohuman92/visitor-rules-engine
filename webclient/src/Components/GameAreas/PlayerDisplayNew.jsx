import React from "react";
import { connect } from "react-redux";
import { knowledgeMap } from "../Helpers/Constants";

import "../../css/StateDisplay.css";
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
    playerVoid: state.extendedGameState.game.player.void,
    playerHealth: state.extendedGameState.game.player.health,
    opponentId: state.extendedGameState.game.opponent.id,
    opponentUserId: state.extendedGameState.game.opponent.userId,
    opponentName: state.extendedGameState.opponentUsername,
    opponentVoid: state.extendedGameState.game.opponent.void,
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
    opponentKnowledgePool: state.extendedGameState.game.opponent.knowledgePool
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
      playerVoid,
      opponentVoid,
      playerHealth,
      opponentHealth,
      selectedCards,
      selectablePlayers,
      displayTargets,
      phase,
      selectCountMax,
      updateExtendedGameState,
      playerEnergy,
      opponentEnergy,
      playerMaxEnergy,
      opponentMaxEnergy,
      playerKnowledgePool,
      opponentKnowledgePool
    } = this.props;

    const { width, height_ } = this.props.size;
    const height = Math.floor(height_);
    console.log("<PLAYER DISPLAY> width: " + width + " height_: " + height_ + " height: " + height);

    const id = isPlayer ? playerId : opponentId;
    const name = isPlayer ? playerName : opponentName;
    const void_ = isPlayer ? playerVoid : opponentVoid;
    const health = isPlayer ? playerHealth : opponentHealth;
    const energy = isPlayer ? playerEnergy : opponentEnergy;
    const maxEnergy = isPlayer ? playerMaxEnergy : opponentMaxEnergy;
    const knowledgePool = isPlayer
      ? playerKnowledgePool
      : opponentKnowledgePool;

    const selectable = selectablePlayers.includes(id);
    const selected = selectedCards.includes(id);
    const targeted = displayTargets.includes(id);

    let voidOnClick = event => {
      updateExtendedGameState({
        dialog: {
          open: true,
          title: `${name}'s Void`,
          cards: void_
        }
      });
    };

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
          maxHeight: "100%",
          display: "flex",
          alignItems: "center"
        }}
      >
        <div
          style={{
            width: (width * 0.9) / 2,
            flexGrow: 9,
          }}
        >
          {name}
        </div>
        <div
          style={{ width: width * 0.1, maxHeight:"100%", flexGrow: 2}}
        >
          <TextOnImage
            style={{ backgroundColor: borderColor }}
            src={process.env.PUBLIC_URL + "/img/card-components/health.png"}
            text={health}
            min={1}
          />
        </div>
        <div
          style={{
            width: (width * 0.9) / 2,
            flexGrow: 9,
            display: "flex"
          }}
        >
          <div
            style={{
              width: (width * 0.9) / 2 / 2,
              display: "flex"
            }}
          >
            {Array(maxEnergy)
              .fill(null)
              .map((c, i) => (
                <div
                  key={i}
                  style={{
                    width: (width * 0.9) / 2 / 2 / maxEnergy,
                    flexGrow: 1
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
            style={{
              width: (width * 0.9) / 2 / 2,
              flexGrow: 1,
              display: "flex"
            }}
          >
            {knowledgePool.map((k, i) => (
              <div
                key={i}
                style={{
                  width: (width * 0.9) / 2 / 2 / knowledgePool.length,
                  flexGrow: 1,
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
            {/*Void: {void_.length}*/}
          </div>
        </div>
      </div>
    );
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withHandlers(withFirebase(withSize({ monitorHeight: true })(PlayerDisplay))));

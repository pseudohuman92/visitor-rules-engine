import React, { Component } from "react";
import { connect } from "react-redux";

import { knowledgeMap } from "../Constants/Constants";

import "../../css/ResourceArea.css";

const mapStateToProps = state => {
  return {
    playerEnergy: state.extendedGameState.game.player.energy,
    playerMaxEnergy: state.extendedGameState.game.player.maxEnergy,
    playerKnowledgePool: state.extendedGameState.game.player.knowledgePool,
    opponentEnergy: state.extendedGameState.game.opponent.energy,
    opponentMaxEnergy: state.extendedGameState.game.opponent.maxEnergy,
    opponentKnowledgePool: state.extendedGameState.game.opponent.knowledgePool
  };
};

class ResourceArea extends Component {
  render() {
    const {
      isPlayer,
      playerEnergy,
      opponentEnergy,
      playerMaxEnergy,
      opponentMaxEnergy,
      playerKnowledgePool,
      opponentKnowledgePool
    } = this.props;

    const energy = isPlayer ? playerEnergy : opponentEnergy;
    const maxEnergy = isPlayer ? playerMaxEnergy : opponentMaxEnergy;
    const knowledgePool = isPlayer
      ? playerKnowledgePool
      : opponentKnowledgePool;

    return (
      <div>
        <section id="resourceArea" className="resource-area">
          <div className="mana-area">
            <span>
              {energy} / {maxEnergy}
            </span>
          </div>
          <div className="knowledge-area">
            {knowledgePool.map((k, i) => (
              <div className="knowledge" key={i}>
                <span
                  className={
                    "knowledge-stone " +
                    knowledgeMap[k.knowledge] +
                    "-knowledge"
                  }
                />
                {k.count}
              </div>
            ))}
          </div>
        </section>
      </div>
    );
  }
}

export default connect(mapStateToProps)(ResourceArea);

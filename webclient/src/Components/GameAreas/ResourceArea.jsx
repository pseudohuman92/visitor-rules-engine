import React, { Component } from "react";
import { connect } from "react-redux";

import { knowledgeMap } from "../Helpers/Constants";

import "../../css/ResourceArea.css";
import { Grid, Divider } from "@material-ui/core";

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
      <Grid
        container
        justify="center"
        alignItems="center"
        style={{ width: "100%", height: "100%", backgroundColor: "#b1b1b1" }}
      >
        <Grid item xs={12} style={{ height: "50%" }}>
          {energy} / {maxEnergy}
        </Grid>
        <Grid item xs={12}>
          <Divider />
        </Grid>
        <Grid item container xs={12} style={{ height: "50%" }}>
        {knowledgePool.map((k, i) => (
          <Grid item xs={2} key={i}>
            <span
              className={
                "knowledge-stone " + knowledgeMap[k.knowledge] + "-knowledge"
              }
            />
            {": " + k.count}
          </Grid>
        ))}
        </Grid>
      </Grid>
    );
  }
}

export default connect(mapStateToProps)(ResourceArea);

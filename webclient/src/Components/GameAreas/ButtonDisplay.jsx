import React, { Component } from "react";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Button from "@material-ui/core/Button";
import Textfit from "react-textfit";
import { connect } from "react-redux";

import proto from "../../protojs/compiled.js";

import { GamePhases } from "../Helpers/Constants";
import { IsSelectCardPhase } from "../Helpers/Helpers";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import "../../css/StateDisplay.css";
import "../../css/Utils.css";

const mapStateToProps = state => {
  return {
    gamePhase: state.extendedGameState.game.phase,
    playerName: state.username,
    playerUserId: state.extendedGameState.game.player.userId,
    opponentName: state.extendedGameState.opponentUsername,
    activePlayer: state.extendedGameState.game.activePlayer,
    turnPlayer: state.extendedGameState.game.turnPlayer,
    phase: state.extendedGameState.phase,
    upTo: state.extendedGameState.upTo,
    autoPass: state.extendedGameState.autoPass,
    selectedCards: state.extendedGameState.selectedCards,
  };
};

class ButtonDisplay extends Component {
  mulligan = event => {
    this.props.gameHandler.Mulligan();
  };

  keep = event => {
    this.props.gameHandler.Keep();
  };

  concede = event => {
    this.props.gameHandler.Concede();
  };

  selectDone = event => {
    let selected = [...this.props.selectedCards];
    let phase = this.props.phase;
    this.props.gameHandler.SelectDone(phase, selected);
  };

  pass = event => {
    this.props.gameHandler.Pass();
  };

  render() {
    const {
      phase,
      gamePhase,
      playerName,
      playerUserId,
      activePlayer,
      turnPlayer,
      upTo,
      autoPass,
      opponentName
    } = this.props;
    const gamePhaseStr = {
      0: "NOPHASE",
      1: "MULLIGAN",
      2: "BEGIN",
      3: "MAIN",
      4: "MAIN_RESOLVING",
      5: "END"
    }[gamePhase];


    const amIActive = activePlayer === playerUserId;
    const actPlayer = amIActive ? playerName : opponentName;
    const turPlayer = turnPlayer === playerUserId ? playerName : opponentName;

    const activeDisplay = (
      <Grid container spacing={0} style={{ height: "100%" }}>
        <Grid item xs={12} style={{ height: "33%" }}>
          <Textfit
            mode="single"
            forceSingleModeWidth={false}
            style={{ margin: "5%", height: "100%" }}
          >
            Phase: {gamePhaseStr}
          </Textfit>
        </Grid>
        <Grid item xs={12} style={{ height: "33%" }}>
          <Textfit
            mode="single"
            forceSingleModeWidth={false}
            style={{ margin: "5%", height: "100%" }}
          >
            Turn: {turPlayer}
          </Textfit>
        </Grid>
        <Grid item xs={12} style={{ height: "33%" }}>
          <Textfit
            mode="single"
            forceSingleModeWidth={false}
            style={{ margin: "5%", height: "100%" }}
          >
            Active: {actPlayer}
          </Textfit>
        </Grid>
      </Grid>
    );

    let buttonMenu = <div />;
    if (
      phase === GamePhases.NOT_STARTED ||
      gamePhase === proto.Phase.MULLIGAN
    ) {
      buttonMenu = (
        <Grid container spacing={0} style={{ height: "100%" }}>
          <Grid item xs={12} style={{ height: "50%" }}>
            <Button
              color="secondary"
              variant="contained"
              onClick={this.mulligan}
              disabled={!amIActive}
              style={{ maxHeight: "100%", maxWidth: "100%" }}
            >
              Mulligan
            </Button>
          </Grid>
          <Grid item xs={12} style={{ height: "50%" }}>
            <Button
              color="primary"
              variant="contained"
              onClick={this.keep}
              disabled={!amIActive}
              style={{ maxHeight: "100%", maxWidth: "100%" }}
            >
              Keep
            </Button>
          </Grid>
        </Grid>
      );
    } else if (IsSelectCardPhase(phase)) {
      buttonMenu = (
        <Grid container spacing={0} style={{ height: "100%" }}>
          <Grid item xs={12} style={{ height: "50%" }}>
            <Button
              color="secondary"
              variant="contained"
              onClick={this.concede}
            >
              Concede
            </Button>
          </Grid>
          <Grid item xs={12} style={{ height: "50%" }}>
            <Button
              color="primary"
              variant="contained"
              disabled={!upTo}
              onClick={this.selectDone}
              style={{ maxHeight: "100%", maxWidth: "100%" }}
            >
              Done
            </Button>
          </Grid>
        </Grid>
      );
    } else {
      buttonMenu = (
        <Grid container spacing={0} style={{ height: "100%" }}>
          <Grid item xs={12} style={{ height: "50%" }}>
            <Button
              color="secondary"
              variant="contained"
              onClick={this.concede}
            >
              Concede
            </Button>
          </Grid>
          <Grid item xs={12} style={{ height: "50%" }}>
            <Button
              color="primary"
              variant="contained"
              disabled={!amIActive || autoPass}
              onClick={this.pass}
              style={{ maxHeight: "100%", maxWidth: "100%" }}
            >
              Pass
            </Button>
          </Grid>
        </Grid>
      );
    }

    return (
      <Paper className="message-display">
        <Grid container spacing={0} style={{ height: "100%", color: "black" }}>
          <Grid item xs={12} style={{ height: "60%" }}>
            {activeDisplay}
          </Grid>
          <Grid item xs={12} style={{ height: "40%" }}>
            {buttonMenu}
          </Grid>
        </Grid>
      </Paper>
    );
  }
}

export default connect(mapStateToProps)(withHandlers(ButtonDisplay));

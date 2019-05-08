import React, { Component } from "react";
import { DragDropContext } from "react-dnd";
import MultiBackend from "react-dnd-multi-backend";
import HTML5toTouch from "react-dnd-multi-backend/lib/HTML5toTouch";
import Grid from "@material-ui/core/Grid";
import { connect } from "react-redux";

import proto from "../../protojs/compiled.js";

import Board from "./Board";
import Stack from "./Stack";
import Altar from "./Altar";
import ResourceArea from "./ResourceArea";
import StateDisplay from "./StateDisplay";

import ChooseDialog from "../Dialogs/ChooseDialog";
import LoadingDialog from "../Dialogs/LoadingDialog";
import WinLoseDialog from "../Dialogs/WinLoseDialog";
import SelectXDialog from "../Dialogs/SelectXDialog";
import { GamePhases } from "../Constants/Constants";
import { debug } from "../../Utils.js";
import "../../css/App.css";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import { mapDispatchToProps } from "../Redux/Store";

const mapStateToProps = state => {
  return {
    username: state.username,
    game: state.extendedGameState.game,
    phase: state.extendedGameState.phase,
    dialog: state.extendedGameState.dialog,
    upTo: state.extendedGameState.upTo,
    selectedCards: state.extendedGameState.selectedCards,
    selectableCards: state.extendedGameState.selectableCards,
    selectCountMax: state.extendedGameState.selectCountMax,
    gameInitialized: state.extendedGameState.gameInitialized,
    maxXValue: state.extendedGameState.maxXValue,
    autoPass: state.extendedGameState.autoPass,
    targets: state.extendedGameState.targets,
    candidateCards: state.extendedGameState.candidateCards,
  };
};

class PlayArea extends Component {
  updateTargets = targets => {
    if (targets !== this.state.targets) {
      this.setState({ targets: targets });
    }
  };

  //???
  updateInfoUpdate = () => {
    this.setState({ waiting: true });
  };

  //Callback function to update game area (This may not be necessary anymore)
  updateView = (params, phase, selectedCards = null) => {
    const game = params.game;
    const toUpdate = {};

    if (this.state.waiting) {
      // XXX This will not be true in the future when we have server messages
      // as well.
      toUpdate.waiting = false;
    }

    //Auto keep function
    if (
      game.phase === proto.Phase.MULLIGAN &&
      game.activePlayer === game.player.name &&
      game.player.hand.length === 0
    ) {
      //TODO: fix this
      //Keep();
    }

    //Auto pass function
    if (
      game.phase !== proto.Phase.MULLIGAN &&
      phase === GamePhases.UPDATE_GAME &&
      game.activePlayer === game.player.name &&
      game.canStudy.length === 0 &&
      game.canActivate.length === 0 &&
      game.canPlay.length === 0
    ) {
      setTimeout(function() {
        //TODO: fix this
        //Pass();
      }, 1000);
      if (!this.state.autoPass) {
        toUpdate.autoPass = true;
      }
    } else {
      if (this.state.autoPass) {
        toUpdate.autoPass = false;
      }
    }

    debug("[toUpdate]", phase, toUpdate);
    this.setState(toUpdate);
  };

  //Updates state for Material UI Dialog component.
  updateDialog = (open, title, cards) => {
    this.props.updateExtendedGameState({
      dialog: {
        open: open,
        title: title
      },
      candidateCards: cards
    });
  };

  render() {
    //Determine if the things that will be selected
    //will be selected from a Dialog component
    const {
      game,
      phase,
      upTo,
      selectedCards,
      selectableCards,
      autoPass,
      targets,
    } = this.props;


    //Determine which message to display here
    let instMessage = "FLALALALLA";
    /*
    if (game.phase === proto.Phase.MULLIGAN) {
      instMessage = "Chose either to keep or mulligan your hand.";
    } else if (IsSelectCardPhase(phase)) {
      instMessage = `Select ${
        upTo ? "up to " : ""
      } ${selectCountMax} cards from ${
        {
          SelectFromList: "list",
          SelectFromPlay: "play",
          SelectFromHand: "hand",
          SelectFromScrapyard: "scrapyard",
          SelectFromVoid: "void",
          SelectFromStack: "stack"
        }[phase]
      }. (${selectedCards.length} selected)`;
    } else if (phase === GamePhases.SELECT_X_VALUE) {
      instMessage = "Select an X value.";
    } else if (phase === GamePhases.SELECT_PLAYER) {
      instMessage = "Select a player.";
    } else if (!amActive) {
      instMessage = "Waiting for opponent...";
    } else {
      instMessage =
        "Play" + (hasStudyable ? ", study," : "") + " or activate a card.";
    }
    */
    return (
      <div className="App">
        <header className="App-header">
          <WinLoseDialog />
          <LoadingDialog />
          <SelectXDialog />
          <ChooseDialog/>
          <Grid
            container
            spacing={24}
            style={{
              padding: "12px 24px",
              height: "100vh"
            }}
            justify="space-between"
          >
            <Grid item xs={2} className="display-col">
              <StateDisplay
                game={game}
                phase={phase}
                updateDialog={this.updateDialog}
                upTo={upTo}
                autoPass={autoPass}
              />
            </Grid>
            <Grid item xs={8} className="display-col">
              <Board
                game={game}
                phase={phase}
                selectedCards={selectedCards}
                selectableCards={selectableCards}
                instMessage={instMessage}
                targets={targets}
                updateTargets={this.updateTargets}
              />
            </Grid>
            <Grid item xs={2} className="display-col">
              <Grid
                container
                spacing={8}
                justify="center"
                alignItems="center"
                style={{
                  height: "100%"
                }}
              >
                <Grid item xs={12} style={{ height: "5%" }}>
                  <ResourceArea player={game.opponent} />
                </Grid>
                <Grid item xs={12} style={{ height: "70%" }}>
                  <Stack />
                </Grid>
                <Grid item xs={12} style={{ height: "5%" }}>
                  <ResourceArea player={game.player} />
                </Grid>
                <Grid item xs={12} style={{ height: "20%" }}>
                  <Altar />
                </Grid>
              </Grid>
            </Grid>
          </Grid>
        </header>
      </div>
    );
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(
  DragDropContext(MultiBackend(HTML5toTouch))(withHandlers (PlayArea))
);

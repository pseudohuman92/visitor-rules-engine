import React, { Component } from "react";
import { DragDropContext } from "react-dnd";
import MultiBackend from "react-dnd-multi-backend";
import HTML5toTouch from "react-dnd-multi-backend/lib/HTML5toTouch";

import Grid from "@material-ui/core/Grid";

import proto from "../../protojs/compiled.js";

import Board from "./Board";
import Stack from "./Stack";
import Altar from "./Altar";
import ResourceArea from "./ResourceArea.jsx";
import StateDisplay from "./StateDisplay";

import ChooseDialog from "../Dialogs/ChooseDialog.js";
import InfoEntryDialog from "../Dialogs/InfoEntryDialog.js";
import LoadingDialog from "../Dialogs/LoadingDialog.js";
import WinLoseDialog from "../Dialogs/WinLoseDialog.js";
import SelectXDialog from "../Dialogs/SelectXDialog.js";
import {
  Keep,
  Pass,
  GamePhases,
  SetGameInfo,
  SetBasicGameInfo,
  RegisterUpdateGameHandler,
  IsSelectCardPhase
} from "../Game/Game";
import { ConnectProfile, RegisterUpdateViewHandler } from "../../PlayerManager.js";
import { emptyGame } from "../../Constants/Constants";
import { debug } from "../../Utils.js";
import "../../css/App.css";

class PlayArea extends Component {
  constructor(props) {
    super(props);
    this.state = {
      game: {},
      phase: GamePhases.NOT_STARTED,
      gameInitialized: false,
      dialog: { title: "", cards: [], open: false },
      waiting: false,
      selectedCards: [],
      selectableCards: [],
      maxXValue: 0,
      upTo: false,
      targets: []
    };

    this.state.game = emptyGame;

    SetBasicGameInfo(this.state.game.id, this.state.game.player.id, this.state.game.opponent.id);
    RegisterUpdateGameHandler(this.updateView);
    RegisterUpdateViewHandler(this.updateView);
  }

  updateTargets = targets => {
    if (targets !== this.state.targets) {
      this.setState({ targets: targets });
    }
  };

  updateInfoUpdate = username => {
    //SetGameInfo({me: username});
    ConnectProfile(username);
    this.setState({ waiting: true });
  };

  updateView = (params, phase, selectedCards = null) => {
    const game = params.game;
    const toUpdate = {};

    if (
      phase === GamePhases.UPDATE_GAME ||
      phase === GamePhases.NOT_STARTED ||
      !this.state.gameInitialized
    ) {
      toUpdate.game = params.game;
      toUpdate.gameInitialized = true;
    }

    if (IsSelectCardPhase(phase) && selectedCards === null) {
      toUpdate.selectableCards = params.canSelected;
      toUpdate.upTo = params.upTo;
      toUpdate.selectCountMax = params.selectionCount;
    }

    if (this.state.waiting) {
      // XXX This will not be true in the future when we have server messages
      // as well.
      toUpdate.waiting = false;
    }

    if (phase === GamePhases.DONE_SELECT) {
      if (this.state.dialog.open) {
        toUpdate["dialog"] = { ...this.state.dialog, open: false };
      }
      toUpdate.selectableCards = [];
      toUpdate.selectedCards = [];
    }

    if (selectedCards !== null) {
      toUpdate.selectedCards = selectedCards;
    } else {
      if (
        phase === GamePhases.SELECT_FROM_LIST ||
        phase === GamePhases.SELECT_FROM_SCRAPYARD ||
        phase === GamePhases.SELECT_FROM_VOID
      ) {
        toUpdate["dialog"] = {
          open: true,
          title: `Select ${params.selectionCount} from the following`,
          cards: params.candidates
        };
      }
    }

    if (phase === GamePhases.SELECT_X_VALUE) {
      toUpdate.maxXValue = params.maxXValue;
    }

    if (phase !== this.state.phase) {
      toUpdate.phase = phase;
    }

    //Auto keep function
    if (
      game.phase === proto.Phase.MULLIGAN &&
      game.activePlayer === game.player.name &&
      game.player.hand.length === 0
    ) {
      Keep();
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
        Pass();
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

  updateDialog = (open, title, cards) => {
    this.setState({
      dialog: {
        open: open,
        title: title,
        cards: cards
      }
    });
  };

  render() {
    const {
      dialog,
      waiting,
      selectedCards,
      phase,
      game,
      selectableCards,
      upTo,
      maxXValue,
      autoPass,
      selectCountMax,
      targets
    } = this.state;
    const hasStudyable =
      phase === GamePhases.UPDATE_GAME &&
      game.activePlayer === game.player.name &&
      game.canStudy.length > 0;

    const isSelectFromDialogPhase =
      phase === GamePhases.SELECT_FROM_LIST ||
      phase === GamePhases.SELECT_FROM_SCRAPYARD ||
      phase === GamePhases.SELECT_FROM_VOID;

    const gameOver = phase === GamePhases.WIN || phase === GamePhases.LOSE;
    const isWin = phase === GamePhases.WIN;

    const chooseDialog = (
      <ChooseDialog
        title={dialog.title}
        cards={dialog.cards}
        open={dialog.open}
        upTo={upTo}
        selectedCards={selectedCards}
        selectableCards={selectableCards}
        isSelectPhase={isSelectFromDialogPhase}
        onClose={event => {
          this.setState({ dialog: { ...dialog, open: false } });
        }}
      />
    );

    const selectXDialogOpen = phase === GamePhases.SELECT_X_VALUE;

    const amActive = game.activePlayer === game.player.name;
    let instMessage;
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
      instMessage = "Play, study, or activate a card.";
    }

    return (
      <div className="App">
        <header className="App-header">
          <WinLoseDialog open={gameOver} win={isWin} />
          { <LoadingDialog open={waiting} /> }
          <SelectXDialog open={selectXDialogOpen} maxValue={maxXValue} />
          { <InfoEntryDialog open={true} onSubmit={this.updateInfoUpdate} /> }
          {chooseDialog}
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
                  <Stack
                    cards={game.stack}
                    selectedCards={selectedCards}
                    selectableCards={selectableCards}
                    targets={targets}
                    updateTargets={this.updateTargets}
                  />
                </Grid>
                <Grid item xs={12} style={{ height: "5%" }}>
                  <ResourceArea player={game.player} />
                </Grid>
                <Grid item xs={12} style={{ height: "20%" }}>
                  <Altar hasStudyable={hasStudyable} />
                </Grid>
              </Grid>
            </Grid>
          </Grid>
        </header>
      </div>
    );
  }
}

export default DragDropContext(MultiBackend(HTML5toTouch))(PlayArea);

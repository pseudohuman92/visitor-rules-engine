import React, { Component } from "react";
import Paper from "@material-ui/core/Paper";
import Textfit from "react-textfit";
import { connect } from "react-redux";


import { GamePhases } from "../Helpers/Constants";
import { IsSelectCardPhase } from "../Helpers/Helpers";
import proto from "../../protojs/compiled";

const mapStateToProps = state => {
  return {
    phase: state.extendedGameState.phase,
    game: state.extendedGameState.game,
    upTo: state.extendedGameState.upTo,
    selectedCards: state.extendedGameState.selectedCards,
    selectCountMax: state.extendedGameState.selectCountMax
  };
};

class InfoMessage extends Component {
  render() {
    const { phase, game, upTo, selectedCards, selectCountMax } = this.props;

    let instMessage = "...";
    if (game.phase === proto.Phase.MULLIGAN) {
      instMessage = "Choose either to keep or redraw your hand.";
    } else if (IsSelectCardPhase(phase)) {
      instMessage = `Select ${
        upTo ? "up to " : ""
      } ${selectCountMax} cards/players from ${
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
    } else if (game.activePlayer !== game.player.userId) {
      instMessage = "Waiting for opponent...";
    } else if (
      game.canStudy.length > 0 ||
      game.canPlay.length > 0 ||
      game.canActivate.length > 0
    ) {
      instMessage =
        "You may " +
        (game.canPlay.length > 0 ? "Play " : "") +
        (game.canPlay.length > 0 &&
        (game.canStudy.length > 0 || game.canActivate.length > 0)
          ? "or "
          : "") +
        (game.canStudy.length > 0 ? "Study " : "") +
        (game.canStudy.length > 0 && game.canActivate.length > 0 ? "or " : "") +
        (game.canActivate.length > 0 ? "Activate " : "") +
        "a card.";
    }

    return (
      <Paper
        style={{
          height: "100%",
          width: "100%",
          background: "white",
          color: "black"
        }}
      >
        <Textfit
          mode="single"
          forceSingleModeWidth={false}
          style={{ height: "100%" }}
        >
          {instMessage}
        </Textfit>
      </Paper>
    );
  }
}

export default connect(mapStateToProps)(InfoMessage);

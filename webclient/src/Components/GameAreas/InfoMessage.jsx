import React, { Component } from "react";
import Paper from "@material-ui/core/Paper";
import Textfit from "react-textfit";
import { connect } from "react-redux";


import { ClientPhase } from "../Helpers/Constants";
import { IsSelectCardPhase } from "../Helpers/Helpers";
import proto from "../../protojs/compiled";

const mapStateToProps = state => {
  return {
    clientPhase: state.extendedGameState.clientPhase,
    game: state.extendedGameState.game,
    upTo: state.extendedGameState.selectionData.upTo,
    selected: state.extendedGameState.selectionData.selected,
    selectCountMax: state.extendedGameState.selectionData.selectCountMax
  };
};

class InfoMessage extends Component {
  render() {
    const { clientPhase, game, upTo, selected, selectCountMax } = this.props;

    let instMessage = "...";
    if (game.clientPhase === proto.Phase.REDRAW) {
      instMessage = "Choose either to keep or redraw your hand.";
    } else if (IsSelectCardPhase(clientPhase)) {
      instMessage = `Select ${
        upTo ? "up to " : ""
      } ${selectCountMax} cards/players from ${
        {
          SelectFromList: "list",
          SelectFromPlay: "play",
          SelectFromHand: "hand",
          SelectFromDiscardPile: "discardPile",
          SelectFromVoid: "void",
          SelectFromStack: "stack"
        }[clientPhase]
      }. (${selected.length} selected)`;
    } else if (clientPhase === ClientPhase.SELECT_X_VALUE) {
      instMessage = "Select an X value.";
    } else if (clientPhase === ClientPhase.SELECT_PLAYER) {
      instMessage = "Select a player.";
    } else if (game.activePlayer !== game.player.id) {
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
          color: "white",
            fontFamily: "Frijole, cursive",
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

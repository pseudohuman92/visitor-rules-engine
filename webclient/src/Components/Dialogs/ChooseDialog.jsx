import React, { Component } from "react";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";
import { connect } from "react-redux";

import PlayingCard from "../Card/PlayingCard";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import { mapDispatchToProps } from "../Redux/Store";
import { GamePhases } from "../Helpers/Constants";

import "../../css/ChooseDialog.css";

const mapStateToProps = state => {
  return {
    phase: state.extendedGameState.phase,
    selectedCards: state.extendedGameState.selectedCards,
    dialog: state.extendedGameState.dialog,
    upTo: state.extendedGameState.upTo,
  };
};

class ChooseDialog extends Component {
  onClose = event => {
    this.props.updateExtendedGameState({
      dialog: {
        title: "",
        cards: [],
        open: false
      }
    });
  };

  selectDone = event => {
    let selected = [...this.props.selectedCards];
    let phase = this.props.phase;
    this.props.gameHandler.SelectDone(phase, selected);
  };

  render = () => {
    const { phase, dialog, upTo } = this.props;

    const isSelectPhase =
      phase === GamePhases.SELECT_FROM_LIST ||
      phase === GamePhases.SELECT_FROM_SCRAPYARD ||
      phase === GamePhases.SELECT_FROM_VOID;

    return (
      <Dialog
        open={dialog.open}
        onClose={this.onClose}
        maxWidth={false}
        fullWidth
        disableBackdropClick={isSelectPhase}
        disableEscapeKeyDown={isSelectPhase}
        scroll="body"
      >
        <DialogTitle> {dialog.title} </DialogTitle>
        <DialogContent>
          <Grid container spacing={0} className="choose-dialog">
            {dialog.cards.map(card => (
              <Grid item xs={1} key={card.id}>
                <PlayingCard {...card} />
              </Grid>
            ))}
          </Grid>
          {upTo && (
            <Button
              color="primary"
              variant="contained"
              onClick={this.selectDone}
            >
              Done
            </Button>
          )}
        </DialogContent>
      </Dialog>
    );
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withHandlers(ChooseDialog));

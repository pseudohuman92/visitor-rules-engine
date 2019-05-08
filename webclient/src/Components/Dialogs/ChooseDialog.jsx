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
import { GamePhases } from "../Constants/Constants";

import "../../css/ChooseDialog.css";

const mapStateToProps = state => {
  return {
    phase: state.extendedGameState.phase,
    title: state.extendedGameState.dialog.title,
    open: state.extendedGameState.dialog.open,
    upTo: state.extendedGameState.upTo,
    cards: state.extendedGameState.candidateCards
  };
};

//You need to pass onClose to this.
class ChooseDialog extends Component {
  onClose = event => {
    this.props.updateExtendedGameState({
      dialog: {
        title: this.props.title,
        open: false
      }
    });
  };

  render = () => {
    const { phase, open, title, cards, upTo, gameHandler } = this.props;

    const isSelectPhase =
      phase === GamePhases.SELECT_FROM_LIST ||
      phase === GamePhases.SELECT_FROM_SCRAPYARD ||
      phase === GamePhases.SELECT_FROM_VOID;

    return (
      <Dialog
        open={open}
        onClose={this.onClose}
        maxWidth={false}
        fullWidth={true}
        disableBackdropClick={isSelectPhase}
        disableEscapeKeyDown={isSelectPhase}
      >
        <DialogTitle> {title} </DialogTitle>
        <DialogContent>
          <Grid container spacing={0} className="choose-dialog">
            {cards.map(card => (
              <Grid item xs={1} key={card.id}>
                <PlayingCard {...card} />
              </Grid>
            ))}
          </Grid>
          {upTo && (
            <Button
              color="primary"
              variant="contained"
              onClick={gameHandler.SelectDone}
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

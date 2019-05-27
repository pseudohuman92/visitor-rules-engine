import React, { Component } from "react";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import { connect } from "react-redux";
import { TextField, Grid } from "@material-ui/core";

import { GamePhases } from "../Helpers/Constants";
import { mapDispatchToProps } from "../Redux/Store";

const mapStateToProps = state => {
  return {
    phase: state.extendedGameState.phase,
    win: state.extendedGameState.win
  };
};

class EndGameDialog extends Component {
  back = () => {
    const { updateExtendedGameState, back } = this.props;
    updateExtendedGameState({ gameInitialized: false, phase: GamePhases.NOT_STARTED });
    back();
  };

  render = () => {
    const { phase, win } = this.props;

    const open = phase === GamePhases.GAME_END;

    return (
      <Dialog open={open}>
        <DialogTitle>{win ? "You Win!" : "You Lose..."}</DialogTitle>
        <DialogContent>
          <Grid container spacing={24}>
            <Grid item xs={12}>
              <TextField label="Please Provide Feedback PLACEHOLDER" disabled />
            </Grid>
            <Grid item xs={12}>
              <TextField multiline label="Description" />
            </Grid>
            <Grid item xs={12}>
              <Button variant="contained" disabled>
                Submit
              </Button>
            </Grid>
          </Grid>
        </DialogContent>
        <Button type="submit" variant="contained" onClick={this.back}>
          Back
        </Button>
      </Dialog>
    );
  };
}
export default connect(
  mapStateToProps,
  mapDispatchToProps
)(EndGameDialog);

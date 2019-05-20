import React, { Component } from "react";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import { connect } from "react-redux";

import { GamePhases } from "../Helpers/Constants";
import { mapDispatchToProps } from "../Redux/Store";

const mapStateToProps = state => {
  return {
    phase: state.extendedGameState.phase,
    win: state.extendedGameState.win
  };
};



class WinLoseDialog extends Component {
  back = () => {
    const {updateExtendedGameState, back} = this.props;
    updateExtendedGameState({gameInitialized: false});
    back();
  }

  render = () => {
    const { phase, win } = this.props;

    const open = phase === GamePhases.GAME_END;

    return (
      <Dialog open={open}>
        <DialogTitle>{win ? "You Win!" : "You Lose..."}</DialogTitle>
        <DialogContent>
          <img
            src={process.env.PUBLIC_URL + "/img/" +(win ? "win" : "lose")+".jpg"}
            alt=""
          />
        </DialogContent>
        <Button type="submit" variant="contained" onClick={this.back}>
              Back
            </Button>
      </Dialog>
    );
  };
}
export default connect(mapStateToProps, mapDispatchToProps)(WinLoseDialog);

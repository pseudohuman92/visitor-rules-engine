import React, { Component } from "react";

import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import { connect } from "react-redux";

import { GamePhases } from "../Constants/Constants";

const mapStateToProps = state => {
  return {
    phase: state.extendedGameState.phase,
    win: state.extendedGameState.win
  };
};

class WinLoseDialog extends Component {
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
      </Dialog>
    );
  };
}
export default connect(mapStateToProps)(WinLoseDialog);

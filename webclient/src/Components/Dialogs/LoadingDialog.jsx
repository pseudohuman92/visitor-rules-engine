import React, { Component } from "react";
import CircularProgress from "@material-ui/core/CircularProgress";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import { connect } from "react-redux";

const mapStateToProps = state => {
  return {
    open: !state.extendedGameState.gameInitialized
  };
};

class ChooseDialog extends Component {
  render = () => {
    return (
      <Dialog open={this.props.open}>
        <DialogTitle>Waiting...</DialogTitle>
        <DialogContent>
          <DialogContentText>Please wait for an opponent...</DialogContentText>
          <CircularProgress />
        </DialogContent>
      </Dialog>
    );
  };
}

export default connect(mapStateToProps)(ChooseDialog);

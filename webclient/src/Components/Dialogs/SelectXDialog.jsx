import React, { Component } from "react";

import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import { connect } from "react-redux";

import { withHandlers } from "../MessageHandlers/HandlerContext";
import {GamePhases} from "../Helpers/Constants";

const mapStateToProps = state => {
  return {
    phase: state.extendedGameState.phase,
    maxXValue: state.extendedGameState.maxXValue
  };
};

class SelectXDialog extends Component {
  constructor(props) {
    super(props);
    this.state = {
      xVal: 0
    };
  }

  onChange = event => {
    if (event.target.value <= this.props.maxXValue && 0 <= event.target.value) {
      this.setState({ xVal: event.target.value });
    }
  };

  onClick = event => {
    this.props.gameHandler.SelectXValue(this.state.xVal);
  };

  render = () => {
    const { phase, xVal } = this.state;

    let open = phase === GamePhases.SELECT_X_VALUE;

    return (
      <Dialog open={open}>
        <DialogTitle> Select X </DialogTitle>
        <DialogContent>
          <TextField
            type="number"
            label="X"
            value={xVal}
            onChange={this.onChange}
          />
          <Button color="primary" variant="contained" onClick={this.onClick}>
            Submit
          </Button>
        </DialogContent>
      </Dialog>
    );
  };
}

export default connect(
  mapStateToProps
)(withHandlers(SelectXDialog));

import React, { Component } from "react";

import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import { connect } from "react-redux";

import { withHandlers } from "../MessageHandlers/HandlerContext";
import {ClientPhase} from "../Helpers/Constants";

const mapStateToProps = state => {
  return {
    clientPhase: state.extendedGameState.clientPhase,
    maxXValue: state.extendedGameState.selectionData.maxXValue
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

  setToMax = event => {
      this.setState({ xVal: this.props.maxXValue });
  };

  submit = event => {
    this.props.gameHandler.SelectXValue(this.state.xVal);
  };

  render = () => {
    const { xVal } = this.state;
    const {clientPhase} = this.props;
    let open = clientPhase === ClientPhase.SELECT_X_VALUE;

    return (
      <Dialog open={open}>
        <DialogTitle> {"Select X between 0 and "+this.props.maxXValue} </DialogTitle>
        <DialogContent>
          <TextField
            type="number"
            label="X"
            value={xVal}
            onChange={this.onChange}
          />
          <Button color="primary" variant="contained" onClick={this.submit}>
            Submit
          </Button>
          <Button variant="contained" onClick={this.setToMax}>
            Set to Max
          </Button>
        </DialogContent>
      </Dialog>
    );
  };
}

export default connect(
  mapStateToProps
)(withHandlers(SelectXDialog));

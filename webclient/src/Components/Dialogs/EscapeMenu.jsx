import React, { Component } from "react";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import BugReport from "./BugReport";
import { Grid } from "@material-ui/core";
import { isProduction } from "../Helpers/Constants";

class EscapeMenu extends Component {
  state = { value: 0 };
  close = event => {
    this.setState({ value: 0 });
  };

  render = () => {
    const { open, close } = this.props;
    const { value } = this.state;
    return (
      <div>
        {value === 0 && (
          <Dialog open={open} onClose={close} scroll="body">
            <DialogTitle>{"MENU"}</DialogTitle>
            <DialogContent>
              <Grid container spacing={1} justify="center">
              {!isProduction && 
                <Grid item xs={12}>
                  <Button
                    variant="contained"
                    onClick={event => this.props.gameHandler.SaveGameState("test_save")}
                  >
                    Save Game
                  </Button>
                </Grid> }
                <Grid item xs={12}>
                  <Button
                    variant="contained"
                    onClick={event => this.setState({ value: 1 })}
                  >
                    Bug Report
                  </Button>
                </Grid>
                <Grid item xs={12}>
                  <Button
                    color="secondary"
                    variant="contained"
                    onClick={event => this.props.gameHandler.Concede()}
                  >
                    Concede
                  </Button>
                </Grid>
              </Grid>
            </DialogContent>
          </Dialog>
        )}
        {value === 1 && (
          <BugReport open={this.state.value} close={this.close} />
        )}
      </div>
    );
  };
}
export default withHandlers(EscapeMenu);

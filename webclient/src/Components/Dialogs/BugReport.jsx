import React, { Component } from "react";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import { TextField, Grid } from "@material-ui/core";

class BugReport extends Component {
  render = () => {
    const { open, close } = this.props;

    return (
      <Dialog open={open === 1} onClose={close}>
        <DialogTitle>{"Bug Report PLACEHOLDER"}</DialogTitle>
        <DialogContent>
          <Grid container spacing={24}>
            <Grid item xs={12}>
              <TextField label="Subject" />
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
      </Dialog>
    );
  };
}
export default withHandlers(BugReport);

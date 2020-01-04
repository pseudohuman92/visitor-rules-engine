import React, { Component } from "react";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import { TextField, Grid } from "@material-ui/core";
import { connect } from "react-redux";
import { withFirebase } from "../Firebase";

const mapStateToProps = state => {
  return {
    gameId: state.extendedGameState.game.id,
    userId: state.firebaseAuthData.user.uid
  };
};

class BugReport extends Component {
  state = {
    subject: "",
    description: ""
  };

  onChange = event => {
    this.setState({ [event.target.name]: event.target.value });
  };

  submit = event => {
    const { firebase, gameId, userId, close } = this.props;
    firebase.submitBugReport({
      userId: userId,
      gameId: gameId,
      ...this.state
    });
    this.setState({
      subject: "",
      description: ""
    });
    close(event);
  };

  render = () => {
    const { open, close } = this.props;
    const { subject, description } = this.state;
    return (
      <Dialog open={open === 1} onClose={close} scroll="body">
        <DialogTitle>{"Bug Report"}</DialogTitle>
        <DialogContent>
          <Grid container spacing={8}>
            <Grid item xs={12}>
              <TextField
                name="subject"
                label="Subject"
                onChange={this.onChange}
                variant="outlined"
                fullWidth
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                multiline
                name="description"
                label="Description"
                onChange={this.onChange}
                rows="4"
                margin="normal"
                variant="outlined"
                fullWidth
              />
            </Grid>
            <Grid item xs={12}>
              <Button
                variant="contained"
                disabled={subject === "" || description === ""}
                onClick={this.submit}
              >
                Submit
              </Button>
            </Grid>
          </Grid>
        </DialogContent>
      </Dialog>
    );
  };
}
export default connect(mapStateToProps)(withFirebase(withHandlers(BugReport)));

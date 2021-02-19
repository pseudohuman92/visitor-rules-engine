import React, { Component } from "react";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import { connect } from "react-redux";
import { TextField, Grid, RadioGroup, FormControl, FormLabel, FormControlLabel, Radio } from "@material-ui/core";

import { ClientPhase } from "../Helpers/Constants";
import { mapDispatchToProps } from "../Redux/Store";
import { withFirebase } from "../Firebase";
import {Redirect} from "react-router-dom";

const mapStateToProps = state => {
  return {
    clientPhase: state.extendedGameState.clientPhase,
    win: state.extendedGameState.win,
    gameId: state.extendedGameState.game.id,
    userId: state.firebaseAuthData.user.uid
  };
};

class EndGameDialog extends Component {
  state = {
    finished: false,
    verdict: "",
    bestAspects: "",
    worstAspects: "",
    potential: "",
    replay: "",
    other: ""
  };

  onChange = event => {
    this.setState({ [event.target.name]: event.target.value });
  };


  back = () => {
    const { updateExtendedGameState } = this.props;
    this.setState({finished: true});
    updateExtendedGameState({
      gameInitialized: false,
      clientPhase: ClientPhase.NOT_STARTED
    });
  };

  submit = event => {
    const { updateExtendedGameState, firebase, gameId, userId } = this.props;
    firebase.submitFeedback({
      userId: userId,
      gameId: gameId,
      ...this.state
    });
    this.setState({
      finished: true,
      verdict: "",
      bestAspects: "",
      worstAspects: "",
      potential: "",
      replay: "",
      other: ""
    });
    updateExtendedGameState({
      gameInitialized: false,
      clientPhase: ClientPhase.NOT_STARTED
    });
  };


  render = () => {
    const { clientPhase, win } = this.props;
    const {verdict, bestAspects, worstAspects, potential, replay, finished} = this.state;

    const open = clientPhase === ClientPhase.WIN || clientPhase === ClientPhase.LOSE;

    const submitDisabled = 
      verdict === "" ||
      bestAspects === "" ||
      worstAspects === "" ||
      potential === "" ||
      replay === "";


    return (
       <div>
      {finished && <Redirect to={"/profile"}/>}
      <Dialog open={open} onEscapeKeyDown={this.back}
              onBackdropClick={this.back}
              scroll="body" PaperProps={{style: {backgroundColor: "gray", color: "black"}}}>
        <DialogTitle>{win ? "You Win!" : "You Lose..."}</DialogTitle>
        <DialogTitle>{"Please Provide Feedback"}</DialogTitle>
        <DialogContent>
          <Grid container spacing={8}>
            <Grid item xs={12}>
              <TextField
                multiline
                name="verdict"
                label="What is your overall verdict of Visitor?"
                onChange={this.onChange}
                margin="normal"
                variant="outlined"
                fullWidth
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                multiline
                name="bestAspects"
                label="What are the best aspects?"
                onChange={this.onChange}
                margin="normal"
                variant="outlined"
                fullWidth
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                multiline
                name="worstAspects"
                label="What are the worst aspects?"
                onChange={this.onChange}
                margin="normal"
                variant="outlined"
                fullWidth
              />
            </Grid>
            <Grid item xs={12}>
              <FormControl component="fieldset">
                <FormLabel component="legend">
                  Do you think Visitor has a potential?
                </FormLabel>
                <RadioGroup
                  aria-label="Potential"
                  name="potential"
                  value={potential}
                  onChange={this.onChange}
                >
                  <FormControlLabel
                    value="yes"
                    control={<Radio />}
                    label="Yes"
                  />
                  <FormControlLabel value="no" control={<Radio/>} label="No" />
                  <FormControlLabel
                    value="maybe"
                    control={<Radio />}
                    label="Maybe"
                  />
                </RadioGroup>
              </FormControl>
            </Grid>
            <Grid item xs={12}>
              <FormControl component="fieldset">
                <FormLabel component="legend">
                  Would you like to play Visitor again in the future?
                </FormLabel>
                <RadioGroup
                  aria-label="Replay"
                  name="replay"
                  value={replay}
                  onChange={this.onChange}
                >
                  <FormControlLabel
                    value="yes"
                    control={<Radio />}
                    label="Yes"
                  />
                  <FormControlLabel value="no" control={<Radio />} label="No" />
                  <FormControlLabel
                    value="maybe"
                    control={<Radio />}
                    label="Maybe"
                  />
                </RadioGroup>
              </FormControl>
            </Grid>
            <Grid item xs={12}>
              <TextField
                multiline
                name="other"
                label="Do you have other comments?"
                onChange={this.onChange}
                margin="normal"
                variant="outlined"
                fullWidth
              />
            </Grid>
            <Grid item xs={12}>
              <Button variant="contained" disabled={submitDisabled} onClick={this.submit}>
                Submit
              </Button>
            </Grid>
            <Grid item xs={12}>
              <Button variant="contained" onClick={this.back}>
                back
              </Button>
            </Grid>
          </Grid>
        </DialogContent>
      </Dialog>
       </div>
    );
  };
}
export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withFirebase(EndGameDialog));

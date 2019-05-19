import React, { Component } from "react";
import { DragDropContext } from "react-dnd";
import MultiBackend from "react-dnd-multi-backend";
import HTML5toTouch from "react-dnd-multi-backend/lib/HTML5toTouch";
import Button from "@material-ui/core/Button";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import Center from "react-center";
import { connect } from "react-redux";

import PlayArea from "../GameAreas/PlayArea";
import OpenPacks from "./OpenPacks";
import { mapDispatchToProps } from "../Redux/Store";
import ServerMessageHandler from "../MessageHandlers/ServerMessageHandler";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import Decks from "./Decks";

const mapStateToProps = state => {
  return { username: state.username,
           userId : state.authUser.user.uid,
          gameInitialized: state.extendedGameState.gameInitialized };
};

class Profile extends Component {
  constructor(props) {
    super(props);
    this.state = { value: 0 };
  }

  play = event => {
    const {userId, updateHandlers, updateExtendedGameState} = this.props;
    updateHandlers({serverMessage : new ServerMessageHandler(userId, updateHandlers, updateExtendedGameState)});
    this.setState({ value: 1 });
  }

  render() {
    const { value } = this.state;
    const { gameInitialized } = this.props;
    return (
      <div>
        {value === 0 && (
          <Center>
            <Paper>
              <Typography component="h1" variant="h1">
                Profile Page
              </Typography>
              <Button
                variant="contained"
                onClick={this.play}
              >
                Play
              </Button>
              <Button
                variant="contained"
                onClick={event => this.setState({ value: 2 })}
              >
                Decks
              </Button>
              <Button
                variant="contained"
                onClick={event => this.setState({ value: 3 })}
              >
                Open Packs
              </Button>
            </Paper>
          </Center>
        )}
        {value === 1 && (gameInitialized ? <PlayArea /> : <div>WAITING FOR THE GAME</div>)}
        {value === 2 && <Decks/>}
        {value === 3 && <OpenPacks value={999}/>}
      </div>
    );
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DragDropContext(MultiBackend(HTML5toTouch))(withHandlers (Profile)));

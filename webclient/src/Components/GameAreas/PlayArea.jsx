import React, { Component } from "react";
import { DragDropContext } from "react-dnd";
import MultiBackend from "react-dnd-multi-backend";
import HTML5toTouch from "react-dnd-multi-backend/lib/HTML5toTouch";
import Grid from "@material-ui/core/Grid";
import { connect } from "react-redux";

import Board from "./Board";
import Stack from "./Stack";
import StudyArea from "./StudyArea";
import ResourceArea from "./ResourceArea";
import StateDisplay from "./StateDisplay";

import ChooseDialog from "../Dialogs/ChooseDialog";
import EndGameDialog from '../Dialogs/EndGameDialog';
import SelectXDialog from "../Dialogs/SelectXDialog";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import { mapDispatchToProps } from "../Redux/Store";
import { GamePhases } from "../Helpers/Constants";
import proto from "../../protojs/compiled";

import "../../css/App.css";
import EscapeMenu from "../Dialogs/EscapeMenu";


const mapStateToProps = state => {
  return {
    phase: state.extendedGameState.phase,
    game: state.extendedGameState.game
  };
};

class PlayArea extends Component {

  state = {menuOpen : false};

  openMenu = event => {
    if(event.keyCode === 27) {
      this.setState({menuOpen: true});
    }
  }

  closeMenu = event => {
    this.setState({menuOpen: false});
  }

  componentDidMount(){
    document.addEventListener("keydown", this.openMenu, false);
  }
  componentWillUnmount(){
    document.removeEventListener("keydown", this.openMenu, false);
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    let gameHandler = this.props.gameHandler;
    let game = this.props.game;
    let phase = this.props.phase;

    //Auto keep function
    if (
      game.phase === proto.Phase.MULLIGAN &&
      game.activePlayer === game.player.userId &&
      game.player.hand.length === 0
    ) {
      gameHandler.Keep();
    }

    //Auto pass function
    if (
      game.phase !== proto.Phase.MULLIGAN &&
      phase === GamePhases.UPDATE_GAME &&
      game.activePlayer === game.player.userId &&
      game.canStudy.length === 0 &&
      game.canActivate.length === 0 &&
      game.canPlay.length === 0
    ) {
      setTimeout(function() {
        gameHandler.Pass();
      }, 1000);
      this.props.updateExtendedGameState({ autoPass: true });
    } else {
      this.props.updateExtendedGameState({ autoPass: false });
    }
  }

  render() {
    const { phase, game, back } = this.props;
    const hasStudyable =
      phase === GamePhases.UPDATE_GAME &&
      game.activePlayer === game.player.userId &&
      game.canStudy.length > 0;

    return (
      
      <div className="App" >
        <header className="App-header">
          <EscapeMenu open={this.state.menuOpen} close={this.closeMenu}/>
          <EndGameDialog back={back}/>
          <SelectXDialog />
          <ChooseDialog />
          <Grid
            container
            spacing={8}
            style={{
              padding: "12px 12px",
              height: "100vh"
            }}
            justify="space-between"
          >
            <Grid item xs={2} className="display-col">
              <StateDisplay />
            </Grid>
            <Grid item xs={8} className="display-col">
              <Board/>
            </Grid>
            <Grid item xs={2} className="display-col">
              <Grid
                container
                spacing={8}
                justify="center"
                alignItems="center"
                style={{
                  height: "100%"
                }}
              >
                <Grid item xs={12} style={{ height: "10%" }}>
                  <ResourceArea isPlayer={false} />
                </Grid>
                <Grid item xs={12} style={{ height: (hasStudyable? "50%" : "65%") }}>
                  <Stack />
                </Grid>
                <Grid item xs={12} style={{ height: "10%" }}>
                  <ResourceArea isPlayer={true} />
                </Grid>
                <Grid item xs={12} style={{ height: (hasStudyable ? "30%" : "15%") }}>
                  <StudyArea />
                </Grid>
              </Grid>
            </Grid>
          </Grid>
        </header>
      </div>
    );
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DragDropContext(MultiBackend(HTML5toTouch))(withHandlers(PlayArea)));

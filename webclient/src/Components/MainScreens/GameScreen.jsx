import React, { Component } from "react";
import { DndProvider } from "react-dnd";
import MultiBackend from "react-dnd-multi-backend";
import { connect } from "react-redux";
import HTML5Backend from "react-dnd-html5-backend";
import TouchBackend from "react-dnd-touch-backend";
import { TouchTransition } from "react-dnd-multi-backend";

import Stack from "../GameAreas/Stack";

import ChooseDialog from "../Dialogs/ChooseDialog";
import EndGameDialog from "../Dialogs/EndGameDialog";
import SelectXDialog from "../Dialogs/SelectXDialog";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import { mapDispatchToProps } from "../Redux/Store";
import { ClientPhase } from "../Helpers/Constants";
import proto from "../../protojs/compiled";

import EscapeMenu from "../Dialogs/EscapeMenu";
import CardDragPreview from "../Card/CardDragPreview";

import BoardSide from "../GameAreas/BoardSide";
import { withSize } from "react-sizeme";

import "../../css/App.css";
import ButtonDisplay from "../GameAreas/ButtonDisplay";
import PlayerArea from "../GameAreas/PlayerArea";

const mapStateToProps = state => {
  return {
    clientPhase: state.extendedGameState.clientPhase,
    game: state.extendedGameState.game
  };
};

class PlayArea extends Component {
  state = { menuOpen: false };

  openMenu = event => {
    if (event.keyCode === 27) {
      this.setState({ menuOpen: true });
    }
  };

  closeMenu = event => {
    this.setState({ menuOpen: false });
  };

  componentDidMount() {
    document.addEventListener("keydown", this.openMenu, false);
  }
  componentWillUnmount() {
    document.removeEventListener("keydown", this.openMenu, false);
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    let gameHandler = this.props.gameHandler;
    let game = this.props.game;
    let clientPhase = this.props.clientPhase;

    //Auto keep function
    if (
      game.clientPhase === proto.Phase.REDRAW &&
      game.activePlayer === game.player.userId &&
      game.player.hand.length === 0
    ) {
      gameHandler.Keep();
    }

    //Auto pass function
    if (
      game.clientPhase !== proto.Phase.REDRAW &&
      clientPhase === ClientPhase.UPDATE_GAME &&
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
    const { back, size } = this.props;
    const { width, height } = size;

    const HTML5toTouch = {
      backends: [
        {
          backend: HTML5Backend,
          preview: true
        },
        {
          backend: TouchBackend,
          preview: true,
          transition: TouchTransition
        }
      ]
    };

    const sideHeight = height * 0.2;
    const midHeight = height * 0.6;

    return (
      <DndProvider backend={MultiBackend(HTML5toTouch)}>
        <div className="App">
          <img
            src={process.env.PUBLIC_URL + "/img/background.jpg"}
            style={{
              position: "absolute",
              top: 0,
              left: 0,
              objectFit: "cover",
              zIndex: -1
            }}
            alt=""
          />
          <header className="App-header">
            <CardDragPreview />
            <EscapeMenu open={this.state.menuOpen} close={this.closeMenu} />
            <EndGameDialog back={back} />
            <SelectXDialog />
            <ChooseDialog />

            <div className="level0">
                <PlayerArea
                  width={width}
                  height={sideHeight}
                  isPlayer={false}
                />
                
              <div
                className="level1-middle"
                style={{ width: width, height: midHeight }}
              >
                <div
                  className="level1-middle-1"
                  style={{ width: width * 0.85, height: midHeight }}
                >
                  <div
                    className="level1-middle-1-1"
                    style={{ width: width * 0.85, height: midHeight * 0.5 }}
                  >
                    <BoardSide isPlayer={false} />
                  </div>
                  <div
                    className="level1-middle-1-2"
                    style={{ width: width * 0.85, height: midHeight * 0.5 }}
                  >
                    <BoardSide isPlayer={true} />
                  </div>
                </div>
                <div
                  className="level1-middle"
                  style={{
                    width: width * 0.15,
                    height: midHeight,
                    flexDirection: "column"
                  }}
                >
                  <div
                    className="level1-middle-2"
                    style={{
                      width: width * 0.15,
                      height: midHeight * 0.8,
                      flexGrow: 8
                    }}
                  >
                    <Stack />
                  </div>
                  <div
                    className="level1-middle-2"
                    style={{
                      width: width * 0.15,
                      height: midHeight * 0.2,
                      flexGrow: 2
                    }}
                  >
                    <ButtonDisplay />
                  </div>
                </div>
              </div>
              <PlayerArea
                  width={width}
                  height={sideHeight}
                  isPlayer={true}
                />
            </div>
          </header>
        </div>
      </DndProvider>
    );
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withHandlers(withSize({ monitorHeight: true })(PlayArea)));

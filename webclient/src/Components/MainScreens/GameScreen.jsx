import React, { Component } from "react";
import { DragDropContext } from "react-beautiful-dnd";
import { connect } from "react-redux";

import Stack from "../GameAreas/Stack";
import ErrorBoundary from "../Primitives/ErrorBoundary";

import ChooseDialog from "../Dialogs/ChooseDialog";
import EndGameDialog from "../Dialogs/EndGameDialog";
import SelectXDialog from "../Dialogs/SelectXDialog";

import EscapeMenu from "../Dialogs/EscapeMenu";

import BoardSide from "../GameAreas/BoardSide";

import "../../css/App.css";
import ButtonDisplay from "../GameAreas/ButtonDisplay";
import PlayerArea from "../GameAreas/PlayerArea";
import { debugPrint } from "../Helpers/Helpers";
import { mapDispatchToProps } from "../Redux/Store";
import { withHandlers } from "../MessageHandlers/HandlerContext";

const mapStateToProps = (state) => {
  return {
    windowDimensions: state.windowDimensions,
    hand: state.extendedGameState.game.player.hand,
  };
};

class GameScreen extends Component {
  state = { menuOpen: false };

  openMenu = (event) => {
    if (event.keyCode === 27) {
      //ESC key
      this.setState({ menuOpen: true });
    }
  };

  closeMenu = (event) => {
    this.setState({ menuOpen: false });
  };

  componentDidMount() {
    document.addEventListener("keydown", this.openMenu, false);
  }
  componentWillUnmount() {
    document.removeEventListener("keydown", this.openMenu, false);
  }

  onDragStart = (result) => {
    /*
    var hand = [...this.props.hand];
    const index = hand.map(c=> {return c.id}).indexOf(result.draggableId);
    var card = hand[index];
    hand.splice (index, 1);
    this.setState({dragging: card});
    this.props.updateGameState({player:{hand:hand}});
    */
    debugPrint("Drag started!", result);
  };

  onDragEnd = (result) => {
    const { destination, draggableId, reason } = result;
    const { gameHandler } = this.props;
    if (destination && reason === "DROP") {
      switch (destination.droppableId) {
        case "player-void":
          if (draggableId.indexOf("[STUDYABLE]") > -1) {
            gameHandler.StudyCard(
              draggableId.substring(draggableId.lastIndexOf("]") + 1)
            );
          }
          break;
        case "opponent-play-area":
        case "player-play-area":
          if (draggableId.indexOf("[PLAYABLE]") > -1) {
            gameHandler.PlayCard(
              draggableId.substring(draggableId.lastIndexOf("]") + 1)
            );
          }
          break;
      }
    }
    debugPrint("Drag ended!", result);
  };

  render() {
    const { back, windowDimensions, stack } = this.props;
    const { width, height } = windowDimensions;

    const sideHeight = height * 0.2;
    const midHeight = height * 0.6;

    return (
      <ErrorBoundary>
        <DragDropContext
          onDragStart={this.onDragStart}
          onDragEnd={this.onDragEnd}
        >
          <div className="App">
            <img
              src={process.env.PUBLIC_URL + "/img/background.jpg"}
              style={{
                position: "absolute",
                top: 0,
                left: 0,
                objectFit: "cover",
                zIndex: -1,
              }}
              alt=""
            />

              <div className="App-header">
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
                        flexDirection: "column",
                      }}
                    >
                      <div
                        className="level1-middle-2"
                        style={{
                          width: width * 0.15,
                          height: midHeight * 0.8,
                          flexGrow: 8,
                        }}
                      >
                        <Stack />
                      </div>

                      <div
                        className="level1-middle-2"
                        style={{
                          width: width * 0.15,
                          height: midHeight * 0.2,
                          flexGrow: 2,
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
              </div>
          </div>
        </DragDropContext>
      </ErrorBoundary>
    );
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withHandlers(GameScreen));

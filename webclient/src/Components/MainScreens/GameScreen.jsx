import React, {Component} from "react";
import {DragDropContext} from "react-beautiful-dnd";
import {connect} from "react-redux";

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
import {debugPrint} from "../Helpers/Helpers";
import {mapDispatchToProps} from "../Redux/Store";
import {withHandlers} from "../MessageHandlers/HandlerContext";
import PhaseDisplay from "../GameAreas/PhaseDisplay";
import SelectionMessage from "../GameAreas/SelectionMessage";
import KnowledgeDialog from "../Dialogs/KnowledgeDialog";
import {ClientPhase} from "../Helpers/Constants";
import {Redirect} from "react-router-dom";

const mapStateToProps = (state) => {
    return {
        windowDimensions: state.windowDimensions,
        gameInitialized: state.extendedGameState.gameInitialized,
        clientPhase: state.extendedGameState.clientPhase,
    };
};

class GameScreen extends Component {
    state = {menuOpen: false};

    openMenu = (event) => {
        if (event.keyCode === 27) {
            //ESC key
            this.setState({menuOpen: !this.state.menuOpen});
        }
    };

    closeMenu = (event) => {
        this.setState({menuOpen: false});
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
        const {destination, draggableId, reason} = result;
        const {gameHandler} = this.props;
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
        const {windowDimensions, gameInitialized} = this.props;
        const {width, height} = windowDimensions;

        const sideHeight = height * 0.2;
        const midHeight = height * 0.6;

        return (
                <div className="game-screen-outer" style={{
                    position : "relative",
                    width: "100%",
                    height: "100%",
                }}>
                    {gameInitialized &&
                    <DragDropContext
                        style={{
                            position : "relative",
                            width: "100%",
                            height: "100%",
                        }}
                        onDragStart={this.onDragStart}
                        onDragEnd={this.onDragEnd}
                    >
                            <div style={{
                                position : "relative",
                                display: "flex",
                                justifyContent: "space-between",
                                width: "100%",
                                height: "100%",
                                border: "3px white solid"
                            }}>
                                <EscapeMenu open={this.state.menuOpen} close={this.closeMenu}/>
                                <EndGameDialog/>
                                <SelectXDialog/>
                                <ChooseDialog/>
                                <KnowledgeDialog/>

                                <div className="game-screen-left" style={{
                                    position : "relative",
                                    display: "flex",
                                    flexDirection: "column",
                                    justifyContent: "space-between",
                                    width: "85%",
                                    height: "100%",
                                    border: "3px green solid"
                                }}>
                                    <PlayerArea
                                        style={{
                                            width: "100%",
                                            height: "22%",
                                            border: "3px yellow solid"
                                        }}
                                        width={width}
                                        height={sideHeight}
                                        isPlayer={false}
                                    />

                                    <BoardSide style={{
                                        width: "100%",
                                        height: "25%",
                                        border: "3px white dashed"
                                    }} isPlayer={false}/>

                                    <SelectionMessage style={{
                                        width: "100%",
                                        height: "5%",
                                        border: "3px red solid"
                                    }}/>

                                    <BoardSide style={{
                                        width: "100%",
                                        height: "25%",
                                        border: "3px white dashed"
                                    }} isPlayer={true}/>

                                    <PlayerArea
                                        style={{
                                            width: "100%",
                                            height: "22%",
                                            border: "3px yellow solid"
                                        }}
                                        width={width}
                                        height={sideHeight}
                                        isPlayer={true}
                                    />

                                </div>

                                <div className="game-screen-right"
                                     style={{
                                    position : "relative",
                                    display: "flex",
                                    flexDirection: "column",
                                    width: "15%",
                                    height: "100%",
                                    border: "3px red solid",
                                     alignItems: "center"
                                }}>
                                    <Stack style={{
                                        width: "100%",
                                        height: "60%",
                                        border: "3px yellow solid"
                                    }} />
                                    <PhaseDisplay style={{
                                        width: "100%",
                                    }}/>
                                    <ButtonDisplay style={{
                                        width: "100%",
                                        alignSelf: "flex-end"
                                    }}/>
                                </div>


                            </div>
                    </DragDropContext>}
                    {!gameInitialized && <div>LOADING</div>}
                </div>

        );
    }
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(withHandlers(GameScreen));

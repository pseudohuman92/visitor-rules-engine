import React from "react";
import { Component } from "react";
import { connect } from "react-redux";
import { Droppable } from "react-beautiful-dnd";
import TextOnImage from "../Primitives/TextOnImage";

import { mapDispatchToProps } from "../Redux/Store";
import { withFirebase } from "../Firebase";
import { withHandlers } from "../MessageHandlers/HandlerContext";

import { ClientPhase } from "../Helpers/Constants";

const mapStateToProps = state => {
  return {
    windowDimensions: state.windowDimensions,
    playerName: state.profile.username,
    opponentName: state.extendedGameState.opponentUsername,
    opponentUserId: state.extendedGameState.game.opponent.username,
    playerVoid: state.extendedGameState.game.player.void,
    opponentVoid: state.extendedGameState.game.opponent.void,
    clientPhase: state.extendedGameState.clientPhase,
    game: state.extendedGameState.game
  };
};

class Void extends Component {
  constructor(props) {
    super(props);
    this.state = { hover: false };
  }

  toggleHover = () => {
    this.setState((state, props) => ({
      hover: !state.hover
    }));
  };

  componentDidMount() {
    const {
      isPlayer,
      opponentName,
      opponentUserId,
      firebase,
      updateExtendedGameState
    } = this.props;
    if (!isPlayer && opponentName === "") {
      firebase.setOpponentUsername(opponentUserId, updateExtendedGameState);
    }
  }

  displayVoid = event => {
    const {
      playerName,
      opponentName,
      playerVoid,
      opponentVoid,
      isPlayer,
      updateExtendedGameState
    } = this.props;
    const void_ = isPlayer ? playerVoid : opponentVoid;
    const name = isPlayer ? playerName : opponentName;
    updateExtendedGameState({
      dialogData: {
        open: true,
        title: `${name}'s Void`,
        cards: void_
      }
    });
  };

  render() {
    const {
      windowDimensions,
      playerVoid,
      opponentVoid,
      game,
      clientPhase,
      isPlayer,
      style
    } = this.props;


    const { width } = windowDimensions;
    const voidWidth = width / 10;
    const voidHeight = voidWidth * (88/63);

    const { hover } = this.state;

    const void_ = isPlayer ? playerVoid : opponentVoid;

    const hasStudyable =
      isPlayer &&
      clientPhase === ClientPhase.UPDATE_GAME &&
      game.activePlayer === game.player.id &&
      game.canStudy.length > 0;

    return (
      <Droppable
        droppableId={isPlayer ? "player-void" : "opponent-void"}
        isDropDisabled={!isPlayer}
      >
        {provided => {
          return (
            <div
              ref={provided.innerRef}
              {...provided.droppableProps}
              onMouseEnter={this.toggleHover}
              onMouseLeave={this.toggleHover}
              onClick={this.displayVoid}
              style={{ ...style, width: voidWidth, height: voidHeight }}
            >
              <TextOnImage
                text={hover ? (void_.length?void_.length:0) : ""}
                src={process.env.PUBLIC_URL + "/img/void-grunge-red.png"}
                imgStyle={{
                  transform: "rotate(" + (isPlayer ? 0 : 180) + "deg)",
                  border: hasStudyable ? "3px green solid" : "none",
                  boxSizing: "border-box"
                }}
                scale={3}
                windowDimensions={windowDimensions}
              />
            </div>
          );
        }}
      </Droppable>
    );
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withHandlers(withFirebase(Void)));

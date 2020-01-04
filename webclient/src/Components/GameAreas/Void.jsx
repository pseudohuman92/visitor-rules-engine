import React from "react";
import { Component } from "react";
import { DropTarget } from "react-dnd";
import { connect } from "react-redux";

import TextOnImage from "../Primitives/TextOnImage";

import { mapDispatchToProps } from "../Redux/Store";
import { withFirebase } from "../Firebase";
import { withHandlers } from "../MessageHandlers/HandlerContext";

import { ItemTypes, ClientPhase } from "../Helpers/Constants";

const voidTarget = {
  drop(props, monitor) {
    return { targetType: ItemTypes.ALTAR };
  },

  canDrop(props, monitor) {
    const item = monitor.getItem();
    return (
      item.sourceType === ItemTypes.CARD && item.studyable && props.isPlayer
    );
  }
};

const mapStateToProps = state => {
  return {
    playerName: state.profile.username,
    opponentName: state.extendedGameState.opponentUsername,
    opponentUserId: state.extendedGameState.game.opponent.userId,
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
      connectDropTarget,
      playerVoid,
      opponentVoid,
      game,
      clientPhase,
      isPlayer,
      style
    } = this.props;
    const { hover } = this.state;

    const void_ = isPlayer ? playerVoid : opponentVoid;

    const hasStudyable =
      isPlayer &&
      clientPhase === ClientPhase.UPDATE_GAME &&
      game.activePlayer === game.player.userId &&
      game.canStudy.length > 0;

    return connectDropTarget(
      <div
        onMouseEnter={this.toggleHover}
        onMouseLeave={this.toggleHover}
        onClick={this.displayVoid}
        style={{ height: "100%", ...style }}
      >
        <TextOnImage
          text={hover ? void_.length : ""}
          src={process.env.PUBLIC_URL + "/img/void.png"}
          imgStyle={{
            transform: "rotate(" + (isPlayer ? 0 : 180) + "deg)",
            border: hasStudyable ? "3px green solid" : "none",
            boxSizing: "border-box"
          }}
          scale={3}
        />
      </div>
    );
  }
}

Void = DropTarget(ItemTypes.CARD, voidTarget, (connect, monitor) => ({
  connectDropTarget: connect.dropTarget(),
  isOver: monitor.isOver(),
  canDrop: monitor.canDrop()
}))(Void);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withHandlers(withFirebase(Void)));

import React, { Component } from "react";
import { connect } from "react-redux";
import { Droppable } from "react-beautiful-dnd";

import PlayingCard from "../Card/PlayingCard";
import { ItemTypes } from "../Helpers/Constants";

import "../../css/Utils.css";

const mapStateToProps = state => {
  return {
    windowDimensions: state.windowDimensions,
    playerCards: state.extendedGameState.game.player.play,
    opponentCards: state.extendedGameState.game.opponent.play
  };
};

class BoardSide extends Component {
  render() {
    const {
      isPlayer,
      playerCards,
      opponentCards,
      windowDimensions
    } = this.props;
    const { width } = windowDimensions;
    const cards = isPlayer ? playerCards : opponentCards;

    return <Droppable droppableId={isPlayer?"player-play-area":"opponent-play-area"}>
        {provided => (
          <div
            ref={provided.innerRef}
            {...provided.droppableProps}
            style={{
              height: "100%",
              display: "flex",
              justifyContent: "center",
              alignItems: "center" //isPlayer ? "flex-end" : "flex-start"
            }}
          >
            {cards.map((card, i) => (
              <div
                key={card.id}
                style={{
                  //width: Math.min(width / (cards.length * 2), width / 20),
                  margin: "1%"
                }}
              >
                <PlayingCard square play cardData={card} isDragDisabled DnDIndex={i}/>
              </div>
            ))}
          </div>
        )}
      </Droppable>
   
  }
}

export default connect(mapStateToProps)(BoardSide);

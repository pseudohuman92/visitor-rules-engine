import React, { Component } from "react";
import { connect } from "react-redux";
import { Droppable } from "react-beautiful-dnd";

import PlayingCard from "../Card/PlayingCard";

import "../../css/Utils.css";

const mapStateToProps = state => {
  return {
    windowDimensions: state.windowDimensions,
    playerCards: state.extendedGameState.game.player.play,
    opponentCards: state.extendedGameState.game.opponent.play
  };
};

function getCardsOfType(cards, type){
  let units = [];
  for (const card of cards){
    if(card.types.indexOf(type) !== -1){
      units.push(card)
    }
  }
  return units;
}

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
              display: "flex",
              flexDirection: isPlayer ? "column" : "column-reverse",
                  ...this.props.style
            }}
          >
            <div
                style={{
                  height: "50%",
                  display: "flex",
                  flexWrap: "wrap",
                  justifyContent: "center",
                  alignItems: "center" //isPlayer ? "flex-end" : "flex-start"
                }}
            >
            {getCardsOfType(cards,"Unit").map((card, i) => (
              <div
                key={card.id}
                style={{
                  //width: Math.min(width / (cards.length * 2), width / 20),
                  margin: "0 1% 1% 1%",
                  maxHeight:"100%"
                }}
              >
                <PlayingCard square cardData={card} isDragDisabled DnDIndex={i}/>
              </div>
            ))}
        </div>
            <div
              style={{
                height: "50%",
                display: "flex",
                flexWrap: "wrap",
                justifyContent: "flex-end",
                alignItems: "center" //isPlayer ? "flex-end" : "flex-start"
              }}
          >
            {getCardsOfType(cards,"Ally").map((card, i) => (
                <div
                    key={card.id}
                    style={{
                      //width: Math.min(width / (cards.length * 2), width / 20),
                      leftMargin: "1%",
                      rightMargin: "1%"
                    }}
                >
                  <PlayingCard square cardData={card} isDragDisabled DnDIndex={i}/>
                </div>
            ))}
          </div>
            </div>
        )}
      </Droppable>
   
  }
}

export default connect(mapStateToProps)(BoardSide);

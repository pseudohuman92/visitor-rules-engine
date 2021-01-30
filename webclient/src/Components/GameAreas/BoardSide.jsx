import React, { Component } from "react";
import { connect } from "react-redux";
import { Droppable } from "react-beautiful-dnd";

import PlayingCard from "../Card/PlayingCard";

import "../../css/Utils.css";
import {organizeCards} from "../Helpers/Helpers";
import CardStack from "../Card/CardStack";

const mapStateToProps = state => {
  return {
    playerCards: state.extendedGameState.game.player.play,
    opponentCards: state.extendedGameState.game.opponent.play
  };
};

function getCardsOfType(cardGroups, type){
  let chosen = [];
  for (const cardGroup of cardGroups){
    if(cardGroup.card.types.includes(type)){
      chosen.push(cardGroup)
    }
  }
  return chosen;
}

class BoardSide extends Component {
  render() {
    const {
      isPlayer,
      playerCards,
      opponentCards,
    } = this.props;
    const cards = organizeCards(isPlayer ? playerCards : opponentCards);


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
            {getCardsOfType(cards,"Unit").map((cardGroup, i) => (
              <div
                key={cardGroup.card.id}
                style={{
                  //width: Math.min(width / (cards.length * 2), width / 20),
                  margin: "0 1% 1% 1%",
                  maxHeight:"100%"
                }}
              >
                <CardStack isPlayer={isPlayer} square cardGroup={cardGroup} isDragDisabled DnDIndex={i}/>
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
            {getCardsOfType(cards,"Ally").map((cardGroup, i) => (
                <div
                    key={cardGroup.card.id}
                    style={{
                      //width: Math.min(width / (cards.length * 2), width / 20),
                      leftMargin: "1%",
                      rightMargin: "1%"
                    }}
                >
                  <CardStack isPlayer={isPlayer} square cardGroup={cardGroup} isDragDisabled DnDIndex={i}/>
                </div>
            ))}
          </div>
            </div>
        )}
      </Droppable>
   
  }
}

export default connect(mapStateToProps)(BoardSide);

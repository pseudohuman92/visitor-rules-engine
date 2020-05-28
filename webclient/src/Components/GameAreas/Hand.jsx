import React from "react";
import { PureComponent } from "react";
import { connect } from "react-redux";

import PlayingCard from "../Card/PlayingCard";
import { Droppable } from "react-beautiful-dnd";
import CardDisplay from "../Card/CardDisplay";

const mapStateToProps = state => {
  return {
    windowDimensions: state.windowDimensions,
    hand: state.extendedGameState.game.player.hand,
    opponentHandSize: state.extendedGameState.game.opponent.handSize
  };
};

class Hand extends PureComponent {
  render() {
    const { hand, opponentHandSize, isPlayer, windowDimensions } = this.props;
    const handCards = isPlayer
      ? hand.map((card, i) => {
          return <PlayingCard key={card.id} cardData={card} DnDIndex={i} play />;
        })
      : Array.apply(null, Array(opponentHandSize)).map((x, i) => {
        return <img
        key ={i}
        className="image"
        src={process.env.PUBLIC_URL + "/img/CardBack.png"}
        alt=""
        style={{
          margin: "1px",
          maxWidth: (100/ (opponentHandSize > 5?opponentHandSize:5)) +"%",
          maxHeight: "100%",
          width: "auto",
          height: "auto",
          objectFit: "scale-down"
        }}
      />;
      });
      
    return (
      <Droppable
        droppableId={isPlayer ? "player-hand" : "opponent-hand"}
        isDropDisabled
        direction={"horizontal"}
        renderClone={(provided, snapshot, rubric) => {
          return (
            <div
              {...provided.draggableProps}
              {...provided.dragHandleProps}
              ref={provided.innerRef}
            >
              <CardDisplay
                cardData={handCards[rubric.source.index].props.cardData}
                windowDimensions={windowDimensions}
              />
            </div>
          );
        }}
      >
        {provided => {
          return (
            <div
              ref={provided.innerRef}
              {...provided.droppableProps}
              style={{ display: "flex", 
              justifyContent: "center", }}
            >
              {handCards}
              {provided.placeholder}
            </div>
          );
        }}
      </Droppable>
    );
  }
}

export default connect(mapStateToProps)(Hand);

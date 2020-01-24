import React from "react";
import { connect } from "react-redux";

import PlayingCard from "../Card/PlayingCard";
import "../../css/Stack.css";
import "../../css/Utils.css";
import ComponentStack from "../Primitives/ComponentStack";
import { Droppable } from "react-beautiful-dnd";

const mapStateToProps = state => {
  return {
    stack: state.extendedGameState.game.stack,
    windowDimensions: state.windowDimensions
  };
};

class Stack extends React.Component {
  render() {
    const { stack, windowDimensions } = this.props;
    const { width } = windowDimensions;
    return (
      <Droppable droppableId={"stack"} isDroppingDisabled>
        {provided => (
          <div ref={provided.innerRef} {...provided.droppableProps}>
            <ComponentStack stepSize={width / 50} width={width / 10}>
              {stack.reverse().map((card, i) => {
                return (
                  <PlayingCard
                    key={card.id}
                    cardData={card}
                    isDragDisabled
                    DnDIndex={i}
                  />
                );
              })}
            </ComponentStack>
          </div>
        )}
      </Droppable>
    );
  }
}

export default connect(mapStateToProps)(Stack);

import React from "react";
import { connect } from "react-redux";

import PlayingCard from "../Card/PlayingCard";
import "../../css/Utils.css";
import { Droppable } from "react-beautiful-dnd";
import { debugPrint } from "../Helpers/Helpers";

const mapStateToProps = (state) => {
  return {
    stack: state.extendedGameState.game.stack,
    windowDimensions: state.windowDimensions,
  };
};

class Stack extends React.Component {
  render() {
    const { stack, windowDimensions } = this.props;
    const { width } = windowDimensions;
    const stepSize = width / 50;
    return (
      <Droppable droppableId={"stack"} isDroppingDisabled>
        {(provided) => (
          <div className="stack-outer" style={this.props.style}
               ref={provided.innerRef} {...provided.droppableProps}>
            {stack.reverse().map((card, i) => {
              debugPrint("Stack Card", card);
              return (
                <div
                  key={i}
                  style={{
                    position: "absolute",
                    top: "" + stepSize * i + "px",
                    zIndex: i,
                    width: stepSize * 5,
                  }}
                >
                  <PlayingCard
                    key={card.id}
                    cardData={card}
                    isDragDisabled
                    DnDIndex={i}
                  />
                </div>
              );
            })}
          </div>
        )}
      </Droppable>
    );
  }
}

export default connect(mapStateToProps)(Stack);

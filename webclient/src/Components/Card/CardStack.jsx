import React from "react";
import "../../css/ComponentStack.css";
import PlayingCard from "./PlayingCard";

class CardStack extends React.Component {
  render() {
    const { cardGroup, style, square, isPlayer, ...rest } = this.props;
    return (
      <div className="card-stack" style={{...style, position: "relative", zIndex: 10}}>
        <PlayingCard cardData={cardGroup.card} square {...rest} />
        {cardGroup.attachments.map((attachment, i) => {
          return (
              <div key={i} style={{
                position: "absolute",
                top: (isPlayer ? "": "-") + 5 * (i+1) + "%",
                left: "" + 5 * (i+1) + "%",
                zIndex: -(i+1),
              }}>
                <PlayingCard cardData={attachment} square/>
              </div>
          );
        })}
      </div>
    );
  }
}

export default CardStack;

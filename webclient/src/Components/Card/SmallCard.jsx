import React from "react";
import { PureComponent } from "react";

import {
  getCardColor,
  getIconColor,
  toKnowledgeString,
} from '../Helpers/Helpers';
import Fonts from '../Primitives/Fonts';
import './css/Card.css';
import "../../fonts/Fonts.css";

class SmallCard extends PureComponent {
  render() {
      const {
          opacity,
          cardData,
          borderColor,
          windowDimensions,
          scale,
      } = this.props;

      const {
          name,
          cost,
          knowledgeCost,
      } = cardData;

      const scale_ = scale ? 7 / scale : 7;
      const { width } = windowDimensions;
      const wtohRatio = 4.25;
      const cardWidth = width / scale_;
      const cardHeight = cardWidth / wtohRatio;

    const backColor = borderColor ? borderColor : "black";
    return (
        <div
            style={{
                width: cardWidth,
                height: cardHeight,
                position: "relative",
                backgroundColor: backColor,
                borderRadius: cardWidth / (4 * scale_) + "px",
                zIndex: 1,
            }}
        >
        <div
          style={{
            opacity: opacity,
            backgroundColor: backColor,
            overflow: "hidden",
            textAlign: "left",
          }}
        >
          <div
            className="card-inner"
            style={{ backgroundColor: getCardColor(knowledgeCost),
              fontSize: "12px" }}
          >
            <div className="card-name" style={{textAlign: "left"}}>
                <span style={{ fontWeight: "500" }}>{cost}</span>
                <span> {toKnowledgeString(knowledgeCost)} </span>
                {" | " + name}
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default SmallCard;
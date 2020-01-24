import React from "react";
import { PureComponent } from "react";
import Rectangle from "react-rectangle";

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
      cardData,
      play,
      borderColor,
      opacity
    } = this.props;

    const {
      name,
      cost,
      knowledgeCost,
    } = cardData

    const backColor = borderColor ? borderColor : "black";
    return (
      <div>
        <Fonts />
        <Rectangle
          aspectRatio={[22, 3]}
          style={{
            opacity: opacity,
            backgroundColor: backColor,
            overflow: "hidden",
            textAlign: "left",
          }}
        >
          <div
            className={"card-inner"+(play?"-play":"")}
            style={{ backgroundColor: getCardColor(knowledgeCost),
              fontSize: 150/20+"px" }}
          >
            <div className="small-card-name">
                <span style={{ fontWeight: "500" }}>{cost}</span>
                <span
                style={{
                  fontFamily: "Visitor Font Small",
                  color: getIconColor(knowledgeCost)
                }}
              >
                {toKnowledgeString(knowledgeCost)}
              </span>
                {" | " + name}
            </div>
          </div>
        </Rectangle>
      </div>
    );
  }
}

export default SmallCard;
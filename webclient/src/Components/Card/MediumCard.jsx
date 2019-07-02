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
import { withSize } from "react-sizeme";
import "../../fonts/Fonts.css"

class MediumCard extends PureComponent {
  render() {
    const {
      name,
      cost,
      knowledgeCost,
      description,
      play,
      type,
      health,
      loyalty,
      favor,
      shield,
      reflect,
      size,
      opacity,
      borderColor
    } = this.props;
    const backColor = borderColor ? borderColor : "black";
    return (
      <div>
        <Fonts />
        <Rectangle
          aspectRatio={[22, 22]}
          style={{
            opacity: opacity,
            backgroundColor: backColor,
            overflow: "hidden",
            textAlign: "justify",
          }}
        >
          <div
            className={"card-inner"+(play?"-play":"")}
            style={{ backgroundColor: getCardColor(knowledgeCost),
              fontSize: size.width/20+"px" }}
          >
            <div className="card-name">
             
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
          <div className="card-type">
                {type}
            </div>


          <div className="card-description" style={{whiteSpace: "pre-wrap"}}>
                {description}
              
                {shield ? "\nShield:" + shield : ""}
              {reflect ? "\nReflect:" + reflect : ""}
                {health ? "\nHealth:" + health : ""}
              {loyalty ? "\nLoyalty:" + loyalty : ""}
              {favor ? "\nFavor:" + favor : ""}
          </div>
          </div>
        </Rectangle>
      </div>
    );
  }
}

export default withSize()(MediumCard)
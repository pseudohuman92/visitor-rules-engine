import React from "react";
import { PureComponent } from "react";
import Textfit from "react-textfit";
import Rectangle from "react-rectangle";

import {
  getCardColor,
  getIconColor,
  toKnowledgeString,
  toIconString,
} from "../Helpers/Helpers";
import Fonts from "../Primitives/Fonts";
import "./css/SmallCard.css";
import "../../css/Utils.css";

export default class SmallCard extends PureComponent {
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
      favor
    } = this.props;

    return (
      <div>
        <Fonts />
        <Rectangle
          aspectRatio={[22, 22]}
          style={{
            backgroundColor: getCardColor(knowledgeCost),
            overflow: "hidden",
            textAlign: "justify"
          }}
        >
          <div
            className={"card-inner"+(play?"-play":"")}
            style={{ backgroundColor: getCardColor(knowledgeCost) }}
          >
            <div className="card-name">
             
                <span style={{ fontWeight: "500" }}>{cost}</span>
                <span
                  style={{
                    fontWeight: "500",
                    color: getIconColor(knowledgeCost)
                  }}
                >
                  {toIconString(toKnowledgeString(knowledgeCost))}
                </span>
                {" | " + name}
            </div>
          <div className="card-type">
                {type}
            </div>


          <div className="card-description" style={{whiteSpace: "pre-wrap"}}>
                {description}
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

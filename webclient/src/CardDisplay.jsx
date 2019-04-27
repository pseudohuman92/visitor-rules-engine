import React from "react";
import { Component } from "react";
import Textfit from "react-textfit";
import Rectangle from "react-rectangle";
import Image from "react-image";

import {
  getCardColor, getIconColor,
  toKnowledgeString,
  toIconString
} from "./constants/Constants";
import Fonts from "./fonts/Fonts";
import "./css/Card.css";
import "./css/Utils.css";
import "./css/ResourceArea.css";

export class CardDisplay extends Component {


  render() {
    const {
      small,
      opacity,
      name,
      description,
      cost,
      type,
      knowledgeCost,
      borderColor
    } = this.props;


    const marginSize = "2%";
    const borderRadius = "3px";

    return small ? (

      <div>
        <Fonts />
        <Rectangle
          aspectRatio={[22, 4]}
          style={{
            borderRadius: borderRadius,
            backgroundColor: getCardColor(knowledgeCost),
            overflow: "hidden"
          }}
        >
          <Textfit
            mode="single"
            forceSingleModeWidth={false}
            style={{ maxHeight: "100%", margin: marginSize }}
          >
            {cost}
            <span style={{ fontWeight: "500", color: getIconColor(knowledgeCost) }}>{toIconString(toKnowledgeString(knowledgeCost))}</span>
            {" | " + name}
          </Textfit>
        </Rectangle>
      </div>
    ) : (
        <div>
          <Fonts />
          <Rectangle
            aspectRatio={[63, 88]}
            style={{
              opacity: opacity,
              borderRadius: borderRadius,
              backgroundColor: borderColor,
              overflow: "hidden"
            }}
          >
            <div
              className="card-inner"
              style={{ backgroundColor: getCardColor(knowledgeCost) }}
            >
              <div className="card-name">
                <Textfit
                  mode="single"
                  forceSingleModeWidth={false}
                  style={{ fontFamily: 'Open Sans, sans-serif', maxWidth: "96%", maxHeight: "100%" }}
                >
                  <span style={{ fontWeight: "500" }}>{cost}</span>
                  <span style={{ fontWeight: "500", color: getIconColor(knowledgeCost) }}>{toIconString(toKnowledgeString(knowledgeCost))}</span>
                  {" | " + name}

                </Textfit>
              </div>

              <div className="card-image">
                <Image
                  src={[
                    process.env.PUBLIC_URL + "/img/" + name + ".jpg",
                    process.env.PUBLIC_URL + "/img/" + type + ".jpg"
                  ]}
                  style={{ maxWidth: "100%" }}
                  decode={false}
                />
              </div>

              <div className="card-type">
                <Textfit
                  mode="single"
                  forceSingleModeWidth={false}
                  style={{ fontFamily: 'Open Sans, sans-serif', maxHeight: "100%" }}
                >
                  {" " + type}
                </Textfit>
              </div>

              <div className="card-description">
                <Textfit style={{
                  fontFamily: 'Open Sans, sans-serif', maxHeight: "100%"
                }}>{description}</Textfit>
              </div>
            </div>
          </Rectangle>
        </div>
      );
  }
}

export default CardDisplay;

import React from "react";
import { Component } from "react";
import Textfit from "react-textfit";
import Rectangle from "react-rectangle";
import Image from "react-image";

import { getCardColor } from "./Constants.js";
import "./css/Card.css";

export class CardDisplay extends Component {
  
  getCostLine(cost, knowledge) {
    var str = "";

    if (cost !== "-") {
      str += cost + " ";
    }
    if (!knowledge.startsWith("-")) {
      str += "[" + knowledge + "] ";
    }
    if (cost !== "-" || !knowledge.startsWith("-")) {
      str += "| ";
    }
    return str;
  }

  render() {
    const {
      small,
      opacity,
      name,
      description,
      cost,
      type,
      knowledge,
      borderColor
    } = this.props;

    const marginSize = "2%";
    const borderRadius = "3px";
    const smallBorder = "3px black solid";

    return small ? (
      <div>
        <Rectangle
          aspectRatio={[22, 4]}
          style={{
            border: smallBorder,
            borderRadius: borderRadius,
            backgroundColor: getCardColor(knowledge),
            overflow: "hidden"
          }}
        >
          <Textfit
            mode="single"
            forceSingleModeWidth={false}
            style={{ maxHeight: "100%", margin: marginSize }}
          >
            {this.getCostLine(cost, knowledge) + name}
          </Textfit>
        </Rectangle>
      </div>
    ) : (
      <div>
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
            style={{ backgroundColor: getCardColor(knowledge) }}
          >
            <div className="card-name">
              <Textfit
                mode="single"
                forceSingleModeWidth={false}
                style={{ maxWidth: "96%", maxHeight: "100%" }}
              >
                {this.getCostLine(cost, knowledge) + name}
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
                style={{ maxHeight: "100%" }}
              >
                {" " + type}
              </Textfit>
            </div>

            <div className="card-description">
              <Textfit style={{ maxHeight: "100%" }}>{description}</Textfit>
            </div>
          </div>
        </Rectangle>
      </div>
    );
  }
}

export default CardDisplay;

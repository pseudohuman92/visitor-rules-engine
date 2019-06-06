import React from "react";
import { PureComponent } from "react";
import Rectangle from "react-rectangle";


import {
  getCardColor,
  getIconColor,
  toKnowledgeString,
  toIconString
} from "../Helpers/Helpers";
import Fonts from "../Primitives/Fonts";
import "./css/Card.css";
import { withSize } from "react-sizeme";

class FullCard extends PureComponent {
  state = { showDialog: false };

  render() {
    const {
      opacity,
      name,
      description,
      cost,
      type,
      knowledgeCost,
      borderColor,
      health,
      favor,
      loyalty,
      shield,
      reflect,
      play,
      size
    } = this.props;
    const backColor = borderColor ? borderColor : "black";
    return (
      <div>
        <Fonts />
        <Rectangle
          aspectRatio={[63, 88]}
          style={{
            opacity: opacity,
            backgroundColor: backColor,
            overflow: "hidden"
          }}
        >
          <div
            className={"card-inner" + (play ? "-play" : "")}
            style={{
              backgroundColor: getCardColor(knowledgeCost),
              fontSize: size.width / 20 + "px"
            }}
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

            <div className="card-image">
                <img
                  src={process.env.PUBLIC_URL + "/img/" + type + ".png"}
                  style={{ maxWidth: "100%" }}
                  alt=""
                />
            </div>

            <div className="card-type">{type}</div>
            <div
              className="card-description"
              style={{ whiteSpace: "pre-wrap" }}
            >
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

export default withSize()(FullCard);

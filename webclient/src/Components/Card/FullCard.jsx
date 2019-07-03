import React from "react";
import { PureComponent } from "react";
import Rectangle from "react-rectangle";
import Center from "react-center";

import {
  getCardColor,
  getIconColor,
  toKnowledgeString
} from "../Helpers/Helpers";
import Fonts from "../Primitives/Fonts";
import "./css/Card.css";
import { withSize } from "react-sizeme";
import "../../fonts/Fonts.css";

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
    const backColor = borderColor ? borderColor : getCardColor(knowledgeCost);
    return (
      <div style={{ position: "relative"}}>
        <Fonts />
        <Rectangle
          aspectRatio={[63, 88]}
          style={{
            opacity: opacity,
            backgroundColor: backColor,
            borderRadius: "10px" 
          }}
        >
          <div
            className={"card-inner" + (play ? "-play" : "")}
            style={{
              backgroundColor: getCardColor(knowledgeCost),
              fontSize: size.width / 20 + "px"
            }}
          >
            {cost ? (
              <div className="card-cost">
                <img
                  src={
                    process.env.PUBLIC_URL + "/img/card-components/energy3.png"
                  }
                  style={{
                    maxWidth: "100%",
                  }}
                  alt=""
                />
                <div
                  className="card-cost-text"
                  style={{ fontSize: size.width / 12 + "px" }}
                >
                  {cost}
                </div>
              </div>
            ) : (
              <div />
            )}
            <div className="card-name">
              <span
                style={{
                  fontFamily: "Visitor Font Small",
                  color: getIconColor(knowledgeCost)
                }}
              >
                {toKnowledgeString(knowledgeCost) + " | "}
              </span>

              {name}
            </div>

            <div className="card-image">
              <img
                src={
                  process.env.PUBLIC_URL + "/img/placeholders/" + type + ".png"
                }
                style={{ maxWidth: "100%" }}
                alt=""
              />
            </div>

            <div className="card-type">{type}</div>
            <div
              className="card-description"
              style={{
                whiteSpace: "pre-wrap"
              }}
            >
              {description}
              {shield ? "\nShield:" + shield : ""}
              {reflect ? "\nReflect:" + reflect : ""}
              {loyalty ? "\nLoyalty:" + loyalty : ""}
              {favor ? "\nFavor:" + favor : ""}
            </div>
            {health ? (
              <div className="card-health">
                <img
                  src={
                    process.env.PUBLIC_URL + "/img/card-components/health.png"
                  }
                  style={{
                    maxWidth: "100%",
                  }}
                  alt=""
                />
                <div
                  className="card-health-text"
                  style={{ fontSize: size.width / 12 + "px" }}
                >
                  {health}
                </div>
              </div>
            ) : (
              <div />
            )}
          </div>
        </Rectangle>
      </div>
    );
  }
}

export default withSize()(FullCard);

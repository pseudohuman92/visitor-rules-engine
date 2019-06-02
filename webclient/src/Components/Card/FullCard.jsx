import React from "react";
import { PureComponent } from "react";
import Rectangle from "react-rectangle";
import VisibilitySensor from "react-visibility-sensor";
import TextFit from "react-textfit";

import {
  getCardColor,
  getIconColor,
  toKnowledgeString,
  toIconString
} from "../Helpers/Helpers";
import Fonts from "../Primitives/Fonts";
import "./css/FullCard.css";

export default class FullCard extends PureComponent {
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
      play
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
            style={{ backgroundColor: getCardColor(knowledgeCost) }}
          >
            <div className="card-name">
            <TextFit mode="single" forceSingleModeWidth={false}>
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
              </TextFit>
            </div>

            <div className="card-image">
              <VisibilitySensor>
                <img
                  src={process.env.PUBLIC_URL + "/img/" + type + ".png"}
                  style={{ maxWidth: "100%" }}
                  alt=""
                />
                {/*
              <Image
                src={[
                  process.env.PUBLIC_URL + "/img/" + name + ".jpg",
                  process.env.PUBLIC_URL + "/img/" + type + ".png"
                ]}
                style={{ maxWidth: "100%" }}
                decode={false}
              />
              */}
              </VisibilitySensor>
            </div>

            <div className="card-type">
            <TextFit mode="single" forceSingleModeWidth={false}>{type}</TextFit></div>
            <div className="card-description">
              <TextFit mode="multi" style={{ whiteSpace: "pre-wrap" }}>
                {description}
                {shield ? "\nShield:" + shield : ""}
                {reflect ? "\nReflect:" + reflect : ""}
                {health ? "\nHealth:" + health : ""}
                {loyalty ? "\nLoyalty:" + loyalty : ""}
                {favor ? "\nFavor:" + favor : ""}
              </TextFit>
            </div>
          </div>
        </Rectangle>
      </div>
    );
  }
}

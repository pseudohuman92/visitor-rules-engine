import React from "react";
import { PureComponent } from "react";
import Rectangle from "react-rectangle";

import {
  getCardColor,
  getIconColor,
  toKnowledgeString
} from "../Helpers/Helpers";
import Fonts from "../Primitives/Fonts";
import "./css/Card.css";
import { withSize } from "react-sizeme";
import "../../fonts/Fonts.css";

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
    const backColor = borderColor ? borderColor : getCardColor(knowledgeCost);
    return (
      <div style={{ position: "relative" }}>
        <Fonts />
        <Rectangle
          aspectRatio={[22, 22]}
          style={{
            opacity: opacity,
            backgroundColor: backColor,
            borderRadius: "10px",
            textAlign: "justify"
          }}
        >
          <div
            className="card-inner"
            style={{
              backgroundColor: getCardColor(knowledgeCost),
              fontSize: size.width / 20 + "px"
            }}
          >
            {cost && (
              <div className="card-cost">
                <img
                  src={
                    process.env.PUBLIC_URL + "/img/card-components/energy.png"
                  }
                  style={{
                    maxWidth: "100%"
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
            )}

            {toKnowledgeString(knowledgeCost)
              .split("")
              .map((c, i) => (
                <div
                  className="card-knowledge"
                  style={{ top: 10 + i * 8 + "%" }}
                  key={i}
                >
                  <img
                    src={
                      process.env.PUBLIC_URL +
                      "/img/card-components/knowledge-" +
                      c +
                      ".png"
                    }
                    style={{
                      maxWidth: "100%"
                    }}
                    alt=""
                  />
                </div>
              ))}
            <div className="card-name">{name}</div>

            <div className="card-type">{type}</div>
            <div
              className="card-description"
              style={{
                whiteSpace: "pre-wrap"
              }}
            >
              {description}
              {reflect ? "\nReflect:" + reflect : ""}
              {favor ? "\nFavor:" + favor : ""}
            </div>
            {health && (
              <div className="card-health">
                <img
                  src={
                    process.env.PUBLIC_URL + "/img/card-components/health.png"
                  }
                  style={{
                    maxWidth: "100%"
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
            )}
            {shield && (
              <div className="card-shield">
                <img
                  src={
                    process.env.PUBLIC_URL + "/img/card-components/shield.png"
                  }
                  style={{
                    maxWidth: "100%"
                  }}
                  alt=""
                />
                <div
                  className="card-shield-text"
                  style={{ fontSize: size.width / 12 + "px" }}
                >
                  {shield}
                </div>
              </div>
            )}
            {loyalty && (
              <div className="card-loyalty">
                <img
                  src={
                    process.env.PUBLIC_URL + "/img/card-components/loyalty.png"
                  }
                  style={{
                    maxWidth: "100%"
                  }}
                  alt=""
                />
                <div
                  className="card-loyalty-text"
                  style={{ fontSize: size.width / 12 + "px" }}
                >
                  {loyalty}
                </div>
              </div>
            )}
          </div>
        </Rectangle>
      </div>
    );
  }
}

export default withSize()(MediumCard);

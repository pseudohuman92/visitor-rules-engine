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
      <div style={{ position: "relative" }}>
        <Fonts />
        <Rectangle
          aspectRatio={[63, 88]}
          style={{
            opacity: opacity,
            backgroundColor: backColor,
            borderRadius: size.width/20+"px",
            textAlign: "justify",
          }}
        >
          <div
            className={"card-inner" + (play ? "-play" : "")}
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
                  style={{ fontSize: size.width / 13 + "px" }}
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
                  style={{ top: 11 + i * 3 + "%" }}
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

export default withSize()(FullCard);

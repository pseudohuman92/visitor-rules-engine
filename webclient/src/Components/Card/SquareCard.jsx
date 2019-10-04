import React from "react";
import { PureComponent } from "react";
import Rectangle from "react-rectangle";

import {
  getCardColor,
  toKnowledgeString
} from "../Helpers/Helpers";
import Fonts from "../Primitives/Fonts";
import "./css/Card.css";
import { withSize } from "react-sizeme";
import "../../fonts/Fonts.css";
import TextOnImage from "../Primitives/TextOnImage";

class SquareCard extends PureComponent {
  state = { showDialog: false };

  render() {
    const {
      opacity,
      name,
      cost,
      type,
      knowledgeCost,
      borderColor,
      attack,
      health,
      delay,
      loyalty,
      shield,
      reflect,
      size
    } = this.props;
    const backColor = borderColor ? borderColor : undefined; //"gainsboro";
    return (
      <div style={{ position: "relative" }}>
        <Fonts />
        <Rectangle
          aspectRatio={[63, 54]}
          style={{
            opacity: opacity,
            backgroundColor: backColor,
            borderRadius: size.width / 20 + "px",
            textAlign: "left"
          }}
        >
          {cost && (
            <div className="card-cost-square">
              <TextOnImage
                src={process.env.PUBLIC_URL + "/img/card-components/energy.png"}
                text={cost}
                min={11}
                max={100}
                font={{ fontFamily: "Special Elite, cursive" }}
              />
            </div>
          )}

          {toKnowledgeString(knowledgeCost)
            .split("")
            .map((c, i) => (
              <div
                className="card-knowledge"
                style={{ top: 15 + i * 5 + "%" }}
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
          <div
            className="card-inner-square"
            style={{
              backgroundColor: getCardColor(knowledgeCost),
              fontSize: size.width / 20 + "px",
              borderRadius: size.width / 25 + "px",
              border: "1px black solid"
            }}
          >
            <div className="card-name-square">{name}</div>

            <div className="card-image-square">
              <img
                src={
                  process.env.PUBLIC_URL + "/img/placeholders/" + type + ".png"
                }
                style={{
                  maxWidth: "100%",
                  maxHeight: "100%",
                  width: "auto",
                  height: "auto",
                  objectFit: "scale-down"
                }}
                alt=""
              />
            </div>
          </div>
          <div className="bottom-icons" 
          style={{
                display: "flex",
                alignItems: "center"
              }}>
                {attack && (
                <TextOnImage
                  src={
                    process.env.PUBLIC_URL + "/img/card-components/attack2.png"
                  }
                  text={attack}
                  min={12}
                  font={{ fontFamily: "Special Elite, cursive" }}
                />
              )}
              {health && (
                <TextOnImage
                  src={
                    process.env.PUBLIC_URL + "/img/card-components/health.png"
                  }
                  text={health}
                  min={12}
                  font={{ fontFamily: "Special Elite, cursive" }}
                />
              )}
              {shield && (
                <TextOnImage
                  src={
                    process.env.PUBLIC_URL + "/img/card-components/shield.png"
                  }
                  text={shield}
                  min={12}
                  font={{ fontFamily: "Special Elite, cursive" }}
                />
              )}
              {loyalty && (
                <TextOnImage
                  src={
                    process.env.PUBLIC_URL + "/img/card-components/loyalty.png"
                  }
                  text={loyalty}
                  min={12}
                  font={{ fontFamily: "Special Elite, cursive" }}
                />
              )}
              {delay && (
                <TextOnImage
                  src={
                    process.env.PUBLIC_URL + "/img/card-components/delay.png"
                  }
                  text={delay}
                  min={12}
                  font={{ fontFamily: "Special Elite, cursive" }}
                />
              )}
          </div>
        </Rectangle>
      </div>
    );
  }
}

export default withSize()(SquareCard);

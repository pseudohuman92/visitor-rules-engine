import React from "react";
import { PureComponent } from "react";

import { getCardColor, toKnowledgeString } from "../Helpers/Helpers";
import Fonts from "../Primitives/Fonts";
import "./css/Card.css";
import "../../fonts/Fonts.css";
import TextOnImage from "../Primitives/TextOnImage";
import FittedText from "../Primitives/FittedText";

class FullCard extends PureComponent {
  state = { showDialog: false };

  render() {
    const {
      opacity,
      cardData,
      borderColor,
      square,
      windowDimensions,
      scale,
    } = this.props;

    const {
      name,
      description,
      cost,
      types,
      knowledgeCost,
      delay,
      loyalty,
      combat,
    } = cardData;

    const scale_ = scale ? 10 / scale : 10;
    const { width } = windowDimensions;
    const wtohRatio = square ? 63 / 54 : 63 / 88;
    const cardWidth = width / scale_;
    const cardHeight = cardWidth / wtohRatio;

    const backColor = borderColor ? borderColor : undefined; //"gainsboro";
    return (
      <div
        style={{
          width: cardWidth,
          height: cardHeight,
          position: "relative",
          opacity: opacity,
          backgroundColor: backColor,
          borderRadius: cardWidth / (2 * scale_) + "px",
          zIndex: 1,
        }}
      >
        {/*<Fonts />*/}
        <div
          className="card-inner"
          style={{
            top: square ? "3%" : "2%",
            height: square ? "94%" : "96%",
            position: "relative",
            fontSize: cardWidth / (2 * scale_) + "px",
            borderRadius: cardWidth / ((square ? 3 : 2.5) * scale_) + "px",
          }}
        >
          <div
            style={{
              position: "absolute",
              top: 0,
              left: 0,
              width: "100%",
              height: "100%",
              zIndex: -1,
              backgroundColor: "gainsboro",
            }}
          >
            <img
              src={
                process.env.PUBLIC_URL +
                "/img/placeholders/" +
                types[0] +
                ".png"
              }
              style={
                square
                  ? {
                      maxWidth: "100%",
                      objectFit: "scale-down",
                    }
                  : {
                      maxHeight: "100%",
                      objectFit: "scale-down",
                    }
              }
              alt=""
            />
          </div>
          {cost !== "" && (
            <div className="card-cost">
              <img
                src={process.env.PUBLIC_URL + "/img/card-components/energy.png"}
                style={{
                  maxWidth: "100%",
                }}
                alt=""
              />
              <div
                className="card-cost-text"
                style={{ fontSize: cardWidth / (1.3 * scale_) + "px" }}
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
                style={{ top: (square ? 17 : 11) + i * (square ? 4 : 3) + "%" }}
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
                    maxWidth: "100%",
                  }}
                  alt=""
                />
              </div>
            ))}
          <div className="card-name" style={{ height: square ? "9%" : "6%" }}>
            {name}
          </div>

          {!square && <div className="card-type">{types}</div>}

          {!square && (
            <div
              className="card-description"
              style={{
                textAlign: "left",
                whiteSpace: "pre-wrap",
              }}
            >
              <FittedText
                text={(combat?combat.combatAbilities + "\n":"") + description}
                max={15}
                windowDimensions={windowDimensions}
              />
            </div>
          )}

          <div
            style={{
              position: "absolute",
              bottom: "0",
              left: "3%",
              height: "10%",
              display: "flex",
              alignItems: "center",
            }}
          >
            {combat && combat.attack > -1 && (
              <TextOnImage
                src={
                  process.env.PUBLIC_URL + "/img/card-components/attack2.png"
                }
                text={combat.attack}
                min={15}
                scale={5 * scale_}
                font={{ fontFamily: "Special Elite, cursive" }}
                windowDimensions={windowDimensions}
              />
            )}
            {combat && combat.health > -1 && (
              <TextOnImage
                src={process.env.PUBLIC_URL + "/img/card-components/health.png"}
                text={combat.health}
                min={15}
                scale={5 * scale_}
                font={{ fontFamily: "Special Elite, cursive" }}
                windowDimensions={windowDimensions}
              />
            )}
            {combat && combat.shield > 0 && (
              <TextOnImage
                src={process.env.PUBLIC_URL + "/img/card-components/shield.png"}
                text={combat.shield}
                min={15}
                scale={5 * scale_}
                font={{ fontFamily: "Special Elite, cursive" }}
                windowDimensions={windowDimensions}
              />
            )}
            {loyalty > -1 && (
              <TextOnImage
                src={
                  process.env.PUBLIC_URL + "/img/card-components/loyalty.png"
                }
                text={loyalty}
                min={15}
                scale={5 * scale_}
                font={{ fontFamily: "Special Elite, cursive" }}
                windowDimensions={windowDimensions}
              />
            )}
            {delay > 0 && (
              <TextOnImage
                src={process.env.PUBLIC_URL + "/img/card-components/delay.png"}
                text={delay}
                min={15}
                scale={5 * scale_}
                font={{ fontFamily: "Special Elite, cursive" }}
                windowDimensions={windowDimensions}
              />
            )}
          </div>
        </div>
      </div>
    );
  }
}

export default FullCard;

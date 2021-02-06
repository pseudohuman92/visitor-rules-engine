import React, { Component } from "react";
import Hand from "./Hand";
import Deck from "./Deck";
import DiscardPile from "./DiscardPile";
import PlayerDisplay from "./PlayerDisplay";
import Void from "./Void";


class PlayerArea extends Component {
  render() {
    const { isPlayer, width, height, style } = this.props;
    return (
      <div
        style={{
          position: "relative",
          width: width,
          height: height,
          flexGrow: 2,
          display: "flex",
          justifyContent: "space-around",
            top: isPlayer ? "-1%" : "1%",
          ...style
        }}
      >
        <div
          style={{
            width: width * 0.2,
            height: height,
            flexGrow: 2,
            display: "flex",
            marginLeft: "1%",
            justifyContent: "center",
          }}
        >
          <DiscardPile
            style={{ width: "33%", marginRight: "1%" }}
            isPlayer={isPlayer}
          />
          <Deck
            style={{ width: "33%", marginRight: "1%" }}
            isPlayer={isPlayer}
          />
          <Void style={{ width: "33%" }} isPlayer={isPlayer} />
        </div>
        <div
          style={{
            width: width * 0.6,
            height: height,
            flexGrow: 6,
            display: "flex",
            flexDirection: isPlayer ? "column" : "column-reverse",
            justifyContent: "space-around"
          }}
        >
          <div
            style={{
              width: width * 0.6,
              height: height * 0.3,
              flexGrow: 3,
              marginTop: "1%"
            }}
          >
            <PlayerDisplay
              isPlayer={isPlayer}
              style={{ width: width * 0.6, height: height * 0.3 }}
            />
          </div>
          <div
            style={{
              width: width * 0.6,
              height: height * 0.7,
              flexGrow: 7,
              transform: "rotate(" + (isPlayer ? 0 : 180) + "deg)"
            }}
          >
            <Hand isPlayer={isPlayer}/>
          </div>
        </div>
        <div
          style={{
            width: width * 0.05,
            height: height,
            flexGrow: 1
            //border: "3px black solid"
          }}
        />
        <div
          style={{
            width: width * 0.15,
            height: height,
            flexGrow: 1
            //border: "3px black solid"
          }}
        ></div>
      </div>
    );
  }
}

export default PlayerArea;

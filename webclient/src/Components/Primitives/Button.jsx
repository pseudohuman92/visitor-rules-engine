import React from "react";
import { Component } from "react";

import Fonts from "./Fonts";
import "./Button.css";

class Button extends Component {
  render() {
    const { text, disabled, onClick, variant } = this.props;
    const type = variant ? variant : "8";
    const opacity = disabled ? 0.5 : 1;
    return (
      <div
        className="container"
        style={{ opacity: opacity }}
        onClick={event => {
          if (!disabled) {
            onClick(event);
          }
        }}
      >
        <Fonts />
        <img
          src={process.env.PUBLIC_URL + "/img/buttons/buttons" + type + ".png"}
          alt=""
          style={{
            maxWidth: "100%",
            maxHeight: "100%"
          }}
        />
        <div
          className="centered"
          style={{
            fontFamily: "Cinzel, serif",
            fontWeight: "700"
          }}
        >
          {text}
        </div>
      </div>
    );
  }
}

export default Button;

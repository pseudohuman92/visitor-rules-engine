import React from "react";
import { Component } from "react";
import { withSize } from "react-sizeme";
import Fonts from "./Fonts";

import "./TextOnImage.css";

class TextOnImage extends Component {
  render() {
    const { text, min, max, src, font, imgStyle, size } = this.props;
    const font_ = font
      ? font
      : { fontFamily: "Cinzel, serif", fontWeight: "700" };
    return (
      <div className="container">
        <Fonts />
        <img
          className="image"
          src={src}
          alt=""
          style={{
            maxWidth: "100%",
            maxHeight: "100%",
            width: "auto",
            height: "auto",
            objectFit: "scale-down",
            ...imgStyle
          }}
        />
        <div
          className="centered"
          style={{
            ...font_,
            fontSize: Math.min(Math.max(size.width / 10, min), max) + "px"
          }}
        >
          {text}
        </div>
      </div>
    );
  }
}

export default withSize()(TextOnImage);

import React from "react";
import { Component } from "react";
import Fonts from "./Fonts";
import Center from "react-center";

import "./TextOnImage.css";

class TextOnImage extends Component {
  render() {
    const { text, min, max, scale, src, font, style, textStyle, windowDimensions, ...rest } = this.props;
    const windowWidth  = windowDimensions.width;
    const width = windowWidth / 5;
    const min_ = min? min : 1;
    const max_ = max?max:100;
    const scale_ = scale?scale:10;
    const font_ = font
      ? font
      : { fontFamily: "Cinzel, serif", fontWeight: "700" };
    return (
      <div {...rest} className="container" style={{
          ...style,
          backgroundImage: "url(" + src +")",
          backgroundPosition: 'center',
          backgroundSize: 'contain',
          backgroundRepeat: 'no-repeat'
      }} >
        <div
            className="centered"
          style={{
            ...font_,
            ...textStyle,
            fontSize: Math.min(Math.max(width / scale_, min_), max_) + "px"
          }}
        >
          {text}
        </div>
      </div>
    );
  }
}

export default TextOnImage;

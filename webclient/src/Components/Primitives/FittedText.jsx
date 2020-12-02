import React from "react";
import { Component } from "react";

class FittedText extends Component {
  render() {
    const { text, min, max, font, scale, pad, style, windowDimensions } = this.props;
    const { windowWidth } = windowDimensions;
    const width = windowWidth / 10;
    const scale_ = scale ? scale : 10;
    const min_ = min ? min : 1;
    const max_ = max ? max : 1000;
    const padding_ = pad ? pad : 2;
    return (
      <div style={style}>
      <div
        style={{
          ...font,
          padding: padding_ + "px",
          fontSize: Math.min(Math.max(width / scale_, min_), max_) + "px"
        }}
      >
        {text}
      </div>
      </div>
    );
  }
}

export default FittedText;

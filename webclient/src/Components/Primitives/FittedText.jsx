import React from "react";
import { Component } from "react";
import { withSize } from "react-sizeme";

class FittedText extends Component {
  render() {
    const { text, min, max, font, scale, style, size } = this.props;
    const scale_ = scale ? scale : 10;
    const min_ = min ? min : 1;
    const max_ = max ? max : 1000;
    return (
      <div style={style}>
      <div
        style={{
          ...font,
          padding: "2px",
          fontSize: Math.min(Math.max(size.width / scale_, min_), max_) + "px"
        }}
      >
        {text}
      </div>
      </div>
    );
  }
}

export default withSize()(FittedText);

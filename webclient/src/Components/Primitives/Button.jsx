import React, { Component } from "react";
import { withSize } from "react-sizeme";

import TextOnImage from "./TextOnImage";

class Button extends Component {
  render() {
    const { text, disabled, onClick, variant, size } = this.props;
    const type = variant ? variant : "8";
    const opacity = disabled ? 0.5 : 1;
    return (
      <div
        style={{ opacity: opacity }}
        onClick={event => {
          if (!disabled) {
            onClick(event);
          }
        }}
      >
        <TextOnImage
          src={process.env.PUBLIC_URL + "/img/buttons/buttons" + type + ".png"}
          min={10}
          max={30}
          text={text}
        />
      </div>
    );
  }
}

export default withSize()(Button);

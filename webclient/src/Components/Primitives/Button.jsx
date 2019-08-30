import React, { Component } from "react";

import TextOnImage from "./TextOnImage";

class Button extends Component {
  render() {
    const { text, disabled, onClick, variant } = this.props;
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

export default Button;

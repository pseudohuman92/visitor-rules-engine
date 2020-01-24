import React, { Component } from "react";
import { connect } from "react-redux";
import TextOnImage from "./TextOnImage";

const mapStateToProps = state => {
  return {
    windowDimensions: state.windowDimensions
  };
};

class Button extends Component {
  render() {
    const { text, disabled, onClick, variant, windowDimensions } = this.props;
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
          windowDimensions={windowDimensions}
        />
      </div>
    );
  }
}

export default connect(mapStateToProps)(Button);

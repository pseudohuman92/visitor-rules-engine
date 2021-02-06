import React, { Component } from "react";
import { connect } from "react-redux";
import Center from "react-center";

const mapStateToProps = state => {
  return {
    windowDimensions: state.windowDimensions
  };
};

class Button extends Component {
  state = {hovering: false};

  toggleHover = () => {
    this.setState({hovering: !this.state.hovering})
  };

  onClick = event => {
    const { disabled, onClick } = this.props;
    if (onClick && !disabled) {
      onClick(event);
    }
  };

  render() {
    const { text, disabled } = this.props;
    const hovering = this.state.hovering;
    const opacity = disabled ? 0.5 : 1;
    return (
      <div className="button cover"
        style={{ ...this.props.style,
            opacity: opacity,
            position:"relative",
          width: "100%",
          height: "100%",
          color: (!disabled && hovering) ? "black" : "white",
          backgroundImage: "url(" + process.env.PUBLIC_URL + "/img/buttons/grunge-highlight-" + (!disabled && hovering ? "white":"red") + ".png)",
            textAlign: "center",
        }}
        onClick={this.onClick}
        onMouseEnter={this.toggleHover}
        onMouseLeave={this.toggleHover}
      >
          {text}
      </div>
    );
  }
}

export default connect(mapStateToProps)(Button);

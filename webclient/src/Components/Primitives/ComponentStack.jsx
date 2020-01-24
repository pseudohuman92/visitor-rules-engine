import React from "react";
import "../../css/ComponentStack.css";

class Child extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hover: false };
  }

  toggleHover = () => {
    this.setState((state, props) => ({
      hover: !state.hover
    }));
  };

  render() {
    const { length, width, stepSize, children, i, horizontal, style } = this.props;
    return (
      <div
        onMouseEnter={this.toggleHover}
        onMouseLeave={this.toggleHover}
        style={{
          ...style,
          position: "absolute",
          ...(horizontal
            ? { left: "" + stepSize * i + "px" }
            : { top: "" + stepSize * i + "px" }),
          zIndex: this.state.hover ? length : i,
          width: width
        }}
      >
        {children}
      </div>
    );
  }
}

class ComponentStack extends React.Component {
  render() {
    const { children, stepSize, horizontal, width } = this.props;
    const length = React.Children.count(children);
    return (
      <div style={{display: "flex", justifyContent: "center", position: "relative", height:"100%"}}>
        {React.Children.map(children, (child, i) => {
          return (
            <Child
              key={i}
              length={length}
              width={width}
              stepSize={stepSize}
              horizontal={horizontal}
              i={i}
            >
              {child}
            </Child>
          );
        })}
      </div>
    );
  }
}

export default ComponentStack;

import React from "react";
import { withSize } from "react-sizeme";
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
    const { length, width, stepSize, child, i, horizontal } = this.props;
    return (
      <div
        onMouseEnter={this.toggleHover}
        onMouseLeave={this.toggleHover}
        style={{
          position: "absolute",
          ...(horizontal
            ? { left: "" + stepSize * i + "px"}
            : { top: "" + stepSize * i + "px" }),
          zIndex: this.state.hover ? length : i,
          width: width
        }}
      >
        {child}
      </div>
    );
  }
}

class VerticalStack extends React.Component {
  render() {
    const { children, size, stepSize, horizontal } = this.props;
    const length = React.Children.count(children);
    return (
      <div
        className="component-stack"
      >
        {React.Children.map(children, (child, i) => {
          return (
            <Child
              key={i}
              length={length}
              child={child}
              width={size.width}
              stepSize={stepSize}
              horizontal={horizontal}
              i={i}
            />
          );
        })}
      </div>
    );
  }
}

export default withSize()(VerticalStack);

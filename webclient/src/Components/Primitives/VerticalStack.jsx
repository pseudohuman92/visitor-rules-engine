import React from "react";
import { withSize } from "react-sizeme";

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
    const {
      length,
      width,
      stepSize,
      child,
      i,
    } = this.props;
    return (
      <div
        onMouseEnter={event => {
          this.toggleHover();
        }}
        onMouseLeave={event => {
          this.toggleHover();
        }}
        style={{
          position: "absolute",
          top: "" +  (stepSize * i) + "px",
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
    const { children, size, stepSize } = this.props;
    const length = React.Children.count(children);
    return (
      <div>
          {React.Children.map(children, (child, i) => {
            return (
              <Child
                key={i}
                length={length}
                child={child}
                width = {size.width}
                stepSize = {stepSize}
                i={i}
              />
            );
          })}
      </div>
    );
  }
}

export default withSize()(VerticalStack);
import React from "react";
import Grid from "@material-ui/core/Grid";


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
      angle,
      elevation,
      width,
      child,
      i,
    } = this.props;
    const oneSide = Math.floor(length / 2);
    const rotationStep = Math.floor(angle / 2 / oneSide);
    const elevationStep = elevation / oneSide;
    const widthStep = Math.floor(width / length);
    function stepCount(i) {
      return length % 2 > 0 ? i - oneSide : i - oneSide + (i < oneSide ? 0 : 1);
    }
    return (
      <div
        onMouseEnter={event => {
          this.toggleHover();
          if (child.props.onMouseEnter) child.props.onMouseEnter(event);
        }}
        onMouseLeave={event => {
          this.toggleHover();
          if (child.props.onMouseExit) child.props.onMouseExit(event);
        }}
        style={{
          transform: "rotate(" + (rotationStep * stepCount(i)) + "deg)",
          position: "absolute",
          top: "" +  (elevationStep * Math.abs(stepCount(i))) + "px",
          //- (this.state.hover ? 2 * elevationStep : 0)}%`,
          left: "" + (widthStep * i) + "px",
          zIndex: this.state.hover ? length : i,
          textAlign: "justify"
        }}
      >
        {child}
      </div>
    );
  }
}

class Fanner extends React.Component {
  render() {
    const { children, angle, elevation, width } = this.props;
    const length = React.Children.count(children);
    return (
      <div style={{height:"100%"}}>
          {React.Children.map(children, (child, i) => {
            return (
              <Child
                length={length}
                angle={angle}
                elevation={elevation}
                width={width}
                child={child}
                i={i}
              />
            );
          })}
      </div>
    );
  }
}

export default Fanner;

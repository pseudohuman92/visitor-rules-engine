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
      angle,
      width,
      child,
      i,
    } = this.props;
    const oneSide = Math.floor(length / 2);
    const rotationStep = Math.floor(angle / 2 / oneSide);
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
          transform: "rotate(" + (rotationStep * stepCount(i) + 180) + "deg)",
          flexGrow: 1,
          zIndex: this.state.hover ? length : i,
          marginBottom: (Math.abs(Math.sin(rotationStep * stepCount(i))) * width)/2,
        }}
      >
        {child}
      </div>
    );
  }
}

class Fanner extends React.Component {
  render() {
    const { children, angle, maxNumItems} = this.props;
    const {width} = this.props.size;
    const length = React.Children.count(children);
    return (
      <div style={{height:"100%", display: "flex", justifyContent: "center", alignItems: "flex-end"}}>
          {React.Children.map(children, (child, i) => {
            return (
              <Child
                length={length}
                angle={angle}
                width={Math.min(width/length, width/maxNumItems)}
                child={child}
                i={i}
              />
            );
          })}
      </div>
    );
  }
}

export default withSize()(Fanner);

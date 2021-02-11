import React from "react";
import { debugPrint } from "../Helpers/Helpers";

class ComponentStack extends React.Component {
  render() {
    const { children, stepSize, horizontal, width } = this.props;
    return (
      <div style={{display: "flex", justifyContent: "center", position: "relative", height:"100%"}}>
        {React.Children.map(children, (child, i) => {
          debugPrint("Component Stack Child", child);
          return (
            <div
              key={i}
              style={{
                position: "absolute",
                ...(horizontal
                  ? { left: "" + stepSize * i + "px" }
                  : { top: "" + stepSize * i + "px" }),
                zIndex: i,
                width: width
              }}
            >
              {child}
            </div>
          );
        })}
      </div>
    );
  }
}

export default ComponentStack;

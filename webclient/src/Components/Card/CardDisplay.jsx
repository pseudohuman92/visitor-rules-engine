import React from "react";
import { PureComponent } from "react";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";

import FullCard from "./FullCard";
import SmallCard from "./SmallCard";
import {keywords} from "../Helpers/Constants";
import FittedText from "../Primitives/FittedText";

export class CardDisplay extends PureComponent {
  state = {
    popoverStyle: {display: "none", width: 0}
  };

  handlePopoverOpen = (event) => {
    const {width, height} = this.props.windowDimensions;
    const rect = event.currentTarget.getBoundingClientRect();

    const style = {};
    style["width"] = width / 5;
    style["height"] = (width / 5) * (88 / 63);
    style["display"] = "flex";
    style["textAlign"] = "left";
    //style["border"] = "2px solid red";

    if (rect.top < height / 2) {
      style["top"] = rect.height;
    } else {
      style["bottom"] = rect.height;
    }

    if (rect.left < width / 2) {
      style["left"] = rect.width;
    } else {
      style["right"] = rect.width;
      style["flexDirection"] = "row-reverse";
    }
    this.setState({
      popoverStyle: style,
    });
  };

  handlePopoverClose = (event) => {
    this.setState({popoverStyle: {display: "none", width: 0}});
  };

  render() {
    const {popoverStyle} = this.state;
    const { onClick, small, style, popoverDisabled, isDragging, withKeywords, dragHandleProps, ...rest } = this.props;
    return (
      <div
        {...dragHandleProps}
        style={{ width: "100%", height: "100%" }}

      >
        {!isDragging && !popoverDisabled && (
            <div
                style={{
                  position: "absolute",
                  zIndex: 20,
                  ...popoverStyle,
                }}
                opacity={1}
            >
              <div
                  style={{
                    width: popoverStyle.width / (withKeywords? 2:1),
                    height: popoverStyle.height,
                    justify: "center",
                    alignContent: "center",
                    //border: "2px blue solid"
                  }}
              >
                <FullCard scale={1.5} opacity={1} {...rest} />
              </div>
              {withKeywords &&
              <div
                  style={{
                    display: "flex",
                    flexDirection: "column",
                    width: popoverStyle.width / 2,
                    height: popoverStyle.height,
                    //border: "2px green solid"
                  }}
              >
                {this.props.cardData.description &&
                Object.keys(keywords).map((keyword, i) => {
                  if (this.props.cardData.description.indexOf(keyword) !== -1 ||
                      (this.props.cardData.combat && this.props.cardData.combat.combatAbilities.indexOf(keyword) !== -1)) {
                    return (
                        <div
                            key={i}
                            style={{
                              color: "white",
                              backgroundColor: "black",
                              border: "1px white solid",
                              borderRadius: "5px",
                              whiteSpace: "pre-wrap",
                            }}
                        >
                          <FittedText
                              text={keyword + "\n" + keywords[keyword]}
                              windowDimensions={this.props.windowDimensions}
                          />
                        </div>
                    );
                  }
                  return <div key={i}/>;
                })}
              </div>
              }
            </div>
        )}
        <div onMouseEnter={this.handlePopoverOpen} onMouseLeave={this.handlePopoverClose} onClick={onClick? onClick : ()=>{}}
             style={style}>
          {small ? (
            <SmallCard {...rest} />
          ) : (
            <FullCard {...rest} />
          )}
        </div>
      </div>
    );
  }
}

export default CardDisplay;

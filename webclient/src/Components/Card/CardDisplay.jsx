import React from "react";
import { PureComponent } from "react";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";

import FullCard from "./FullCard";
import SmallCard from "./SmallCard";

export class CardDisplay extends PureComponent {
  state = { showDialog: false };

  openDialog = event => {
    if (event.ctrlKey) {
      this.setState({ showDialog: true });
    } else if (this.props.onClick) {
      this.props.onClick(event);
    }
  };

  render() {
    const { small, style, dragHandleProps, ...rest } = this.props;
    return (
      <div
        {...dragHandleProps}
        style={{ width: "100%", height: "100%", ...style }}
      >
        <Dialog
          open={this.state.showDialog}
          onClose={event => this.setState({ showDialog: false })}
          maxWidth="xs"
          fullWidth
          scroll="body"
        >
          <DialogContent>
            <FullCard {...rest} opacity="1" play={false} />
          </DialogContent>
        </Dialog>
        <div onClick={this.openDialog}>
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

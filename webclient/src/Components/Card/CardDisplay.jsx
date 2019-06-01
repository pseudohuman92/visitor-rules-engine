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
    const { small } = this.props;

    return (
      <div>
        <Dialog
          open={this.state.showDialog}
          onClose={event => this.setState({ showDialog: false })}
          maxWidth="xs"
          fullWidth={true}
        >
          <DialogContent>
            <FullCard {...this.props} opacity="1" play={false} />
          </DialogContent>
        </Dialog>
        <div
          onClick={this.openDialog}
        >
          {small ? <SmallCard {...this.props} /> : <FullCard {...this.props} />}
        </div>
      </div>
    );
  }
}

export default CardDisplay;

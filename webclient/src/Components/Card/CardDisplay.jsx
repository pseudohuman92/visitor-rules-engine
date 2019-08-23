import React from "react";
import { PureComponent } from "react";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import { getEmptyImage } from "react-dnd-html5-backend";
import { withSize } from "react-sizeme";

import FullCard from "./FullCard";
import SmallCard from "./SmallCard";


export class CardDisplay extends PureComponent {
  state = { showDialog: false };

  componentDidMount() {
    const { connectDragPreview } = this.props;
    if (connectDragPreview) {
      connectDragPreview(getEmptyImage(), { captureDraggingState: true });
    }
  }

  openDialog = event => {
      if (event.ctrlKey) {
        this.setState({ showDialog: true });
      } else if (this.props.onClick) {
        this.props.onClick(event);
      }
  };

  render() {
    const {
      small,
      size,
      ...rest
    } = this.props;
    return (
      <div>
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

export default withSize()(CardDisplay);

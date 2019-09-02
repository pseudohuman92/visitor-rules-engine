import React from "react";
import { PureComponent } from "react";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import { getEmptyImage } from "react-dnd-html5-backend";

import FullCard from "./FullCard";
import SmallCard from "./SmallCard";
import SquareCard from "./SquareCard";
import { withSize } from "react-sizeme";


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
      square,
      style,
      size,
      ...rest
    } = this.props;
    return (
      <div style={{width:"100%", height: "100%", ...style}}>
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
              square ?
              <SquareCard {...rest} />:
              <FullCard {...rest} />
            )}
          </div>
      </div>
    );
  }
}

export default withSize({monitorHeight: true})(CardDisplay);

import React from "react";
import { PureComponent } from "react";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import { getEmptyImage } from 'react-dnd-html5-backend'
import { defineHold, Holdable } from 'react-touch';
import { withSize } from "react-sizeme";

import FullCard from "./FullCard";
import MediumCard from './MediumCard';
import SmallCard from "./SmallCard";

export class CardDisplay extends PureComponent {
  state = { showDialog: false };

  componentDidMount() {
    const { connectDragPreview } = this.props
    if (connectDragPreview) {
      connectDragPreview(getEmptyImage(), {captureDraggingState: true,})
    }
  }

  openDialog = event => {
    if (event){
      if (event.ctrlKey) {
        this.setState({ showDialog: true });
      } else if (this.props.onClick) {
        this.props.onClick(event);
      }
    } else { //Hold event
      this.setState({ showDialog: true });
    }
  };

  render() {
    const { small, medium, size, ...rest } = this.props;
    const hold = defineHold({updateEvery: 50, holdFor: 250});
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
        <Holdable config={hold} onHoldComplete={this.openDialog}>
        <div
          onClick={this.openDialog}
        >
          {small ? <SmallCard {...rest} /> : (medium? <MediumCard {...rest}/>: <FullCard {...rest} />)}
        </div>
        </Holdable>
      </div>
    );
  }
}

export default withSize()(CardDisplay);

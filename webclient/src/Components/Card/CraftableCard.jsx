import React from "react";
import { PureComponent } from "react";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import { getEmptyImage } from "react-dnd-html5-backend";
import { withSize } from "react-sizeme";

import FullCard from './FullCard';
import Button from '../Primitives/Button';
import { Grid } from "@material-ui/core";
import { craftCost, salvageValue } from "../Helpers/Constants";

class CraftableCard extends PureComponent {
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
      size,
      craft,
      onCraft,
      craftDisabled,
      onSalvage,
      salvageDisabled,
      count,
      ...rest
    } = this.props;
    return (
      <div>
        <Dialog
          open={this.state.showDialog}
          onClose={event => this.setState({ showDialog: false })}
          maxWidth={"sm"}
          fullWidth
          scroll="body"
        >
          <DialogContent>
            <Grid container spacing={8}>
                <Grid item xs={2}>
                  <Button
                    onClick={onCraft}
                    disabled={craftDisabled}
                    text="Craft"
                  />
                  {"Dust: -"+ craftCost}
                </Grid>
              <Grid item xs={8}>
                <center>{count}</center>
                <FullCard {...rest} opacity="1" play={false} />
              </Grid>
                <Grid item xs={2}>
                  <Button
                    onClick={onSalvage}
                    disabled={salvageDisabled}
                    text="Salvage"
                  />
                  {"Dust: +" + salvageValue}
                </Grid>
            </Grid>
          </DialogContent>
        </Dialog>
          <div onClick={this.openDialog}>
              <FullCard {...rest} />
          </div>
      </div>
    );
  }
}

export default withSize()(CraftableCard);

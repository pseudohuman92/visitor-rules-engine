import React from "react";
import { PureComponent } from "react";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import { withSize } from "react-sizeme";

import Button from '../Primitives/Button';
import { Grid } from "@material-ui/core";
import { craftCost, salvageValue } from "../Helpers/Constants";
import { CardDisplay } from "./CardDisplay";

class CraftableCard extends PureComponent {
  state = { showDialog: false };

  openDialog = event => {

      if (event.ctrlKey) {
        this.setState({ showDialog: true });
      } else if (this.props.onClick) {
        this.props.onClick(event);
      }
  };

  render() {
    const {
      onCraft,
      craftDisabled,
      onSalvage,
      salvageDisabled,
      count,
      cardData,
      windowDimensions
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
                <CardDisplay cardData={cardData}  opacity="1" windowDimensions={windowDimensions}/>
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
              <CardDisplay cardData={cardData} windowDimensions={windowDimensions}/>
          </div>
      </div>
    );
  }
}

export default withSize()(CraftableCard);

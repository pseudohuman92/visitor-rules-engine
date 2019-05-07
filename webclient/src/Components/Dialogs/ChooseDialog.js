import React, { Component } from "react";

import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";

import PlayingCard from "../Card/PlayingCard";
import "../../css/ChooseDialog.css";

export default class ChooseDialog extends Component {
  render = () => {
    const {
      open,
      title,
      cards,
      onClose,
      selectedCards,
      selectableCards,
      upTo,
      isSelectPhase
    } = this.props;

    let doneButton = null;
    if (upTo) {
      doneButton = (
        <Button
          color="primary"
          variant="contained"
          //TODO: fix this
          //onClick={event => SelectDone()}
        >
          Done{" "}
        </Button>
      );
    }

    return (
      <Dialog
        open={open}
        onClose={onClose}
        maxWidth={false}
        fullWidth={true}
        disableBackdropClick={isSelectPhase}
        disableEscapeKeyDown={isSelectPhase}
      >
        <DialogTitle> {title} </DialogTitle>{" "}
        <DialogContent>
          <Grid container spacing={0} className="choose-dialog">
            {" "}
            {cards.map(card => (
              <Grid item xs={1} key={card.id}>
                <PlayingCard
                  selectable={selectableCards.includes(card.id)}
                  selected={selectedCards.includes(card.id)}
                  {...card}
                />{" "}
              </Grid>
            ))}{" "}
          </Grid>{" "}
          {doneButton}{" "}
        </DialogContent>{" "}
      </Dialog>
    );
  };
}

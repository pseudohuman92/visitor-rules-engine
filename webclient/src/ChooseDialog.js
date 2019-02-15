import React, {Component} from 'react';

import Dialog from '@material-ui/core/Dialog';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import Grid from '@material-ui/core/Grid';

import PlayingCard from './PlayingCard.js';
import './ChooseDialog.css';

export default class ChooseDialog extends Component {
  render = () => {
    const {open, title, cards, onClose} = this.props;
    const selectCandIDs = this.props.selectCands.map(card => card.id);

    return (
      <Dialog open={open} onClose={onClose} maxWidth={false} fullWidth={true}>
        <DialogTitle>{title}</DialogTitle>
        <DialogContent>
          <Grid container spacing={0} className="choose-dialog">
            {cards.map(card => (
              <Grid item xs={1} key={card.id}>
                <PlayingCard
                  inHand={false}
                  myCard={false}
                  selectable={selectCandIDs.includes(card.id)}
                  {...card}
                />
              </Grid>
            ))}
          </Grid>
        </DialogContent>
      </Dialog>
    );
  };
}

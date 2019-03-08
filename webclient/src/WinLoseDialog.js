import React, {Component} from 'react';

import Dialog from '@material-ui/core/Dialog';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';

export default class WinLoseDialog extends Component {
  render = () => {
    const {open, win} = this.props;

    return (
      <Dialog open={open}>
        <DialogTitle>{win ? 'You Win!' : 'You Lose...'}</DialogTitle>
        <DialogContent>
          <img
            src={process.env.PUBLIC_URL + `/img/${win ? 'win' : 'lose'}.jpg`}
          />
        </DialogContent>
      </Dialog>
    );
  };
}

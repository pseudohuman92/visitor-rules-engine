import React, {Component} from 'react';

import CircularProgress from '@material-ui/core/CircularProgress';
import Dialog from '@material-ui/core/Dialog';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';

export default class ChooseDialog extends Component {
  render = () => {
    const open = this.props.open;

    return (
      <Dialog open={open}>
        <DialogTitle>Waiting...</DialogTitle>
        <DialogContent>
          <DialogContentText>Please wait for an opponent...</DialogContentText>
          <CircularProgress />
        </DialogContent>
      </Dialog>
    );
  };
}

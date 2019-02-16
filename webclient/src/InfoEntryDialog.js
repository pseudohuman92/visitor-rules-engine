import React, {Component} from 'react';

import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogActions from '@material-ui/core/DialogActions';
import TextField from '@material-ui/core/TextField';

import {JoinTable} from './Manage.js';

export default class InfoEntryDialog extends Component {
  constructor(props) {
    super(props);
    this.state = {
      open: props.open,
      username: '',
      onSubmit: props.onSubmit,
    };
  }

  handleInfoUpdate = event => {
    const username = this.state.username;
    console.assert(username);
    JoinTable(username, 'best game');
    this.setState({open: false});
    this.state.onSubmit(event);
  };

  handleChange = event => {
    this.setState({username: event.target.value});
  };

  handleKeyPress = event => {
    if (event.keyCode == 13) {
      this.handleInfoUpdate(event);
      event.preventDefault();
    }
  };

  render = () => {
    const open = this.state.open;

    return (
      <Dialog open={open}>
        <DialogTitle>Enter Info</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please enter the following info to get started.
          </DialogContentText>
          <TextField
            autoFocus
            id="username"
            label="Username"
            type="text"
            onChange={this.handleChange}
            onKeyDown={this.handleKeyPress}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={this.handleInfoUpdate}>Submit</Button>
        </DialogActions>
      </Dialog>
    );
  };
}

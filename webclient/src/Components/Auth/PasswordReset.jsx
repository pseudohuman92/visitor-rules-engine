import React, { Component } from "react";
import Button from "@material-ui/core/Button";
import FormControl from "@material-ui/core/FormControl";
import Input from "@material-ui/core/Input";
import InputLabel from "@material-ui/core/InputLabel";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import Center from "react-center";

import { withFirebase } from "../Firebase";

class PasswordReset extends Component {
  constructor(props) {
    super(props);

    this.state = {
      value: 0,
      email: "",
      error: null
    };
  }

  onChange = event => {
    this.setState({ [event.target.name]: event.target.value });
  };

  resetPassword = event => {
    const { email } = this.state;

    this.props.firebase
      .doPasswordReset(email)
      .then(() => {
        this.setState({ value: 1 });
      })
      .catch(error => {
        this.setState({ error });
      });
  };

  render() {
    const { value, email, error } = this.state;

    const isInvalid = email === "" || value !== 0;

    return (
      <Center >
        <Paper>
          <Typography component="h1" variant="h5">
            Reset Password
          </Typography>
          <FormControl margin="normal" required fullWidth>
            <InputLabel htmlFor="email">Email Address</InputLabel>
            <Input
              id="email"
              name="email"
              autoComplete="email"
              autoFocus
              onChange={this.onChange}
            />
          </FormControl>
          <Button
            disabled={isInvalid}
            variant="contained"
            onClick={this.resetPassword}
          >
            Reset
          </Button>
          {error && <p>{error.message}</p>}
          {value === 1 && <p>{"Password reset email sent to "+email}</p>}
        </Paper>
      </Center>
    );
  }
}

export default withFirebase(PasswordReset);

import React, { Component } from "react";
import Button from "@material-ui/core/Button";
import FormControl from "@material-ui/core/FormControl";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Checkbox from "@material-ui/core/Checkbox";
import Input from "@material-ui/core/Input";
import InputLabel from "@material-ui/core/InputLabel";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import Center from "react-center";
import { connect } from "react-redux";

import { withFirebase } from "../Firebase";
import Profile from "../MainScreens/Profile";
import PasswordReset from "./PasswordReset";
import { mapDispatchToProps } from "../Redux/Store";

class SignIn extends Component {
  state = {
      value: 0,
      email: "",
      password: "",
      error: null
    };

  onChange = event => {
    this.setState({ [event.target.name]: event.target.value });
  };

  Signin = event => {
    const { email, password } = this.state;
    const { firebase, updateState } = this.props;

    firebase
      .doSignInWithEmailAndPassword(email, password)
      .then(authUser => {
        updateState({ authUser: authUser });
        firebase.setUserData(authUser.user.uid, updateState);
        this.setState({ value: 1 });
      })
      .catch(error => {
        this.setState({ error });
      });

    event.preventDefault();
  };

  render() {
    const { value, email, password, error } = this.state;

    const isInvalid = password === "" || email === "";

    return (
      <div>
        {value === 0 && (
          <Center>
            <Paper>
              <Typography component="h1" variant="h5">
                Sign in
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
              <FormControl margin="normal" required fullWidth>
                <InputLabel htmlFor="password">Password</InputLabel>
                <Input
                  name="password"
                  type="password"
                  id="password"
                  autoComplete="current-password"
                  onChange={this.onChange}
                />
              </FormControl>
              <FormControlLabel
                control={<Checkbox value="remember" color="primary" />}
                label="Remember me"
              />
              <Button
                disabled={isInvalid}
                type="submit"
                variant="contained"
                onClick={this.Signin}
              >
                Sign in
              </Button>
              <Button
                color="primary"
                onClick={event => this.setState({ value: 2 })}
              >
                Forgot password?
              </Button>
              {error && <p>{error.message}</p>}
            </Paper>
          </Center>
        )}
        {value === 1 && <Profile />}
        {value === 2 && <PasswordReset />}
      </div>
    );
  }
}

export default connect(
  null,
  mapDispatchToProps
)(withFirebase(SignIn));

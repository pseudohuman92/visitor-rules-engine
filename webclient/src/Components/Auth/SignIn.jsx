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
import { isProduction } from "../Helpers/Constants";
import { withCookies } from 'react-cookie';
import {debugPrint} from "../Helpers/Helpers";

class SignIn extends Component {
  constructor(props) {
    super(props);

    const { cookies } = props;
    const loginCredentials = cookies.get("Login Credentials");
    debugPrint(loginCredentials);
    this.state = {
      value: 0,
      email: loginCredentials ? loginCredentials.email : "",
      password: loginCredentials ? loginCredentials.password : "",
      error: null,
      rememberMe: loginCredentials?loginCredentials.rememberMe : false,
    };
  }

  handleTextbox = event => {
    this.setState({ [event.target.name]: event.target.value });
  };

  handleCheckbox = event => {
    this.setState({ [event.target.name]: event.target.checked });
  };

  Signin = event => {
    const { email, password, rememberMe } = this.state;
    const { cookies, firebase, updateState } = this.props;

    firebase
      .doSignInWithEmailAndPassword(email, password)
      .then(firebaseAuthData => {
        updateState({ firebaseAuthData: firebaseAuthData });
        firebase.setUserData(firebaseAuthData.user.uid, updateState);
        if (rememberMe){
          cookies.set("Login Credentials", {email: email, password: password, rememberMe: rememberMe});
        } else {
          cookies.remove("Login Credentials");
        }

        this.setState({ value: 1 });
      })
      .catch(error => {
        this.setState({ error });
      });

    event.preventDefault();
  };

  Test1 = event => {
    const { firebase, updateState } = this.props;
    firebase
      .doSignInWithEmailAndPassword("tester1@testers.com", "asdqwe")
      .then(firebaseAuthData => {
        updateState({ firebaseAuthData: firebaseAuthData });
        firebase.setUserData(firebaseAuthData.user.uid, updateState);
        this.setState({ value: 1 });
      })
      .catch(error => {
        this.setState({ error });
      });

    event.preventDefault();
  };

  Test2 = event => {
    const { firebase, updateState } = this.props;
    firebase
      .doSignInWithEmailAndPassword("tester2@testers.com", "asdqwe")
      .then(firebaseAuthData => {
        updateState({ firebaseAuthData: firebaseAuthData });
        firebase.setUserData(firebaseAuthData.user.uid, updateState);
        this.setState({ value: 1 });
      })
      .catch(error => {
        this.setState({ error });
      });

    event.preventDefault();
  };

  render() {
    const { value, email, password, rememberMe, error } = this.state;

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
                  value = {email}
                  id="email"
                  name="email"
                  autoComplete="email"
                  autoFocus
                  onChange={this.handleTextbox}
                />
              </FormControl>
              <FormControl margin="normal" required fullWidth>
                <InputLabel htmlFor="password">Password</InputLabel>
                <Input
                  value={password}
                  name="password"
                  type="password"
                  id="password"
                  autoComplete="current-password"
                  onChange={this.handleTextbox}
                />
              </FormControl>
              <FormControlLabel
                control={<Checkbox name="rememberMe" color="primary" checked={rememberMe} onChange={this.handleCheckbox}/>}
                label="Remember me"
              />
              {!isProduction && (
                <Button type="submit" variant="contained" onClick={this.Test1}>
                  Tester 1
                </Button>
              )}
              {!isProduction && (
                <Button type="submit" variant="contained" onClick={this.Test2}>
                  Tester 2
                </Button>
              )}
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

export default connect(null, mapDispatchToProps)(withFirebase(withCookies(SignIn)));

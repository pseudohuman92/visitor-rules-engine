import React, { Component } from "react";
import Button from "@material-ui/core/Button";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import Center from "react-center";

import SignIn from "./Components/Auth/SignIn";
import SignUp from "./Components/Auth/SignUp";

export default class MainPage extends Component {
  constructor(props) {
    super(props);

    this.state = { value: 0 };
  }

  Signin = event => {
    this.setState({ value: 1 });
  };

  Signup = event => {
    this.setState({ value: 2 });
  };

  render() {
    const { value } = this.state;

    return (
      <div>
        {value === 0 && (
          <Center>
            <Paper>
              <Typography component="h1" variant="h1">
                Visitor: The Card Game
              </Typography>
              <Button variant="contained" onClick={this.Signin}>
                Sign In
              </Button>
              <Button variant="contained" onClick={this.Signup}>
                Sign Up
              </Button>
            </Paper>
          </Center>
        )}
        {value === 1 && <SignIn />}
        {value === 2 && <SignUp />}
      </div>
    );
  }
}

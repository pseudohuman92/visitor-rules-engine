import React, { Component } from "react";
import Button from "@material-ui/core/Button";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import Center from "react-center";

import SignIn from "../Auth/SignIn";
import SignUp from "../Auth/SignUp";
import DesignCard from "./DesignCard";
import CustomCardPage from "./CustomCardPage";

export default class MainPage extends Component {
  constructor(props) {
    super(props);

    this.state = { value: 0 };
  }
  
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
              <Button
                variant="contained"
                onClick={event => this.setState({ value: 1 })}
              >
                Sign In
              </Button>
              <Button
                variant="contained"
                onClick={event => this.setState({ value: 2 })}
              >
                Sign Up
              </Button>
              <Button
                variant="contained"
                onClick={event => this.setState({ value: 3 })}
              >
                Design a Card
              </Button>
              <Button
                variant="contained"
                onClick={event => this.setState({ value: 4 })}
              >
                Custom Cards
              </Button>
            </Paper>
          </Center>
        )}
        {value === 1 && <SignIn />}
        {value === 2 && <SignUp />}
        {value === 3 && <DesignCard />}
        {value === 4 && <CustomCardPage/>}
      </div>
    );
  }
}

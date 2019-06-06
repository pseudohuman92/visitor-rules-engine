import React, { Component } from "react";
import Button from "../Primitives/Button";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import Center from "react-center";

import SignIn from "../Auth/SignIn";
import SignUp from "../Auth/SignUp";
import DesignCard from "./DesignCard";
import CustomCardGallery from "./CustomCardGallery";
import CardGallery from "./CardGallery";
import { Grid } from "@material-ui/core";
import Fonts from '../Primitives/Fonts';

export default class MainPage extends Component {
  constructor(props) {
    super(props);

    this.state = { value: 0 };
    this.back = () => this.setState({ value: 0 });
  }

  render() {
    const { value } = this.state;

    return (
      <div>
        {value === 0 && (
          <Center>
            <Fonts />
            <Paper>
              <Center>
                <Typography
                  component="h1"
                  variant="h1"
                  style={{ fontFamily: "Cinzel, serif" }}
                >
                  Visitor: The Card Game
                </Typography>
              </Center>
              <Grid container spacing={8}>
                <Grid item xs>
                  <Button
                    onClick={event => this.setState({ value: 1 })}
                    text="Sign In"
                  />
                </Grid>
                <Grid item xs>
                  <Button
                    onClick={event => this.setState({ value: 2 })}
                    text="Sign Up"
                  />
                </Grid>
                <Grid item xs>
                  <Button
                    onClick={event => this.setState({ value: 3 })}
                    text="Design a Card"
                  />
                </Grid>
                <Grid item xs>
                  <Button
                    onClick={event => this.setState({ value: 4 })}
                    text="Custom Cards"
                  />
                </Grid>
                <Grid item xs>
                  <Button
                    onClick={event => this.setState({ value: 5 })}
                    text="Card Gallery"
                  />
                </Grid>
              </Grid>
            </Paper>
          </Center>
        )}
        {value === 1 && <SignIn />}
        {value === 2 && <SignUp />}
        {value === 3 && <DesignCard back={this.back} />}
        {value === 4 && <CustomCardGallery back={this.back} />}
        {value === 5 && <CardGallery back={this.back} />}
        <div hidden>
        <CardGallery back={this.back} />
        </div>
      </div>
    );
  }
}

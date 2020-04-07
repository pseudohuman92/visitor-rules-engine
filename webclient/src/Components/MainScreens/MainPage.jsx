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
import { Grid, Link } from "@material-ui/core";
import Fonts from "../Primitives/Fonts";

import "../../css/App.css";
import ErrorBoundary from "../Primitives/ErrorBoundary";

export default class MainPage extends Component {
  constructor(props) {
    super(props);

    this.state = { value: 0 };
    this.back = () => this.setState({ value: 0 });
  }

  render() {
    const { value } = this.state;

    return (
      <ErrorBoundary>
      <div>
        {value === 0 && (
          <Center>
            <Fonts />
            <Paper>
              <Center>
                <Typography
                  variant="h3"
                  style={{ fontFamily: "Cinzel, serif" }}
                >
                  Visitor: The Card Game
                </Typography>
              </Center>
              <Typography variant="body1" paragraph>
                Visitor is a completely free collectible card game that brings
                together some less developed aspects of the genre to solve
                common issues of such games. Some of the notable features are
              </Typography>

              <Typography variant="body1" gutterBottom>
                <b>* Completely free:</b> No way to buy boosters, just earn them
                by playing.
              </Typography>
              <Typography variant="body1" gutterBottom>
                <b>* No rarities:</b> No hard to get chase cards.
              </Typography>
              <Typography variant="body1" gutterBottom>
                <b>* Web based client:</b> No need to download a dedicated
                program.
              </Typography>
              <Typography variant="body1" gutterBottom>
                <b>* No units or combat:</b> Only spells and other permanent
                card types.
              </Typography>
              <Typography variant="body1" gutterBottom>
                <b>* No relying on dedicated resource cards:</b> Each card can
                be used as a resource.
              </Typography>
              <Typography variant="body1" gutterBottom>
                <b>* Stack-based interactive play:</b> No waiting your turn to
                play.
              </Typography>
              <Typography variant="body1" paragraph>
                <b>* Brewer oriented design:</b> Will emphasize build-around
                cards with complex interactions.
              </Typography>
              <Typography variant="body1" paragraph>
                Please join us in our{" "}
                <Link href="https://discord.gg/JSMPStm" gutterBottom>
                  Discord Server
                </Link>{" "}
                to learn the game and follow the development process.
              </Typography>

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
      </div>
      </ErrorBoundary>
    );
  }
}

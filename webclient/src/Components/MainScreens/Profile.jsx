import React, { Component } from "react";
import Button from "../Primitives/Button";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import Center from "react-center";
import { connect } from "react-redux";

import OpenPacks from "./OpenPacks";
import { mapDispatchToProps } from "../Redux/Store";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import Decks from "./Decks";
import DeckSelection from "./DeckSelection";
import Fonts from "../Primitives/Fonts";
import CollectionScreen from "./CollectionScreen";
import GameStore from "./GameStore";

const mapStateToProps = state => {
  return {
    username: state.username,
    userId: state.authUser.user.uid,
    dust: state.dust,
    coins: state.coins,
    dailyWins: state.dailyWins
  };
};

class Profile extends Component {
  constructor(props) {
    super(props);
    this.state = { value: 0 };
    this.back = () => this.setState({ value: 0 });
  }

  play = event => {
    this.setState({ value: 1 });
  };

  render() {
    const { username, userId, dust, coins, dailyWins } = this.props;
    const { value } = this.state;
    return (
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
                  {username + "'s Profile"}
                </Typography>
              </Center>
              <Center>
                <Typography>{"ID: " + userId}</Typography>
              </Center>
              <Center>
                <Typography>{"Dust: " + dust}</Typography>
              </Center>
              <Center>
                <Typography>{"Coins: " + coins}</Typography>
              </Center>
              <Center>
                <Typography>{"Daily Wins: " + dailyWins}</Typography>
              </Center>
              <Grid container spacing={8}>
                <Grid item xs>
                  <Button onClick={this.play} text="Play" />
                </Grid>
                <Grid item xs>
                  <Button
                    onClick={event => this.setState({ value: 2 })}
                    text="Decks"
                  />
                </Grid>
                <Grid item xs>
                  <Button
                    onClick={event => this.setState({ value: 3 })}
                    text="Open Packs"
                  />
                </Grid>
                <Grid item xs>
                  <Button
                    onClick={event => this.setState({ value: 4 })}
                    text="Collection"
                  />
                </Grid>
                <Grid item xs>
                  <Button
                    onClick={event => this.setState({ value: 5 })}
                    text="Store"
                  />
                </Grid>
              </Grid>
            </Paper>
          </Center>
        )}
        {value === 1 && <DeckSelection back={this.back} />}
        {value === 2 && <Decks back={this.back} />}
        {value === 3 && <OpenPacks back={this.back} />}
        {value === 4 && <CollectionScreen back={this.back} />}
        {value === 5 && <GameStore back={this.back} />}
      </div>
    );
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withHandlers(Profile));

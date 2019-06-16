import React, { Component } from "react";
import Button from "../Primitives/Button";
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
    const style = { fontFamily: "Cinzel, serif" };
    return (
      <div>
        {value === 0 && (
          <Center>
            <Fonts />
            <Paper>
              <Typography style={style}>{username + "'s Profile"}</Typography>
              <Typography style={style}>{"ID: " + userId}</Typography>
              <Typography style={style}>{"Dust: " + dust}</Typography>
              <Typography style={style}>{"Coins: " + coins}</Typography>
              <Typography style={style}>
                {"Daily Wins: " + dailyWins}
              </Typography>
              <Button onClick={this.play} text="Play" />
              <Button
                onClick={event => this.setState({ value: 2 })}
                text="Decks"
              />
              <Button
                onClick={event => this.setState({ value: 3 })}
                text="Open Packs"
              />
              <Button
                onClick={event => this.setState({ value: 4 })}
                text="Collection"
              />
              <Button
                onClick={event => this.setState({ value: 5 })}
                text="Store"
              />
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

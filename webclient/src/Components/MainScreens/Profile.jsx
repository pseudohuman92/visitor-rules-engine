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
import Fonts from '../Primitives/Fonts';

const mapStateToProps = state => {
  return { username: state.username, userId: state.authUser.user.uid };
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
    const { username } = this.props
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
                  {username+"'s Profile"}
                </Typography>
              </Center>
              <Button onClick={this.play} text="Play" />
              <Button
                onClick={event => this.setState({ value: 2 })}
                text="Decks"
              />
              <Button
                onClick={event => this.setState({ value: 3 })}
                text="Open Packs"
              />
            </Paper>
          </Center>
        )}
        {value === 1 && <DeckSelection back={this.back} />}
        {value === 2 && <Decks back={this.back} />}
        {value === 3 && <OpenPacks back={this.back} />}
      </div>
    );
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withHandlers(Profile));

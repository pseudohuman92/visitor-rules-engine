import React, { Component } from "react";
import Button from "@material-ui/core/Button";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import Center from "react-center";
import { connect } from "react-redux";

import OpenPacks from "./OpenPacks";
import { mapDispatchToProps } from "../Redux/Store";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import Decks from "./Decks";
import DeckSelection from "./DeckSelection";

const mapStateToProps = state => {
  return { username: state.username,
           userId : state.authUser.user.uid };
};

class Profile extends Component {
  constructor(props) {
    super(props);
    this.state = { value: 0 };
    this.back = () => this.setState({value: 0});
  }

  play = event => {
    this.setState({ value: 1 });
  }

  render() {
    const { value } = this.state;
    return (
      <div>
        {value === 0 && (
          <Center>
            <Paper>
              <Typography component="h1" variant="h1">
                Profile Page
              </Typography>
              <Button
                variant="contained"
                onClick={this.play}
              >
                Play
              </Button>
              <Button
                variant="contained"
                onClick={event => this.setState({ value: 2 })}
              >
                Decks
              </Button>
              <Button
                variant="contained"
                onClick={event => this.setState({ value: 3 })}
              >
                Open Packs
              </Button>
            </Paper>
          </Center>
        )}
        {value === 1 && <DeckSelection back={this.back}/>}
        {value === 2 && <Decks back={this.back}/>}
        {value === 3 && <OpenPacks back={this.back}/>}
      </div>
    );
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withHandlers (Profile));

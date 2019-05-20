import React, { Component } from "react";
import Grid from "@material-ui/core/Grid";
import Center from "react-center";
import Button from "@material-ui/core/Button";

import { withFirebase } from "../Firebase";
import CardDisplay from "../Card/CardDisplay";

class CustomCardGallery extends Component {
  constructor(props) {
    super(props);
    this.state = {
      cards: []
    };
  }

  componentDidMount() {
    const setState = this.setState.bind(this);
    this.props.firebase.getCustomCards(setState);
  }

  render() {
    return (
      <div>
        <Button type="submit" variant="contained" onClick={this.props.back}>
          Back
        </Button>
        <Grid container spacing={8}>
          {this.state.cards.map((card, i) => (
            <Grid item key={i} xs={3}>
              <CardDisplay {...card} />
              <Center>{card.creator}</Center>
            </Grid>
          ))}
        </Grid>
      </div>
    );
  }
}

export default withFirebase(CustomCardGallery);

import React, { Component } from "react";
import Grid from "@material-ui/core/Grid";
import Center from "react-center";

import { withFirebase } from "../Firebase";
import CardDisplay from "../Card/CardDisplay";

class CustomCardPage extends Component {
  constructor(props) {
    super(props);
    this.state = {
      cards: []
    };
  }

  componentDidMount() {
    const setState = this.setState.bind(this)
    this.props.firebase.getCustomCards(setState);
  }

  render() {
    return <Grid container spacing={8}>
      {this.state.cards.map((card, i) => 
        <Grid item key={i} xs={2}>
          <CardDisplay {...card} />
          <Center>{card.creator}</Center>
        </Grid>
      )}
    </Grid>;
  }
}

export default withFirebase(CustomCardPage)

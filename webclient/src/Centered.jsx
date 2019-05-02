import React, { Component } from "react";
import Grid from "@material-ui/core/Grid";

export default class Centered extends Component {
  render() {
    return (
      <Grid
        container
        spacing={0}
        direction="column"
        alignItems="center"
        justify="center"
        style={{ minHeight: "100vh" }}
      >
        <Grid item xs={3}>
          {this.props.children}
        </Grid>
      </Grid>
    );
  }
}

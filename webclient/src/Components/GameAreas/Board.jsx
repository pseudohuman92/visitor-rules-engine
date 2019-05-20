import React from "react";
import Grid from "@material-ui/core/Grid";

import Hand from "./Hand";
import OpponentHand from "./OpponentHand";
import BoardSide from "./BoardSide";
import InfoMessage from "./InfoMessage";

export default class Board extends React.Component {
  render() {
    return (
      <Grid
        container
        spacing={0}
        style={{ height: "100%" }}
      >
        <Grid item xs={12} style={{ height: "15%" }}>
          <OpponentHand/>
        </Grid>
        <Grid item xs={12} style={{ height: "30%" }}>
          <BoardSide isPlayer={false} />
        </Grid>
        <Grid item xs={12} style={{ height: "30%" }}>
          <BoardSide isPlayer={true} />
        </Grid>
        <Grid item xs={12} style={{ height: "10%" }}>
          <InfoMessage/>
        </Grid>
        <Grid item xs={12} style={{ height: "15%"}}>
          <Hand />
        </Grid>
      </Grid>
    );
  }
}

import React from "react";
import Grid from "@material-ui/core/Grid";

import Hand from "./Hand";
import OpponentHand from "./OpponentHand";
import BoardSide from "./BoardSide";
import InfoMessage from "./InfoMessage";
import Deck from "./Deck";
import Scrapyard from "./Scrapyard";
import PlayerDisplay from './PlayerDisplay';

export default class Board extends React.Component {
  render() {
    return (
      <Grid container style={{ height: "100%" }}>
        <Grid
          item
          container
          justify="space-between"
          alignContent="flex-end"
          xs={12}
          style={{ height: "10%" }}
        >
          <Grid item xs={9}>
            <OpponentHand
              style={{ height: "100%" }}
            />
          </Grid>
          <Grid item xs={1}>
            <Deck isPlayer={false} style={{ height: "100%" }} />
          </Grid>
          <Grid item xs={1}>
            <Scrapyard isPlayer={false} style={{ height: "100%" }} />
          </Grid>
        </Grid>
        <Grid item xs={12} style={{ height: "5%" }}>
          <PlayerDisplay isPlayer={false} />
        </Grid>
        <Grid item xs={12} style={{ height: "35%" }}>
          <BoardSide isPlayer={false} />
        </Grid>
        <Grid item xs={12} style={{ height: "35%" }}>
          <BoardSide isPlayer={true} />
        </Grid>
        <Grid item xs={12} style={{ height: "5%" }}>
          <PlayerDisplay isPlayer={true} />
        </Grid>
        {/*
        <Grid item xs={12} style={{ height: "5%" }}>
          <InfoMessage />
        </Grid>
        */}
        <Grid
          item
          container
          justify="space-between"
          xs={12}
          style={{ height: "10%" }}
        >
          <Grid item xs={1}>
            <Scrapyard isPlayer={true} />
          </Grid>
          <Grid item xs={1}>
            <Deck isPlayer={true} />
          </Grid>
          <Grid item xs={9}>
            <Hand />
          </Grid>
        </Grid>
      </Grid>
    );
  }
}

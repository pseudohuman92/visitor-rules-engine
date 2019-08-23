import React from "react";
import Grid from "@material-ui/core/Grid";

import PlayerDisplay from "./PlayerDisplay";
import ButtonDisplay from "./ButtonDisplay";

import "../../css/StateDisplay.css";
import "../../css/Utils.css";
import Deck from "./Deck";
import Scrapyard from "./Scrapyard";

class StateDisplay extends React.Component {
  render() {
    return (
      <Grid
        container
        spacing={8}
        style={{
          padding: 0,
          backgroundColor: "lightgreen",
          height: "100%"
        }}
      >
        <Grid item xs={6}>
          <Scrapyard isPlayer={false} />
        </Grid>
        <Grid item xs={6}>
          <Deck isPlayer={false} />
        </Grid>
        <Grid item xs={12}>
          <PlayerDisplay isPlayer={false} />
        </Grid>
        <Grid item xs={12}>
          <ButtonDisplay />
        </Grid>
        <Grid item xs={12}>
          <PlayerDisplay isPlayer={true} />
        </Grid>
        <Grid item xs={6}>
          <Scrapyard isPlayer={true} />
        </Grid>
        <Grid item xs={6}>
          <Deck isPlayer={true} />
        </Grid>
      </Grid>
    );
  }
}

export default StateDisplay;

import React from "react";
import Grid from "@material-ui/core/Grid";

import PlayerDisplay from "./PlayerDisplay";
import ButtonDisplay from "./ButtonDisplay";

import "../../css/StateDisplay.css";
import "../../css/Utils.css";

class StateDisplay extends React.Component {
  render() {
    return (
      <Grid
        container
        spacing={8}
        style={{
          padding: 0
        }}
        className="state-display"
      >
        <Grid item xs={12} style={{ height: "32%" }}>
          <PlayerDisplay isPlayer={false} />
        </Grid>
        <Grid item xs={12} style={{ height: "32%" }}>
          <ButtonDisplay />
        </Grid>
        <Grid item xs={12} style={{ height: "32%" }}>
          <PlayerDisplay isPlayer={true} />
        </Grid>
      </Grid>
    );
  }
}

export default StateDisplay;

import React from "react";
import Grid from "@material-ui/core/Grid";

import Hand from "./Hand";
import BoardSide from "./BoardSide";
import InfoMessage from "./InfoMessage";

import "../../css/Board.css";
import "../../css/Utils.css";


export default class Board extends React.Component {
  render() {
    return (
      <Grid
        container
        spacing={0}
        style={{ height: "100%" }}
      >
        <Grid item xs={12} style={{ height: "35%" }}>
          <BoardSide isPlayer={false} />
        </Grid>
        <Grid item xs={12} style={{ height: "35%" }}>
          <BoardSide isPlayer={true} />
        </Grid>
        <Grid item xs={12} style={{ height: "5%" }}>
          <InfoMessage/>
        </Grid>
        <Grid item xs={12} style={{ height: "25%" }}>
          <Hand />
        </Grid>
      </Grid>
    );
  }
}

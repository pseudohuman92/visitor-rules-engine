import React from "react";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Fittext from "@kennethormandy/react-fittext";

import "./DisplayCard.css";

export class DisplayCard extends React.Component {
  render() {
    const {
      small,
      opacity,
      name,
      description,
      cost,
      type,
      knowledge
    } = this.props;
    if (small) {
      return (
        <div>
          <Paper className="playing-card-small">
            <div className="card-grid">
              <Fittext>
                <div>
                  {cost !== "-" ? cost : ""}{" "}
                  {knowledge !== "-" ? "[" + knowledge + "]" : ""} {" | "}
                  {name}
                </div>
              </Fittext>
            </div>
          </Paper>
        </div>
      );
    }

    return (
      <div>
        <Paper className="playing-card" style={{ opacity: opacity }}>
          <Grid
            container
            className="card-grid"
            justify="flex-start"
            align-content="space-around"
            align-items="space-around"
          >
            <Grid item xs={12} style={{ padding: "5% 0 0 5%", height: "15%" }}>
              <Fittext>
                <div>
                  {cost !== "-" ? cost : ""}{" "}
                  {knowledge !== "-" ? "[" + knowledge + "]" : ""}
                </div>
              </Fittext>
            </Grid>
            <Grid item xs={12} style={{ padding: "0 0 0 5%", height: "15%" }}>
              <Fittext>
                <div>{name}</div>
              </Fittext>
            </Grid>
            <Grid item xs={12} style={{ padding: "0 5% 0 5%", height: "55%" }}>
              <Fittext>
                <div>{description}</div>
              </Fittext>
            </Grid>
            <Grid item xs={12} style={{ padding: "0 0 0 5%", height: "15%" }}>
              <Fittext>
                <div>{type}</div>
              </Fittext>
            </Grid>
          </Grid>
        </Paper>
      </div>
    );
  }
}

export default DisplayCard;


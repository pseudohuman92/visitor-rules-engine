import React from "react";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Fittext from "@kennethormandy/react-fittext";

import "./DisplayCard.css";

export class DisplayCard extends React.Component {
    
  getCardColor(knowlString){
      if(knowlString.startsWith("B")){
          return "#666666";
      } else if (knowlString.startsWith("U")) {
          return "#0066ff";
      } else if (knowlString.startsWith("R")) {
          return "#ff1a1a";
      } else if (knowlString.startsWith("Y")) {
          return "#ffff00";
      } else {
          return "#e6e6e6";
      }
  };
  
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
          <Paper className="playing-card-small" style={{backgroundColor: this.getCardColor(knowledge)}}>
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
        <Paper className="playing-card" style={{ opacity: opacity, backgroundColor: this.getCardColor(knowledge) }}>
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


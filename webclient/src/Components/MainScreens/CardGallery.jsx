import React, { Component } from "react";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";

import CardDisplay from "../Card/CardDisplay";
import { fullCollection } from "../Helpers/Constants";

class CardGallery extends Component {
  render() {
    return (
      <div>
        <Button type="submit" variant="contained" onClick={this.props.back}>
          Back
        </Button>
        <Grid container spacing={8}>
          {Object.values(fullCollection).map((card, i) => (
            <Grid item key={i} xs={3}>
              <CardDisplay {...card} />
            </Grid>
          ))}
        </Grid>
      </div>
    );
  }
}

export default CardGallery;

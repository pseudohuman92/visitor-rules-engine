import React, { Component } from "react";
import Grid from "@material-ui/core/Grid";
import Button from "../Primitives/Button";

import CardDisplay from "../Card/CardDisplay";
import { fullCollection } from "../Helpers/Constants";

class CardGallery extends Component {
  render() {
    return (
      <div>
        <Button onClick={this.props.back} text="Back"/>
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

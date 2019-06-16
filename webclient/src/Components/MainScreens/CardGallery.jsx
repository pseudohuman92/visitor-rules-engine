import React, { Component } from "react";
import Grid from "@material-ui/core/Grid";
import Button from "../Primitives/Button";

import CardDisplay from "../Card/CardDisplay";
import { fullCollection } from "../Helpers/Constants";
import { compareCardsByKnowledge } from "../Helpers/Helpers";

const cardsPerPage = 12;

class CardGallery extends Component {
  state = {
    page: 0,
    maxPage: Math.floor(Object.keys(fullCollection).length / cardsPerPage)
  };

  prev = () => {
    this.setState(state => ({ page: Math.max(state.page - 1, 0) }));
  };

  next = () => {
    this.setState(state => ({ page: Math.min(state.page + 1, state.maxPage) }));
  };

  render() {
    const { page, maxPage } = this.state;
    return (
      <div>
        <Grid container spacing={8}>
        <Grid item xs={4}>
            <Button onClick={this.props.back} text="Back" />
          </Grid>
          <Grid item xs={4}>
            <Button onClick={this.prev} text="Prev" disabled={page===0}/>
          </Grid>
          <Grid item xs={4}>
            <Button onClick={this.next} text="Next" disabled={page===maxPage}/>
          </Grid>
          <Grid container item spacing={8} xs={12}>
            {Object.values(fullCollection).sort(compareCardsByKnowledge)
              .slice(page * cardsPerPage, (page + 1) * cardsPerPage)
              .map((card, i) => (
                <Grid item key={i} xs={2}>
                  <CardDisplay {...card} />
                </Grid>
              ))}
          </Grid>
        </Grid>
      </div>
    );
  }
}

export default CardGallery;

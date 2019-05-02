import React, { Component } from "react";
import Grid from "@material-ui/core/Grid";
import Dialog from "@material-ui/core/Dialog";
import CardDisplay from "./Components/Card/CardDisplay";
import { Button } from "@material-ui/core";
import Centered from "./Centered";

class OpenPacks extends Component {
  constructor(props) {
    super(props);
    this.state = {
      show: false,
      value: props.value,
      collection: props.collection
    };
    this.openPack = this.openPack.bind(this);
    this.hideDialog = this.hideDialog.bind(this);
    this.generateRandomCard = this.generateRandomCard.bind(this);
    this.generatePack = this.generatePack.bind(this);
  }

  openPack() {
    if (this.state.value > 0) {
      this.setState((state, props) => ({ show: true, value: state.value - 1 }));
    }
  }

  generateRandomCard() {
    const collection = this.state.collection;
    if (collection) {
      return collection[Math.floor(Math.random() * collection.length)].card;
    }
  }

  generatePack(size, unique) {
    var result = [];
    for (var i = 0; i < size; i++) {
      let c = this.generateRandomCard();
      if (unique && result.includes(c)) {
        i--;
      } else {
        result.push(c);
      }
    }
    return result;
  }

  hideDialog() {
    this.setState({ show: false });
  }

  render() {
    const { show, value } = this.state;

    return (
      <div>
        {show && (
          <Dialog fullScreen open={show}>
            <Grid
              container
              alignContent="space-around"
              justify="flex-start"
              spacing={8}
            >
              {this.generatePack(10, true).map((card, i) => (
                <Grid item key={i} xs={3}>
                  <CardDisplay {...card} />
                </Grid>
              ))}
            </Grid>
            <Button
              variant="contained"
              color="primary"
              onClick={this.hideDialog}
            >
              Done
            </Button>
          </Dialog>
        )}
        <Centered>
          <div style={{ height: "350px", width: "250px" }}>
            {
              <div>
                <div
                  onClick={this.openPack}
                  style={{
                    backgroundColor: "blue",
                    height: "350px",
                    maxWidth: "250px"
                  }}
                >
                  PACK
                </div>
                <div style={{ color: "black" }}>{value}</div>
              </div>
            }
          </div>
        </Centered>
      </div>
    );
  }
}

export default OpenPacks;

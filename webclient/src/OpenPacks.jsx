import React, { Component } from "react";
import Grid from "@material-ui/core/Grid";
import Dialog from "@material-ui/core/Dialog";
import CardDisplay from "./Components/Card/CardDisplay";
import { Button } from "@material-ui/core";
import { connect } from "react-redux";
import Center from "react-center";

import { withFirebase } from "./Components/Firebase/index";


const mapStateToProps = state => {
  return { fullCollection: state.fullCollection };
};

class OpenPacks extends Component {
  constructor(props) {
    super(props);
    this.state = {
      show: false,
      value: props.value
    };
  }

  openPack = () => {
    if (this.state.value > 0) {
      /* TODO: This will replace the pack open code. Must do it transactionally.
      let cards = this.generatePack(10, true);
      this.props.firebase.addCardsToCollection(collectionId, cards);
      this.props.firebase.decreasePackCount(userId);
      */
      this.setState((state, props) => ({ show: true, value: state.value - 1 }));
    }
  }

  generateRandomCard = () => {
    const collection = this.props.fullCollection;
    if (collection) {
      return collection[Math.floor(Math.random() * collection.length)].card;
    }
  }

  generatePack = (size, unique) => {
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

  hideDialog = () => {
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
        <Center>
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
        </Center>
      </div>
    );
  }
}

export default connect(mapStateToProps)(withFirebase(OpenPacks));

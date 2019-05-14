import React, { Component } from "react";
import Grid from "@material-ui/core/Grid";
import Dialog from "@material-ui/core/Dialog";
import { Button } from "@material-ui/core";
import { connect } from "react-redux";
import Center from "react-center";

import { withFirebase } from "./Components/Firebase/index";
import CardDisplay from "./Components/Card/CardDisplay";
import { fullCollection } from "./Components/Helpers/Constants";

const mapStateToProps = state => {
  return {
    packs: state.packs
  };
};

class OpenPacks extends Component {
  constructor(props) {
    super(props);
    this.state = {
      show: false
    };
  }

  openPack = packName => {
    if (this.props.packs[packName] > 0) {
      /* TODO: This will replace the pack open code. Must do it transactionally.
      let cards = this.generatePack(10, true);
      this.props.firebase.addCardsToCollection(collectionId, cards);
      this.props.firebase.decreasePackCount(userId);
      */
      this.setState((state, props) => ({ show: true }));
    }
  };

  generateRandomCard = () => {
    const collection = Object.values(fullCollection);
    if (collection) {
      return collection[Math.floor(Math.random() * collection.length)];
    }
  };

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
  };

  hideDialog = () => {
    this.setState({ show: false });
  };

  render() {
    const { packs } = this.props;
    const { show } = this.state;

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
        <Grid
          container
          alignContent="space-around"
          justify="flex-start"
          spacing={8}
        >
          {Object.keys(packs).map(key => {
            return (
              <Grid item xs key={key}>
                <Center>
                  <div style={{ height: "350px", width: "250px" }}>
                      <div
                        onClick={event => this.openPack(key)}
                        style={{
                          backgroundColor: "blue",
                          height: "350px",
                          maxWidth: "250px"
                        }}
                      >
                        {key}
                      </div>
                      <div style={{ color: "black" }}>{packs[key]}</div>
                  </div>
                </Center>
              </Grid>
            );
          })}
        </Grid>
      </div>
    );
  }
}

export default connect(mapStateToProps)(withFirebase(OpenPacks));

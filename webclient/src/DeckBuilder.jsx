import React from "react";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";
import ReactFileReader from "react-file-reader";
import GridList from "@material-ui/core/GridList";
import GridListTile from "@material-ui/core/GridListTile";
import TextField from "@material-ui/core/TextField";
import Center from "react-center";
import { connect } from "react-redux";

import CardDisplay from "./Components/Card/CardDisplay";
import { copy, spliceToSubarrays } from "./Utils";
import { toCollectionArray } from "./Components/Constants/Constants"

const mapStateToProps = state => {
  return { collection: state.collection };
};

class DeckBuilder extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      initialCollection: toCollectionArray(props.collection),
      collection: toCollectionArray(props.collection),
      name: "New Deck",
      deck: [],
    };
    this.fileReader = new FileReader();
    this.fileReader.onload = event => {
      var cards = this.fileReader.result.split("\n");
      cards.forEach(c => {
        let count = c.split("\t")[0];
        let name = c.split("\t")[1];
        for (let i = 0; i < count; i++) {
          this.addToDeck({ name: name });
        }
      });
    };
  }

  addToDeck = card => {
    var collection = this.state.collection;
    var colPos = collection.map(c => c.card.name).indexOf(card.name);
    var deck = this.state.deck;
    var deckPos = deck.map(c => c.card.name).indexOf(card.name);
    if (colPos !== -1 && collection[colPos].count > 0) {
      collection[colPos].count--;

      if (deckPos === -1) {
        deck.push({ count: 1, card: collection[colPos].card });
      } else {
        deck[deckPos].count++;
      }
      this.setState({ collection: collection, deck: deck });
      console.log(card.name + " added");
    }
  }

  removeFromDeck = card => {
    var collection = this.state.collection;
    var colPos = collection.map(c => c.card.name).indexOf(card.name);
    var deck = this.state.deck;
    var deckPos = deck.map(c => c.card.name).indexOf(card.name);
    if (deck[deckPos].count === 1) {
      deck.splice(deckPos, 1);
    } else {
      deck[deckPos].count--;
    }
    if (colPos === -1) {
      collection.push({ count: 1, card: card });
    } else {
      collection[colPos].count++;
    }
    this.setState({ collection: collection, deck: deck });
    console.log(card.name + " removed");
  }

  clearDeck = () => {
    this.setState((state, props) => ({
      name: "New Deck",
      deck: [],
      collection: copy(state.initialCollection)
    }));
  }

  changeName = name => {
    this.setState({ name: name });
  }

  loadDeck = files => {
    this.clearDeck();
    this.fileReader.readAsText(files[0]);
    this.setState({ name: files[0].name.split(".")[0] });
  }

  saveDeck = () => {
    let FileSaver = require("file-saver");
    let blob = new Blob(
      this.state.deck.map(c => c.count + "\t" + c.card.name + "\n"),
      {
        type: "text/plain;charset=utf-8"
      }
    );
    FileSaver.saveAs(blob, this.state.name + ".deck");
  }

  render() {
    return (
      <Grid container spacing={16} style={{ maxHeight: "90vh" }}>
        <Grid item style={{ maxHeight: "5%" }}>
          <ReactFileReader
            fileTypes={[".deck"]}
            multipleFiles={false}
            handleFiles={this.loadDeck}
          >
            <Button variant="contained"> Load </Button>
          </ReactFileReader>
        </Grid>
        <Grid item style={{ maxHeight: "5%" }}>
          <Button variant="contained" onClick={this.saveDeck}>
            Save
          </Button>
        </Grid>
        <Grid item style={{ maxHeight: "5%" }}>
          <Button variant="contained" onClick={this.clearDeck}>
            New
          </Button>
        </Grid>
        <Grid item style={{ maxHeight: "5%" }}>
          <TextField
            value={this.state.name}
            onChange={event => {
              this.changeName(event.target.value);
            }}
          />
        </Grid>
        <Grid item xs={9} style={{ height: "95%" }}>
          <GridList
            cols={2}
            style={{
              backgroundColor: "skyblue",
              border: "2px black solid",
              flexWrap: "nowrap"
            }}
            cellHeight="auto"
            spacing={8}
          >
            
            {spliceToSubarrays(this.state.collection, 6).map((arr, i) => (
              <GridListTile key={i}>
                <Grid
                  container
                  alignContent="space-around"
                  justify="flex-start"
                  spacing={8}
                >
                  {arr.map((card, i) => (
                    <Grid
                      item
                      key={i}
                      xs={4}
                      onClick={() => this.addToDeck(card.card)}
                    >
                      <center> {card.count} </center>
                      <CardDisplay
                        opacity={card.count > 0 ? 1 : 0.5}
                        {...card.card}
                      />
                    </Grid>
                  ))}
                </Grid>
              </GridListTile>
            ))}
          </GridList>
        </Grid>
        <Grid item xs={3} style={{ height: "95%" }}>
          <GridList
            cols={1}
            style={{
              backgroundColor: "#ffe6ff",
              border: "2px black solid",
              flexWrap: "nowrap",
              height: "100%"
            }}
            cellHeight="auto"
            spacing={8}
          >
            {spliceToSubarrays(this.state.deck, 9).map((arr, i) => (
              <GridListTile key={i}>
                <Grid
                  container
                  alignItems="center"
                  justify="flex-start"
                  spacing={8}
                >
                  {arr.map((card, i) => (
                    <Grid
                      container
                      item
                      key={i}
                      xs={12}
                      onClick={() => this.removeFromDeck(card.card)}
                    >
                      <Center>
                        <Grid item xs={1}>
                          {card.count}
                        </Grid>
                      </Center>
                      <Grid item xs={1} />
                      <Grid item xs={10}>
                        <CardDisplay small={true} {...card.card} />
                      </Grid>
                    </Grid>
                  ))}
                </Grid>
              </GridListTile>
            ))}
          </GridList>
        </Grid>
      </Grid>
    );
  }
}

export default connect(mapStateToProps)(DeckBuilder);

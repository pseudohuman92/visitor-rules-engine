import React from "react";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";
import GridList from "@material-ui/core/GridList";
import GridListTile from "@material-ui/core/GridListTile";
import TextField from "@material-ui/core/TextField";
import Center from "react-center";
import { connect } from "react-redux";

import { withFirebase } from "../Firebase";
import CardDisplay from "../Card/CardDisplay";
import { spliceToSubarrays } from "../Helpers/Helpers";
import { toCollectionArray } from "../Helpers/Constants";

const mapStateToProps = state => {
  return { collection: state.collection, userId: state.authUser.user.uid };
};

class DeckBuilder extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      initialCollection: toCollectionArray(props.collection),
      collection: toCollectionArray(props.collection),
      name: "",
      deck: []
    };
  }

  setDeck = (name, cards) => {
    this.setState({ name: name });
    cards.forEach(card => {
      for (let i = 0; i < card.count; i++) {
        this.addToDeck(card.name);
      }
    });
  };

  componentWillMount() {
    const { firebase, deckId } = this.props;
    const setDeck = this.setDeck.bind(this);
    firebase.getDeck(deckId, setDeck);
  }

  addToDeck = cardName => {
    var collection = this.state.collection;
    var colPos = collection.map(c => c.card.name).indexOf(cardName);
    var deck = this.state.deck;
    var deckPos = deck.map(c => c.card.name).indexOf(cardName);
    if (colPos !== -1 && collection[colPos].count > 0) {
      collection[colPos].count--;

      if (deckPos === -1) {
        deck.push({ count: 1, card: collection[colPos].card });
      } else {
        deck[deckPos].count++;
      }
      this.setState({ collection: collection, deck: deck });
    }
  };

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
  };

  changeName = name => {
    this.setState({ name: name });
  };

  saveDeck = () => {
    let { name, deck } = this.state;
    let cards = {};
    deck.forEach(card => {
      cards = { ...cards, [card.card.name]: card.count };
    });
    this.props.firebase.updateDeck(this.props.deckId, name, cards);
  };

  render() {
    return (
      <Grid container spacing={16} style={{ maxHeight: "90vh" }}>
        <Grid item style={{ maxHeight: "5%" }}>
          <Button variant="contained" onClick={this.saveDeck}>
            Save
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
                      onClick={() => this.addToDeck(card.card.name)}
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

export default connect(mapStateToProps)(withFirebase(DeckBuilder));

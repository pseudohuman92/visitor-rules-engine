import React from "react";
import Grid from "@material-ui/core/Grid";
import GridList from "@material-ui/core/GridList";
import GridListTile from "@material-ui/core/GridListTile";
import Button from "../Primitives/Button";
import TextField from "@material-ui/core/TextField";
import Center from "react-center";
import { connect } from "react-redux";

import { withFirebase } from "../Firebase";
import CardDisplay from "../Card/CardDisplay";
import { fullCollection } from "../Helpers/Constants";

const mapStateToProps = state => {
  return { collection: state.collection, userId: state.authUser.user.uid };
};

class DeckBuilder extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      collection: Object.assign({}, props.collection),
      name: "",
      deck: {},
      initialized: false
    };
  }

  setDeck = (name, cards) => {
    let collection = this.state.collection;
    Object.keys(cards).forEach(cardName => {
      for (let i = 0; i < cards[cardName]; i++) {
        collection[cardName]--;
      }
    });
    this.setState({
      name: name,
      collection: collection,
      deck: cards,
      initialized: true
    });
  };

  componentDidMount() {
    if (!this.state.initialized) {
      const { firebase, deckId } = this.props;
      const setDeck = this.setDeck.bind(this);
      firebase.getDeck(deckId, setDeck);
    }
  }

  addToDeck = cardName => {
    var collection = this.state.collection;
    var deck = this.state.deck;
    if (collection[cardName] > 0) {
      collection[cardName]--;

      if (deck[cardName] > 0) {
        deck[cardName]++;
      } else {
        deck[cardName] = 1;
      }
      this.setState({ collection: collection, deck: deck });
    }
  };

  removeFromDeck = cardName => {
    var collection = this.state.collection;
    var deck = this.state.deck;
    if (deck[cardName] === 1) {
      delete deck[cardName];
    } else {
      deck[cardName]--;
    }
    collection[cardName]++;
    this.setState({ collection: collection, deck: deck });
  };

  changeName = name => {
    this.setState({ name: name });
  };

  saveDeck = () => {
    let { name, deck } = this.state;
    this.props.firebase.updateDeck(this.props.deckId, name, deck);
  };

  render() {
    const { collection, name, deck, initialized } = this.state;
    return (collection && initialized) ? (
      <Grid container spacing={16} style={{ maxHeight: "90vh" }}>
        <Grid item xs={2} style={{ maxHeight: "5%" }}>
          
        <Button onClick={this.props.back} text="Back"/>
        </Grid>
        <Grid item xs={2} style={{ maxHeight: "5%" }}>
          <Button onClick={this.saveDeck} text="Save"/>
        </Grid>
        <Grid item xs={8} style={{ maxHeight: "5%" }}>
          <TextField
            value={name}
            onChange={event => {
              this.changeName(event.target.value);
            }}
          />
        </Grid>
        <Grid container item xs={9}>
          {Object.keys(collection).map((cardName, i) => (
            <Grid item key={i} xs={3}>
              <center> {collection[cardName]} </center>
              <CardDisplay
                onClick={() => this.addToDeck(cardName)}
                opacity={collection[cardName] > 0 ? 1 : 0.5}
                {...fullCollection[cardName]}
              />
            </Grid>
          ))}
        </Grid>

        <Grid
          container
          item
          xs={3}
          spacing={8}
          style={{
            position: "fixed",
            right: "0",
            backgroundColor: "#ffe6ff",
            width: "100%"
          }}
        >
          <Grid item xs={12}>
            {"Deck List"}
            <GridList cellHeight="auto" cols={1} style={{ maxHeight: "95vh" }}>
              {Object.keys(deck).map((cardName, i) => (
                <GridListTile
                  key={i}
                  cols={1}
                >
                  <Center>{deck[cardName]}</Center>
                  <CardDisplay 
                  small 
                  onClick={() => this.removeFromDeck(cardName)}
                  {...fullCollection[cardName]} />
                </GridListTile>
              ))}
            </GridList>
          </Grid>
        </Grid>
      </Grid>
    ) : (
      <div>Loading Deck</div>
    );
  }
}

export default connect(mapStateToProps)(withFirebase(DeckBuilder));

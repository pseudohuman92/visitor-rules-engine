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

const cardsPerPage = 18;

class DeckBuilder extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      name: "",
      deck: {},
      initialized: false,
      page: 0,
      maxPage: Math.floor(Object.keys(props.collection).length / cardsPerPage)
    };
  }

  prev = () => {
    this.setState(state => ({ page: Math.max(state.page - 1, 0) }));
  };

  next = () => {
    this.setState(state => ({ page: Math.min(state.page + 1, state.maxPage) }));
  };

  setDeck = (name, cards) => {
    this.setState({
      name: name,
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
    var collection = this.props.collection;
    var deck = this.state.deck;
    if (collection[cardName] - (deck[cardName]?deck[cardName]:0) > 0) {
      if (deck[cardName]) {
        deck[cardName]++;
      } else {
        deck[cardName] = 1;
      }
      this.setState({ deck: deck });
    }
  };

  removeFromDeck = cardName => {
    var deck = this.state.deck;
    if (deck[cardName] === 1) {
      delete deck[cardName];
    } else {
      deck[cardName]--;
    }
    this.setState({ deck: deck });
  };

  changeName = name => {
    this.setState({ name: name });
  };

  saveDeck = () => {
    let { name, deck } = this.state;
    this.props.firebase.updateDeck(this.props.deckId, name, deck);
  };

  render() {
    const { name, deck, initialized, page } = this.state;
    const {collection} = this.props;
    return collection && initialized ? (
      <Grid container spacing={8} style={{ maxHeight: "95vh" }}>
        <Grid item xs={1}>
          <Button onClick={this.props.back} text="Back" />
        </Grid>
        <Grid item xs={1}>
          <Button onClick={this.saveDeck} text="Save" />
        </Grid>
        <Grid item xs={4}>
          <TextField
            value={name}
            fullWidth
            onChange={event => {
              this.changeName(event.target.value);
            }}
          />
        </Grid>
        <Grid item xs={1}>
          <Button onClick={this.prev} text="Prev" />
        </Grid>
        <Grid item xs={1}>
          <Button onClick={this.next} text="Next" />
        </Grid>
        <Grid container item xs={9} spacing={8}>
          {Object.keys(collection)
            .slice(page * cardsPerPage, (page + 1) * cardsPerPage)
            .map((cardName, i) => (
              <Grid item key={i} xs={2}>
                <center> {collection[cardName] - (deck[cardName]?deck[cardName]:0)} </center>
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
                <GridListTile key={i} cols={1}>
                  <Center>{deck[cardName]}</Center>
                  <CardDisplay
                    small
                    onClick={() => this.removeFromDeck(cardName)}
                    {...fullCollection[cardName]}
                  />
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

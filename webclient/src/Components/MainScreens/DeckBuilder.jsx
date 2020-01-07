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
import { mapDispatchToProps } from "../Redux/Store";
import { toFullCards, compareCardsByKnowledge } from "../Helpers/Helpers";

const mapStateToProps = state => {
  return { collection: state.profile.collection, userId: state.firebaseAuthData.user.uid };
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
      maxPage: Math.floor(Object.keys(props.collection).length / cardsPerPage),
      changed: false
    };
  }

  prev = () => {
    this.setState(state => ({ page: Math.max(state.page - 1, 0) }));
  };

  next = () => {
    this.setState(state => ({ page: Math.min(state.page + 1, state.maxPage) }));
  };

  setDeck = (name, deck) => {
    this.setState({
      name: name,
      deck: deck,
      initialized: true
    });
  };

  componentWillMount() {
    const { firebase, deckId, updateState, userId } = this.props;
    const setDeck = this.setDeck.bind(this);
    firebase.getDeck(deckId, setDeck);
    firebase.setUserData(userId, updateState);
  }

  addToDeck = cardName => {
    var collection = this.props.collection;
    var deck = this.state.deck;
    if (collection[cardName] - (deck[cardName] ? deck[cardName] : 0) > 0) {
      if (deck[cardName]) {
        deck[cardName]++;
      } else {
        deck[cardName] = 1;
      }
      this.setState({ deck: deck, changed: true });
    }
  };

  removeFromDeck = cardName => {
    var deck = this.state.deck;
    if (deck[cardName] === 1) {
      delete deck[cardName];
    } else {
      deck[cardName]--;
    }
    this.setState({ deck: deck, changed: true });
  };

  changeName = name => {
    this.setState({ name: name, changed: true });
  };

  saveDeck = () => {
    let { name, deck } = this.state;
    this.props.firebase.updateDeck(this.props.deckId, name, deck);
    this.setState({ changed: false });
  };

  render() {
    const { name, deck, initialized, page, maxPage, changed } = this.state;
    const { collection } = this.props;
    return collection && initialized ? (
      <Grid container spacing={8} style={{ maxHeight: "95vh" }}>
        <Grid item xs={1}>
          <Button onClick={this.props.back} text="Back" />
        </Grid>
        <Grid item xs={1}>
          <Button onClick={this.saveDeck} disabled={!changed} text="Save" />
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
          <Button onClick={this.prev} text="Prev" disabled={page === 0} />
        </Grid>
        <Grid item xs={1}>
          <Button onClick={this.next} text="Next" disabled={page === maxPage} />
        </Grid>
        <Grid container item xs={9} spacing={8}>
          {Object.values(toFullCards(collection))
            .sort(compareCardsByKnowledge)
            .slice(page * cardsPerPage, (page + 1) * cardsPerPage)
            .map(
              (card, i) =>
                card && (
                  <Grid item key={i} xs={2}>
                    <center>
                      {" "}
                      {collection[card.name] -
                        (deck[card.name] ? deck[card.name] : 0)}{" "}
                    </center>
                    <CardDisplay
                      onClick={() => this.addToDeck(card.name)}
                      opacity={
                        collection[card.name] -
                          (deck[card.name] ? deck[card.name] : 0) >
                        0
                          ? 1
                          : 0.5
                      }
                      cardData={card}
                    />
                  </Grid>
                )
            )}
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
              {Object.values(toFullCards(deck))
                .sort(compareCardsByKnowledge)
                .map((card, i) => (
                  <GridListTile key={i} cols={1}>
                    <Center>{deck[card.name]}</Center>
                    <CardDisplay
                      small
                      onClick={() => this.removeFromDeck(card.name)}
                      cardData={card}
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

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withFirebase(DeckBuilder));

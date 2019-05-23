import React from "react";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";
import Center from "react-center";
import { connect } from "react-redux";

import { withFirebase } from "../Firebase";
import DeckBuilder from "./DeckBuilder";
import { delayClick } from "../Helpers/Helpers";

const mapStateToProps = state => {
  return { userId: state.authUser.user.uid };
};

class Decks extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: 0,
      loadedDeck: "",
      selectedDeckId: "",
      decks: []
    };
    this.back = () => this.setState({value:0});
  }

  componentWillMount() {
    const Return = this.addDeck.bind(this);
    this.props.firebase.getAllDecks(this.props.userId, Return);
  }

  selectDeck = deckId => {
    if (deckId === this.state.selectedDeckId) {
      this.setState({ selectedDeckId: "" });
    } else {
      this.setState({ selectedDeckId: deckId });
    }
  };

  loadDeck = deckId => {
      this.setState({ value: 1, loadedDeck: deckId });
  };

  addDeck = deck => {
    this.setState((state, props) => ({decks: state.decks.concat([deck]) }));
  };

  deleteDeck = () => {
      this.props.firebase.deleteDeck(this.props.userId, this.state.selectedDeckId);
      this.setState((state, props) => ({ selectedDeckId: "",
       decks: state.decks.filter(item => item.id !== this.state.selectedDeckId) }));
  };

  createDeck = () => {
    const Return = this.loadDeck.bind(this);
    this.props.firebase.createNewDeck(this.props.userId, "New Deck", Return);
  };

  render() {
    const { value, decks, selectedDeckId, loadedDeck } = this.state;
    return (
      <div>
        {value === 0 && (
          <div>
          <Button
          type="submit"
          variant="contained"
          onClick={this.props.back}
        >
          Back
        </Button>
          <Grid container spacing={8}>
            {decks.map((deck, i) => (
              <Grid
                item
                key={i}
                xs={1}
                onClick={delayClick(() => this.selectDeck(deck.id), () => this.setState({ value: 1, loadedDeck: deck.id }))}
              >
                <Center>
                  <img
                    src={process.env.PUBLIC_URL + "/img/Logo.png"}
                    style={{
                      maxWidth: "100%",
                      maxHeight: "100%",
                      opacity: deck.id === selectedDeckId ? 0.5 : 1
                    }}
                    alt=""
                  />
                </Center>
                <Center>{deck.name}</Center>
              </Grid>
            ))}
            <Grid container item xs={12} spacing={8}>
              <Grid item xs>
                <Button
                  type="submit"
                  variant="contained"
                  onClick={this.createDeck}
                >
                  Create
                </Button>
              </Grid>
              <Grid item xs>
                <Button
                  disabled={selectedDeckId === ""}
                  type="submit"
                  variant="contained"
                  onClick={this.deleteDeck}
                >
                  Delete
                </Button>
              </Grid>
            </Grid>
          </Grid>
          </div>
        )}
        {value === 1 && <DeckBuilder back={this.back} deckId={loadedDeck}/>}
      </div>
    );
  }
}

export default connect(mapStateToProps)(withFirebase(Decks));

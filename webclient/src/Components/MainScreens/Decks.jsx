import React from "react";
import Grid from "@material-ui/core/Grid";
import Button from "../Primitives/Button";
import Center from "react-center";
import { connect } from "react-redux";

import { withFirebase } from "../Firebase";
import DeckBuilder from "./DeckBuilder";
import { delayClick } from "../Helpers/Helpers";
import { mapDispatchToProps } from "../Redux/Store";

import TextFit from "react-textfit";

const mapStateToProps = state => {
  return { userId: state.firebaseAuthData.user.uid };
};

class Decks extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: 0,
      loadedDeck: "",
      decks: []
    };
    this.back = () => {
      this.setState({ loadedDeck: "", selectedDeckId: "", decks: [] });
      const Return = this.addDeck.bind(this);
      this.props.firebase.getAllDecks(this.props.userId, Return);
      this.setState({ value: 0 });
    };
  }

  componentWillMount() {
    this.setState({ loadedDeck: "", selectedDeckId: "", decks: [] });
    const Return = this.addDeck.bind(this);
    this.props.firebase.getAllDecks(this.props.userId, Return);
  }

  loadDeck = deckId => {
    this.setState({ value: 1, loadedDeck: deckId });
  };

  addDeck = deck => {
    this.setState((state, props) => ({ decks: state.decks.concat([deck]) }));
  };

  deleteDeck = (selectedDeckId) => {
    this.props.firebase.deleteDeck(
      this.props.userId, selectedDeckId
    );
    this.setState((state, props) => ({
      decks: state.decks.filter(item => item.id !== selectedDeckId)
    }));
  };

  createDeck = () => {
    const { firebase, userId } = this.props;
    const Return = this.loadDeck.bind(this);
    firebase.createNewDeck(userId, "New Deck", Return);
  };

  render() {
    const { value, decks, loadedDeck } = this.state;
    return (
      <div>
        {value === 0 && (
          <div>
            <Grid container spacing={8}>
              <Grid
                  item
                  xs={2}
                  onClick={this.createDeck}
              >
              <Center style={{position: "relative"}}>
                <img
                    src={process.env.PUBLIC_URL + "/img/deck-selection/border.png"}
                    alt=""
                />
                <img
                    src={process.env.PUBLIC_URL + "/img/deck-selection/cross.png"}
                    style={{
                      position: "absolute",
                      maxWidth: "35%",
                      maxHeight: "50%",
                      transform: "rotate(45deg)"
                    }}
                    alt=""
                />
              </Center>
                </Grid>
              {decks.map((deck, i) => (
                <Grid
                  item
                  key={i}
                  xs={2}
                >
                  <Center style={{position:"relative"}}>
                    <div style={{position:"absolute"}}
                       onClick={() => this.setState({ value: 1, loadedDeck: deck.id })}>
                      {deck.name}
                    </div>

                    <img
                        src={process.env.PUBLIC_URL + "/img/deck-selection/cross.png"}
                        style={{
                          position:"absolute",
                          maxWidth: "10%",
                          maxHeight: "10%",
                          top: "5%",
                          right: "9%",
                          filter: "invert(50%)"
                        }}
                        alt=""
                        onClick={() => this.deleteDeck(deck.id)}
                    />

                    <img
                      src={process.env.PUBLIC_URL + "/img/deck-selection/border.png"}
                      alt=""
                      onClick={() => this.setState({ value: 1, loadedDeck: deck.id })}
                    />
                  </Center>
                </Grid>
              ))}

            </Grid>
          </div>
        )}
        {value === 1 && <DeckBuilder back={this.back} deckId={loadedDeck} />}
      </div>
    );
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withFirebase(Decks));

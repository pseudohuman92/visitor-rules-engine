import React from "react";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";
import Center from "react-center";
import { connect } from "react-redux";

import { withFirebase } from "../Firebase";
import { mapDispatchToProps } from "../Redux/Store";
import ServerMessageHandler from "../MessageHandlers/ServerMessageHandler";
import PlayArea from "../GameAreas/PlayArea";
import { withHandlers } from "../MessageHandlers/HandlerContext";

const mapStateToProps = state => {
  return {
    userId: state.authUser.user.uid,
    gameInitialized: state.extendedGameState.gameInitialized
  };
};

class DeckSelection extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: 0,
      decks: [],
      message: ""
    };
  }

  componentWillMount() {
    const Return = this.addDeck.bind(this);
    this.props.firebase.getAllDecks(this.props.userId, Return);
  }

  componentDidMount() {
    const { userId, updateHandlers, updateExtendedGameState } = this.props;
    updateHandlers({
      serverHandler: new ServerMessageHandler(
        userId,
        updateHandlers,
        updateExtendedGameState,
        () => this.setState({ value: 1 })
      )
    });
  }

  addDeck = deck => {
    this.setState((state, props) => ({ decks: state.decks.concat([deck]) }));
    console.log(deck);
  };

  loadDeck = (name, deck) => {
    console.log(deck);
    const decklist = this.toDecklist(deck);
    if (decklist) {
      this.props.serverHandler.joinQueue(decklist);
      this.setState({message: "", value: 1 });
    } else {
      this.setState({message: "Invalid Deck" });
    }
  };

  joinQueue = deckId => {
    console.log(deckId);
    const Return = this.loadDeck.bind(this);
    this.props.firebase.getDeck(deckId, Return);
  };

  toDecklist = deck => {
    var decklist = [];
    Object.keys(deck).forEach(cardName => {
      let count = deck[cardName];
      if (count <= 3 && count > 0) {
        decklist.push("" + count + ";" + cardName);
      } else {
        return [];
      }
    });
    return decklist;
  };

  render() {
    const { value, decks} = this.state;
    return (
      <div>
        {value === 0 && (
          <div>
            <Button type="submit" variant="contained" onClick={this.props.back}>
              Back
            </Button>
            <Grid container spacing={8}>
              {decks.map((deck, i) => (
                <Grid
                  item
                  key={i}
                  xs={3}
                  onDoubleClick={event => this.joinQueue(deck.id)}
                >
                  <Center>
                    <img
                      src={process.env.PUBLIC_URL + "/img/Logo.png"}
                      style={{
                        maxWidth: "100%",
                        maxHeight: "100%",
                      }}
                      alt=""
                    />
                  </Center>
                  <Center>{deck.name}</Center>
                </Grid>
              ))}
            </Grid>
            {this.state.message}
          </div>
        )}
        {value === 1 &&
          (this.props.gameInitialized ? (
            <PlayArea back={this.props.back} />
          ) : (
            <div>
              {
                "Waiting For An Opponent... You can open a second tab and login with a different account to play against yourself"
              }
            </div>
          ))}
      </div>
    );
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withHandlers(withFirebase(DeckSelection)));

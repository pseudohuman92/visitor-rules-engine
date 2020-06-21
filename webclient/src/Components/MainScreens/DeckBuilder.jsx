import React from "react";
import Button from "../Primitives/Button";
import {connect} from "react-redux";
import LazyLoad from 'react-lazy-load';

import {withFirebase} from "../Firebase";
import CardDisplay from "../Card/CardDisplay";
import {mapDispatchToProps} from "../Redux/Store";
import {compareCardsByKnowledge, toFullCards} from "../Helpers/Helpers";
import TextField from "@material-ui/core/TextField";

const mapStateToProps = state => {
    return {
        collection: state.profile.collection,
        userId: state.firebaseAuthData.user.uid,
        windowDimensions: state.windowDimensions
    };
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
        this.setState(state => ({page: Math.max(state.page - 1, 0)}));
    };

    next = () => {
        this.setState(state => ({page: Math.min(state.page + 1, state.maxPage)}));
    };

    setDeck = (name, deck) => {
        this.setState({
            name: name,
            deck: deck,
            initialized: true
        });
    };

    componentWillMount() {
        const {firebase, deckId, updateState, userId} = this.props;
        const setDeck = this.setDeck.bind(this);
        firebase.getDeck(deckId, setDeck);
        firebase.setUserData(userId, updateState);
    }

    addToDeck = cardName => {
        const collection = this.props.collection;
        const deck = this.state.deck;
        if (collection[cardName] - (deck[cardName] ? deck[cardName] : 0) > 0) {
            if (deck[cardName]) {
                deck[cardName]++;
            } else {
                deck[cardName] = 1;
            }
            this.setState({deck: deck, changed: true});
        }
    };

    removeFromDeck = cardName => {
        const deck = this.state.deck;
        if (deck[cardName] === 1) {
            delete deck[cardName];
        } else {
            deck[cardName]--;
        }
        this.setState({deck: deck, changed: true});
    };

    changeName = name => {
        this.setState({name: name, changed: true});
    };

    saveDeck = () => {
        let {name, deck} = this.state;
        this.props.firebase.updateDeck(this.props.deckId, name, deck);
        this.setState({changed: false});
    };

    render() {
        const {name, deck, initialized, page, maxPage, changed, forDraft} = this.state;
        const {collection, windowDimensions} = this.props;
        return collection && initialized ? (
            <div style={{display: "flex", flexDirection: "column", maxHeight: "95vh"}}>
                {!forDraft && (
                    <div style={{display: "flex"}}>
                        <Button onClick={this.props.back} text="Back"/>
                        <Button onClick={this.saveDeck} disabled={!changed} text="Save"/>
                        <TextField
                            value={name}
                            fullWidth
                            onChange={event => {
                                this.changeName(event.target.value);
                            }}
                        />
                    </div>)
                }
                <div style={{display: "flex"}}>
                    <div style={{display: "flex", flexWrap: "wrap", flexGrow: 9}}>
                        {Object.values(toFullCards(collection))
                            .sort(compareCardsByKnowledge)
                            //.slice(page * cardsPerPage, (page + 1) * cardsPerPage)
                            .map(
                                (card, i) =>
                                    card && (
                                        <LazyLoad>
                                            <div onClick={() => this.addToDeck(card.set + "." + card.name)} key={i}>
                                                <div style={{textAlign: "center"}}>
                                                    {" "}
                                                    {collection[card.set + "." + card.name] -
                                                    (deck[card.set + "." + card.name] ? deck[card.set + "." + card.name] : 0)}{" "}
                                                </div>
                                                <CardDisplay
                                                    popoverDisabled
                                                    scale={1.5}
                                                    opacity={
                                                        collection[card.set + "." + card.name] -
                                                        (deck[card.set + "." + card.name] ? deck[card.set + "." + card.name] : 0) >
                                                        0
                                                            ? 1
                                                            : 0.5
                                                    }
                                                    cardData={card}
                                                    windowDimensions={windowDimensions}
                                                />
                                            </div>
                                        </LazyLoad>
                                    )
                            )}
                    </div>

                    <div
                        style={{
                            display: "flex",
                            flexDirection: "column",
                            backgroundColor: "#ffe6ff",
                            flexGrow: 1
                        }}
                    >
                        <div style={{textAlign: "center"}}>{"Deck List"}</div>
                        {Object.values(toFullCards(deck))
                            .sort(compareCardsByKnowledge)
                            .map((card, i) => (
                                <div style={{display: "flex", padding: "3px"}}
                                     onClick={() => this.removeFromDeck(card.set + "." + card.name)} key={i}>
                                    <div style={{paddingRight: "3px"}}>{deck[card.set + "." + card.name]}</div>
                                    <CardDisplay
                                        small

                                        cardData={card}
                                        windowDimensions={windowDimensions}
                                    />
                                </div>
                            ))}
                    </div>
                </div>
            </div>
        ) : (
            <div>Loading Deck</div>
        );
    }
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(withFirebase(DeckBuilder));

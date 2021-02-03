import React from "react";
import Button from "../Primitives/Button";
import {connect} from "react-redux";
import LazyLoad from 'react-lazy-load';

import {withFirebase} from "../Firebase";
import CardDisplay from "../Card/CardDisplay";
import {mapDispatchToProps} from "../Redux/Store";
import {compareCardsByKnowledge, debugPrint, toDeck, toDecklist, toFullCards} from "../Helpers/Helpers";
import TextField from "@material-ui/core/TextField";
import * as proto from "../../protojs/compiled";
import {Redirect} from "react-router-dom";
import {withHandlers} from "../MessageHandlers/HandlerContext";

const mapStateToProps = state => {
    return {
        collection_: state.profile.collection,
        userId: state.firebaseAuthData.user.uid,
        windowDimensions: state.windowDimensions,
        draft: state.extendedGameState.draft,
    };
};



class DeckBuilder extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            name: "",
            deck: {},
            changed: false,
        };
    }

    setDeck = (name, deck) => {
        this.setState({
            name: name,
            deck: deck,
        });
    };

    componentWillMount() {
        const {firebase, deckId, updateState, userId} = this.props;
        if (deckId) {
            const setDeck = this.setDeck.bind(this);
            firebase.getDeck(deckId, setDeck);
            firebase.setUserData(userId, updateState);
        }
    }

    joinDraftGame = event => {
        const { serverHandler, draft } = this.props;
        const { deck } = this.state;
        serverHandler.joinQueue(proto.GameType.P2_DRAFT_GAME, toDecklist(deck), draft.id);
        this.setState({name: "SUBMITTED"});

    }

    addToDeck = cardName => {
        const {forDraft, draft, collection_} = this.props;
        const collection = forDraft ? toDeck(draft.decklist) : collection_;
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
        const {name, deck, changed} = this.state;
        const {forDraft, draft, collection_, windowDimensions} = this.props;
        const collection = forDraft ? toDeck(draft.decklist) : collection_;
        return (
            <div style={{display: "flex", flexDirection: "column", maxHeight: "95vh", color : "white"}}>
                {forDraft && name === "SUBMITTED" && <Redirect to={"/profile/play/game"}/>}
                {forDraft ? (<Button onClick={this.joinDraftGame} text="Submit"/>)
                    : (
                        <div style={{display: "flex", flexDirection: "row"}}>
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
                            .filter(card => {return card})
                            .sort(compareCardsByKnowledge)
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
                            .filter(card => {return card})
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
        );
    }
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(withFirebase(withHandlers(DeckBuilder)));

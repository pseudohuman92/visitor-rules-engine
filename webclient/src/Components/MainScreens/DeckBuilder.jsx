import React from "react";
import Button from "../Primitives/Button";
import {connect} from "react-redux";
import LazyLoad from 'react-lazy-load';

import {withFirebase} from "../Firebase";
import CardDisplay from "../Card/CardDisplay";
import {mapDispatchToProps} from "../Redux/Store";
import {
    compareCardsByKnowledge,
    debugPrint,
    getIconColor,
    toDeck,
    toDecklist,
    toFullCards,
    toKnowledgeString
} from "../Helpers/Helpers";
import TextField from "@material-ui/core/TextField";
import * as proto from "../../protojs/compiled";
import {Redirect} from "react-router-dom";
import {withHandlers} from "../MessageHandlers/HandlerContext";
import {knowledgeIconURLs} from "../Helpers/URLS";

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
            filter: {
                showGreen: true,
                showRed: true,
                showBlue: true,
                showPurple: true,
                showYellow: true,
                showColorless: true,
                },
            }
        };

    filter = card => {
        let {showGreen, showRed, showBlue, showPurple, showYellow, showColorless} = this.state.filter;
        return card &&
            !((!showGreen && toKnowledgeString(card.knowledgeCost).indexOf("G") > -1) ||
            (!showRed && toKnowledgeString(card.knowledgeCost).indexOf("R") > -1) ||
            (!showBlue && toKnowledgeString(card.knowledgeCost).indexOf("U") > -1) ||
            (!showPurple && toKnowledgeString(card.knowledgeCost).indexOf("P") > -1) ||
            (!showYellow && toKnowledgeString(card.knowledgeCost).indexOf("Y") > -1) ||
            (!showColorless && toKnowledgeString(card.knowledgeCost).length === 0) );
    }

    flipFilter = label => event => {
        let filter = this.state.filter;
        filter[label] = !this.state.filter[label];
        this.setState({filter: filter})
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
        const {name, deck, changed, filter} = this.state;
        const {forDraft, draft, collection_, windowDimensions, style} = this.props;
        const collection = forDraft ? toDeck(draft.decklist) : collection_;
        return (
            <div className="hidden-scrollable" style={{...style, display: "flex", flexDirection: "column"}}>
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
                <div style={{display: "flex", alignSelf:"center", flexDirection:"row", justifyContent:"center"}}>
                    <img src={knowledgeIconURLs.green} style={{maxWidth:"5%", opacity: filter.showGreen ? 1 : 0.5}}
                         onClick={this.flipFilter("showGreen")}/>
                    <img src={knowledgeIconURLs.red} style={{maxWidth:"5%", opacity: filter.showRed ? 1 : 0.5}}
                         onClick={this.flipFilter("showRed")}/>
                    <img src={knowledgeIconURLs.blue} style={{maxWidth:"5%", opacity: filter.showBlue ? 1 : 0.5}}
                         onClick={this.flipFilter("showBlue")}/>
                    <img src={knowledgeIconURLs.purple} style={{maxWidth:"5%", opacity: filter.showPurple ? 1 : 0.5}}
                         onClick={this.flipFilter("showPurple")}/>
                    <img src={knowledgeIconURLs.yellow} style={{maxWidth:"5%", opacity: filter.showYellow ? 1 : 0.5}}
                         onClick={this.flipFilter("showYellow")}/>
                    <img src={knowledgeIconURLs.colorless} style={{maxWidth:"5%", opacity: filter.showColorless ? 1 : 0.5}}
                         onClick={this.flipFilter("showColorless")}/>
                </div>
                <div style={{display: "flex", position: "relative"}}>
                    <div className="hidden-scrollable" style={{
                        position: "relative",
                        justifyContent: "center",
                        height: "80%", flexGrow: 8,
                        display: "flex", flexWrap: "wrap"}}>
                        {Object.values(toFullCards(collection))
                            .filter(this.filter)
                            .sort(compareCardsByKnowledge)
                            .map(
                                (card, i) =>
                                        <LazyLoad key={i}>
                                            <div onClick={() => this.addToDeck(card.set + "." + card.name)} >
                                                <div style={{textAlign: "center"}}>
                                                    {" "}
                                                    {collection[card.set + "." + card.name] -
                                                    (deck[card.set + "." + card.name] ? deck[card.set + "." + card.name] : 0)}{" "}
                                                </div>
                                                <CardDisplay
                                                    popoverDisabled
                                                    //scale={1.5}
                                                    opacity={
                                                        collection[card.set + "." + card.name] -
                                                        (deck[card.set + "." + card.name] ? deck[card.set + "." + card.name] : 0) >
                                                        0
                                                            ? 100
                                                            : 50
                                                    }
                                                    cardData={card}
                                                    windowDimensions={windowDimensions}
                                                />
                                            </div>
                                        </LazyLoad>
                            )}
                    </div>
                    <div
                        style={{
                            flexGrow: 2,
                            display: "flex",
                            flexDirection: "column",
                        }}
                    >
                        <div style={{textAlign: "center"}}>{"Deck List"}</div>
                        <div
                            style={{
                                width: "100%",
                                display: "flex",
                                flexDirection: "column",
                                border: "3px white solid",
                            }}
                        >
                        {Object.values(toFullCards(deck))
                            .filter(card => {return card})
                            .sort(compareCardsByKnowledge)
                            .map((card, i) => (
                                <div style={{display: "flex"}}
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
            </div>
        );
    }
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(withFirebase(withHandlers(DeckBuilder)));

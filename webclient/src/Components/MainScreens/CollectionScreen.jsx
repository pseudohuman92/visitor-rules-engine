import React from "react";
import {connect} from "react-redux";

import {withFirebase} from "../Firebase";
import CardDisplay from "../Card/CardDisplay";
import {craftCost, fullCollection, isProduction, salvageValue} from "../Helpers/Constants";
import {mapDispatchToProps} from "../Redux/Store";
import CraftableCard from "../Card/CraftableCard";
import {compareCardsByKnowledge, debugPrint, sleep, toFullCards, toKnowledgeString} from "../Helpers/Helpers";
import Switch from "@material-ui/core/Switch";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Button from "@material-ui/core/Button";
import LazyLoad from 'react-lazy-load';
import {knowledgeIconURLs} from "../Helpers/URLS";

const mapStateToProps = state => {
    return {
        collection: state.profile.collection,
        dust: state.profile.dust,
        userId: state.firebaseAuthData.user.uid,
        windowDimensions: state.windowDimensions
    };
};

class CollectionScreen extends React.Component {
    state = {
        craft: false,
        filter: {
            showGreen: true,
            showRed: true,
            showBlue: true,
            showPurple: true,
            showYellow: true,
            showColorless: true,
        },
    };

    componentWillMount() {
        const {firebase, updateState, userId} = this.props;
        firebase.setUserData(userId, updateState);
    }

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

    craft = () => {
        this.setState(state => ({page: 0, craft: !state.craft}));
    };

    onSalvage = cardName => event => {
        const {firebase, updateState, collection, dust, userId} = this.props;
        let dust2 = dust;
        if (collection[cardName] > 0) {
            if (collection[cardName] === 1) {
                delete collection[cardName];
            } else {
                collection[cardName]--;
            }
            dust2 += salvageValue;
            debugPrint("Salvaging " + cardName);
            firebase.salvageCard(userId, cardName, salvageValue);
            updateState({dust: dust2, collection: collection});
        }
    };

    craftMissing = async event => {
        const {firebase, updateState, collection, userId} = this.props;
        const cards = Object.values(fullCollection);
        for (let cardIndex = 0; cardIndex < cards.length; cardIndex++) {
            const card = cards[cardIndex];
            let fullName = card.set + "." + card.name;
            if (!collection[fullName] || collection[fullName] < 3) {
                let i = collection[fullName] ? collection[fullName] : 0;
                for (; i < 3; i++) {
                    if (!collection[fullName]) {
                        collection[fullName] = 1;
                    } else {
                        collection[fullName]++;
                    }
                    console.log("Crafting", fullName);
                    await firebase.craftCard(userId, fullName, craftCost);
                    await sleep(500);
                }
            }
        }
        updateState({profile: {collection: collection}});
    };

    onCraft = cardName => event => {
        const {firebase, updateState, collection, dust, userId} = this.props;
        let dust2 = dust;
        if (dust >= craftCost) {
            if (!collection[cardName]) {
                collection[cardName] = 1;
            } else {
                collection[cardName]++;
            }
            dust2 -= craftCost;
            debugPrint("Crafting " + cardName);
            firebase.craftCard(userId, cardName, craftCost);
            updateState({profile: {dust: dust2, collection: collection}});
        }
    };

    salvageExtras = () => {
        const {firebase, updateState, collection, dust, userId} = this.props;
        let dust2 = dust;
        debugPrint("Salvaging all extras.");
        Object.keys(collection).forEach(cardName => {
            if (collection[cardName] > 3) {
                let toDust = collection[cardName] - 3;
                for (let i = 0; i < toDust; i++) {
                    firebase.salvageCard(userId, cardName, salvageValue);
                }
                collection[cardName] -= toDust;
                dust2 += toDust * salvageValue;
                debugPrint("Salvaged " + toDust + " of " + cardName);
            }
        });
        updateState({profile: {dust: dust2, collection: collection}});
    };

    hasExtras = () => {
        const {collection} = this.props;
        return Object.values(collection).some(v => v > 3);
    };

    render() {
        const {craft, filter} = this.state;
        const {collection, dust, windowDimensions} = this.props;
        const displayCollection = craft ? fullCollection : toFullCards(collection);
        return collection ? (
                <div
                style={{
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "center",
                    color: "white"
                }}
            >
                <div style={{
                    display: "flex",
                    flexDirection: "row",
                    alignItems: "center",
                    flexGrow: 1
                }}>
                    <FormControlLabel
                        control={
                            <Switch
                                checked={craft}
                                onChange={this.craft}
                                color="primary"
                            />
                        }
                        label={craft ? "Craft" : "Display"}
                    />
                    <div>
                        {"Dust: " + dust}
                    </div>
                    {craft && (
                        <Button
                            onClick={this.salvageExtras}
                            disabled={!this.hasExtras()}
                            variant="contained"
                            text="Salvage Extras"
                        >Salvage Extras</Button>
                    )}
                    {craft && !isProduction && (
                        <Button
                            onClick={this.craftMissing}
                            variant="contained"
                            text="Craft Missing"
                        > Craft Missing </Button>
                    )}
                </div>
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
                    <div className="hidden-scrollable"
                         style={{display: "flex", flexWrap: "wrap", justifyContent: "center"}}>
                        {Object.values(displayCollection)
                            .sort(compareCardsByKnowledge)
                            .filter(this.filter)
                            .map((card, i) =>
                                    <LazyLoad key={i}>
                                        <div style={{textAlign: "center"}}>
                                    {collection[card.set + "." + card.name] ? collection[card.set + "." + card.name] : 0}
                                    {craft ? (
                                        <CraftableCard
                                            scale = {1.5}
                                            brightness={collection[card.set + "." + card.name] ? 100 : 50}
                                            craft={craft}
                                            count={collection[card.set + "." + card.name] ? collection[card.set + "." + card.name] : 0}
                                            onCraft={this.onCraft(card.set + "." + card.name)}
                                            onSalvage={this.onSalvage(card.set + "." + card.name)}
                                            craftDisabled={dust < craftCost}
                                            salvageDisabled={!collection[card.set + "." + card.name]}
                                            cardData={card}
                                            windowDimensions={windowDimensions}
                                        />
                                    ) : (
                                        <CardDisplay
                                            scale = {1.5}
                                            brightness={collection[card.set + "." + card.name] ? 100 : 50}
                                            cardData={card}
                                            windowDimensions={windowDimensions}
                                            popoverDisabled
                                        />
                                    )}
                                </div>
                            </LazyLoad>
                            )}
                    </div>

            </div>
        ) : (
            <div>Loading Collection</div>
        );
    }
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(withFirebase(CollectionScreen));

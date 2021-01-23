import React from "react";
import {connect} from "react-redux";

import {withFirebase} from "../Firebase";
import CardDisplay from "../Card/CardDisplay";
import {craftCost, fullCollection, isProduction, salvageValue} from "../Helpers/Constants";
import {mapDispatchToProps} from "../Redux/Store";
import CraftableCard from "../Card/CraftableCard";
import {compareCardsByKnowledge, debugPrint, sleep, toFullCards} from "../Helpers/Helpers";
import Switch from "@material-ui/core/Switch";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Button from "@material-ui/core/Button";

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
        craft: false
    };

    componentWillMount() {
        const {firebase, updateState, userId} = this.props;
        firebase.setUserData(userId, updateState);
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
        const {craft} = this.state;
        const {collection, dust, windowDimensions} = this.props;
        const displayCollection = craft ? fullCollection : toFullCards(collection);
        return collection ? (
                <div
                style={{
                    display: "flex",
                    flexDirection: "column",
                    maxHeight: "95vh"
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
                    <div style={{fontFamily: "Cinzel, serif"}}>
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
                <div style={{display: "flex"}}>
                    <div style={{display: "flex", flexWrap: "wrap"}}>
                        {Object.values(displayCollection)
                            .sort(compareCardsByKnowledge)
                            .filter(card => {
                                return card;
                            })
                            .map((card, i) => (
                                <div key={i} style={{textAlign: "center"}}>
                                    {collection[card.set + "." + card.name] ? collection[card.set + "." + card.name] : 0}
                                    {craft ? (
                                        <CraftableCard
                                            scale = {1.5}
                                            opacity={collection[card.set + "." + card.name] ? 1 : 0.5}
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
                                            opacity={collection[card.set + "." + card.name] ? 1 : 0.5}
                                            cardData={card}
                                            windowDimensions={windowDimensions}
                                        />
                                    )}
                                </div>)
                            )}
                    </div>
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

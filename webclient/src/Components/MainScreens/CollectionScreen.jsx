import React from "react";
import Grid from "@material-ui/core/Grid";
import Button from "../Primitives/Button";
import Center from "react-center";
import {connect} from "react-redux";

import {withFirebase} from "../Firebase";
import CardDisplay from "../Card/CardDisplay";
import {fullCollection, craftCost, salvageValue, isProduction} from "../Helpers/Constants";
import {mapDispatchToProps} from "../Redux/Store";
import {Typography} from "@material-ui/core";
import CraftableCard from "../Card/CraftableCard";
import {
    debugPrint,
    toFullCards,
    compareCardsByKnowledge, sleep
} from "../Helpers/Helpers";

const mapStateToProps = state => {
    return {
        collection: state.profile.collection,
        dust: state.profile.dust,
        userId: state.firebaseAuthData.user.uid,
        windowDimensions: state.windowDimensions
    };
};

const cardsPerPage = 12;

class CollectionScreen extends React.Component {
    state = {
        page: 0,
        craft: false
    };

    componentWillMount() {
        const {firebase, updateState, userId} = this.props;
        firebase.setUserData(userId, updateState);
    }

    prev = () => {
        this.setState(state => ({page: Math.max(state.page - 1, 0)}));
    };

    next = () => {
        this.setState(state => ({
            page: Math.min(
                state.page + 1,
                Math.floor(
                    Object.keys(state.craft ? fullCollection : this.props.collection)
                        .length / cardsPerPage
                )
            )
        }));
    };

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
        const {page, craft} = this.state;
        const {collection, dust, windowDimensions} = this.props;
        const displayCollection = craft ? fullCollection : toFullCards(collection);
        const maxPage = Math.floor(
            Object.keys(craft ? fullCollection : collection).length / cardsPerPage
        );
        return collection ? (
            <Grid
                container
                spacing={8}
                style={{maxHeight: "95vh"}}
                alignItems="center"
            >
                <Grid item xs={1}>
                    <Button onClick={this.prev} text="Prev" disabled={page === 0}/>
                </Grid>
                <Grid item xs={1}>
                    <Button onClick={this.next} text="Next" disabled={page === maxPage}/>
                </Grid>
                <Grid item xs={1}>
                    <Button
                        onClick={this.craft}
                        text={craft ? "Craft Mode" : "Display Mode"}
                    />
                </Grid>
                <Grid item xs={1}>
                    <Center>
                        <Typography style={{fontFamily: "Cinzel, serif"}}>
                            {"Dust: " + dust}
                        </Typography>
                    </Center>
                </Grid>
                {craft && (
                    <Grid item xs={1}>
                        <Button
                            onClick={this.salvageExtras}
                            disabled={!this.hasExtras()}
                            text="Salvage Extras"
                        />
                    </Grid>
                )}
                {craft && !isProduction && (
                    <Grid item xs={1}>
                        <Button
                            onClick={this.craftMissing}
                            text="Craft Missing"
                        />
                    </Grid>
                )}
                <Grid container item xs={12} spacing={8}>
                    {Object.values(displayCollection)
                        .sort(compareCardsByKnowledge)
                        .slice(page * cardsPerPage, (page + 1) * cardsPerPage)
                        .map((card, i) => (
                            <Grid item key={i} xs={2} style={{textAlign: "center"}}>
                                {collection[card.set + "." + card.name] ? collection[card.set + "." + card.name] : 0}
                                {craft ? (
                                    <CraftableCard
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
                                        opacity={collection[card.set + "." + card.name] ? 1 : 0.5}
                                        cardData={card}
                                        windowDimensions={windowDimensions}
                                    />
                                )}
                            </Grid>
                        ))}
                </Grid>
            </Grid>
        ) : (
            <div>Loading Collection</div>
        );
    }
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(withFirebase(CollectionScreen));

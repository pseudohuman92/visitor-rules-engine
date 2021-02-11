import React, {Component} from "react";
import {connect} from "react-redux";
import {withHandlers} from "../MessageHandlers/HandlerContext";
import {mapDispatchToProps} from "../Redux/Store";

import CardDisplay from "../Card/CardDisplay";
import * as proto from "../../protojs/compiled";
import {Redirect} from "react-router-dom";
import {compareCardsByKnowledge, toDeck, toFullCards} from "../Helpers/Helpers";

const mapStateToProps = state => {
    return {
        dialog: state.extendedGameState.dialogData,
        windowDimensions: state.windowDimensions,
        draft : state.extendedGameState.draft,
    };
};

class DraftScreen extends Component {

    componentWillMount() {

        const {serverHandler} = this.props;
            serverHandler.joinQueue(proto.GameType.P2_DRAFT);
    }

    pickCard = selected => event => {
        this.props.gameHandler.PickCard(selected);
    };

    render = () => {
        const {windowDimensions, dialog, draft} = this.props;
        const deck = toDeck(draft.decklist);
        return (
            <div style={{display:"flex"}}>
            <div style={{flexGrow: 9}}>
                {draft.completed && <Redirect to={"/profile/play/draft/deck_builder"}/>}
                <div style={{
                    textAlign: "center",
                    fontSize: "20px",
                    marginBottom: "10px"
                }}> {dialog.title} </div>
                <div style={{display: "flex", justifyContent: "center"}}>
                    {dialog.cards.map((card, i) => (
                        <div style={{margin: "3px"}} key={i} onClick={this.pickCard(card.id)}>
                            <CardDisplay
                                cardData={card}
                                windowDimensions={windowDimensions}
                                withKeywords
                            />
                        </div>
                    ))}
                </div>
            </div>
                <div
                    style={{
                        display: "flex",
                        flexDirection: "column",
                        border: "3pc white solid",
                        flexGrow: 1
                    }}
                >
                    <div style={{textAlign: "center"}}>{"Drafted Cards"}</div>
                    {Object.values(toFullCards(deck))
                        .sort(compareCardsByKnowledge)
                        .map((card, i) => (
                            <div style={{display: "flex", padding: "3px"}} key={i}>
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
        );
    };
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(withHandlers(DraftScreen));

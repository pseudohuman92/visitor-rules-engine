import React, {Component} from "react";
import {connect} from "react-redux";
import {withHandlers} from "../MessageHandlers/HandlerContext";
import {mapDispatchToProps} from "../Redux/Store";

import "../../css/ChooseDialog.css";
import CardDisplay from "../Card/CardDisplay";
import ServerMessageHandler from "../MessageHandlers/ServerMessageHandler";
import * as proto from "../../protojs/compiled";

const mapStateToProps = state => {
    return {
        clientPhase: state.extendedGameState.clientPhase,
        dialog: state.extendedGameState.dialogData,
        windowDimensions: state.windowDimensions,
        userId: state.firebaseAuthData.user.uid,
        gameInitialized: state.extendedGameState.gameInitialized
    };
};

class DraftScreen extends Component {

    componentWillMount() {
        const {userId, updateHandlers, updateExtendedGameState} = this.props;
        const handler = new ServerMessageHandler(
            userId,
            updateHandlers,
            updateExtendedGameState,
            () => {
            }
        );
        updateHandlers({
            serverHandler: handler
        });
        handler.joinQueue(proto.GameType.P2_DRAFT, "");
    }

    pickCard = selected => event => {
        this.props.gameHandler.PickCard(selected);
    };

    render = () => {
        const {windowDimensions, dialog} = this.props;

        return (
            <div>
                <div style={{
                    textAlign: "center",
                    fontSize: "20px",
                    color: "black",
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
        );
    };
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(withHandlers(DraftScreen));

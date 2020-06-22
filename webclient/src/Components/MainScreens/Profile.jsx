import React, {Component} from "react";
import ProfileBar from "../Other Components/ProfileBar";
import LinkedButton from "../Primitives/LinkedButton";
import ServerMessageHandler from "../MessageHandlers/ServerMessageHandler";
import * as proto from "../../protojs/compiled";
import {mapDispatchToProps} from "../Redux/Store";
import {withHandlers} from "../MessageHandlers/HandlerContext";
import {connect} from "react-redux";
import {Route, Switch} from "react-router-dom";
import Decks from "./Decks";
import CollectionScreen from "./CollectionScreen";
import DeckSelection from "./DeckSelection";
import GameStore from "./GameStore";
import OpenPacks from "./OpenPacks";
import DraftScreen from "./DraftScreen";
import GameScreen from "./GameScreen";
import DeckBuilder from "./DeckBuilder";

const mapStateToProps = state => {
    return {
        userId: state.firebaseAuthData.user.uid,
    };
};

class Profile extends Component {

    componentWillMount() {
        const {userId, updateHandlers, updateExtendedGameState} = this.props;
        const handler = new ServerMessageHandler(
            userId,
            updateHandlers,
            updateExtendedGameState,
            () => {}
        );
        updateHandlers({
            serverHandler: handler
        });
    }

    render() {
        return (
            <div>
                <ProfileBar style={{maxHeight:"10%"}}/>
                <div style={{display: "flex"}}>
                    <LinkedButton to={"/profile/play"} text="Play"/>
                    <LinkedButton to={"/profile/decks"} text="Decks"/>
                    <LinkedButton to={"/profile/packs"} text="Packs"/>
                    <LinkedButton to={"/profile/collection"} text="Collection"/>
                    <LinkedButton to={"/profile/store"} text="Store"/>
                </div>
            </div>
        );
    }
}

export default connect (mapStateToProps, mapDispatchToProps)(withHandlers(Profile))
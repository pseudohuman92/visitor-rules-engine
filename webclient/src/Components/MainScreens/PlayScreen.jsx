import React, {Component} from "react";
import ServerMessageHandler from "../MessageHandlers/ServerMessageHandler";
import ProfileBar from "../Other Components/ProfileBar";
import LinkedButton from "../Primitives/LinkedButton";
import {connect} from "react-redux";
import {mapDispatchToProps} from "../Redux/Store";
import {withHandlers} from "../MessageHandlers/HandlerContext";
import {Redirect} from "react-router-dom";

const mapStateToProps = state => {
    return {
        gameInitialized: state.extendedGameState.gameInitialized,
    };
};

class PlayScreen extends Component {

    render() {
        return (
            <div className="play-screen-outer">
                {this.props.gameInitialized && <Redirect to={"/profile/play/game"}/>}
                <ProfileBar style={{maxHeight:"10%"}}/>
                <div style={{display: "flex"}}>
                    <LinkedButton to={"/profile/play/deck_selection"} text="Constructed"/>
                    <LinkedButton to={"/profile/play/draft"} text="Draft"/>
                </div>
            </div>
        );
    }
}

export default connect (mapStateToProps)(PlayScreen)
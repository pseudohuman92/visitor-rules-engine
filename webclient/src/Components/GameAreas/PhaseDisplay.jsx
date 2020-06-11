import React, {Component, PureComponent} from "react";
import Grid from "@material-ui/core/Grid";
import Button from "../Primitives/Button";
import {connect} from "react-redux";

import proto from "../../protojs/compiled.js";

import {ClientPhase} from "../Helpers/Constants";
import {IsSelectCardPhase} from "../Helpers/Helpers";
import {withHandlers} from "../MessageHandlers/HandlerContext";
import "../../css/Utils.css";
import {mapDispatchToProps} from "../Redux/Store";

const mapStateToProps = state => {
    return {
        gamePhase: state.extendedGameState.game.phase,
    };
};

class PhaseDisplay extends PureComponent {
    render() {
        const {gamePhase} = this.props;
        return (
            <div style={{display: "flex", justifyContent:"space-around", borderRadius: "10px", backgroundColor: "rgba(0,0,0,0.75)", color: "white", width: "90%"}}>
                <div style={{backgroundColor: gamePhase === proto.Phase.BEGIN ? "green" : ""}}>S</div>
                <div style={{backgroundColor: gamePhase === proto.Phase.MAIN_BEFORE ? "green" : ""}}>M1</div>
                <div style={{backgroundColor: gamePhase === proto.Phase.ATTACK ? "green" : ""}}>A</div>
                <div style={{backgroundColor: gamePhase === proto.Phase.BLOCK ? "green" : ""}}>B</div>
                <div style={{backgroundColor: gamePhase === proto.Phase.MAIN_AFTER ? "green" : ""}}>M2</div>
                <div style={{backgroundColor: gamePhase === proto.Phase.END ? "green" : ""}}>E</div>
            </div>);
    }
}

export default connect(
    mapStateToProps
)(PhaseDisplay);
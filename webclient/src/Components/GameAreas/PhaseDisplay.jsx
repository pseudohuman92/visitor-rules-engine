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
import {colorPalette} from "../Helpers/Constants";

const mapStateToProps = state => {
    return {
        gamePhase: state.extendedGameState.game.phase,
    };
};

class PhaseDisplay extends PureComponent {
    getColor = (phase) => {
        return this.props.gamePhase === phase ? colorPalette.red : colorPalette.black;
    }
    render() {
        return (
            <div className="phase-display"
                 style={{
                ...this.props.style,
                display: "flex", justifyContent:"space-around",
                backgroundImage: "url(" + process.env.PUBLIC_URL + "/img/buttons/grunge-highlight-white.png)",
                backgroundPosition: 'center',
                backgroundSize: 'cover',
                backgroundRepeat: 'no-repeat',
                fontFamily:"Frijole, cursive",
            }}>
                <div style={{color: this.getColor(proto.Phase.BEGIN)}}>S</div>
                <div style={{color: this.getColor(proto.Phase.MAIN_BEFORE )}}>M1</div>
                <div style={{color: this.getColor(proto.Phase.ATTACK)}}>A</div>
                <div style={{color: this.getColor(proto.Phase.BLOCK)}}>B</div>
                <div style={{color: this.getColor(proto.Phase.MAIN_AFTER)}}>M2</div>
                <div style={{color: this.getColor(proto.Phase.END)}}>E</div>
            </div>);
    }
}

export default connect(
    mapStateToProps
)(PhaseDisplay);
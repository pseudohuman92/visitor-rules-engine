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
        message: state.extendedGameState.message,
    };
};

class SelectionMessage extends PureComponent {
    render() {
        const {message} = this.props;
        return (
            <div style={{backgroundColor: "rgba(0, 0, 0, 0.75)", textAlign: "center", color: "white", borderRadius: "10px" }}>{message}</div>);
    }
}

export default connect(
    mapStateToProps
)(SelectionMessage);
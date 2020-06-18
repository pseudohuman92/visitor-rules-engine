import React, {Component} from "react";
import Dialog from "@material-ui/core/Dialog";
import {connect} from "react-redux";
import {withHandlers} from "../MessageHandlers/HandlerContext";
import {mapDispatchToProps} from "../Redux/Store";
import {ClientPhase, knowledgeNameMap} from "../Helpers/Constants";

import "../../css/ChooseDialog.css";
import {toKnowledgeName} from "../Helpers/Helpers";

const mapStateToProps = state => {
    return {
        clientPhase: state.extendedGameState.clientPhase,
        dialog: state.extendedGameState.dialogData,
    };
};



class KnowledgeDialog extends Component {
    onClose = event => {
        this.props.updateExtendedGameState({
            dialogData: {
                title: "",
                cards: [],
                open: false
            }
        });
    };

    selectDone = selected => event => {
        this.props.gameHandler.SelectKnowledge(selected);
    };

    render = () => {
        const {clientPhase, dialog} = this.props;

        const isSelectPhase =
            clientPhase === ClientPhase.SELECT_KNOWLEDGE;

        return (
            <Dialog
                open={dialog.open && isSelectPhase}
                onClose={this.onClose}
                fullWidth
                disableBackdropClick={isSelectPhase}
                disableEscapeKeyDown={isSelectPhase}
                scroll="body"
                PaperComponent="div"
            >
                <div style={{
                    textAlign: "center",
                    fontSize: "20px",
                    color: "white",
                    marginBottom: "10px"
                }}> {dialog.title} </div>
                <div style={{
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center"
                }}
                >
                    {dialog.cards.map((card, i) => (
                        <div style={{margin: "3px"}} key={i}>
                            <div style={{
                                padding: "10px",
                                textAlign: "center",
                                color: "white",
                                backgroundColor: "black",
                                fontSize: "30px",
                            }}
                                 onClick={this.selectDone(card)}>
                                {knowledgeNameMap[card]}
                            </div>
                        </div>
                    ))}
                </div>
            </Dialog>
        );
    };
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(withHandlers(KnowledgeDialog));

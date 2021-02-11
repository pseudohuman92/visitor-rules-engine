import React, {Component} from "react";
import Dialog from "@material-ui/core/Dialog";
import Button from "@material-ui/core/Button";
import {connect} from "react-redux";

import PlayingCard from "../Card/PlayingCard";
import {withHandlers} from "../MessageHandlers/HandlerContext";
import {mapDispatchToProps} from "../Redux/Store";
import {ClientPhase} from "../Helpers/Constants";
import {Droppable} from "react-beautiful-dnd";


const mapStateToProps = state => {
    return {
        clientPhase: state.extendedGameState.clientPhase,
        selected: state.extendedGameState.selectionData.selected,
        dialog: state.extendedGameState.dialogData,
        upTo: state.extendedGameState.selectionData.upTo,
        message: state.extendedGameState.message,
    };
};

class ChooseDialog extends Component {
    onClose = event => {
        this.props.updateExtendedGameState({
            dialogData: {
                title: "",
                cards: [],
                open: false
            }
        });
    };

    selectDone = event => {
        let selected = [...this.props.selected];
        let clientPhase = this.props.clientPhase;
        this.props.gameHandler.SelectDone(clientPhase, selected);
    };

    render = () => {
        const {clientPhase, dialog, upTo} = this.props;

        const isSelectPhase =
            clientPhase === ClientPhase.SELECT_FROM_LIST ||
            clientPhase === ClientPhase.SELECT_FROM_DISCARD_PILE ||
            clientPhase === ClientPhase.SELECT_FROM_VOID;

        return (
            <Dialog
                open={clientPhase !== ClientPhase.SELECT_KNOWLEDGE && dialog.open}
                onClose={this.onClose}
                fullWidth
                disableBackdropClick={isSelectPhase}
                disableEscapeKeyDown={isSelectPhase}
                scroll="body"
                PaperComponent="div"
            >
                <div style={{
                    height: "10%",
                    width: "100%",
                    textAlign: "center",
                    fontSize: "20px",
                    color: "white",
                    marginBottom: "10px"
                }}> {dialog.title} </div>
                <Droppable style={{position: "absolute", top: "20%", height: "60%%", width: "100%",}}
                           droppableId={"modal-list"} isDropDisabled>
                    {provided => (
                        <div style={{height: "100%", width: "100%",}}
                             ref={provided.innerRef} {...provided.droppableProps}>
                            <div style={{display: "flex", justifyContent: "center"}}
                            >
                                {dialog.cards.map((card, i) => (
                                    <div style={{margin: "3px"}} DnDIndex={i}>
                                        <PlayingCard
                                            cardData={card}
                                            isDragDisabled
                                            popoverDisabled
                                        />
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}
                </Droppable>
                {upTo && (
                    <Button
                        color="primary"
                        variant="contained"
                        onClick={this.selectDone}
                    >
                        Done
                    </Button>
                )}
            </Dialog>
        );
    };
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(withHandlers(ChooseDialog));

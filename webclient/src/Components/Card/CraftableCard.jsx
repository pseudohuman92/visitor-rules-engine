import React, {PureComponent} from "react";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";

import {craftCost, salvageValue} from "../Helpers/Constants";
import {CardDisplay} from "./CardDisplay";
import Button from "@material-ui/core/Button";

class CraftableCard extends PureComponent {
    state = {showDialog: false};

    openDialog = event => {
        this.setState({showDialog: true});
        if (this.props.onClick) {
            this.props.onClick(event);
        }
    };

    render() {
        const {
            onCraft,
            craftDisabled,
            onSalvage,
            salvageDisabled,
            count,
            cardData,
            windowDimensions,
            scale,
            brightness,
        } = this.props;
        return (
            <div>
                <Dialog
                    open={this.state.showDialog}
                    onClose={event => this.setState({showDialog: false})}
                    maxWidth={"sm"}
                    fullWidth
                    scroll="body"
                >
                    <DialogContent>
                        <div style={{display: "flex", justifyContents: "center", alignContents: "center"}}>
                            <div style={{
                                display: "flex",
                                flexDirection: "column",
                                justifyContents: "center",
                                alignItems: "center"
                            }}>
                                <Button
                                    onClick={onCraft}
                                    disabled={craftDisabled}
                                    text="Craft"
                                    variant="contained"
                                    color="primary"
                                >Craft</Button>

                                {"Dust: -" + craftCost}
                            </div>
                            <div style={{
                                display: "flex",
                                flexDirection: "column",
                                justifyContents: "center",
                                alignContents: "center"
                            }}>
                                <div style={{textAlign: "center"}}>{count}</div>
                                <CardDisplay popoverDisabled scale={scale} cardData={cardData} windowDimensions={windowDimensions}/>
                            </div>
                            <div style={{
                                display: "flex",
                                flexDirection: "column",
                                justifyContents: "center",
                                alignContents: "center"
                            }}>
                                <Button
                                    onClick={onSalvage}
                                    disabled={salvageDisabled}
                                    text="Salvage"
                                    variant="contained"
                                    color="secondary"
                                >Salvage</Button>
                                {"Dust: +" + salvageValue}
                            </div>
                        </div>
                    </DialogContent>
                </Dialog>
                <div onClick={this.openDialog}>
                    <CardDisplay popoverDisabled scale={scale} cardData={cardData} brightness={brightness} windowDimensions={windowDimensions}/>
                </div>
            </div>
        );
    }
}

export default CraftableCard;

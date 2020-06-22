import React, {PureComponent} from "react";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";

import Button from '../Primitives/Button';
import {craftCost, salvageValue} from "../Helpers/Constants";
import {CardDisplay} from "./CardDisplay";

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
            scale
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
                                />
                                {"Dust: -" + craftCost}
                            </div>
                            <div style={{
                                display: "flex",
                                flexDirection: "column",
                                justifyContents: "center",
                                alignContents: "center"
                            }}>
                                <div style={{textAlign: "center"}}>{count}</div>
                                <CardDisplay popupDisabled scale={scale} cardData={cardData} opacity="1" windowDimensions={windowDimensions}/>
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
                                />
                                {"Dust: +" + salvageValue}
                            </div>
                        </div>
                    </DialogContent>
                </Dialog>
                <div onClick={this.openDialog}>
                    <CardDisplay popupDisabled scale={scale} cardData={cardData} windowDimensions={windowDimensions}/>
                </div>
            </div>
        );
    }
}

export default CraftableCard;

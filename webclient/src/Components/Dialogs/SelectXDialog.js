import React, { Component } from 'react';

import Dialog from '@material-ui/core/Dialog';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';

import { SelectXValue } from '../../MessageHandlers/ServerGameMessageHandler';

export default class SelectXDialog extends Component {
    constructor(props) {
        super(props);
        this.state = {
            open: this.props.open,
            xVal: 0,
        };
    }

    onChange = maxValue => event => {
        if (event.target.value <= maxValue) {
            this.setState({ xVal: event.target.value });
        }
    };

    onClick = event => {
        SelectXValue(this.state.xVal);
        this.setState({ open: false });
    };

    render = () => {
        const { xVal } = this.state;
        const open = this.state.open || this.props.open;
        const maxValue = this.props.maxValue;

        return ( <
            Dialog open = { open } >
            <
            DialogTitle > Select X < /DialogTitle> <
            DialogContent >
            <
            TextField type = "number"
            label = "X"
            value = { xVal }
            onChange = { this.onChange(maxValue) }
            /> <
            Button color = "primary"
            variant = "contained"
            onClick = { this.onClick } >
            Submit <
            /Button> < /
            DialogContent > <
            /Dialog>
        );
    };
}
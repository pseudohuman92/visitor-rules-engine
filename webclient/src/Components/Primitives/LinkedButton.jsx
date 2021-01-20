import React, {Component} from "react";
import Button from "./Button";
import {Link} from "react-router-dom";

export default class LinkedButton extends Component {
    render() {
        const {to, ...otherProps} = this.props;
        return (
            <Link style={{textDecoration: "none"}}  to={to}>
                <Button {...otherProps}/>
            </Link>);
    }
}
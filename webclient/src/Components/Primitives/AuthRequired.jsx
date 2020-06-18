import React, {Component} from 'react'
import {Redirect} from 'react-router-dom'
import {connect} from "react-redux";

const mapStateToProps = state => {
    return {
        isAuthenticated: state.profile.isAuthenticated,
    };
};

class AuthRequired extends Component {
    render() {
        const {isAuthenticated, children} = this.props;

        return isAuthenticated ? children : <Redirect to={"/signin"}/>;
    }
}

export default connect(mapStateToProps)(AuthRequired);
import React, {Component} from "react";
import Button from "@material-ui/core/Button";
import FormControl from "@material-ui/core/FormControl";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Checkbox from "@material-ui/core/Checkbox";
import Input from "@material-ui/core/Input";
import InputLabel from "@material-ui/core/InputLabel";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import Center from "react-center";
import {connect} from "react-redux";

import {withFirebase} from "../Firebase";
import {mapDispatchToProps} from "../Redux/Store";
import {isProduction} from "../Helpers/Constants";
import {withCookies} from 'react-cookie';
import {debugPrint} from "../Helpers/Helpers";
import {Link, Redirect} from "react-router-dom";

class SignIn extends Component {
    constructor(props) {
        super(props);

        const {cookies} = props;
        const loginCredentials = cookies.get("Login Credentials");
        debugPrint(loginCredentials);
        this.state = {
            value: 0,
            email: loginCredentials ? loginCredentials.email : "",
            password: loginCredentials ? loginCredentials.password : "",
            error: null,
            rememberMe: loginCredentials ? loginCredentials.rememberMe : false,
        };
    }

    handleTextbox = event => {
        this.setState({[event.target.name]: event.target.value});
    };

    handleCheckbox = event => {
        this.setState({[event.target.name]: event.target.checked});
    };

    Signin = event => {
        const {email, password, rememberMe} = this.state;
        const {cookies, firebase, updateState} = this.props;

        firebase
            .doSignInWithEmailAndPassword(email, password)
            .then(firebaseAuthData => {
                updateState({firebaseAuthData: {user: {uid: firebaseAuthData.user.uid}}});
                firebase.setUserData(firebaseAuthData.user.uid, updateState);

                if (rememberMe) {
                    cookies.set("Login Credentials", {
                        email: email,
                        password: password,
                        rememberMe: rememberMe
                    });
                } else {
                    cookies.remove("Login Credentials");
                }
                setTimeout(() => this.setState({value: 1}), 500);

            })
            .catch(error => {
                this.setState({error});
            });

        event.preventDefault();
    };

    Test1 = event => {
        const {firebase, updateState} = this.props;
        firebase
            .doSignInWithEmailAndPassword("tester1@testers.com", "asdqwe")
            .then(firebaseAuthData => {
                updateState({firebaseAuthData: {user: {uid: firebaseAuthData.user.uid}}});
                firebase.setUserData(firebaseAuthData.user.uid, updateState);
                setTimeout(() => this.setState({value: 1}), 500);
            })
            .catch(error => {
                this.setState({error});
            });

        event.preventDefault();
    };

    Test2 = event => {
        const {firebase, updateState} = this.props;
        firebase
            .doSignInWithEmailAndPassword("tester2@testers.com", "asdqwe")
            .then(firebaseAuthData => {
                updateState({firebaseAuthData: {user: {uid: firebaseAuthData.user.uid}}});
                firebase.setUserData(firebaseAuthData.user.uid, updateState)
                setTimeout(() => this.setState({value: 1}), 500);
            })
            .catch(error => {
                this.setState({error});
            });

        event.preventDefault();
    };

    render() {
        const {value, email, password, rememberMe, error} = this.state;

        const isInvalid = password === "" || email === "";

        return (
            <div>
                {value === 0 && <Center style={{minHeight: "100vh",  backgroundSize: "cover", /*, backgroundImage: "url("+process.env.PUBLIC_URL + "/img/gate.jpg)"*/}}>
                    <Paper style={{backgroundColor: "#ffffff44", width: "30%", marginLeft: "50%"}} className="pa-3">
                        <Typography component="h1" variant="h5">
                            Sign in
                        </Typography>
                        <FormControl margin="normal" required fullWidth>
                            <InputLabel htmlFor="email">Email Address</InputLabel>
                            <Input
                                value={email}
                                id="email"
                                name="email"
                                autoComplete="email"
                                autoFocus
                                onChange={this.handleTextbox}
                            />
                        </FormControl>
                        <FormControl margin="normal" required fullWidth>
                            <InputLabel htmlFor="password">Password</InputLabel>
                            <Input
                                value={password}
                                name="password"
                                type="password"
                                id="password"
                                autoComplete="current-password"
                                onChange={this.handleTextbox}
                            />
                        </FormControl>
                        <FormControlLabel
                            control={<Checkbox name="rememberMe" color="primary" checked={rememberMe}
                                               onChange={this.handleCheckbox}/>}
                            label="Remember me"
                        />
                        {!isProduction && (
                            <Button type="submit" variant="contained" onClick={this.Test1}>
                                Tester 1
                            </Button>
                        )}
                        {!isProduction && (
                            <Button type="submit" variant="contained" onClick={this.Test2}>
                                Tester 2
                            </Button>
                        )}
                        <Button
                            disabled={isInvalid}
                            type="submit"
                            variant="contained"
                            onClick={this.Signin}
                        >
                            Sign in
                        </Button>
                        <Link to="/resetpassword">
                            <Button
                                color="primary"
                            >
                                Forgot password?
                            </Button>
                        </Link>
                        {error && <p>{error.message}</p>}
                    </Paper>
                </Center>}
                {value === 1 && <Redirect to="/profile"/>}
            </div>

        );
    }
}

export default connect(null, mapDispatchToProps)(withFirebase(withCookies(SignIn)));

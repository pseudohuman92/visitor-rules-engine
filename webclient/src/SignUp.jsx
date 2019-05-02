import React, { Component } from "react";
import Button from "@material-ui/core/Button";
import FormControl from "@material-ui/core/FormControl";
import Input from "@material-ui/core/Input";
import InputLabel from "@material-ui/core/InputLabel";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import { withFirebase } from "./Components/Firebase";
import Centered from "./Centered";
import Profile from "./Profile";

class SignUp extends Component {
  constructor(props) {
    super(props);

    this.state = {
      value: 0,
      username: "",
      email: "",
      password: "",
      error: null
    };
  }

  onChange = event => {
    this.setState({ [event.target.name]: event.target.value });
  };

  Signup = event => {
    const { username, email, password } = this.state;

    this.props.firebase
      .doCreateUserWithEmailAndPassword(email, password)
      .then(authUser => {
        //TODO: Create new database entry for the user here
        this.setState({ value: 1 });
      })
      .catch(error => {
        this.setState({ error });
      });

    event.preventDefault();
  };

  render() {
    const { value, username, email, password, error } = this.state;

    const isInvalid = username === "" || password === "" || email === "";

    return (
      <div>
        {" "}
        {value === 0 && (
          <Centered>
            <Paper>
              <Typography component="h1" variant="h5">
                Sign up
              </Typography>
              <FormControl margin="normal" required fullWidth>
                <InputLabel htmlFor="username">Username</InputLabel>
                <Input
                  id="username"
                  name="username"
                  autoComplete="username"
                  autoFocus
                  onChange={this.onChange}
                />
              </FormControl>
              <FormControl margin="normal" required fullWidth>
                <InputLabel htmlFor="email">Email Address</InputLabel>
                <Input
                  id="email"
                  name="email"
                  autoComplete="email"
                  onChange={this.onChange}
                />
              </FormControl>
              <FormControl margin="normal" required fullWidth>
                <InputLabel htmlFor="password">Password</InputLabel>
                <Input
                  name="password"
                  type="password"
                  id="password"
                  autoComplete="current-password"
                  onChange={this.onChange}
                />
              </FormControl>
              <Button
                disabled={isInvalid}
                type="submit"
                variant="contained"
                color="primary"
                onClick={this.Signup}
              >
                Sign Up
              </Button>
              {error && <p>{error.message}</p>}
            </Paper>
          </Centered>
        )}
        {value === 1 && <Profile />}
      </div>
    );
  }
}

export default withFirebase(SignUp);

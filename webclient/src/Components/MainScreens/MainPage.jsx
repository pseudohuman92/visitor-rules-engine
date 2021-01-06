import React, {Component} from "react";
import Button from "../Primitives/Button";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import Center from "react-center";
import {Grid} from "@material-ui/core";
import Fonts from "../Primitives/Fonts";

import "../../css/App.css";
import {Link} from "react-router-dom";

export default class MainPage extends Component {
    render() {
        return (
            <div>
                <Center style={{width: "90%", margin: "auto", padding: "1rem"} }>
                    <Fonts/>
                    <Paper>
                        <Center>
                            <Typography
                                variant="h3"
                                style={{fontFamily: "Cinzel, serif", fontWeight: "800"}}
                            >
                                Visitor: The Card Game
                            </Typography>
                        </Center>
                        <Typography variant="body1" paragraph>
                            Visitor is a completely free collectible card game that brings
                            together some less developed aspects of the genre to solve
                            common issues of such games. Some of the notable features are
                        </Typography>

                        <Typography variant="body1" gutterBottom>
                            <b>* Completely free:</b> No way to buy boosters, just earn them
                            by playing.
                        </Typography>
                        <Typography variant="body1" gutterBottom>
                            <b>* No rarities:</b> No hard to get chase cards.
                        </Typography>
                        <Typography variant="body1" gutterBottom>
                            <b>* Web based client:</b> No need to download a dedicated
                            program.
                        </Typography>
                        <Typography variant="body1" gutterBottom>
                            <b>* No units or combat:</b> Only spells and other permanent
                            card types.
                        </Typography>
                        <Typography variant="body1" gutterBottom>
                            <b>* No relying on dedicated resource cards:</b> Each card can
                            be used as a resource.
                        </Typography>
                        <Typography variant="body1" gutterBottom>
                            <b>* Stack-based interactive play:</b> No waiting your turn to
                            play.
                        </Typography>
                        <Typography variant="body1" paragraph>
                            <b>* Brewer oriented design:</b> Will emphasize build-around
                            cards with complex interactions.
                        </Typography>
                        <Typography variant="body1" paragraph>
                            Please join us in our{" "}
                            <Link href="https://discord.gg/JSMPStm" gutterBottom>
                                Discord Server
                            </Link>{" "}
                            to learn the game and follow the development process.
                        </Typography>

                        <Grid container spacing={8}>
                            <Grid item xs>
                                <Link to="/signin">
                                    <Button text="Sign In" />
                                </Link>
                            </Grid>
                            <Grid item xs>
                                <Link to="/signup">
                                    <Button text="Sign Up" />
                                </Link>
                            </Grid>
                            {/*
                                    <Grid item xs>
                                        <Link to="/designcard">
                                            <Button
                                                text="Design a Card"
                                            />
                                        </Link>
                                    </Grid>
                                    <Grid item xs>
                                        <Link to="/customcards">
                                            <Button
                                                text="Custom Cards"
                                            />
                                        </Link>
                                    </Grid>
                                    */}
                            <Grid item xs>
                                <Link to="/cardgallery">
                                    <Button text="Card Gallery" />
                                </Link>
                            </Grid>
                        </Grid>
                    </Paper>
                </Center>

            </div>
        )
            ;
    }
}

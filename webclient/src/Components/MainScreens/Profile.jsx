import React, {Component} from "react";
import Button from "../Primitives/Button";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import Center from "react-center";
import {connect} from "react-redux";
import {mapDispatchToProps} from "../Redux/Store";
import {withHandlers} from "../MessageHandlers/HandlerContext";
import Fonts from "../Primitives/Fonts";
import {Link} from "react-router-dom";

const mapStateToProps = state => {
    return {
        username: state.profile.username,
        userId: state.firebaseAuthData.user.uid,
        dust: state.profile.dust,
        coins: state.profile.coins,
        dailyWins: state.profile.dailyWins
    };
};

class Profile extends Component {
    render() {
        const {username, userId, dust, coins, dailyWins} = this.props;
        return (
            <div>
                <Center>
                    <Fonts/>
                    <Paper>
                        <Center>
                            <Typography
                                variant="h3"
                                style={{fontFamily: "Cinzel, serif"}}
                            >
                                {username + "'s Profile"}
                            </Typography>
                        </Center>
                        <Center>
                            <Typography>{"ID: " + userId}</Typography>
                        </Center>
                        <Center>
                            <Typography>{"Dust: " + dust}</Typography>
                        </Center>
                        <Center>
                            <Typography>{"Coins: " + coins}</Typography>
                        </Center>
                        <Center>
                            <Typography>{"Daily Wins: " + dailyWins}</Typography>
                        </Center>
                        <Grid container spacing={8}>
                            <Grid item xs>
                                <Link to={"/profile/playgame"}>
                                    <Button text="Play"/>
                                </Link>
                            </Grid>
                            <Grid item xs>
                                <Link to={"/profile/decks"}>
                                    <Button text="Decks"/>
                                </Link>
                            </Grid>
                            <Grid item xs>
                                <Link to={"/profile/packs"}>
                                    <Button text="Packs"/>
                                </Link>
                            </Grid>
                            <Grid item xs>
                                <Link to={"/profile/collection"}>
                                    <Button text="Collection"/>
                                </Link>
                            </Grid>
                            <Grid item xs>
                                <Link to={"/profile/store"}>
                                    <Button text="Store"/>
                                </Link>
                            </Grid>
                        </Grid>
                    </Paper>
                </Center>
            </div>
        );
    }
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(withHandlers(Profile));

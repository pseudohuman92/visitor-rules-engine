import React, {Component} from "react";
import {Route, Switch} from "react-router-dom";
import MainPage from "./MainPage";
import SignIn from "../Auth/SignIn";
import SignUp from "../Auth/SignUp";
import CardGallery from "./CardGallery";
import ResetPassword from "../Auth/PasswordReset";
import AuthRequired from "../Primitives/AuthRequired";
import Profile from "./Profile";
import Decks from "./Decks";
import CollectionScreen from "./CollectionScreen";
import DeckSelection from "./DeckSelection";
import GameStore from "./GameStore";
import OpenPacks from "./OpenPacks";
import GameScreen from "./GameScreen";
import DraftScreen from "./DraftScreen";
import DeckBuilder from "./DeckBuilder";
import PlayScreen from "./PlayScreen";

export default class SwitchPage extends Component {
    render() {
        return (
            <div className="switch-page"
                 style={{
                width: "100%",
                height: "100%",
            }}>
                <Switch>
                    <Route exact path="/">
                        <MainPage/>
                    </Route>
                    <Route path="/signin">
                        <SignIn/>
                    </Route>
                    <Route path="/signup">
                        <SignUp/>
                    </Route>
                    <Route path="/cardgallery">
                        <CardGallery/>
                    </Route>
                    <Route path={"/resetpassword"}>
                        <ResetPassword/>
                    </Route>
                    <AuthRequired>
                        <Route exact path={"/profile"}>
                            <Profile/>
                        </Route>
                        <Route path={"/profile/decks"}>
                            <Decks/>
                        </Route>
                        <Route path={"/profile/collection"}>
                            <CollectionScreen/>
                        </Route>
                        <Route path={"/profile/store"}>
                            <GameStore/>
                        </Route>
                        <Route path={"/profile/packs"}>
                            <OpenPacks/>
                        </Route>
                        <Route exact path={"/profile/play"}>
                            <PlayScreen />
                        </Route>
                        <Route path={"/profile/play/deck_selection"}>
                            <DeckSelection/>
                        </Route>
                        <Route path={"/profile/play/game"}>
                            <GameScreen/>
                        </Route>
                        <Route exact path={"/profile/play/draft"}>
                            <DraftScreen/>
                        </Route>
                        <Route path={"/profile/play/draft/deck_builder"}>
                            <DeckBuilder forDraft/>
                        </Route>

                    </AuthRequired>
                </Switch>
            </div>);
    }
}
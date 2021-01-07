import React from "react";
import ReactDOM from "react-dom";
import {connect, Provider as ReduxProvider} from "react-redux";
import MainPage from "./Components/MainScreens/MainPage";
import Firebase, {FirebaseContext} from "./Components/Firebase";
import store, {mapDispatchToProps} from "./Components/Redux/Store";
import HandlerContext from "./Components/MessageHandlers/HandlerContext";
import {CookiesProvider} from 'react-cookie';
import {BrowserRouter, Route, Switch} from "react-router-dom";
import SignIn from "./Components/Auth/SignIn";
import SignUp from "./Components/Auth/SignUp";
import CardGallery from "./Components/MainScreens/CardGallery";
import ResetPassword from "./Components/Auth/PasswordReset";
import Profile from "./Components/MainScreens/Profile";
import Decks from "./Components/MainScreens/Decks";
import CollectionScreen from "./Components/MainScreens/CollectionScreen";
import DeckSelection from "./Components/MainScreens/DeckSelection";
import GameStore from "./Components/MainScreens/GameStore";
import OpenPacks from "./Components/MainScreens/OpenPacks";
import DraftScreen from "./Components/MainScreens/DraftScreen";
import GameScreen from "./Components/MainScreens/GameScreen";
import AuthRequired from "./Components/Primitives/AuthRequired";
import SwitchPage from "./Components/MainScreens/SwitchPage";
import "./css/Normalize.css";


class App extends React.Component {
    constructor(props) {
        super(props);

        this.updateHandlers = (handlers) => {
            this.setState(handlers);
        };

        this.state = {
            serverHandler: null,
            gameHandler: null,
            updateHandlers: this.updateHandlers,
        };
    }

    componentDidMount = () => {
        this.updateWindowDimensions();
        window.addEventListener("resize", this.updateWindowDimensions);
    };

    componentWillUnmount = () => {
        window.removeEventListener("resize", this.updateWindowDimensions);
    };

    updateWindowDimensions = () => {
        this.props.updateState({
            windowDimensions: {
                width: window.innerWidth,
                height: window.innerHeight,
            },
        });
    };

    render() {
        return (
            <HandlerContext.Provider value={this.state}>
                <BrowserRouter>
                    <SwitchPage/>
                </BrowserRouter>
            </HandlerContext.Provider>
        );
    }
}

App = connect(null, mapDispatchToProps)(App);

const rootElement = document.getElementById("root");
ReactDOM.render(
    <CookiesProvider>
        <ReduxProvider store={store}>
            <FirebaseContext.Provider value={new Firebase()}>
                <App/>
            </FirebaseContext.Provider>
        </ReduxProvider>
    </CookiesProvider>

    ,
    rootElement
);

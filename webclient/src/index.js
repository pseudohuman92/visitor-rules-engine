import React from "react";
import ReactDOM from "react-dom";
import {connect, Provider as ReduxProvider} from "react-redux";
import Firebase, {FirebaseContext} from "./Components/Firebase";
import store, {mapDispatchToProps} from "./Components/Redux/Store";
import HandlerContext from "./Components/MessageHandlers/HandlerContext";
import {CookiesProvider} from 'react-cookie';
import {BrowserRouter} from "react-router-dom";
import SwitchPage from "./Components/MainScreens/SwitchPage";
import {colorPalette} from "./Components/Helpers/Constants";

import "./css/App.css";
import "./css/Normalize.css";
import "./fonts/Fonts.css";


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
                <div className="background"
                     style={{ backgroundImage: "url("+process.env.PUBLIC_URL + "/img/grunge-background-black.png)" }}>
                    <div className="inner-area">
                        <App/>
                    </div>
                </div>
            </FirebaseContext.Provider>
        </ReduxProvider>
    </CookiesProvider>

    ,
    rootElement
);

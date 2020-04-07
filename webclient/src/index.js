import React from "react";
import ReactDOM from "react-dom";
import { Provider as ReduxProvider, connect } from "react-redux";
import MainPage from "./Components/MainScreens/MainPage";
import Firebase, { FirebaseContext } from "./Components/Firebase";
import store, { mapDispatchToProps } from "./Components/Redux/Store";
import HandlerContext from "./Components/MessageHandlers/HandlerContext";
import ErrorBoundary from "./Components/Primitives/ErrorBoundary";

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
        <MainPage />
      </HandlerContext.Provider>
    );
  }
}

App = connect(null, mapDispatchToProps)(App);

const rootElement = document.getElementById("root");
ReactDOM.render(
  <ErrorBoundary>
    <ReduxProvider store={store}>
      <FirebaseContext.Provider value={new Firebase()}>
        <App />
      </FirebaseContext.Provider>
    </ReduxProvider>
  </ErrorBoundary>,
  rootElement
);

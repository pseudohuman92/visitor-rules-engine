import React from "react";
import ReactDOM from "react-dom";
import { Provider as ReduxProvider } from "react-redux";

import MainPage from "./MainPage";
import Firebase, { FirebaseContext } from "./Components/Firebase";
import store from "./Components/Redux/Store";
import HandlerContext from "./Components/MessageHandlers/HandlerContext";

class App extends React.Component {
  constructor(props) {
    super(props);

    this.updateHandlers = handlers => {
      this.setState(handlers);
    }

    this.state = {
      serverHandler: null,
      gameHandler : null,
      updateHandlers: this.updateHandlers,
    };
  }

  render() {
    return (
      <HandlerContext.Provider value={this.state}>
        <MainPage />
      </HandlerContext.Provider>
    );
  }
}

const rootElement = document.getElementById("root");
ReactDOM.render(
  <ReduxProvider store={store}>
    <FirebaseContext.Provider value={new Firebase()}>
      <App/>
    </FirebaseContext.Provider>
  </ReduxProvider>,
  rootElement
);

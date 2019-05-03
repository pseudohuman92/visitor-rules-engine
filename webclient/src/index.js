import React from "react";
import ReactDOM from "react-dom";
import MainPage from "./MainPage";
import Firebase, { FirebaseContext } from "./Components/Firebase";
import { Provider as ReduxProvider } from "react-redux";
import store from "./Redux/Store";

const rootElement = document.getElementById("root");
ReactDOM.render(
  <ReduxProvider store={store}>
    <FirebaseContext.Provider value={new Firebase()}>
      <MainPage />
    </FirebaseContext.Provider>
  </ReduxProvider>,
  rootElement
);

import React from "react";
import ReactDOM from "react-dom";
import MainPage from "./MainPage"
import Firebase, { FirebaseContext } from "./Components/Firebase";

const rootElement = document.getElementById("root");
ReactDOM.render(
  <FirebaseContext.Provider value={new Firebase()}>
    <MainPage />
  </FirebaseContext.Provider>,
  rootElement
);

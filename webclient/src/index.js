import React, { Component } from "react";
import ReactDOM from "react-dom";
import SiteTabs from "./SiteTabs";
import Firebase, { FirebaseContext } from "./Components/Firebase";

const rootElement = document.getElementById("root");
ReactDOM.render(
  <FirebaseContext.Provider value={new Firebase()}>
    <SiteTabs />
  </FirebaseContext.Provider>,
  rootElement
);

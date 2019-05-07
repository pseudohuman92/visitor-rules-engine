import React from "react";

const HandlerContext = React.createContext(null);

export const withHandlers = Component => props => (
  <HandlerContext.Consumer>
    {({serverHandler, gameHandler, updateHandlers}) => (
      <Component
        {...props}
        serverHandler={serverHandler}
        gameHandler={gameHandler}
        updateHandlers={updateHandlers}
      />
    )}
  </HandlerContext.Consumer>
);

export default HandlerContext;

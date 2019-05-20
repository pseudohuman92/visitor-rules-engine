import React from "react";
import { Component } from "react";
import { connect } from "react-redux";
import { withSize } from 'react-sizeme';
import { Tooltip, Grid } from "@material-ui/core";

import "../../css/Board.css";
import "../../css/Utils.css";

const mapStateToProps = state => {
  return {
    opponentHandSize: state.extendedGameState.game.opponent.handSize
  };
};

class OpponentHand extends Component {
  render() {
    const{opponentHandSize} = this.props;
    return (
      <Tooltip title={opponentHandSize} placement="bottom">
        {/*
        <Fanner angle={-15} elevation={size.height} width={size.width} style={{width: "100%"}}>
        {Array(opponentHandSize).fill(null).map((card, i) => (
            <img
            key={i} 
            src={[process.env.PUBLIC_URL + "/img/CardBack.png"]}
            style={{
              width: Math.min((size.width / opponentHandSize), (size.width/5)),
              height: size.height,
              maxWidth:"100%",
              maxHeight:"100%", transform: "rotate(180deg)"
               }}
            alt=""
          />
        ))}
        </Fanner>
              */}
      <Grid container justify="center">
        {Array(opponentHandSize).fill(null).map((card, i) => (
          <Grid item key={i} xs={1}>
            <img
            src={[process.env.PUBLIC_URL + "/img/CardBack.png"]}
            style={{
              maxWidth: "100%",
              maxHeight: "100%",
              transform: "rotate(180deg)" }}
            alt=""
          />
          </Grid>
        ))}
      </Grid>
      </Tooltip>
    );
  }
}

export default connect(mapStateToProps)(withSize()(OpponentHand));

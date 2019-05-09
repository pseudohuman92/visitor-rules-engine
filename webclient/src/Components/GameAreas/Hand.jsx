import React from "react";
import { Component } from "react";
import GridList from "@material-ui/core/GridList";
import GridListTile from "@material-ui/core/GridListTile";
import { connect } from "react-redux";
import { withSize } from 'react-sizeme';

import PlayingCard from "../Card/PlayingCard";
import "../../css/Board.css";
import "../../css/Utils.css";
import Fanner from "../Fanner/Fanner";
import { Paper } from "@material-ui/core";

const mapStateToProps = state => {
  return {
    hand: state.extendedGameState.game.player.hand
  };
};

class Hand extends Component {
  render() {
    const{hand, size} = this.props;
    return (
      <Fanner angle={15} elevation={size.height} width={size.width} style={{width: "100%"}}>
        {hand.map(card => (
            <PlayingCard key={card.id} {...card} style={{width: (size.width / hand.length)}}/>
            //<Paper key={card.id} style={{backgroundColor: "white", width: (size.width / hand.length), height: "250px"}}/>
        ))}
      </Fanner>
    );
  }
}

export default connect(mapStateToProps)(withSize()(Hand));

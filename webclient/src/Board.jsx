import React from "react";

import { DropTarget } from "react-dnd";

import Grid from "@material-ui/core/Grid";
import GridList from "@material-ui/core/GridList";
import GridListTile from "@material-ui/core/GridListTile";
import Paper from "@material-ui/core/Paper";

import PlayingCard from "./PlayingCard.jsx";
import Hand from "./Hand";

import { ItemTypes, FieldIDs } from "./Constants.js";
import "./css/Board.css";
import "./css/Utils.css";

// From now on field is an alias for board side.
const fieldTarget = {
  drop(props, monitor) {
    if (monitor.didDrop()) {
      return;
    }

    return { targetType: ItemTypes.FIELD, id: props.id };
  },

  canDrop(props, monitor) {
    const item = monitor.getItem();
    return (
      item.sourceType === ItemTypes.CARD &&
      item.playable &&
      props.id !== FieldIDs.GARY_FIELD
    );
  }
};

class BoardSide extends React.Component {
  render() {
    const {
      cards,
      isOverCurrent,
      canDrop,
      connectDropTarget,
      activatableCards,
      selectableCards,
      selectedCards,
      targets,
      updateTargets
    } = this.props;
    var style = {};
    if (canDrop && isOverCurrent) {
      style.border = "5px red solid";
    }

    return connectDropTarget(
      <div style={{ height: "100%" }}>
        <GridList
          cols={6.25}
          className="board-side"
          style={{ flexWrap: "nowrap", ...style }}
          cellHeight="auto"
        >
          {cards.map(card => (
            <GridListTile
              key={card.id}
              style={{ maxWidth: "100%", maxHeight: "100%" }}
            >
              <PlayingCard
                playable={false}
                activatable={activatableCards.includes(card.id)}
                selectable={selectableCards.includes(card.id)}
                selected={selectedCards.includes(card.id)}
                studyable={false}
                targeted={targets.includes(card.id)}
                updateTargets={updateTargets}
                {...card}
              />
            </GridListTile>
          ))}
        </GridList>
      </div>
    );
  }
}

BoardSide = DropTarget(ItemTypes.CARD, fieldTarget, (connect, monitor) => ({
  connectDropTarget: connect.dropTarget(),
  isOver: monitor.isOver(),
  isOverCurrent: monitor.isOver({ shallow: true }),
  canDrop: monitor.canDrop()
}))(BoardSide);

export default class Board extends React.Component {
  //constructor(props) {
  //  super(props);
  //  this.state = {
  //    myCards: props.myCards,
  //    garyCards: props.garyCards,
  //    hand: props.hand,
  //  };
  //}

  render() {
    const myPlayCards = this.props.game.player.play;
    const myHandCards = this.props.game.player.hand;
    const garyPlayCards = this.props.game.opponent.play;
    let activatableCards = this.props.game.canActivate;
    let playableCards = this.props.game.canPlay;
    let studyableCards = this.props.game.canStudy;
    const selectableCards = this.props.selectableCards;
    const selectedCards = this.props.selectedCards;
    const instMessage = this.props.instMessage;
    const { targets, updateTargets } = this.props;
    const otherActive =
      this.props.game.opponent.name === this.props.game.activePlayer;

    if (selectableCards.length > 0 || otherActive) {
      activatableCards = [];
      playableCards = [];
      studyableCards = [];
    }

    return (
      <Grid
        container
        spacing={0}
        style={{
          margin: "-12px 0px",
          height: "100%"
        }}
      >
        <Grid item xs={12} style={{ height: "35%" }}>
          <BoardSide
            id={FieldIDs.GARY_FIELD}
            cards={garyPlayCards}
            selectableCards={selectableCards}
            selectedCards={selectedCards}
            activatableCards={[]}
            targets={targets}
            updateTargets={updateTargets}
          />
        </Grid>
        <Grid item xs={12} style={{ height: "35%" }}>
          <BoardSide
            id={FieldIDs.MY_FIELD}
            cards={myPlayCards}
            selectableCards={selectableCards}
            selectedCards={selectedCards}
            activatableCards={activatableCards}
            targets={targets}
            updateTargets={updateTargets}
          />
        </Grid>
        <Grid item xs={12} style={{ height: "5%" }}>
          <Paper
            style={{
              height: "100%",
              width: "100%",
              background: "white",
              color: "black"
            }}
          >
            {instMessage}
          </Paper>
        </Grid>
        <Grid item xs={12} style={{ height: "25%" }}>
          <Hand
            cards={myHandCards}
            selectableCards={selectableCards}
            selectedCards={selectedCards}
            playableCards={playableCards}
            studyableCards={studyableCards}
          />
        </Grid>
      </Grid>
    );
  }
}

import React, { Component } from "react";
import Button from "../Primitives/Button";
import { connect } from "react-redux";

import proto from "../../protojs/compiled.js";

import { ClientPhase } from "../Helpers/Constants";
import { IsSelectCardPhase } from "../Helpers/Helpers";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import "../../css/Utils.css";

const mapStateToProps = state => {
  return {
    gamePhase: state.extendedGameState.game.phase,
    playerName: state.username,
    playerUserId: state.extendedGameState.game.player.id,
    opponentName: state.extendedGameState.opponentUsername,
    activePlayer: state.extendedGameState.game.activePlayer,
    turnPlayer: state.extendedGameState.game.turnPlayer,
    clientPhase: state.extendedGameState.clientPhase,
    upTo: state.extendedGameState.selectionData.upTo,
    autoPass: state.extendedGameState.autoPass,
    selected: state.extendedGameState.selectionData.selected,
    attackerAssignments: state.extendedGameState.attackerAssignmentData.attackerAssignments,
    blockerAssignments: state.extendedGameState.blockerAssignmentData.blockerAssignments,
    damageAssignmentData: state.extendedGameState.damageAssignmentData,
  };
};

class ButtonDisplay extends Component {
  redraw = event => {
    this.props.gameHandler.Redraw();
  };

  keep = event => {
    this.props.gameHandler.Keep();
  };

  selectDone = event => {
    let selected = [...this.props.selected];
    let clientPhase = this.props.clientPhase;
    this.props.gameHandler.SelectDone(clientPhase, selected);
  };

  pass = event => {
    this.props.gameHandler.Pass();
  };

  selectAttackers = event => {
    let attackerAssignments = [...this.props.attackerAssignments];
    this.props.gameHandler.SelectAttackers(attackerAssignments);
  };

  selectBlockers = event => {
    let blockerAssignments = [...this.props.blockerAssignments];
    this.props.gameHandler.SelectBlockers(blockerAssignments);
  };

  assignDamage = event => {
    let damageAssignments = [...this.props.damageAssignmentData.damageAssignments];
    this.props.gameHandler.AssignDamage(damageAssignments);
  };

  render() {
    const {
      clientPhase,
      gamePhase,
      playerName,
      playerUserId,
      activePlayer,
      turnPlayer,
      upTo,
      autoPass,
      opponentName,
      damageAssignmentData
    } = this.props;

    const amIActive = activePlayer === playerUserId;
    const actPlayer = amIActive ? playerName : opponentName;
    const turPlayer = turnPlayer === playerUserId ? playerName : opponentName;

    const canAssignDamage = damageAssignmentData.totalDamage !== damageAssignmentData.totalAssignedDamage;

    let buttonMenu = <div />;
    if (amIActive &&
        (clientPhase === ClientPhase.NOT_STARTED ||
      gamePhase === proto.Phase.REDRAW)
    ) {
      buttonMenu = (
        <div style={{display: "flex", flexDirection: "column"}}>
            <Button style={{margin:"4px"}} onClick={this.keep} disabled={!amIActive} text="Keep" />
            <Button style={{margin:"4px"}} onClick={this.redraw} disabled={!amIActive} text="Redraw" />
        </div>
      );
    } else if (IsSelectCardPhase(clientPhase)) {
      buttonMenu = (
        <Button style={{margin:"2px"}} disabled={!upTo} onClick={this.selectDone} text="Done" />
      );
    } else if (clientPhase === ClientPhase.SELECT_ATTACKERS) {
      buttonMenu = (turnPlayer === playerUserId ) ?
        <Button style={{margin:"2px"}} onClick={this.selectAttackers} text="Attack" /> : <div/>
        ;
    } else if (clientPhase === ClientPhase.SELECT_BLOCKERS ) {
      buttonMenu = (turnPlayer !== playerUserId) ?
        <Button style={{margin:"2px"}} onClick={this.selectBlockers} text="Block" /> : <div/>;
    } else if (clientPhase === ClientPhase.ASSIGN_DAMAGE) {
      buttonMenu = (turnPlayer === playerUserId) ?
        <Button style={{margin:"2px"}} disabled={canAssignDamage} onClick={this.assignDamage} text="Assign" /> : <div/>;
    } else {
      buttonMenu = (
        <Button
          style={{margin:"2px"}}
          disabled={!amIActive || autoPass || clientPhase === ClientPhase.WAITING}
          onClick={this.pass}
          text="Pass"
        />
      );
    }

    return (<div style={this.props.style}>{buttonMenu}</div>);
  }
}

export default connect(mapStateToProps)(withHandlers(ButtonDisplay));

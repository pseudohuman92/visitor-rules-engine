import React from 'react';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import Button from '@material-ui/core/Button';

import proto from './protojs/compiled.js';

import {GamePhases, Concede, Mulligan, Keep, Pass} from './Game.js';
import './StateDisplay.css';
import './Utils.css';

export class PlayerDisplay extends React.Component {
  render() {
    const {
      name,
      energy,
      maxEnergy,
      knowledgePool,
      deckSize,
      scrapyard,
    } = this.props.player;
    const void_ = this.props.player.void;
    const updateDialog = this.props.updateDialog;
    let handSize;
    if (this.props.player.hasOwnProperty('hand')) {
      handSize = this.props.player.hand.length;
    } else {
      handSize = this.props.player.handSize;
    }

    const scrapyardOnClick = event => {
      updateDialog(true, `${name}'s Scrapyard`, scrapyard);
    };

    const voidOnClick = event => {
      updateDialog(true, `${name}'s Void`, void_);
    };

    return (
      <Paper className="player-display">
        <Grid container spacing={0} style={{height: '100%'}}>
          <Grid item xs={12} className="grid-elem" style={{height: '20%'}}>
            {name}
          </Grid>
          <Grid item xs={12} className="grid-elem" style={{height: '20%'}}>
            {energy + '/' + maxEnergy}
          </Grid>
          <Grid item xs={12} className="grid-elem" style={{height: '20%'}}>
            <Grid container spacing={16} justify="space-evenly">
              {knowledgePool.map(knowledge => (
                <Grid item xs={2} key={knowledge.knowledge}>
                  {knowledge.count}
                </Grid>
              ))}
            </Grid>
          </Grid>
          <Grid item xs={6} className="grid-elem" style={{height: '20%'}}>
            {deckSize}
          </Grid>
          <Grid item xs={6} className="grid-elem" style={{height: '20%'}}>
            {handSize}
          </Grid>
          <Grid
            item
            xs={6}
            className="grid-elem"
            onClick={scrapyardOnClick}
            style={{height: '20%'}}>
            {scrapyard.length}
          </Grid>
          <Grid
            item
            xs={6}
            className="grid-elem"
            onClick={voidOnClick}
            style={{height: '20%'}}>
            {void_.length}
          </Grid>
        </Grid>
      </Paper>
    );
  }
}

export class MessageDisplay extends React.Component {
  render() {
    const phase = this.props.phase;
    const gamePhase = this.props.game.phase;
    const gamePhaseStr = {
      0: 'NOPHASE',
      1: 'MULLIGAN',
      2: 'BEGIN',
      3: 'MAIN',
      4: 'MAIN_RESOLVING',
      5: 'END',
    }[gamePhase];
    const {activePlayer, turnPlayer} = this.props.game;

    const amActive =
      this.props.game.activePlayer === this.props.game.player.name;

    const activeDisplay = (
      <Grid container spacing={0} style={{height: '100%'}}>
        <Grid item xs={12} style={{height: '33%'}}>
          {`Phase: ${gamePhaseStr}`}
        </Grid>
        <Grid item xs={12} style={{height: '33%'}}>
          {`Turn: ${turnPlayer}`}
        </Grid>
        <Grid item xs={12} style={{height: '33%'}}>
          {`Active: ${activePlayer}`}
        </Grid>
      </Grid>
    );

    let buttonMenu;
    if (
      phase === GamePhases.NOT_STARTED ||
      gamePhase === proto.Phase.MULLIGAN
    ) {
      buttonMenu = (
        <Grid container spacing={0} style={{height: '100%'}}>
          <Grid item xs={12} style={{height: '50%'}}>
            <Button
              color="secondary"
              variant="contained"
              onClick={Mulligan}
              disabled={!amActive}>
              Mulligan
            </Button>
          </Grid>
          <Grid item xs={12} style={{height: '50%'}}>
            <Button
              color="primary"
              variant="contained"
              onClick={Keep}
              disabled={!amActive}>
              Keep
            </Button>
          </Grid>
        </Grid>
      );
    } else {
      buttonMenu = (
        <Grid container spacing={0} style={{height: '100%'}}>
          <Grid item xs={12} style={{height: '50%'}}>
            {/* <Button color="secondary" variant="contained" onClick={Concede}>
              Concede
            </Button> */}
          </Grid>
          <Grid item xs={12} style={{height: '50%'}}>
            <Button
              color="primary"
              variant="contained"
              disabled={!amActive}
              onClick={Pass}>
              Pass
            </Button>
          </Grid>
        </Grid>
      );
    }

    return (
      <Paper className="message-display">
        <Grid container spacing={0} style={{height: '100%', color: 'black'}}>
          <Grid item xs={12} style={{height: '60%'}}>
            {activeDisplay}
          </Grid>
          <Grid item xs={12} style={{height: '40%'}}>
            {buttonMenu}
          </Grid>
        </Grid>
      </Paper>
    );
  }
}

export default class StateDisplay extends React.Component {
  render() {
    const gary = this.props.game.opponent;
    const me = this.props.game.player;
    const phase = this.props.phase;
    const updateDialog = this.props.updateDialog;

    return (
      <Grid
        container
        spacing={24}
        style={{
          padding: 0,
        }}
        className="state-display"
        direction="column">
        <Grid item xs={4} className="grid-col-item no-max-width">
          <PlayerDisplay player={gary} updateDialog={updateDialog} />
        </Grid>
        <Grid item xs={4} className="grid-col-item no-max-width">
          <MessageDisplay game={this.props.game} phase={phase} />
        </Grid>
        <Grid item xs={4} className="grid-col-item no-max-width">
          <PlayerDisplay player={me} updateDialog={updateDialog} />
        </Grid>
      </Grid>
    );
  }
}

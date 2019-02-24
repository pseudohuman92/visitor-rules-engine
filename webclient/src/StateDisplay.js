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
        <Grid container spacing={0}>
          <Grid item xs={12} className="grid-elem">
            {name}
          </Grid>
          <Grid item xs={12} className="grid-elem">
            {energy + '/' + maxEnergy}
          </Grid>
          <Grid item xs={12} className="grid-elem">
            <Grid container spacing={16} justify="space-evenly">
              {knowledgePool.map(knowledge => (
                <Grid item xs={2} key={knowledge.knowledge}>
                  {knowledge.count}
                </Grid>
              ))}
            </Grid>
          </Grid>
          <Grid item xs={6} className="grid-elem">
            {deckSize}
          </Grid>
          <Grid item xs={6} className="grid-elem">
            {handSize}
          </Grid>
          <Grid item xs={6} className="grid-elem" onClick={scrapyardOnClick}>
            {scrapyard.length}
          </Grid>
          <Grid item xs={6} className="grid-elem" onClick={voidOnClick}>
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
      <Grid container spacing={0} direction="column" style={{height: '100%'}}>
        <Grid item xs={4} className="grid-col-item no-max-width">
          {`Phase: ${gamePhaseStr}`}
        </Grid>
        <Grid item xs={4} className="grid-col-item no-max-width">
          {`Turn: ${turnPlayer}`}
        </Grid>
        <Grid item xs={4} className="grid-col-item no-max-width">
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
        <Grid container spacing={0} direction="column" style={{height: '100%'}}>
          <Grid item xs={6} className="grid-col-item no-max-width">
            <Button
              color="secondary"
              variant="contained"
              onClick={Mulligan}
              disabled={!amActive}>
              Mulligan
            </Button>
          </Grid>
          <Grid item xs={6} className="grid-col-item no-max-width">
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
        <Grid container spacing={0} direction="column" style={{height: '100%'}}>
          <Grid item xs={6} className="grid-col-item no-max-width">
            {/* <Button color="secondary" variant="contained" onClick={Concede}>
              Concede
            </Button> */}
          </Grid>
          <Grid item xs={6} className="grid-col-item no-max-width">
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
        <Grid container spacing={0} direction="column" style={{height: '100%'}}>
          <Grid item xs={6} className="grid-col-item no-max-width">
            {activeDisplay}
          </Grid>
          <Grid item xs={6} className="grid-col-item no-max-width">
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

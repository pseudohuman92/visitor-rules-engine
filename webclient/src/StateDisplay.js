import React from 'react';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import Button from '@material-ui/core/Button';
import Fittext  from '@kennethormandy/react-fittext';
import Textfit  from 'react-textfit';

import proto from './protojs/compiled.js';

import {
  GamePhases,
  IsSelectCardPhase,
  Concede,
  Mulligan,
  Keep,
  Pass,
  SelectDone,
  SelectPlayer,
} from './Game.js';
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
    const {updateDialog, selectPlayer} = this.props;
    let handSize;
    if (this.props.player.hasOwnProperty('hand')) {
      handSize = this.props.player.hand.length;
    } else {
      handSize = this.props.player.handSize;
    }

    const knowledgeMap = {};
    knowledgeMap[proto.Knowledge.BLACK] = 'B';
    knowledgeMap[proto.Knowledge.GREEN] = 'G';
    knowledgeMap[proto.Knowledge.RED] = 'R';
    knowledgeMap[proto.Knowledge.BLUE] = 'U';
    knowledgeMap[proto.Knowledge.YELLOW] = 'Y';
    
    function knowledgeString (knowledgeCost) {
        var str = "";
        
        for (var i = 0; i < knowledgeCost.length; i++) {                     
            str = str + " "
                      + knowledgeMap[knowledgeCost[i].knowledge] 
                      + ": " 
                      + knowledgeCost[i].count
                      + " ";
        }
        return str;
    };


    let scrapyardOnClick, voidOnClick, playerOnClick;
    const style = {};
    if (selectPlayer) {
      style.border = '5px green solid';
      playerOnClick = event => {
        SelectPlayer(name);
      };
    } else {
      scrapyardOnClick = event => {
        updateDialog(true, `${name}'s Scrapyard`, scrapyard);
      };

      voidOnClick = event => {
        updateDialog(true, `${name}'s Void`, void_);
      };
    }
    
    
    return (
      <Paper className="player-display" onClick={playerOnClick} style={style}>
        <Grid container spacing={0} style={{height: '100%'}}>
          <Grid item xs={12} className="grid-elem" style={{height: '20%'}}>
            <Textfit mode="single" forceSingleModeWidth={false} style={{padding: '0 5% 0 5%'}}>
                {name}
            </Textfit>
          </Grid>
          <Grid item xs={12} className="grid-elem" style={{height: '20%'}}>
            <Textfit mode="single" forceSingleModeWidth={false} style={{padding: '0 5% 0 5%'}}>
                {energy + '/' + maxEnergy}
            </Textfit>
          </Grid>
          <Grid item xs={12} className="grid-elem" style={{height: '20%'}}>
            <Textfit mode="single" forceSingleModeWidth={false} style={{padding: '0 5% 0 5%', height: '100%'}}>
                {knowledgeString(knowledgePool)}
            </Textfit>
          </Grid>
          <Grid item xs={6} className="grid-elem" style={{height: '20%'}}>
            <Textfit mode="single" forceSingleModeWidth={false} style={{padding: '0 5% 0 5%'}}>
                Deck: {deckSize}
            </Textfit>
          </Grid>
          <Grid item xs={6} className="grid-elem" style={{height: '20%'}}>
            <Textfit mode="single" forceSingleModeWidth={false} style={{padding: '0 5% 0 5%'}}>
                Hand: {handSize}
            </Textfit>
          </Grid>
          <Grid
            item
            xs={6}
            className="grid-elem"
            onClick={scrapyardOnClick}
            style={{height: '20%'}}>
            <Textfit mode="single" forceSingleModeWidth={false} style={{padding: '0 5% 0 5%'}}>
                Scrap: {scrapyard.length}
            </Textfit>
          </Grid>
          <Grid
            item
            xs={6}
            className="grid-elem"
            onClick={voidOnClick}
            style={{height: '20%'}}>
            <Textfit mode="single" forceSingleModeWidth={false} style={{padding: '0 5% 0 5%'}}>
                Void: {void_.length}
            </Textfit>
          </Grid>
        </Grid>
      </Paper>
    );
  }
}

export class MessageDisplay extends React.Component {
  render() {
    const {phase, upTo} = this.props;
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
            <Textfit mode="single" forceSingleModeWidth={false} style={{margin: '5%'}}>
                Phase: {gamePhaseStr}
            </Textfit>
        </Grid>
        <Grid item xs={12} style={{height: '33%'}}>
            <Textfit mode="single" forceSingleModeWidth={false} style={{margin: '5%'}}>
                Turn: {turnPlayer}
            </Textfit>
        </Grid>
        <Grid item xs={12} style={{height: '33%'}}>
            <Textfit mode="single" forceSingleModeWidth={false} style={{margin: '5%'}}>
                Active: {activePlayer}
            </Textfit>
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
    } else if (IsSelectCardPhase(phase)) {
      buttonMenu = (
        <Grid container spacing={0} style={{height: '100%'}}>
          <Grid item xs={12} style={{height: '50%'}}>
            /*{ <Button color="secondary" variant="contained" onClick={Concede}>
              Concede
            </Button> }*/
          </Grid>
          <Grid item xs={12} style={{height: '50%'}}>
            <Button
              color="primary"
              variant="contained"
              disabled={!upTo}
              onClick={SelectDone}>
              Done
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
    const {phase, updateDialog, upTo, game} = this.props;
    const selectPlayer = phase === GamePhases.SELECT_PLAYER;

    return (
      <Grid
        container
        spacing={8}
        style={{
          padding: 0,
        }}
        className="state-display"
        direction="column">
        <Grid item xs className="grid-col-item no-max-width" style={{height: '33%'}}>
          <PlayerDisplay
            player={gary}
            updateDialog={updateDialog}
            selectPlayer={selectPlayer}
          />
        </Grid>
        <Grid item xs className="grid-col-item no-max-width" style={{height: '33%'}}>
          <MessageDisplay game={game} phase={phase} upTo={upTo} />
        </Grid>
        <Grid item xs className="grid-col-item no-max-width" style={{height: '33%'}}>
          <PlayerDisplay
            player={me}
            updateDialog={updateDialog}
            selectPlayer={selectPlayer}
          />
        </Grid>
      </Grid>
    );
  }
}

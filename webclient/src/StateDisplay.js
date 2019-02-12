import React from 'react';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import './StateDisplay.css';
import './Utils.css';

export class PlayerDisplay extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      player: props.player,
    };
  }

  render() {
    return (
      <Paper className="player-display">
        <Grid container spacing={0}>
          <Grid item xs={12} className="grid-elem">
            {this.state.player.name}
          </Grid>
          <Grid item xs={12} className="grid-elem">
            {this.state.player.energy + '/' + this.state.player.maxEnergy}
          </Grid>
          <Grid item xs={12} className="grid-elem">
            <Grid container spacing={16} justify="space-evenly">
              {this.state.player.knowledgePool.map((knowledge, idx) => (
                <Grid item xs={2} key={idx}>
                  {knowledge.count}
                </Grid>
              ))}
            </Grid>
          </Grid>
          <Grid item xs={6} className="grid-elem">
            {this.state.player.deckSize}
          </Grid>
          <Grid item xs={6} className="grid-elem">
            {this.state.player.hand.length}
          </Grid>
          <Grid item xs={6} className="grid-elem">
            {this.state.player.scrapyard.length}
          </Grid>
          <Grid item xs={6} className="grid-elem">
            {this.state.player.void.length}
          </Grid>
        </Grid>
      </Paper>
    );
  }
}

export class MessageDisplay extends React.Component {
  render() {
    return <Paper className="message-display">ain 't that some shit </Paper>;
  }
}

export default class StateDisplay extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      me: props.me,
      gary: props.gary,
    };
  }

  render() {
    return (
      <Grid
        container
        spacing={24}
        style={{
          padding: 0,
        }}
        className="state-display"
        direction="column">
        <Grid item xs={5} className="grid-col-item no-max-width">
          <PlayerDisplay player={this.state.gary} />
        </Grid>
        <Grid item xs={2} className="grid-col-item no-max-width">
          <MessageDisplay />
        </Grid>
        <Grid item xs={5} className="grid-col-item no-max-width">
          <PlayerDisplay player={this.state.me} />
        </Grid>
      </Grid>
    );
  }
}

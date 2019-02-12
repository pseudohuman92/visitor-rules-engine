import React from 'react';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import './StateDisplay.css';

export class PlayerDisplay extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      player: props.player,
    };
  }

  render() {
    return (
      <div className="PlayerDisplay">
        <Grid
          container
          spacing={24}
          style={{
            padding: 24,
          }}>
          <Grid item xs={12}>
            {this.state.player.name}
          </Grid>
          <Grid item xs={12}>
            {this.state.player.energy + '/' + this.state.player.maxEnergy}
          </Grid>
          <Grid item xs={12}>
            <Grid
              container
              spacing={16}
              style={{
                padding: 12,
              }}
              justify="space-evenly">
              {this.state.player.knowledgePool.map((knowledge, idx) => (
                <Grid item xs={2} key={idx}>
                  {knowledge.count}
                </Grid>
              ))}
            </Grid>
          </Grid>
          <Grid item xs={6}>
            {this.state.player.deckSize}
          </Grid>
          <Grid item xs={6}>
            {this.state.player.hand.length}
          </Grid>
          <Grid item xs={6}>
            {this.state.player.scrapyard.length}
          </Grid>
          <Grid item xs={6}>
            {this.state.player.void.length}
          </Grid>
        </Grid>
      </div>
    );
  }
}

export class MessageDisplay extends React.Component {
  render() {
    return (
      <div className="MessageDisplay">
        <Paper>ain 't that some shit </Paper>
      </div>
    );
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
      <div className="StateDisplay">
        <Grid
          container
          spacing={24}
          style={{
            padding: 24,
          }}>
          <Grid item xs={12}>
            <PlayerDisplay player={this.state.gary} />
          </Grid>
          <Grid item xs={12}>
            <MessageDisplay />
          </Grid>
          <Grid item xs={12}>
            <PlayerDisplay player={this.state.me} />
          </Grid>
        </Grid>
      </div>
    );
  }
}

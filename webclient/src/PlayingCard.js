import React from 'react';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import './PlayingCard.css';

export default class PlayingCard extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      id: props.id,
      name: props.name,
      description: 'Some shit',
    };
  }

  render() {
    return (
      <Card>
        <CardHeader title={this.state.name} />
        <CardContent>{this.state.description}</CardContent>
      </Card>
    );
  }
}

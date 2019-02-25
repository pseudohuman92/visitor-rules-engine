import React from 'react';

import {DragSource, DropTarget} from 'react-dnd';

import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Avatar from '@material-ui/core/Avatar';
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';
//import Popover from '@material-ui/core/Popover';
import Popper from '@material-ui/core/Popper';

import {ItemTypes, FieldIDs} from './Constants.js';
import {PlayCard, ActivateCard, SelectCard, StudyCard} from './Game.js';
import './PlayingCard.css';

import proto from './protojs/compiled.js';

const cardSource = {
  beginDrag(props) {
    return {
      sourceType: ItemTypes.CARD,
      playable: props.playable,
      studyable: props.studyable,
    };
  },

  canDrag(props) {
    return props.playable || props.studyable;
  },

  endDrag(props, monitor) {
    if (!monitor.didDrop()) {
      return;
    }

    const targetProps = monitor.getDropResult();
    if (
      props.playable &&
      (targetProps.targetType === ItemTypes.FIELD &&
        targetProps.id === FieldIDs.MY_FIELD)
    ) {
      PlayCard(props.id);
    } else if (targetProps.targetType === ItemTypes.ALTAR) {
      StudyCard(props.id);
    }
  },
};

const cardTarget = {
  drop(props, monitor) {
    if (monitor.didDrop()) {
      return;
    }

    return {targetType: ItemTypes.CARD, id: props.id};
  },
};

export class PlayingCard extends React.Component {
  state = {
    timer: null,
    anchorEl: null,
    openPopover: false,
  };

  onMouseEnter = event => {
    const that = this;
    const timer = setTimeout(function() {
      that.setState({openPopover: true});
    }, 2000);
    this.setState({timer: timer, anchorEl: event.currentTarget});
  };

  //onMouseOut = event => {
  //  if (this.state.timer) {
  //    clearTimeout(this.state.timer);
  //    this.setState({time: null});
  //  }
  //};

  onMouseLeave = event => {
    clearTimeout(this.state.timer);
    if (this.state.openPopover) {
      this.setState({openPopover: false});
    }
  };

  render() {
    const {
      id,
      name,
      description,
      depleted,
      activatable,
      playable,
      isOver,
      canDrop,
      isDragging,
      connectDragSource,
      connectDropTarget,
      selectable,
      selected,
      cost,
      knowledgeCost,
      counters,
      type,
    } = this.props;

    const {anchorEl, openPopover} = this.state;

    var opacity = 1,
      border = 'none';
    let clickHandler = undefined;
    if (isDragging) {
      opacity = 0.5;
      border = '5px yellow solid';
    } else if (canDrop && isOver) {
      border = '5px red solid';
    } else if (selected) {
      border = '5px magenta solid';
    } else if (selectable) {
      clickHandler = event => {
        SelectCard(id);
      };
      border = '5px green solid';
    } else if (activatable) {
      clickHandler = event => {
        ActivateCard(id);
      };
      border = '5px blue solid';
    } else if (playable) {
      border = '5px blue solid';
    } else if (depleted) {
      opacity = 0.7;
    }

    const knowledgeMap = {};
    knowledgeMap[proto.Knowledge.BLACK] = 'B';
    knowledgeMap[proto.Knowledge.GREEN] = 'G';
    knowledgeMap[proto.Knowledge.RED] = 'R';
    knowledgeMap[proto.Knowledge.BLUE] = 'U';
    knowledgeMap[proto.Knowledge.YELLOW] = 'Y';

    const counterMap = {};
    counterMap[proto.Counter.CHARGE] = 'Charge';

    return connectDragSource(
      <div style={{display: 'inline-block'}}>
        <Popper open={openPopover} anchorEl={anchorEl}>
          <Card className="playing-card-detail">
            <CardHeader avatar={<Avatar>{cost}</Avatar>} title={name} />
            <CardMedia
              image={process.env.PUBLIC_URL + '/img/doggy.jpg'}
              style={{
                height: 0,
                paddingTop: '56.25%', // 16:9
              }}
            />
            <CardContent>
              <Grid container spacing={16}>
                <Grid item xs={12}>
                  <TextField
                    variant="outlined"
                    InputProps={{readOnly: true}}
                    style={{width: '100%'}}
                    multiline={true}
                    label="Description"
                    value={description}
                  />
                </Grid>
                <Grid item xs={4}>
                  <TextField
                    variant="filled"
                    InputProps={{readOnly: true}}
                    value={knowledgeCost.map(
                      knowledge =>
                        `${knowledgeMap[knowledge.knowledge]}${
                          knowledge.count
                        }`,
                    )}
                  />
                </Grid>
                <Grid item xs={8}>
                  <TextField
                    variant="filled"
                    InputProps={{readOnly: true}}
                    value={counters.map(
                      c => `${counterMap[c.counter]}: ${c.count}`,
                    )}
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    variant="filled"
                    InputProps={{readOnly: true}}
                    style={{width: '100%'}}
                    value={type}
                  />
                </Grid>
              </Grid>
            </CardContent>
          </Card>
        </Popper>
        <Card
          style={{
            opacity: opacity,
            border: border,
          }}
          onClick={clickHandler}
          className="playing-card"
          onMouseEnter={this.onMouseEnter}
          onMouseLeave={this.onMouseLeave}>
          <CardHeader avatar={<Avatar>{cost}</Avatar>} title={name} />
          <CardContent>
            <Grid container spacing={8}>
              <Grid item xs={4}>
                <TextField
                  variant="filled"
                  InputProps={{readOnly: true}}
                  value={knowledgeCost.map(
                    knowledge =>
                      `${knowledgeMap[knowledge.knowledge]}${knowledge.count}`,
                  )}
                />
              </Grid>
              <Grid item xs={8}>
                <TextField
                  variant="filled"
                  InputProps={{readOnly: true}}
                  value={counters.map(
                    c => `${counterMap[c.counter]}: ${c.count}`,
                  )}
                />
              </Grid>
            </Grid>
          </CardContent>
        </Card>
      </div>,
    );
  }
}

PlayingCard = DragSource(ItemTypes.CARD, cardSource, (connect, monitor) => ({
  connectDragSource: connect.dragSource(),
  isDragging: monitor.isDragging(),
}))(PlayingCard);

PlayingCard = DropTarget(ItemTypes.CARD, cardTarget, (connect, monitor) => ({
  connectDropTarget: connect.dropTarget(),
  isOver: monitor.isOver(),
  canDrop: monitor.canDrop(),
}))(PlayingCard);

export default PlayingCard;

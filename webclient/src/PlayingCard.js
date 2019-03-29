import React from 'react';
import {DragSource, DropTarget} from 'react-dnd';
import Grid from '@material-ui/core/Grid';
import Fittext from '@kennethormandy/react-fittext';
import Rectangle from 'react-rectangle';

import {ItemTypes, FieldIDs} from './Constants.js';
import {
  PlayCard,
  ActivateCard,
  UnselectCard,
  SelectCard,
  StudyCard,
} from './Game.js';
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
  constructor(props) {
    super(props);
    this.state = {
      updateTargets: props.updateTargets,
      targets: props.targets,
    };
  }

  componentDidUpdate(prevProps) {
    // Typical usage (don't forget to compare props):
    if (this.props.targets !== prevProps.targets) {
      this.setState({targets: this.props.targets});
    }
  }

  onMouseEnter = event => {
    if (this.state.updateTargets) {
      this.state.updateTargets(this.state.targets);
    }
  };

  onMouseLeave = event => {
    if (this.state.updateTargets) {
      this.state.updateTargets([]);
    }
  };
  
  getCardColor(knowlString){
      if(knowlString.startsWith("B")){
          return "#666666";
      } else if (knowlString.startsWith("U")) {
          return "#0066ff";
      } else if (knowlString.startsWith("R")) {
          return "#ff1a1a";
      } else if (knowlString.startsWith("Y")) {
          return "#ffff00";
      } else {
          return "#e6e6e6";
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
      targeted,
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
    } else if (targeted) {
      border = '5px yellow solid';
    } else if (selected) {
      border = '5px magenta solid';
      clickHandler = event => {
        UnselectCard(id);
      };
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
      opacity = 0.5;
    }

    const knowledgeMap = {};
    knowledgeMap[proto.Knowledge.BLACK] = 'B';
    knowledgeMap[proto.Knowledge.GREEN] = 'G';
    knowledgeMap[proto.Knowledge.RED] = 'R';
    knowledgeMap[proto.Knowledge.BLUE] = 'U';
    knowledgeMap[proto.Knowledge.YELLOW] = 'Y';

    const counterMap = {};
    counterMap[proto.Counter.CHARGE] = 'C';

    function knowledgeString(knowledgeCost) {
      var str = '';

      for (var i = 0; i < knowledgeCost.length; i++) {
        for (var j = 0; j < knowledgeCost[i].count; j++) {
          str = str + knowledgeMap[knowledgeCost[i].knowledge];
        }
      }
      return str;
    }

    return connectDragSource(
      <div>
        <Rectangle aspectRatio={[25, 35]} 
          style={{
            opacity: opacity,
            border: border,
            backgroundColor: this.getCardColor(knowledgeString(knowledgeCost))
          }}
          onClick={clickHandler}
          onMouseEnter={this.onMouseEnter}
          onMouseLeave={this.onMouseLeave}>
          <Grid
            container
            className="card-grid"
            justify="flex-start"
            align-content="space-around"
            align-items="space-around">
            <Grid item xs={12} style={{padding: '5% 0 0 5%', height: '15%'}}>
              <Fittext>
                <div>
                  {cost} [{knowledgeString(knowledgeCost)}]
                </div>
              </Fittext>
            </Grid>
            <Grid item xs={12} style={{padding: '0 0 0 5%', height: '15%'}}>
              <Fittext>
                <div>{name}</div>
              </Fittext>
            </Grid>
            <Grid item xs={12} style={{padding: '0 5% 0 5%', height: '55%'}}>
              <Fittext>
                <div>{description}</div>
              </Fittext>
            </Grid>
            <Grid item xs={12} style={{padding: '0 0 0 5%', height: '15%'}}>
              <Fittext>
                <div>
                  {type} ---{' '}
                  {counters
                    .map(c => `${counterMap[c.counter]}: ${c.count}`)
                    .join()}
                </div>
              </Fittext>
            </Grid>
          </Grid>
        </Rectangle>
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

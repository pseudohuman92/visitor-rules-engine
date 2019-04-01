import React from 'react';
import {DragSource, DropTarget} from 'react-dnd';
import Grid from '@material-ui/core/Grid';
import Fittext from '@kennethormandy/react-fittext';
import Rectangle from 'react-rectangle';
import Textfit from "react-textfit";
import Image from "react-image";

import {ItemTypes, FieldIDs} from './Constants.js';
import {
  PlayCard,
  ActivateCard,
  UnselectCard,
  SelectCard,
  StudyCard,
} from './Game.js';
import './deckbuilder/Card.css';

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
  
  getCardColor(knowledgeCost){
      var knowlString = this.toKnowledgeString(knowledgeCost);
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
  
  toKnowledgeString(knowledgeCost) {
       var knowledgeMap = {};
            knowledgeMap[proto.Knowledge.BLACK] = 'B';
            knowledgeMap[proto.Knowledge.GREEN] = 'G';
            knowledgeMap[proto.Knowledge.RED] = 'R';
            knowledgeMap[proto.Knowledge.BLUE] = 'U';
            knowledgeMap[proto.Knowledge.YELLOW] = 'Y';
        var str = "";
      
      for (var i = 0; i < knowledgeCost.length; i++) {
        for (var j = 0; j < knowledgeCost[i].count; j++) {
          str = str + knowledgeMap[knowledgeCost[i].knowledge];
        }
      }
      return str;
    };
    
    getCostLine(cost, knowledge){
        var str = "";
        
        if(cost !== "-"){
           str += cost + " ";
        }
        if (!knowledge.startsWith("-")){
            str += "[" + knowledge + "] ";
        }
        if (cost !== "-" || !knowledge.startsWith("-")){
            str += "| ";
        }
        return str;
    }

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
      borderColor = "black";
    let clickHandler = undefined;
    if (isDragging) {
      opacity = 0.5;
      borderColor = "yellow";
    } else if (canDrop && isOver) {
      borderColor = "red";
    } else if (targeted) {
      borderColor = "yellow";
    } else if (selected) {
      borderColor = "magenta";
      clickHandler = event => {
        UnselectCard(id);
      };
    } else if (selectable) {
      clickHandler = event => {
        SelectCard(id);
      };
      borderColor = "green";
    } else if (activatable) {
      clickHandler = event => {
        ActivateCard(id);
      };
      borderColor = "blue";
    } else if (playable) {
      borderColor = "blue";
    } else if (depleted) {
      opacity = 0.5;
    }

    

    const counterMap = {};
    counterMap[proto.Counter.CHARGE] = 'C';
    
    const cardColor = this.getCardColor(knowledgeCost);
    const knowledge = this.toKnowledgeString(knowledgeCost);

    return connectDragSource(
    <div>
          <Rectangle aspectRatio={[63, 88]} 
                     style={{opacity: opacity,
                     borderRadius: "3px",
                     backgroundColor: borderColor,
                     overflow: "hidden"}}>
              <div className="card-inner"
                   style={{backgroundColor: cardColor}}>
                  <div className="card-name">
                      <Textfit
                          mode="single"
                          forceSingleModeWidth={false}
                          style={{maxWidth: '96%', maxHeight: '100%'}}>
                          {this.getCostLine(cost, knowledge) + name}
                      </Textfit>
                  </div>

                  <div className="card-image">
                      <Image src={[process.env.PUBLIC_URL + "/img/" + name + ".jpg",
                                                       process.env.PUBLIC_URL + "/img/" + type + ".jpg"]} 
                             style={{maxWidth: "100%"}} decode={false}/>
                  </div>

                  <div className="card-type">
                      <Textfit
                          mode="single"
                          forceSingleModeWidth={false}
                          style={{maxHeight: '100%'}}>
                          {" " + type}
                      </Textfit>
                  </div>

                  <div className="card-description">
                      <Textfit style={{maxHeight: '100%'}}>
                        {description}
                        {counters.toString() !== "" &&
                        ("\n-----\n" + counters.map(c => `${counterMap[c.counter]}: ${c.count}`).join())}
                      </Textfit>
                  </div>
              </div>
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

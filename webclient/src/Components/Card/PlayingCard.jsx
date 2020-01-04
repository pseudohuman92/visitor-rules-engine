import React from "react";
import { DragSource, DropTarget } from "react-dnd";
import { connect } from "react-redux";

import CardDisplay from "./CardDisplay";
import { ItemTypes, keywords, ClientPhase } from "../Helpers/Constants";
import { cardSource, cardTarget } from "./PlayingCardDnd";

import proto from "../../protojs/compiled.js";
import { mapDispatchToProps } from "../Redux/Store";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import { withSize } from "react-sizeme";
import FittedText from "../Primitives/FittedText";
import { debug } from "util";

const mapStateToProps = state => {
  return {
    clientPhase: state.extendedGameState.clientPhase,

    playableCards: state.extendedGameState.game.canPlay,
    studyableCards: state.extendedGameState.game.canStudy,
    activatableCards: state.extendedGameState.game.canActivate,

    selectionData: state.extendedGameState.selectionData,
    attackerAssignmentData: state.extendedGameState.attackerAssignmentData,

    displayTargets: state.extendedGameState.targets,
    attackers: state.extendedGameState.game.attackers,
  };
};

export class PlayingCard extends React.Component {
  state = { popoverStyle: { display: "none", width: 0 } };

  onMouseEnter = event => {
    if (this.props.targets.length !== 0) {
      this.props.updateExtendedGameState({ targets: this.props.targets });
    }
    this.handlePopoverOpen(event);
  };

  onMouseLeave = event => {
    if (this.props.targets.length !== 0) {
      this.props.updateExtendedGameState({ targets: [] });
    }
    this.handlePopoverClose(event);
  };

  handlePopoverOpen = event => {
    var rect = event.currentTarget.getBoundingClientRect();

    var style = {};
    style["width"] = (2 * window.innerWidth) / 6;
    style["display"] = "flex";
    style["textAlign"] = "left";

    if (rect.top < window.innerHeight / 2) {
      style["top"] = rect.height;
    } else {
      style["bottom"] = rect.height;
    }

    if (rect.left < window.innerWidth / 2) {
      style["left"] = rect.width;
    } else {
      style["right"] = rect.width;
      style["flexDirection"] = "row-reverse";
    }
    this.setState({
      popoverStyle: style
    });
  };

  handlePopoverClose = event => {
    this.setState({ popoverStyle: { display: "none", width: 0 } });
  };

  unsetAttacking = event => {
    let id = this.props.id;
    let attackerAssignments = [...this.props.attackerAssignmentData.attackerAssignments];
    let attackerAssignmentIds = attackerAssignments.map(a=>{return a.attackerId;});
    
    if (attackerAssignmentIds.indexOf(id) > -1 ) {
      attackerAssignments.splice(attackerAssignmentIds.indexOf(id), 1);
    }
    
    this.props.updateExtendedGameState({
      attackerAssignmentData: {
        currentAttacker: "",
        possibleAttackTargets: [],  
        attackerAssignments: attackerAssignments
      }
    });
  };

  setAttacking = event => {
    let id = this.props.id;

    let attackerAssignments = [...this.props.attackerAssignmentData.attackerAssignments];
    let possibleAttackers = this.props.attackerAssignmentData.possibleAttackers;
    let possibleAttackerIds = possibleAttackers.map(a=>{return a.attackerId});
    let possibleAttackerEntry = possibleAttackers[possibleAttackerIds.indexOf(id)];
    let possibleAttackTargets = possibleAttackerEntry.possibleAttackTargets;

    if (possibleAttackTargets.length === 1) {
        attackerAssignments.push({attackerId: id, attacksTo: possibleAttackTargets[0] });
        this.props.updateExtendedGameState({
          attackerAssignmentData: {
            currentAttacker: "",
            possibleAttackTargets: [],
            attackerAssignments: attackerAssignments
          }
        });
      } else {
        this.props.updateExtendedGameState({
          attackerAssignmentData: {
            currentAttacker: id,
            possibleAttackTargets: possibleAttackTargets
          }
        });
      }
    };

  setAttacked = event => {
    let id = this.props.id;

    let attackerAssignments = [...this.props.attackerAssignmentData.attackerAssignments];
    let currentAttacker = this.props.attackerAssignmentData.currentAttacker;

    attackerAssignments.push({attackerId: currentAttacker, attacksTo: id });
    this.props.updateExtendedGameState({
      attackerAssignmentData: {
        currentAttacker: "",
        possibleAttackTargets: [],
        attackerAssignments: attackerAssignments
      }
    });
  };

  select = event => {
    let id = this.props.id;
    let selected = [...this.props.selectionData.selected];
    let maxCount = this.props.selectionData.selectionCount;
    let clientPhase = this.props.clientPhase;

    if (selected.length < maxCount) {
      selected.push(id);
      this.props.updateExtendedGameState({
        selectionData: {
           selected: selected 
        }
      });
    }
    if (selected.length === maxCount) {
      this.props.gameHandler.SelectDone(clientPhase, selected);
    }
  };

  unselect = event => {
    let id = this.props.id;
    let selected = [...this.props.selectionData.selected];

    if (selected.includes(id)) {
      selected.splice(selected.indexOf(id), 1);
      this.props.updateExtendedGameState({
        selectionData: {
          selected: selected
        }
      });
    }
  };

  render() {
    const {
      id,
      clientPhase,
      depleted,
      deploying,

      isOver,
      canDrop,
      isDragging,
      connectDragSource,

      selectionData,
      attackerAssignmentData,

      displayTargets,
      activatableCards,
      playableCards,

      gameHandler,
      small,
      square,
      style,
      ...cardProps
    } = this.props;

    const activatable = activatableCards.includes(id);
    const playable = playableCards.includes(id);
    const selectable_ = selectionData.selectable.includes(id);
    const selected_ = selectionData.selected.includes(id);
    const targeted = displayTargets.includes(id);

    const attacking = 
      clientPhase === ClientPhase.SELECT_ATTACKERS &&
      (attackerAssignmentData.attackerAssignments.map(c => {return c.attackerId}).includes(id) || 
        attackerAssignmentData.currentAttacker === id);
    const canAttack =     
        !attacking && 
        !attackerAssignmentData.currentAttacker &&
        clientPhase === ClientPhase.SELECT_ATTACKERS &&
        attackerAssignmentData.possibleAttackers.map(c => {return c.attackerId}).includes(id);
    const canBeAttacked = 
      clientPhase === ClientPhase.SELECT_ATTACKERS &&
      attackerAssignmentData.currentAttacker &&
      attackerAssignmentData.possibleAttackTargets.includes(id);

    var borderColor = "";
    if (isDragging) {
      //borderColor = "yellow";
    } else if ((canDrop && isOver) || attacking) {
      borderColor = "red";
    } else if (targeted) {
      borderColor = "yellow";
    } else if (selected_) {
      borderColor = "blue";
    } else if (canAttack || 
              canBeAttacked || 
              selectable_ || 
              activatable || 
              playable) {
      borderColor = "green";
    }


    let clickHandler = undefined;
    if (canAttack){
      clickHandler = this.setAttacking;
      console.log("Set Attacking");
    } else if (attacking){
        clickHandler = this.unsetAttacking;
        console.log("Unset Attacking");
    } else if (canBeAttacked) {
      clickHandler = this.setAttacked;
    } else if (selected_) {
      clickHandler = this.unselect;
    } else if (selectable_) {
      clickHandler = this.select;
    } else if (activatable) {
      clickHandler = event => {
        gameHandler.ActivateCard(id);
      };
    }

    var opacity = 1;
    if (isDragging || deploying || depleted) {
      opacity = 0.5;
    }

    var rotation = "rotate(0deg)";
    if (depleted){
      rotation = "rotate(5deg)";
    }

    const counterMap = {};
    counterMap[proto.Counter.CHARGE] = "C";

    const { popoverStyle } = this.state;

    return connectDragSource(
      <div
        onMouseEnter={this.onMouseEnter}
        onMouseLeave={this.onMouseLeave}
        style={{width:"100%", height:"100%", position:"relative", ...style}}
      >
        {!isDragging &&
        <div
          style={{
            position: "absolute",
            zIndex: 20,
            ...popoverStyle
          }}
        >
          <div style={{ width: popoverStyle.width / 2 }}>
            <CardDisplay {...cardProps} />
          </div>
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              width: popoverStyle.width / 2,
              
            }}
          >
            {cardProps.description && Object.keys(keywords).map((keyword, i) => {
              if (cardProps.description.indexOf(keyword) !== -1) {
                return (
                  <div
                    key={i}
                    style={{
                      color: "white",
                      backgroundColor: "black",
                      border: "1px white solid",
                      borderRadius: "5px",
                      whiteSpace: "pre-wrap"
                    }}
                  >
                    <FittedText
                      text={keyword + "\n" + keywords[keyword]}
                    />
                  </div>
                );
              }
              return (<div key={i}/>);
            })}
          </div>
        </div>}
        <CardDisplay
          opacity={opacity}
          borderColor={borderColor}
          onClick={clickHandler}
          small={small}
          square={square}
          style={{transform: rotation}}
          {...cardProps}
        />
      </div>
    );
  }
}

PlayingCard = DragSource(ItemTypes.CARD, cardSource, (connect, monitor) => ({
  connectDragSource: connect.dragSource(),
  connectDragPreview: connect.dragPreview(),
  isDragging: monitor.isDragging()
}))(PlayingCard);

PlayingCard = DropTarget(ItemTypes.CARD, cardTarget, (connect, monitor) => ({
  connectDropTarget: connect.dropTarget(),
  isOver: monitor.isOver(),
  canDrop: monitor.canDrop()
}))(PlayingCard);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withHandlers(withSize()(PlayingCard)));

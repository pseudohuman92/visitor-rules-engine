import React from "react";
import { DragSource, DropTarget } from "react-dnd";
import { connect } from "react-redux";

import CardDisplay from "./CardDisplay";
import { ItemTypes, keywords } from "../Helpers/Constants";
import { cardSource, cardTarget } from "./PlayingCardDnd";

import proto from "../../protojs/compiled.js";
import { mapDispatchToProps } from "../Redux/Store";
import { withHandlers } from "../MessageHandlers/HandlerContext";
import { withSize } from "react-sizeme";
import FittedText from "../Primitives/FittedText";
import { debug } from "util";

const mapStateToProps = state => {
  return {
    phase: state.extendedGameState.phase,
    playableCards: state.extendedGameState.game.canPlay,
    studyableCards: state.extendedGameState.game.canStudy,
    activatableCards: state.extendedGameState.game.canActivate,
    selectedCards: state.extendedGameState.selectedCards,
    selectableCards: state.extendedGameState.selectableCards,
    displayTargets: state.extendedGameState.targets,
    selectCountMax: state.extendedGameState.selectCountMax,
    canAttack: state.extendedGameState.canAttack,
    attacking: state.extendedGameState.attacking,
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

  toggleAttacking = attackTargets => event => {
    let id = this.props.id;
    let attacking = [...this.props.attacking];
    console.log(attacking);
    if (attacking.includes(id)) {
      attacking.splice(attacking.indexOf(id), 1);
      this.props.updateExtendedGameState({
        attacking: attacking
      });
    } else {

      attacking.push(id);
      this.props.updateExtendedGameState({
        attacking: attacking
      });
    }
  };

  select = event => {
    let id = this.props.id;
    let selected = [...this.props.selectedCards];
    let maxCount = this.props.selectCountMax;
    let phase = this.props.phase;

    if (selected.length < maxCount) {
      selected.push(id);
      this.props.updateExtendedGameState({ selectedCards: selected });
    }
    if (selected.length === maxCount) {
      this.props.gameHandler.SelectDone(phase, selected);
    }
  };

  unselect = event => {
    let id = this.props.id;
    let selected = [...this.props.selectedCards];

    if (selected.includes(id)) {
      selected.splice(selected.indexOf(id), 1);
      this.props.updateExtendedGameState({
        selectedCards: selected
      });
    }
  };

  render() {
    const {
      id,
      depleted,
      deploying,
      isOver,
      canDrop,
      isDragging,
      connectDragSource,
      selectableCards,
      selectedCards,
      displayTargets,
      activatableCards,
      playableCards,
      canAttack,
      attackers,
      attacking,
      gameHandler,
      small,
      square,
      style,
      ...cardProps
    } = this.props;

    const activatable = activatableCards.includes(id);
    const playable = playableCards.includes(id);
    const selectable = selectableCards.includes(id);
    const selected = selectedCards.includes(id);
    const targeted = displayTargets.includes(id);
    const canAttack_ = canAttack.map(c => {return c.attackerId}).includes(id);
    const attacking_ = attacking.includes(id) || attackers.includes(id);

    var borderColor = "";
    if (isDragging) {
      borderColor = "yellow";
    } else if ((canDrop && isOver) || attacking_) {
      borderColor = "red";
    } else if (canAttack_ || targeted) {
      borderColor = "yellow";
    } else if (selected) {
      borderColor = "magenta";
    } else if (selectable) {
      borderColor = "green";
    } else if (activatable || playable) {
      borderColor = "blue";
    }


    let clickHandler = undefined;
    if (canAttack_){
      clickHandler = this.toggleAttacking();
    } else if (selected) {
      clickHandler = this.unselect;
    } else if (selectable) {
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
        </div>
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

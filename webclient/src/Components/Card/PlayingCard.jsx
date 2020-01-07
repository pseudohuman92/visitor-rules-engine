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
import { ArcherElement } from "react-archer";

const mapStateToProps = state => {
  return {
    clientPhase: state.extendedGameState.clientPhase,

    playableCards: state.extendedGameState.game.canPlay,
    studyableCards: state.extendedGameState.game.canStudy,
    activatableCards: state.extendedGameState.game.canActivate,
    attackers: state.extendedGameState.game.attackers,
    blockers: state.extendedGameState.game.blockers,

    selectionData: state.extendedGameState.selectionData,
    attackerAssignmentData: state.extendedGameState.attackerAssignmentData,
    blockerAssignmentData: state.extendedGameState.blockerAssignmentData,
    attackers: state.extendedGameState.game.attackers
  };
};

export class PlayingCard extends React.Component {
  constructor(props) {
    super(props);
    var relations = [];
    if (this.props.targets.length > 0) {
      relations = this.getArrowRelations();
    }
    this.state = {
      popoverStyle: { display: "none", width: 0 },
      arrowRelations: relations,
      showArrows: false
    };
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    if (JSON.stringify(prevProps.targets) !== JSON.stringify(this.props.targets)) {
      var relations = this.getArrowRelations();
      this.setState({
        arrowRelations: relations
      });
    }
  }

  onMouseEnter = event => {
    if (this.props.targets.length !== 0) {
      this.setState({ showArrows: true });
    }
    this.handlePopoverOpen(event);
  };

  onMouseLeave = event => {
    if (this.props.targets.length !== 0) {
      this.setState({ showArrows: false });
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

  /////// Attack Handlers /////////

  setAttacking = event => {
    let id = this.props.id;

    let attackerAssignments = [
      ...this.props.attackerAssignmentData.attackerAssignments
    ];
    let possibleAttackers = this.props.attackerAssignmentData.possibleAttackers;
    let possibleAttackerIds = possibleAttackers.map(a => {
      return a.attackerId;
    });
    let possibleAttackerEntry =
      possibleAttackers[possibleAttackerIds.indexOf(id)];
    let possibleAttackTargets = possibleAttackerEntry.possibleAttackTargets;

    if (possibleAttackTargets.length === 1) {
      attackerAssignments.push({
        attackerId: id,
        attacksTo: possibleAttackTargets[0]
      });
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

  unsetAttacking = event => {
    let id = this.props.id;
    let attackerAssignments = [
      ...this.props.attackerAssignmentData.attackerAssignments
    ];
    let attackerAssignmentIds = attackerAssignments.map(a => {
      return a.attackerId;
    });

    if (attackerAssignmentIds.indexOf(id) > -1) {
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

  setAttacked = event => {
    let id = this.props.id;

    let attackerAssignments = [
      ...this.props.attackerAssignmentData.attackerAssignments
    ];
    let currentAttacker = this.props.attackerAssignmentData.currentAttacker;

    attackerAssignments.push({ attackerId: currentAttacker, attacksTo: id });
    this.props.updateExtendedGameState({
      attackerAssignmentData: {
        currentAttacker: "",
        possibleAttackTargets: [],
        attackerAssignments: attackerAssignments
      }
    });
  };

  /////// Block Handlers /////////

  setBlocking = event => {
    let id = this.props.id;

    let blockerAssignments = [
      ...this.props.blockerAssignmentData.blockerAssignments
    ];
    let possibleBlockers = this.props.blockerAssignmentData.possibleBlockers;
    let possibleBlockerIds = possibleBlockers.map(a => {
      return a.blockerId;
    });
    let possibleBlockerEntry = possibleBlockers[possibleBlockerIds.indexOf(id)];
    let possibleBlockTargets = possibleBlockerEntry.possibleBlockTargets;

    if (possibleBlockTargets.length === 1) {
      blockerAssignments.push({
        blockerId: id,
        blockedBy: possibleBlockTargets[0]
      });
      this.props.updateExtendedGameState({
        blockerAssignmentData: {
          currentBlocker: "",
          possibleBlockTargets: [],
          blockerAssignments: blockerAssignments
        }
      });
    } else {
      this.props.updateExtendedGameState({
        blockerAssignmentData: {
          currentBlocker: id,
          possibleBlockTargets: possibleBlockTargets
        }
      });
    }
  };

  unsetBlocking = event => {
    let id = this.props.id;
    let blockerAssignments = [
      ...this.props.blockerAssignmentData.blockerAssignments
    ];
    let blockerAssignmentIds = blockerAssignments.map(a => {
      return a.blockerId;
    });

    if (blockerAssignmentIds.indexOf(id) > -1) {
      blockerAssignments.splice(blockerAssignmentIds.indexOf(id), 1);
    }

    this.props.updateExtendedGameState({
      blockerAssignmentData: {
        currentBlocker: "",
        possibleBlockTargets: [],
        blockerAssignments: blockerAssignments
      }
    });
  };

  setBlocked = event => {
    let id = this.props.id;

    let blockerAssignments = [
      ...this.props.blockerAssignmentData.blockerAssignments
    ];
    let currentBlocker = this.props.blockerAssignmentData.currentBlocker;

    blockerAssignments.push({ blockerId: currentBlocker, blockedBy: id });
    this.props.updateExtendedGameState({
      blockerAssignmentData: {
        currentBlocker: "",
        possibleBlockTargets: [],
        blockerAssignments: blockerAssignments
      }
    });
  };

  ///////// Selection Handlers ///////////

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

  /////////////////////////////////

  getArrowRelations = () => {
    let targets = this.props.targets;

    /* {
      //strokeColor: string,
      //strokeWidth: number,
      //strokeDasharray: number,
      //arrowLength: number,
      //arrowThickness: number,
      //noCurves: boolean,
    } : Style */
    var style = {
      strokeColor: "yellow",
      strokeWidth: 4,
      noCurves: false
    };

    /* {
      targetId: string,
      targetAnchor: 'top' | 'bottom' | 'left' | 'right' | 'middle',
      sourceAnchor: 'top' | 'bottom' | 'left' | 'right' | 'middle',
      label: React.Node,
      style: Style,
    } : Relation */
    let relations = [];

    targets.forEach(target => {
      var relation = {
        targetId: target,
        targetAnchor: "middle",
        sourceAnchor: "middle",
        style: style
      };

      relations.push(relation);
    });

    console.log("Relations", relations);
    return relations;
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
      blockerAssignmentData,

      activatableCards,
      playableCards,
      attackers,
      blockers,

      gameHandler,
      small,
      square,
      style,
      ...cardProps
    } = this.props;

    const { arrowRelations, showArrows } = this.state;

    const activatable = activatableCards.includes(id);
    const playable = playableCards.includes(id);
    const selectable_ = selectionData.selectable.includes(id);
    const selected_ = selectionData.selected.includes(id);

    const attacking =
      (clientPhase === ClientPhase.SELECT_ATTACKERS ||
        clientPhase === ClientPhase.SELECT_BLOCKERS ||
        clientPhase === ClientPhase.UPDATE_GAME) &&
      (attackerAssignmentData.attackerAssignments
        .map(c => {
          return c.attackerId;
        })
        .includes(id) ||
        attackerAssignmentData.currentAttacker === id ||
        attackers.includes(id));
    const canAttack =
      !attacking &&
      !attackerAssignmentData.currentAttacker &&
      clientPhase === ClientPhase.SELECT_ATTACKERS &&
      attackerAssignmentData.possibleAttackers
        .map(c => {
          return c.attackerId;
        })
        .includes(id);
    const canBeAttacked =
      clientPhase === ClientPhase.SELECT_ATTACKERS &&
      attackerAssignmentData.currentAttacker &&
      attackerAssignmentData.possibleAttackTargets.includes(id);

    const blocking =
      (clientPhase === ClientPhase.SELECT_ATTACKERS ||
        clientPhase === ClientPhase.SELECT_BLOCKERS ||
        clientPhase === ClientPhase.UPDATE_GAME) &&
      (blockerAssignmentData.blockerAssignments
        .map(c => {
          return c.blockerId;
        })
        .includes(id) ||
        blockerAssignmentData.currentBlocker === id ||
        blockers.includes(id));
    const canBlock =
      !blocking &&
      !blockerAssignmentData.currentBlocker &&
      clientPhase === ClientPhase.SELECT_BLOCKERS &&
      blockerAssignmentData.possibleBlockers
        .map(c => {
          return c.blockerId;
        })
        .includes(id);
    const canBeBlocked =
      clientPhase === ClientPhase.SELECT_BLOCKERS &&
      blockerAssignmentData.currentBlocker &&
      blockerAssignmentData.possibleBlockTargets.includes(id);

    var borderColor = "";
    if (isDragging) {
      //borderColor = "yellow";
    } else if (
      canAttack ||
      canBeAttacked ||
      canBlock ||
      canBeBlocked ||
      selectable_ ||
      activatable ||
      playable
    ) {
      borderColor = "green";
    } else if ((canDrop && isOver) || attacking) {
      borderColor = "red";
    } else if (selected_ || blocking) {
      borderColor = "blue";
    }

    let clickHandler = undefined;
    if (canAttack) {
      clickHandler = this.setAttacking;
      console.log("Set Attacking");
    } else if (attacking && clientPhase === ClientPhase.SELECT_ATTACKERS) {
      clickHandler = this.unsetAttacking;
      console.log("Unset Attacking");
    } else if (canBeAttacked) {
      clickHandler = this.setAttacked;
    } else if (canBlock) {
      clickHandler = this.setBlocking;
      console.log("Set Blocking");
    } else if (blocking && clientPhase === ClientPhase.SELECT_BLOCKERS) {
      clickHandler = this.unsetBlocking;
      console.log("Unset Blocking");
    } else if (canBeBlocked) {
      clickHandler = this.setBlocked;
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
    if (isDragging || deploying) {
      opacity = 0.5;
    }

    var rotation = "rotate(0deg)";
    if (depleted) {
      rotation = "rotate(7.5deg)";
    }

    const counterMap = {};
    counterMap[proto.Counter.CHARGE] = "C";

    const { popoverStyle } = this.state;

    return connectDragSource(
      <div
        onMouseEnter={this.onMouseEnter}
        onMouseLeave={this.onMouseLeave}
        style={{
          width: "100%",
          height: "100%",
          position: "relative",
          ...style
        }}
      >
        {!isDragging && (
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
                width: popoverStyle.width / 2
              }}
            >
              {cardProps.description &&
                Object.keys(keywords).map((keyword, i) => {
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
                        <FittedText text={keyword + "\n" + keywords[keyword]} />
                      </div>
                    );
                  }
                  return <div key={i} />;
                })}
            </div>
          </div>
        )}
        <ArcherElement id={id} relations={showArrows ? arrowRelations : []} style={{zIndex: 100}}>
          <CardDisplay
            opacity={opacity}
            borderColor={borderColor}
            onClick={clickHandler}
            small={small}
            square={square}
            style={{ transform: rotation }}
            {...cardProps}
          />
        </ArcherElement>
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

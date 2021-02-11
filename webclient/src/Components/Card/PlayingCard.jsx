import React from "react";
import {connect} from "react-redux";

import CardDisplay from "./CardDisplay";
import {keywords, ClientPhase} from "../Helpers/Constants";

import proto from "../../protojs/compiled.js";
import {mapDispatchToProps} from "../Redux/Store";
import {withHandlers} from "../MessageHandlers/HandlerContext";
import FittedText from "../Primitives/FittedText";
import LineTo from "react-lineto";
import {Draggable} from "react-beautiful-dnd";

const mapStateToProps = (state) => {
    return {
        clientPhase: state.extendedGameState.clientPhase,
        gamePhase: state.extendedGameState.game.phase,
        windowDimensions: state.windowDimensions,

        playableCards: state.extendedGameState.game.canPlay,
        studyableCards: state.extendedGameState.game.canStudy,
        activatableCards: state.extendedGameState.game.canActivate,
        attackers: state.extendedGameState.game.attackers,
        blockers: state.extendedGameState.game.blockers,

        selectionData: state.extendedGameState.selectionData,
        attackerAssignmentData: state.extendedGameState.attackerAssignmentData,
        blockerAssignmentData: state.extendedGameState.blockerAssignmentData,
        damageAssignmentData: state.extendedGameState.damageAssignmentData,
    };
};

function combatPhase(phase) {
    return phase === proto.Phase.ATTACK || phase === proto.Phase.BLOCK;
}

class PlayingCard extends React.Component {
    constructor(props) {
        super(props);
        const relations = this.getArrowRelations();
        this.state = {
            arrowRelations: relations,
            showArrows: false,
        };
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        let prevArrows = this.state.arrowRelations;
        let newArrows = this.getArrowRelations();

        if (prevArrows.length !== newArrows.length) {
            this.setState({
                arrowRelations: newArrows,
            });
        } else if (
            prevArrows.some((pv) => {
                return !newArrows.some((nv) => {
                    return JSON.stringify(pv) === JSON.stringify(nv);
                });
            })
        ) {
            this.setState({
                arrowRelations: newArrows,
            });
        }
    }

    onMouseEnter = (event) => {
        this.setState({showArrows: true});
    };

    onMouseLeave = (event) => {
        this.setState({showArrows: false});
    };

    // General Properties
    isActivatable = () => this.props.activatableCards.includes(this.props.cardData.id);
    isStudyable = () => this.props.studyableCards.includes(this.props.cardData.id);
    isPlayable = () => this.props.playableCards.includes(this.props.cardData.id);
    isSelectable = () => this.props.selectionData.selectable.includes(this.props.cardData.id);
    isSelected = () => this.props.selectionData.selected.includes(this.props.cardData.id);

    //! ///// Attack Handlers /////////

    setAttacking = (event) => {
        let id = this.props.cardData.id;

        let attackerAssignments = [
            ...this.props.attackerAssignmentData.attackerAssignments,
        ];
        let possibleAttackers = this.props.attackerAssignmentData.possibleAttackers;
        let possibleAttackerIds = possibleAttackers.map((a) => {
            return a.attackerId;
        });
        let possibleAttackerEntry =
            possibleAttackers[possibleAttackerIds.indexOf(id)];
        let possibleAttackTargets = possibleAttackerEntry.possibleAttackTargets;

        if (possibleAttackTargets.length === 1) {
            attackerAssignments.push({
                attackerId: id,
                attacksTo: possibleAttackTargets[0],
            });
            this.props.updateExtendedGameState({
                attackerAssignmentData: {
                    currentAttacker: "",
                    possibleAttackTargets: [],
                    attackerAssignments: attackerAssignments,
                },
            });
        } else {
            this.props.updateExtendedGameState({
                attackerAssignmentData: {
                    currentAttacker: id,
                    possibleAttackTargets: possibleAttackTargets,
                },
            });
        }
    };

    unsetAttacking = (event) => {
        let id = this.props.cardData.id;
        let attackerAssignments = [
            ...this.props.attackerAssignmentData.attackerAssignments,
        ];
        let attackerAssignmentIds = attackerAssignments.map((a) => {
            return a.attackerId;
        });

        if (attackerAssignmentIds.indexOf(id) > -1) {
            attackerAssignments.splice(attackerAssignmentIds.indexOf(id), 1);
        }

        this.props.updateExtendedGameState({
            attackerAssignmentData: {
                currentAttacker: "",
                possibleAttackTargets: [],
                attackerAssignments: attackerAssignments,
            },
        });
    };

    setAttacked = (event) => {
        let id = this.props.cardData.id;

        let attackerAssignments = [
            ...this.props.attackerAssignmentData.attackerAssignments,
        ];
        let currentAttacker = this.props.attackerAssignmentData.currentAttacker;

        attackerAssignments.push({attackerId: currentAttacker, attacksTo: id});
        this.props.updateExtendedGameState({
            attackerAssignmentData: {
                currentAttacker: "",
                possibleAttackTargets: [],
                attackerAssignments: attackerAssignments,
            },
        });
    };

    //Attacker Properties
    isAttacking = () => (this.props.clientPhase === ClientPhase.SELECT_ATTACKERS ||
        this.props.clientPhase === ClientPhase.SELECT_BLOCKERS ||
        this.props.clientPhase === ClientPhase.UPDATE_GAME) &&
            (this.props.attackerAssignmentData.attackerAssignments
                    .map((c) => {
                        return c.attackerId;
                    })
                    .includes(this.props.cardData.id) ||
                this.props.attackerAssignmentData.currentAttacker === this.props.cardData.id ||
                this.props.attackers.includes(this.props.cardData.id));

    canAttack = () =>
        !this.isAttacking() &&
        !this.props.attackerAssignmentData.currentAttacker &&
        this.props.clientPhase === ClientPhase.SELECT_ATTACKERS &&
        this.props.attackerAssignmentData.possibleAttackers
            .map((c) => {
                return c.attackerId;
            })
            .includes(this.props.cardData.id );

    canBeBlocked = () =>
        this.props.clientPhase === ClientPhase.SELECT_BLOCKERS &&
        this.props.blockerAssignmentData.possibleBlockTargets.includes(this.props.cardData.id);

    //! /////// Block Handlers /////////

    setBlocking = (event) => {
        let id = this.props.cardData.id;

        let blockerAssignments = [
            ...this.props.blockerAssignmentData.blockerAssignments,
        ];
        let possibleBlockers = this.props.blockerAssignmentData.possibleBlockers;
        let possibleBlockerIds = possibleBlockers.map((a) => {
            return a.blockerId;
        });
        let possibleBlockerEntry = possibleBlockers[possibleBlockerIds.indexOf(id)];
        let possibleBlockTargets = possibleBlockerEntry.possibleBlockTargets;

        if (possibleBlockTargets.length === 1) {
            blockerAssignments.push({
                blockerId: id,
                blockedBy: possibleBlockTargets[0],
            });
            this.props.updateExtendedGameState({
                blockerAssignmentData: {
                    currentBlocker: "",
                    possibleBlockTargets: [],
                    blockerAssignments: blockerAssignments,
                },
            });
        } else {
            this.props.updateExtendedGameState({
                blockerAssignmentData: {
                    currentBlocker: id,
                    possibleBlockTargets: possibleBlockTargets,
                },
            });
        }
    };

    unsetBlocking = (event) => {
        let id = this.props.cardData.id;
        let blockerAssignments = [
            ...this.props.blockerAssignmentData.blockerAssignments,
        ];
        let blockerAssignmentIds = blockerAssignments.map((a) => {
            return a.blockerId;
        });

        if (blockerAssignmentIds.indexOf(id) > -1) {
            blockerAssignments.splice(blockerAssignmentIds.indexOf(id), 1);
        }

        this.props.updateExtendedGameState({
            blockerAssignmentData: {
                currentBlocker: "",
                possibleBlockTargets: [],
                blockerAssignments: blockerAssignments,
            },
        });
    };

    setBlocked = (event) => {
        let id = this.props.cardData.id;

        let blockerAssignments = [
            ...this.props.blockerAssignmentData.blockerAssignments,
        ];
        let currentBlocker = this.props.blockerAssignmentData.currentBlocker;

        blockerAssignments.push({blockerId: currentBlocker, blockedBy: id});
        this.props.updateExtendedGameState({
            blockerAssignmentData: {
                currentBlocker: "",
                possibleBlockTargets: [],
                blockerAssignments: blockerAssignments,
            },
        });
    };

    //Blocker Properties
    isBlocking = () =>
        (this.props.clientPhase === ClientPhase.SELECT_ATTACKERS ||
            this.props.clientPhase === ClientPhase.SELECT_BLOCKERS ||
            this.props.clientPhase === ClientPhase.UPDATE_GAME) &&
        (this.props.blockerAssignmentData.blockerAssignments
                .map((c) => {
                    return c.blockerId;
                })
                .includes(this.props.cardData.id) ||
            this.props.blockerAssignmentData.currentBlocker === this.props.cardData.id ||
            this.props.blockers.includes(this.props.cardData.id));

    canBlock = () =>
        !this.isBlocking() &&
        !this.props.blockerAssignmentData.currentBlocker &&
        this.props.clientPhase === ClientPhase.SELECT_BLOCKERS &&
        this.props.blockerAssignmentData.possibleBlockers
            .map((c) => {
                return c.blockerId;
            })
            .includes(this.props.cardData.id);

    canBeAttacked = () =>
        this.props.clientPhase === ClientPhase.SELECT_ATTACKERS &&
        this.props.attackerAssignmentData.currentAttacker &&
        this.props.attackerAssignmentData.possibleAttackTargets.includes(this.props.cardData.id);

    //! /////// Selection Handlers ///////////

    select = (event) => {
        let id = this.props.cardData.id;
        let selected = [...this.props.selectionData.selected];
        let maxCount = this.props.selectionData.selectionCount;
        let clientPhase = this.props.clientPhase;

        if (selected.length < maxCount) {
            selected.push(id);
            this.props.updateExtendedGameState({
                selectionData: {
                    selected: selected,
                },
            });
        }
        if (selected.length === maxCount) {
            this.props.gameHandler.SelectDone(clientPhase, selected);
        }
    };

    unselect = (event) => {
        let id = this.props.cardData.id;
        let selected = [...this.props.selectionData.selected];

        if (selected.includes(id)) {
            selected.splice(selected.indexOf(id), 1);
            this.props.updateExtendedGameState({
                selectionData: {
                    selected: selected,
                },
            });
        }
    };

    //! //// Connection Arrows //////

    getArrowRelations = () => {
        let id = this.props.cardData.id;
        let targets = this.props.cardData.targets;
        let attackTarget = this.props.cardData.attackTarget;
        let blockedAttacker = this.props.cardData.blockedAttacker;
        let attackerAssignments = this.props.attackerAssignmentData
            .attackerAssignments;
        let blockerAssignments = this.props.blockerAssignmentData
            .blockerAssignments;
        let attackerAssignmentIds = attackerAssignments.map((a) => {
            return a.attackerId;
        });
        let blockerAssignmentIds = blockerAssignments.map((a) => {
            return a.blockerId;
        });
        let relations = [];

        if (targets) {
            targets.forEach((target) => {
                relations.push({
                    from: id,
                    to: target,
                    fromAnchor: "center center",
                    toAnchor: "center center",
                    borderColor: "yellow",
                    borderWidth: 4,
                    zIndex: 10,
                });
            });
        }

        // if card is attacking
        const attackerIndex = attackerAssignmentIds.indexOf(id);
        if (attackerIndex > -1) {
            const attackerAssignment = attackerAssignments[attackerIndex];
            relations.push({
                from: id,
                to: attackerAssignment.attacksTo,
                fromAnchor: "center center",
                toAnchor: "center center",
                borderColor: "red",
                borderWidth: 4,
                zIndex: 10,
            });
        }

        if (attackTarget) {
            relations.push({
                from: id,
                to: attackTarget,
                fromAnchor: "center center",
                toAnchor: "center center",
                borderColor: "red",
                borderWidth: 4,
                zIndex: 10,
            });
        }

        // If card is blocking
        const blockerIndex = blockerAssignmentIds.indexOf(id);
        if (blockerIndex > -1) {
            const blockerAssignment = blockerAssignments[blockerIndex];
            relations.push({
                from: id,
                to: blockerAssignment.blockedBy,
                fromAnchor: "center center",
                toAnchor: "center center",
                borderColor: "blue",
                borderWidth: 4,
                zIndex: 10,
            });
        }

        if (blockedAttacker) {
            relations.push({
                from: id,
                to: blockedAttacker,
                fromAnchor: "center center",
                toAnchor: "center center",
                borderColor: "blue",
                borderWidth: 4,
                zIndex: 10,
            });
        }

        //console.log("Relations", relations);
        return relations;
    };

    //! Damage Assignment /////
    handleDamageAssignment = (event) => {
        const newAssignedDamage = parseInt(event.target.value);
        if (newAssignedDamage < 0) {
            return;
        }

        const id = this.props.cardData.id;
        const damageAssignmentData = this.props.damageAssignmentData;

        const totalDamage = damageAssignmentData.totalDamage;
        let totalAssignedDamage = damageAssignmentData.totalAssignedDamage;
        const canAssignMore = totalDamage > totalAssignedDamage;
        const damageAssignments = [...damageAssignmentData.damageAssignments];

        const damageAssignmentIndex = damageAssignments
            .map((a) => {
                return a.targetId;
            })
            .indexOf(id);
        const damageAssignment = damageAssignments[damageAssignmentIndex];
        const assignedDamage = damageAssignment ? damageAssignment.damage : 0;

        if (damageAssignmentIndex > -1) {
            damageAssignments.splice(damageAssignmentIndex, 1);
            totalAssignedDamage -= assignedDamage;
        }

        damageAssignments.push({
            targetId: id,
            damage: newAssignedDamage,
        });
        totalAssignedDamage += newAssignedDamage;

        if (
            newAssignedDamage <= assignedDamage ||
            (newAssignedDamage > assignedDamage && canAssignMore)
        ) {
            this.props.updateExtendedGameState({
                damageAssignmentData: {
                    damageAssignments: damageAssignments,
                    totalAssignedDamage: totalAssignedDamage,
                },
            });
        }
    };

    //Damage Assignment Properties
    itsDamageIsGettingAssigned = () =>
        this.props.clientPhase === ClientPhase.ASSIGN_DAMAGE &&
        this.props.damageAssignmentData.damageSource === this.props.cardData.id;

    canBeAssignedDamage = () =>
        this.props.clientPhase === ClientPhase.ASSIGN_DAMAGE &&
        this.props.damageAssignmentData.possibleTargets.includes(this.props.cardData.id);

    damageAssignments = () => this.props.damageAssignmentData.damageAssignments;
    damageAssignmentIndex = () => this.damageAssignments()
        .map((a) => {
            return a.targetId;
        })
        .indexOf(this.props.cardData.id);
    damageAssignment = () =>this.damageAssignments()[this.damageAssignmentIndex()];
    assignedDamage = () => this.damageAssignment() ? this.damageAssignment().damage : 0;

    //Highlighting
    getBorderColor = () => {
        let borderColor = "";
        if (this.isSelected() || this.isBlocking() || this.canBeAssignedDamage()) {
            borderColor = "blue";
        } else if (
            this.canAttack() ||
            this.canBeAttacked() ||
            this.canBlock() ||
            this.canBeBlocked() ||
            this.isSelectable() ||
            this.isActivatable() ||
            this.isPlayable()
        ) {
            borderColor = "green";
        } else if (this.isAttacking() || this.itsDamageIsGettingAssigned()) {
            borderColor = "red";
        }
        return borderColor;
    }

    //Click behavior
    getClickHandler = () => {
        let clickHandler = undefined;
        if (this.canAttack()) {
            clickHandler = this.setAttacking;
            console.log("Set Attacking");
        } else if (this.isAttacking() && this.props.clientPhase === ClientPhase.SELECT_ATTACKERS) {
            clickHandler = this.unsetAttacking;
            console.log("Unset Attacking");
        } else if (this.canBeAttacked()) {
            clickHandler = this.setAttacked;
        } else if (this.canBlock()) {
            clickHandler = this.setBlocking;
            console.log("Set Blocking");
        } else if (this.isBlocking() && this.props.clientPhase === ClientPhase.SELECT_BLOCKERS) {
            clickHandler = this.unsetBlocking;
            console.log("Unset Blocking");
        } else if (this.canBeBlocked()) {
            clickHandler = this.setBlocked;
        } else if (this.isSelected()) {
            clickHandler = this.unselect;
        } else if (this.isSelectable()) {
            clickHandler = this.select;
        } else if (this.isActivatable()) {
            clickHandler = (event) => {
                this.props.gameHandler.ActivateCard(this.props.cardData.id);
            };
        }
        return clickHandler;
    }

    //! Rendering //////
    render() {
        const {
            gamePhase,
            DnDIndex,
            isDragDisabled,
            windowDimensions,
            small,
            square,
            style,
            cardData,
            popoverDisabled,
        } = this.props;

        const {id, depleted} = cardData;
        const {arrowRelations, showArrows} = this.state;


        const counterMap = {};
        counterMap[proto.Counter.CHARGE] = "C";

        let draggableId = id;
        if (this.isStudyable()) {
            draggableId = "[STUDYABLE]" + draggableId;
        }
        if (this.isPlayable()) {
            draggableId = "[PLAYABLE]" + draggableId;
        }

        return (
            <Draggable
                className = "playing-card"
                draggableId={draggableId}
                index={DnDIndex}
                isDragDisabled={isDragDisabled || (!this.isPlayable() && !this.isStudyable())}
            >
                {(provided, snapshot) => {
                    const isDragging = snapshot.isDragging;
                    return (
                        <div
                            ref={provided.innerRef}
                            {...provided.draggableProps}
                            style={{
                                ...style,
                                position: "relative",
                            }}
                        >


                            <div {...provided.dragHandleProps}>
                                {
                                    (showArrows || combatPhase(gamePhase)) &&
                                    arrowRelations.map((rel, i) => {
                                        return <LineTo key={i} {...rel} />;
                                    })
                                }
                                {this.canBeAssignedDamage() && (
                                    <input
                                        type="number"
                                        value={this.assignedDamage()}
                                        onChange={this.handleDamageAssignment()}
                                        style={{
                                            position: "absolute",
                                            top: "33%",
                                            left: "25%",
                                            opacity: 1,
                                            width: "50%",
                                            zIndex: 50
                                        }}
                                    />
                                )}

                                <div
                                    className={cardData.id}
                                    onMouseEnter={this.onMouseEnter}
                                    onMouseLeave={this.onMouseLeave}
                                >
                                    <CardDisplay
                                        brightness={depleted ? 50 : 100}
                                        borderColor={this.getBorderColor()}
                                        onClick={this.getClickHandler()}
                                        small={small}
                                        square={square}
                                        cardData={cardData}
                                        windowDimensions={windowDimensions}
                                        isDragging = {isDragging}
                                        popoverDisabled = {popoverDisabled}
                                        withKeywords
                                        style={style}
                                    />
                                </div>
                            </div>
                        </div>
                    );
                }}
            </Draggable>
        );
    }
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(withHandlers(PlayingCard));

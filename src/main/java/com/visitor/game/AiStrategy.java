package com.visitor.game;

import com.visitor.protocol.ClientGameMessages;
import com.visitor.protocol.ServerGameMessages;
import com.visitor.protocol.Types;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Heuristic, rules-aware play policy. It only emits actions advertised as legal by the engine. */
public final class AiStrategy {
    private static final double ACTION_THRESHOLD = 0.35;
    private static final Pattern NUMBER = Pattern.compile("\\b(\\d+)\\b");

    private AiStrategy() {}

    public static ClientGameMessages.ClientGameMessage respond(
            ServerGameMessages.ServerGameMessage message, UUID aiId) {
        switch (message.getPayloadCase()) {
            case UPDATEGAMESTATEP:
                return respondToState(message.getUpdateGameStateP().getGame(), aiId);
            case SELECTFROM:
                return selectFrom(message.getSelectFrom());
            case ASSIGNDAMAGE:
                return assignDamage(message.getAssignDamage());
            case SELECTXVALUE:
                return selectX(message.getSelectXValue());
            case SELECTKNOWLEDGE:
                return selectKnowledge(message.getSelectKnowledge());
            case ORDERCARDS:
                return orderCards(message.getOrderCards());
            case PICKCARD:
                return pickCard(message.getPickCard());
            default:
                return null;
        }
    }

    private static ClientGameMessages.ClientGameMessage respondToState(Types.GameStateP state, UUID aiId) {
        String id = aiId.toString();
        if (!state.getActivePlayer().equals(id)) return null;
        if (state.getStackCount() > 0) return respondOnStack(state, id);

        if (state.getPhase() == Types.Phase.REDRAW) {
            return openingHandIsKeepable(state.getPlayer())
                    ? message().setKeep(ClientGameMessages.Keep.newBuilder()).build()
                    : message().setRedraw(ClientGameMessages.Redraw.newBuilder()).build();
        }
        if (state.getPhase() == Types.Phase.ATTACK) return declareAttackers(state);
        if (state.getPhase() == Types.Phase.BLOCK) {
            Candidate trick = bestCombatTrick(state, id);
            return trick != null ? trick.message : declareBlockers(state);
        }

        Candidate best = bestAction(state, id);
        if (best != null && best.score >= ACTION_THRESHOLD) return best.message;
        return message().setPass(ClientGameMessages.Pass.newBuilder()).build();
    }

    private static boolean openingHandIsKeepable(Types.Player player) {
        if (player.getHandCount() <= 4 || player.getDeckSize() == 0) return true;
        double score = 0.0;
        int units = 0;
        for (Types.CardP card : player.getHandList()) {
            score += cardValue(card);
            if (isUnit(card)) units++;
            int cost = cardCost(card);
            if (cost > 5) score -= (cost - 5) * 0.65;
        }
        if (units == 0) score -= 2.5;
        return score >= 11.0;
    }

    private static Candidate bestAction(Types.GameStateP state, String aiId) {
        Candidate best = null;
        for (Types.CardP card : state.getPlayer().getHandList()) {
            if (card.getCanPlay()) {
                List<Types.TargetSelection> targets = chooseTargets(
                        card.getPlayTargetsList(), state, aiId, card.getDescription());
                double score = playScore(card, state, aiId, targets);
                best = better(best, new Candidate(score,
                        message().setPlayCard(ClientGameMessages.PlayCard.newBuilder()
                                .setCardId(card.getId()).addAllTargets(targets)).build()));
            }
            if (card.getCanStudy()) {
                Types.Knowledge knowledge = bestKnowledgeFromGroups(card.getKnowledgeCostList(), state);
                double score = studyScore(card, state);
                best = better(best, new Candidate(score,
                        message().setStudyCard(ClientGameMessages.StudyCard.newBuilder()
                                .setCardId(card.getId()).setSelectedKnowledge(knowledge)).build()));
            }
        }

        for (Types.CardP card : state.getPlayer().getPlayList()) {
            if (!card.getCanActivate()) continue;
            for (Types.TargetingAbility ability : card.getActivateTargetsList()) {
                List<Types.TargetSelection> targets = chooseTargets(
                        ability.getTargetsList(), state, aiId,
                        card.getDescription() + " " + ability.getText());
                double score = 1.8 + effectScore(ability.getText())
                        + targetsScore(targets, state, aiId, card.getDescription() + " " + ability.getText());
                best = better(best, new Candidate(score,
                        message().setActivateCard(ClientGameMessages.ActivateCard.newBuilder()
                                .setCardId(card.getId()).setAbilityId(ability.getId())
                                .addAllTargets(targets)).build()));
            }
        }
        return best;
    }

    private static Candidate better(Candidate current, Candidate next) {
        return current == null || next.score > current.score ? next : current;
    }

    private static Candidate bestCombatTrick(Types.GameStateP state, String aiId) {
        Candidate best = null;
        for (Types.CardP card : state.getPlayer().getHandList()) {
            String text = card.getName() + " " + card.getDescription();
            if (!card.getCanPlay() || !isCombatTrick(text)) continue;
            List<Types.TargetSelection> targets = chooseTargets(
                    card.getPlayTargetsList(), state, aiId, card.getDescription());
            double score = playScore(card, state, aiId, targets);
            if (score >= 3.0) {
                best = better(best, new Candidate(score,
                        message().setPlayCard(ClientGameMessages.PlayCard.newBuilder()
                                .setCardId(card.getId()).addAllTargets(targets)).build()));
            }
        }
        for (Types.CardP card : state.getPlayer().getPlayList()) {
            if (!card.getCanActivate()) continue;
            for (Types.TargetingAbility ability : card.getActivateTargetsList()) {
                if (!isCombatTrick(ability.getText())) continue;
                List<Types.TargetSelection> targets = chooseTargets(ability.getTargetsList(), state,
                        aiId, ability.getText());
                double score = 1.8 + effectScore(ability.getText())
                        + targetsScore(targets, state, aiId, ability.getText());
                if (score >= 3.0) {
                    best = better(best, new Candidate(score,
                            message().setActivateCard(ClientGameMessages.ActivateCard.newBuilder()
                                    .setCardId(card.getId()).setAbilityId(ability.getId())
                                    .addAllTargets(targets)).build()));
                }
            }
        }
        return best;
    }

    private static ClientGameMessages.ClientGameMessage respondOnStack(Types.GameStateP state,
                                                                         String aiId) {
        Candidate best = null;
        for (Types.CardP card : state.getPlayer().getHandList()) {
            String text = lower(card.getName() + " " + card.getDescription());
            if (!card.getCanPlay() || !isStackResponse(text)) continue;
            List<Types.TargetSelection> targets = chooseTargets(
                    card.getPlayTargetsList(), state, aiId, text);
            double score = playScore(card, state, aiId, targets);
            if (score >= 3.2) {
                best = better(best, new Candidate(score,
                        message().setPlayCard(ClientGameMessages.PlayCard.newBuilder()
                                .setCardId(card.getId()).addAllTargets(targets)).build()));
            }
        }
        for (Types.CardP card : state.getPlayer().getPlayList()) {
            if (!card.getCanActivate()) continue;
            for (Types.TargetingAbility ability : card.getActivateTargetsList()) {
                String text = lower(ability.getText());
                if (!isStackResponse(text)) continue;
                List<Types.TargetSelection> targets = chooseTargets(ability.getTargetsList(), state,
                        aiId, text);
                double score = 1.8 + effectScore(text)
                        + targetsScore(targets, state, aiId, text);
                if (score >= 3.2) {
                    best = better(best, new Candidate(score,
                            message().setActivateCard(ClientGameMessages.ActivateCard.newBuilder()
                                    .setCardId(card.getId()).setAbilityId(ability.getId())
                                    .addAllTargets(targets)).build()));
                }
            }
        }
        return best == null ? message().setPass(ClientGameMessages.Pass.newBuilder()).build()
                : best.message;
    }

    private static double studyScore(Types.CardP card, Types.GameStateP state) {
        int maxEnergy = state.getPlayer().getMaxEnergy();
        double acceleration = Math.max(0, 7 - maxEnergy) * 0.72;
        double colorDemand = 0.0;
        Map<Types.Knowledge, Integer> pool = knowledgePool(state.getPlayer());
        for (Types.CardP held : state.getPlayer().getHandList()) {
            for (Types.KnowledgeGroup need : held.getKnowledgeCostList()) {
                if (pool.getOrDefault(need.getKnowledge(), 0) < need.getCount()) {
                    colorDemand += 0.45;
                    break;
                }
            }
        }
        return 1.2 + acceleration + colorDemand - cardValue(card) * 0.55;
    }

    private static double playScore(Types.CardP card, Types.GameStateP state, String aiId,
                                    List<Types.TargetSelection> targets) {
        String text = lower(card.getName() + " " + card.getDescription());
        double score;
        if (isUnit(card)) {
            Types.Combat combat = card.getCombat();
            score = 4.4 + combat.getAttack() * 0.72 + combat.getHealth() * 0.48
                    + combat.getShield() * 0.24;
            score += combatAbilityValue(combat.getCombatAbilitiesList());
            int ownUnits = countUnits(state.getPlayer().getPlayList());
            score += Math.max(0.0, 3.0 - ownUnits) * 0.55;
            if (has(combat.getCombatAbilitiesList(), "Blitz")
                    || has(combat.getCombatAbilitiesList(), "Haste")) score += 1.4;
        } else {
            score = Math.max(1.4, 1.0 + effectScore(text));
        }

        score += targetsScore(targets, state, aiId, text);
        score -= Math.max(0, cardCost(card)) * 0.33;

        boolean responseWindow = state.getPhase() == Types.Phase.ATTACK_PLAY
                || state.getPhase() == Types.Phase.BLOCK
                || state.getPhase() == Types.Phase.BLOCK_PLAY;
        if (responseWindow && isCombatTrick(text)) {
            if (state.getAttackersCount() > 0) score += 3.8;
            else score -= 2.8;
        } else if (responseWindow && state.getAttackersCount() > 0
                && (containsAny(text, "damage", "destroy", "kill", "remove", "prevent"))) {
            score += 1.7;
        }

        if (state.getPlayer().getHealth() <= 6 && containsAny(text, "heal", "health", "shield", "prevent")) {
            score += 2.0;
        }
        return score;
    }

    private static double effectScore(String text) {
        String value = lower(text);
        double score = 0.0;
        if (containsAny(value, "draw", "look at the top", "search your deck")) score += 3.1;
        if (containsAny(value, "destroy", "kill", "banish", "exile", "remove from play")) score += 3.2;
        if (containsAny(value, "counter", "cancel", "negate")) score += 3.2;
        if (containsAny(value, "deal damage", "deals damage", "damage to")) score += 2.5;
        if (containsAny(value, "return to hand", "put on top of", "discard")) score += 2.0;
        if (containsAny(value, "summon", "create a", "put a unit")) score += 2.7;
        if (containsAny(value, "gain energy", "add energy", "max energy")) score += 2.0;
        if (containsAny(value, "shield", "prevent", "heal", "health")) score += 1.5;
        if (containsAny(value, "attack", "first strike", "trample", "evasive", "deadly",
                "buff", "boost", "strengthen", "until end of turn", "gets +", "gains +")) score += 1.4;
        if (containsAny(value, "for each", "all units", "each unit", "each enemy")) score += 1.2;
        return score;
    }

    private static double targetsScore(List<Types.TargetSelection> selections, Types.GameStateP state,
                                       String actorId, String sourceText) {
        Map<String, Types.CardP> cards = cardsById(state);
        double total = 0.0;
        for (Types.TargetSelection selection : selections) {
            for (String id : selection.getTargetsList()) {
                total += targetScore(id, cards, state, actorId, sourceText);
            }
        }
        return total;
    }

    private static List<Types.TargetSelection> chooseTargets(List<Types.Targeting> groups,
                                                               Types.GameStateP state,
                                                               String actorId, String sourceText) {
        List<Types.TargetSelection> result = new ArrayList<>();
        Map<String, Types.CardP> cards = cardsById(state);
        for (Types.Targeting group : groups) {
            List<String> choices = new ArrayList<>(group.getPossibleTargetsList());
            choices.sort(Comparator.comparingDouble((String target) ->
                    targetScore(target, cards, state, actorId,
                            sourceText + " " + group.getTargetMessage())).reversed());

            int min = Math.max(0, group.getMinTargets());
            int max = Math.max(min, group.getMaxTargets());
            int count = Math.min(min, choices.size());
            for (int i = count; i < Math.min(max, choices.size()); i++) {
                double score = targetScore(choices.get(i), cards, state, actorId,
                        sourceText + " " + group.getTargetMessage());
                if (score <= 0.0) break;
                count++;
            }
            if (count == 0) continue;

            Types.TargetSelection.Builder selection = Types.TargetSelection.newBuilder().setId(group.getId());
            for (int i = 0; i < count; i++) selection.addTargets(choices.get(i));
            result.add(selection.build());
        }
        return result;
    }

    private static double targetScore(String id, Map<String, Types.CardP> cards,
                                      Types.GameStateP state, String actorId, String sourceText) {
        String intent = lower(sourceText);
        boolean hostile = containsAny(intent, "damage", "destroy", "kill", "banish", "exile",
                "discard", "weaken", "return to hand", "remove", "sacrifice", "counter", "cancel", "negate");
        boolean supportive = containsAny(intent, "heal", "gain health", "give", "add", "increase",
                "protect", "shield", "prevent", "buff", "ready", "strengthen", "gains +",
                "gets +", "boost", "power");
        boolean sacrifice = intent.contains("sacrifice");
        String selfId = state.getPlayer().getId();
        String opponentId = state.getOpponent().getId();

        if (id.equals(selfId) || id.equals(opponentId)) {
            if (hostile) return id.equals(opponentId) ? 5.0 : -6.0;
            if (supportive) return id.equals(selfId) ? 4.5 : -5.0;
            return id.equals(opponentId) ? 0.3 : 0.0;
        }

        Types.CardP target = cards.get(id);
        if (target == null) return 0.0;
        boolean friendly = actorId.equals(target.getController());
        double value = cardValue(target);
        boolean combatParticipant = target.hasCombat()
                && (!target.getCombat().getAttackTarget().isEmpty()
                || !target.getCombat().getBlockedAttacker().isEmpty());

        if (sacrifice) return friendly ? Math.max(0.2, 5.0 - value) : -5.0;
        if (hostile) {
            if (friendly) return -4.5 - value * 0.35;
            double lethalBonus = knownDamage(intent) >= effectiveHealth(target) ? 2.0 : 0.0;
            return 3.0 + value * 0.24 + lethalBonus + (combatParticipant ? 2.1 : 0.0);
        }
        if (supportive) {
            if (!friendly) return -3.0;
            double score = 2.2 + value * 0.12;
            if (combatParticipant) score += 3.0;
            if (target.hasCombat() && target.getCombat().getHealth() <= 2) score += 1.0;
            return score;
        }
        return friendly ? value * 0.12 : value * 0.16;
    }

    private static int knownDamage(String text) {
        Matcher matcher = NUMBER.matcher(text);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 0;
    }

    private static ClientGameMessages.ClientGameMessage selectFrom(ServerGameMessages.SelectFrom request) {
        Types.GameStateP state = request.getGame();
        Types.Targeting targets = request.getTargets();
        Map<String, Types.CardP> cards = cardsById(state);
        List<String> choices = new ArrayList<>(targets.getPossibleTargetsList());
        String intent = lower(targets.getTargetMessage());
        choices.sort(Comparator.comparingDouble((String id) ->
                selectValue(id, cards, state, intent)).reversed());

        int min = Math.max(0, targets.getMinTargets());
        int max = Math.max(min, targets.getMaxTargets());
        int count = Math.min(min, choices.size());
        for (int i = count; i < Math.min(max, choices.size()); i++) {
            if (selectValue(choices.get(i), cards, state, intent) <= 0.0) break;
            count++;
        }
        List<String> selected = new ArrayList<>(choices.subList(0, Math.min(count, choices.size())));
        return message().setSelectFromResponse(ClientGameMessages.SelectFromResponse.newBuilder()
                .setMessageType(request.getMessageType()).addAllSelected(selected)).build();
    }

    private static double selectValue(String id, Map<String, Types.CardP> cards,
                                      Types.GameStateP state, String intent) {
        Types.CardP card = cards.get(id);
        if (card == null) {
            if (id.equals(state.getOpponent().getId())) return intent.contains("damage") ? 5.0 : 0.5;
            if (id.equals(state.getPlayer().getId())) return intent.contains("heal") ? 5.0 : 0.0;
            return 0.0;
        }
        double value = cardValue(card);
        boolean friendly = state.getPlayer().getId().equals(card.getController());
        if (containsAny(intent, "discard", "sacrifice", "purge")) return friendly ? 5.0 - value : -5.0;
        if (containsAny(intent, "destroy", "damage", "kill", "remove", "banish", "exile")) {
            return friendly ? -5.0 - value : 3.0 + value + (card.hasCombat() ? card.getCombat().getAttack() * 0.4 : 0.0);
        }
        if (containsAny(intent, "return to hand", "bounce")) return friendly ? -3.0 : 3.0 + value;
        if (containsAny(intent, "top of", "draw", "choose a card")) return friendly ? value : value * 0.45;
        return friendly ? value * 0.2 : value * 0.25;
    }

    private static ClientGameMessages.ClientGameMessage assignDamage(ServerGameMessages.AssignDamage request) {
        List<String> targets = new ArrayList<>(request.getPossibleTargetsList());
        Types.GameStateP state = request.getGame();
        Map<String, Types.CardP> cards = cardsById(state);
        targets.sort(Comparator.comparingDouble((String id) ->
                targetScore(id, cards, state, state.getPlayer().getId(), "deal combat damage")).reversed());

        String chosen = null;
        if (request.getTrample()) {
            for (String id : targets) {
                if (cards.containsKey(id)) {
                    Types.CardP candidate = cards.get(id);
                    if (!candidate.getController().equals(state.getPlayer().getId())
                            && effectiveHealth(candidate) <= request.getTotalDamage()) {
                        chosen = id;
                        break;
                    }
                }
            }
            if (chosen == null && targets.contains(state.getOpponent().getId())) chosen = state.getOpponent().getId();
        }
        if (chosen == null && !targets.isEmpty()) chosen = targets.get(0);

        ClientGameMessages.AssignDamageResponse.Builder response =
                ClientGameMessages.AssignDamageResponse.newBuilder();
        if (chosen != null) {
            response.addDamageAssignments(Types.DamageAssignment.newBuilder()
                    .setTargetId(chosen).setDamage(Math.max(0, request.getTotalDamage())));
        }
        return message().setAssignDamageResponse(response).build();
    }

    private static ClientGameMessages.ClientGameMessage selectX(ServerGameMessages.SelectXValue request) {
        return message().setSelectXValueResponse(ClientGameMessages.SelectXValueResponse.newBuilder()
                .setSelectedXValue(Math.max(0, request.getMaxXValue()))).build();
    }

    private static ClientGameMessages.ClientGameMessage selectKnowledge(ServerGameMessages.SelectKnowledge request) {
        Types.Knowledge selected = bestKnowledge(request.getKnowledgeListList(), request.getGame());
        return message().setSelectKnowledgeResponse(ClientGameMessages.SelectKnowledgeResponse.newBuilder()
                .setSelectedKnowledge(selected)).build();
    }

    private static Types.Knowledge bestKnowledge(List<Types.Knowledge> options, Types.GameStateP state) {
        Types.Knowledge best = options.isEmpty() ? Types.Knowledge.NONE : options.get(0);
        double bestScore = Double.NEGATIVE_INFINITY;
        Map<Types.Knowledge, Integer> pool = knowledgePool(state.getPlayer());
        for (Types.Knowledge option : options) {
            double score = 0.0;
            for (Types.CardP card : state.getPlayer().getHandList()) {
                for (Types.KnowledgeGroup need : card.getKnowledgeCostList()) {
                    if (need.getKnowledge() == option && pool.getOrDefault(option, 0) < need.getCount()) {
                        score += cardValue(card) * (pool.getOrDefault(option, 0) + 1 >= need.getCount() ? 1.0 : 0.28);
                    }
                }
            }
            for (Types.KnowledgeGroup color : state.getPlayer().getDeckColorsList()) {
                if (color.getKnowledge() == option) score += color.getCount() * 0.08;
            }
            if (score > bestScore) {
                best = option;
                bestScore = score;
            }
        }
        return best;
    }

    private static Types.Knowledge bestKnowledgeFromGroups(List<Types.KnowledgeGroup> options,
                                                            Types.GameStateP state) {
        List<Types.Knowledge> colors = new ArrayList<>();
        for (Types.KnowledgeGroup option : options) colors.add(option.getKnowledge());
        return bestKnowledge(colors, state);
    }

    private static ClientGameMessages.ClientGameMessage orderCards(ServerGameMessages.OrderCards request) {
        List<Types.CardP> cards = new ArrayList<>(request.getCardsToOrderList());
        cards.sort(Comparator.comparingDouble(AiStrategy::cardValue).reversed());
        List<String> order = new ArrayList<>();
        for (Types.CardP card : cards) order.add(card.getId());
        return message().setOrderCardsResponse(ClientGameMessages.OrderCardsResponse.newBuilder()
                .addAllOrderedCards(order)).build();
    }

    private static ClientGameMessages.ClientGameMessage pickCard(ServerGameMessages.PickCard request) {
        Types.CardP best = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (Types.CardP card : request.getCandidatesList()) {
            double score = cardValue(card) + effectScore(card.getDescription());
            if (score > bestScore) {
                best = card;
                bestScore = score;
            }
        }
        return best == null ? null : message().setPickCardResponse(
                ClientGameMessages.PickCardResponse.newBuilder().setPickedCard(best.getId())).build();
    }

    private static ClientGameMessages.ClientGameMessage declareAttackers(Types.GameStateP state) {
        List<Types.CardP> defenders = state.getOpponent().getPlayList();
        ClientGameMessages.SelectAttackers.Builder attacks = ClientGameMessages.SelectAttackers.newBuilder();
        for (Types.CardP attacker : state.getPlayer().getPlayList()) {
            if (!attacker.getCanAttack() || !attacker.hasCombat()) continue;
            double pressure = attacker.getCombat().getAttack() * 0.8;
            if (state.getOpponent().getHealth() <= attacker.getCombat().getAttack()) pressure += 10.0;
            if (has(attacker.getCombat().getCombatAbilitiesList(), "Trample")) pressure += 0.7;
            if (has(attacker.getCombat().getCombatAbilitiesList(), "FirstStrike")) pressure += 0.7;

            double worstTrade = 0.0;
            for (Types.CardP defender : defenders) {
                if (!canPotentiallyBlock(defender, attacker)) continue;
                boolean attackerDies = wouldDieInCombat(attacker, defender);
                boolean defenderDies = wouldDieInCombat(defender, attacker);
                if (attackerDies && !defenderDies) {
                    worstTrade = Math.max(worstTrade, cardValue(attacker) * 0.75);
                } else if (attackerDies) {
                    worstTrade = Math.max(worstTrade,
                            (cardValue(attacker) - cardValue(defender)) * 0.45);
                }
            }
            if (pressure - worstTrade > 0.4) {
                attacks.addAttackers(Types.AttackerAssignment.newBuilder()
                        .setAttackerId(attacker.getId())
                        .setAttacksTo(state.getOpponent().getId()));
            }
        }
        return message().setSelectAttackers(attacks).build();
    }

    private static ClientGameMessages.ClientGameMessage declareBlockers(Types.GameStateP state) {
        List<Pair> pairs = new ArrayList<>();
        for (Types.CardP blocker : state.getPlayer().getPlayList()) {
            if (!blocker.getCanBlock() || !blocker.hasCombat()) continue;
            for (String attackerId : blocker.getCombat().getPossibleBlockTargetsList()) {
                Types.CardP attacker = findCard(state.getOpponent().getPlayList(), attackerId);
                if (attacker == null || !attacker.hasCombat()) continue;
                pairs.add(new Pair(blocker, attacker, blockScore(blocker, attacker, state)));
            }
        }
        pairs.sort(Comparator.comparingDouble((Pair pair) -> pair.score).reversed());
        Set<String> usedBlockers = new HashSet<>();
        Set<String> blockedAttackers = new HashSet<>();
        ClientGameMessages.SelectBlockers.Builder blocks = ClientGameMessages.SelectBlockers.newBuilder();
        for (Pair pair : pairs) {
            if (pair.score <= 0.0 || usedBlockers.contains(pair.blocker.getId())
                    || blockedAttackers.contains(pair.attacker.getId())) continue;
            usedBlockers.add(pair.blocker.getId());
            blockedAttackers.add(pair.attacker.getId());
            blocks.addBlockers(Types.BlockerAssignment.newBuilder()
                    .setBlockerId(pair.blocker.getId()).setBlockedBy(pair.attacker.getId()));
        }
        return message().setSelectBlockers(blocks).build();
    }

    private static double blockScore(Types.CardP blocker, Types.CardP attacker, Types.GameStateP state) {
        boolean attackerDies = wouldDieInCombat(attacker, blocker);
        boolean blockerDies = wouldDieInCombat(blocker, attacker);
        double score = attacker.getCombat().getAttack() * 0.65;
        if (has(attacker.getCombat().getCombatAbilitiesList(), "Trample")) {
            score = Math.max(0.0, attacker.getCombat().getAttack()
                    - effectiveHealth(blocker)) * 0.45;
        }
        if (state.getPlayer().getHealth() <= attacker.getCombat().getAttack()) score += 6.0;
        if (attackerDies) score += cardValue(attacker) * 0.72;
        if (blockerDies) score -= cardValue(blocker) * 0.78;
        if (!attackerDies && !blockerDies) {
            score += Math.min(blocker.getCombat().getAttack(), effectiveHealth(attacker)) * 0.15;
        }
        return score;
    }

    private static boolean canPotentiallyBlock(Types.CardP blocker, Types.CardP attacker) {
        if (blocker.getDepleted() || !blocker.hasCombat() || !attacker.hasCombat()) return false;
        List<String> attackerAbilities = attacker.getCombat().getCombatAbilitiesList();
        List<String> blockerAbilities = blocker.getCombat().getCombatAbilitiesList();
        if (has(attackerAbilities, "Unblockable")) return false;
        return !has(attackerAbilities, "Evasive") || has(blockerAbilities, "Evasive")
                || has(blockerAbilities, "Reach");
    }

    private static boolean wouldDieInCombat(Types.CardP target, Types.CardP source) {
        if (!target.hasCombat() || !source.hasCombat()) return false;
        int targetHealth = effectiveHealth(target);
        int sourceHealth = effectiveHealth(source);
        int targetDamage = source.getCombat().getAttack();
        int sourceDamage = target.getCombat().getAttack();
        boolean targetDead = false;
        boolean sourceDead = false;
        boolean targetFirst = has(target.getCombat().getCombatAbilitiesList(), "FirstStrike");
        boolean sourceFirst = has(source.getCombat().getCombatAbilitiesList(), "FirstStrike");

        if (targetFirst && sourceFirst) {
            targetDead = diesFrom(target, source, targetHealth, targetDamage);
            sourceDead = diesFrom(source, target, sourceHealth, sourceDamage);
        } else if (targetFirst) {
            sourceDead = diesFrom(source, target, sourceHealth, sourceDamage);
            if (!sourceDead) targetDead = diesFrom(target, source, targetHealth, targetDamage);
        } else if (sourceFirst) {
            targetDead = diesFrom(target, source, targetHealth, targetDamage);
            if (!targetDead) sourceDead = diesFrom(source, target, sourceHealth, sourceDamage);
        }

        if (!targetDead && !sourceDead) {
            targetDead = diesFrom(target, source, targetHealth, targetDamage);
            sourceDead = diesFrom(source, target, sourceHealth, sourceDamage);
        }
        return targetDead;
    }

    private static boolean diesFrom(Types.CardP target, Types.CardP source, int health, int damage) {
        return (has(source.getCombat().getCombatAbilitiesList(), "Deadly") && damage > 0)
                || damage >= health;
    }

    private static double cardValue(Types.CardP card) {
        double value = 1.6 + effectScore(card.getDescription()) * 0.48 + cardCost(card) * 0.24;
        if (isUnit(card) && card.hasCombat()) {
            value += card.getCombat().getAttack() * 0.72 + card.getCombat().getHealth() * 0.52
                    + card.getCombat().getShield() * 0.28
                    + combatAbilityValue(card.getCombat().getCombatAbilitiesList());
        }
        if (card.getTypesList().contains("Asset")) value += 1.2;
        return value;
    }

    private static double combatAbilityValue(List<String> abilities) {
        double value = 0.0;
        if (has(abilities, "Evasive")) value += 1.5;
        if (has(abilities, "Trample")) value += 1.1;
        if (has(abilities, "FirstStrike")) value += 1.0;
        if (has(abilities, "Deadly")) value += 1.5;
        if (has(abilities, "Lifelink") || has(abilities, "Drain")) value += 1.0;
        if (has(abilities, "Vigilance")) value += 0.8;
        if (has(abilities, "Blitz")) value += 0.5;
        return value;
    }

    private static boolean isUnit(Types.CardP card) {
        return card.getTypesList().contains("Unit");
    }

    private static int cardCost(Types.CardP card) {
        try {
            return Math.max(0, Integer.parseInt(card.getCost()));
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private static int effectiveHealth(Types.CardP card) {
        return card.hasCombat() ? Math.max(0, card.getCombat().getHealth())
                + Math.max(0, card.getCombat().getShield()) : Integer.MAX_VALUE;
    }

    private static int countUnits(List<Types.CardP> cards) {
        int count = 0;
        for (Types.CardP card : cards) if (isUnit(card)) count++;
        return count;
    }

    private static Map<Types.Knowledge, Integer> knowledgePool(Types.Player player) {
        Map<Types.Knowledge, Integer> result = new HashMap<>();
        for (Types.KnowledgeGroup group : player.getKnowledgePoolList()) {
            result.put(group.getKnowledge(), group.getCount());
        }
        return result;
    }

    private static Map<String, Types.CardP> cardsById(Types.GameStateP state) {
        Map<String, Types.CardP> result = new HashMap<>();
        addCards(result, state.getPlayer().getHandList());
        addCards(result, state.getPlayer().getPlayList());
        addCards(result, state.getPlayer().getDiscardPileList());
        addCards(result, state.getOpponent().getPlayList());
        addCards(result, state.getOpponent().getDiscardPileList());
        addCards(result, state.getStackList());
        return result;
    }

    private static void addCards(Map<String, Types.CardP> map, List<Types.CardP> cards) {
        for (Types.CardP card : cards) map.put(card.getId(), card);
    }

    private static Types.CardP findCard(List<Types.CardP> cards, String id) {
        for (Types.CardP card : cards) if (card.getId().equals(id)) return card;
        return null;
    }

    private static boolean has(List<String> values, String expected) {
        return values.contains(expected);
    }

    private static boolean isCombatTrick(String text) {
        return containsAny(text, "combat", "attack", "block", "first strike", "trample",
                "evasive", "deadly", "prevent", "shield", "damage", "heal", "health",
                "buff", "boost", "strengthen", "until end of turn", "gets +", "gain +");
    }

    private static boolean isStackResponse(String text) {
        return containsAny(text, "counter", "cancel", "negate", "destroy", "damage", "kill",
                "banish", "exile", "remove", "prevent", "return to hand", "bounce", "shield",
                "heal", "combat", "attack", "block", "first strike", "trample", "evasive",
                "deadly", "buff", "boost", "strengthen", "until end of turn", "gets +", "gain +");
    }

    private static boolean containsAny(String text, String... terms) {
        for (String term : terms) if (text.contains(term)) return true;
        return false;
    }

    private static String lower(String text) {
        return text == null ? "" : text.toLowerCase(Locale.ROOT);
    }

    private static ClientGameMessages.ClientGameMessage.Builder message() {
        return ClientGameMessages.ClientGameMessage.newBuilder();
    }

    private static final class Candidate {
        private final double score;
        private final ClientGameMessages.ClientGameMessage message;

        private Candidate(double score, ClientGameMessages.ClientGameMessage message) {
            this.score = score;
            this.message = message;
        }
    }

    private static final class Pair {
        private final Types.CardP blocker;
        private final Types.CardP attacker;
        private final double score;

        private Pair(Types.CardP blocker, Types.CardP attacker, double score) {
            this.blocker = blocker;
            this.attacker = attacker;
            this.score = score;
        }
    }
}

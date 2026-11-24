package com.visitor.game;

import com.visitor.card.Card;
import com.visitor.protocol.Types;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Picks a playable draft deck after reading the first five round packs. */
public final class DraftAi {
    private static final int PACKS_BEFORE_COMMITMENT = 5;
    private static final Types.Knowledge[] COLORS = {
            Types.Knowledge.PURPLE, Types.Knowledge.GREEN, Types.Knowledge.RED,
            Types.Knowledge.BLUE, Types.Knowledge.YELLOW};

    private final List<Card> seen = new ArrayList<>();
    private final Set<java.util.UUID> seenIds = new HashSet<>();
    private EnumSet<Types.Knowledge> committedColors = EnumSet.noneOf(Types.Knowledge.class);
    private int lastRoundSeen = -1;
    private int packsSeen;

    public Card choose(List<Card> offer, List<Card> drafted, int roundIndex) {
        if (offer.isEmpty()) throw new IllegalArgumentException("Draft offer is empty");
        if (roundIndex != lastRoundSeen) {
            lastRoundSeen = roundIndex;
            packsSeen++;
        }
        for (Card card : offer) {
            if (seenIds.add(card.getId())) seen.add(card);
        }
        // The fifth pack is completed before the sixth round begins.
        if (roundIndex >= PACKS_BEFORE_COMMITMENT && packsSeen >= PACKS_BEFORE_COMMITMENT
                && committedColors.isEmpty()) {
            committedColors = choosePalette(drafted);
        }

        Card best = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (Card card : offer) {
            double score = pickScore(card, drafted);
            if (score > bestScore) {
                best = card;
                bestScore = score;
            }
        }
        return best;
    }

    public int getPacksSeen() {
        return packsSeen;
    }

    public Set<Types.Knowledge> getCommittedColors() {
        return EnumSet.copyOf(committedColors);
    }

    private EnumSet<Types.Knowledge> choosePalette(List<Card> drafted) {
        EnumSet<Types.Knowledge> best = EnumSet.noneOf(Types.Knowledge.class);
        double bestScore = Double.NEGATIVE_INFINITY;
        for (int first = 0; first < COLORS.length; first++) {
            for (int second = first + 1; second < COLORS.length; second++) {
                EnumSet<Types.Knowledge> pair = EnumSet.of(COLORS[first], COLORS[second]);
                double pairScore = paletteScore(pair, drafted);
                if (pairScore > bestScore) {
                    best = pair;
                    bestScore = pairScore;
                }
                for (int third = second + 1; third < COLORS.length; third++) {
                    EnumSet<Types.Knowledge> trio = EnumSet.copyOf(pair);
                    trio.add(COLORS[third]);
                    double trioScore = paletteScore(trio, drafted);
                    if (trioScore > bestScore) {
                        best = trio;
                        bestScore = trioScore;
                    }
                }
            }
        }
        return best;
    }

    private double paletteScore(Set<Types.Knowledge> palette, List<Card> drafted) {
        // Picks matter more than cards merely offered. Three colors cost consistency.
        double score = palette.size() == 2 ? 4.0 : 0.0;
        for (Card card : drafted) {
            double quality = quality(card);
            score += palette.containsAll(colors(card)) ? quality * 1.8 : -quality * 0.9;
            score += matchingColors(palette, studyColors(card)) * 2.0;
        }
        for (Card card : seen) {
            if (palette.containsAll(colors(card))) score += quality(card) * 0.12;
            score += matchingColors(palette, studyColors(card)) * 0.2;
        }
        return score;
    }

    private double pickScore(Card card, List<Card> drafted) {
        Types.CardP details = card.toCardMessage().build();
        double score = AiStrategy.cardValue(details);
        int cost = parseCost(details.getCost());
        score -= Math.max(0, cost - 3) * 0.45;
        if (details.getTypesList().contains("Unit") && cost <= 3) {
            long cheapUnits = drafted.stream().filter(c -> {
                Types.CardP p = c.toCardMessage().build();
                return p.getTypesList().contains("Unit") && parseCost(p.getCost()) <= 3;
            }).count();
            if (cheapUnits * 3 < drafted.size() + 1) score += 1.2;
        }

        Set<Types.Knowledge> required = colors(card);
        if (!committedColors.isEmpty()) {
            int outside = 0;
            for (Types.Knowledge color : required) {
                if (!committedColors.contains(color)) outside++;
            }
            score += outside == 0 ? 8.0 : -30.0 * outside;
            Set<Types.Knowledge> produces = studyColors(card);
            int useful = matchingColors(committedColors, produces);
            score += useful * 2.0 - (produces.size() - useful) * 1.5;
        } else {
            EnumSet<Types.Knowledge> pickedColors = EnumSet.noneOf(Types.Knowledge.class);
            for (Card picked : drafted) pickedColors.addAll(colors(picked));
            if (!required.isEmpty() && pickedColors.containsAll(required)) score += 1.0;
            EnumSet<Types.Knowledge> combined = EnumSet.copyOf(pickedColors);
            combined.addAll(required);
            if (combined.size() > 3) score -= (combined.size() - 3) * 6.0;
            score -= Math.max(0, required.size() - 1) * 0.6;
        }
        return score;
    }

    private static Set<Types.Knowledge> colors(Card card) {
        EnumSet<Types.Knowledge> colors = EnumSet.noneOf(Types.Knowledge.class);
        for (Types.Knowledge color : card.knowledge.keySet()) {
            if (color != Types.Knowledge.NONE && card.knowledge.get(color) > 0) colors.add(color);
        }
        return colors;
    }

    private static Set<Types.Knowledge> studyColors(Card card) {
        EnumSet<Types.Knowledge> colors = EnumSet.noneOf(Types.Knowledge.class);
        if (!card.types.contains(Card.CardType.Tome)) return colors;
        if (card.text.contains("{P}")) colors.add(Types.Knowledge.PURPLE);
        if (card.text.contains("{G}")) colors.add(Types.Knowledge.GREEN);
        if (card.text.contains("{R}")) colors.add(Types.Knowledge.RED);
        if (card.text.contains("{B}")) colors.add(Types.Knowledge.BLUE);
        if (card.text.contains("{Y}")) colors.add(Types.Knowledge.YELLOW);
        return colors;
    }

    private static int matchingColors(Set<Types.Knowledge> palette, Set<Types.Knowledge> other) {
        int count = 0;
        for (Types.Knowledge color : other) {
            if (palette.contains(color)) count++;
        }
        return count;
    }

    private static double quality(Card card) {
        return AiStrategy.cardValue(card.toCardMessage().build());
    }

    private static int parseCost(String cost) {
        try {
            return Integer.parseInt(cost);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }
}

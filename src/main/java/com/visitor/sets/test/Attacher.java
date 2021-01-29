/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.test;

import com.visitor.card.types.Attachment;
import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.card.properties.Combat.CombatAbility.Vigilance;

/**
 * @author pseudo
 */
public class Attacher extends Attachment {

	public Attacher (Game game, UUID owner) {
		super(game, "Attacher",
				0, new CounterMap(),
				"Attach to Unit\n+1/+1",
				owner, Predicates::isUnit,
				attachedId -> game.addAttackAndHealth(attachedId, 1, 1),
				attachedId -> game.removeAttackAndHealth(attachedId, 1, 1));
	}
}

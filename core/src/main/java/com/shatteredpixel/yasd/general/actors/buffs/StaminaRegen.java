package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;

public class StaminaRegen extends Buff {

	{
		actPriority = HERO_PRIO + 1;
	}

	//Track whether regen is enabled. Will need to improve this code if I add stamina for mobs.
	public boolean regen = true;

	@Override
	public boolean act() {
		if (target.isAlive()) {

			float increase = 6f;
			if (target instanceof Hero && ((Hero)target).isStarving()) {
				increase /= 2f;
			}
			int max = target.maxStamina();
			if (target.stamina < max) {
				target.stamina = Math.min(max, target.stamina + increase);
			}
		}
		spend(TICK);
		return true;
	}

	public static void toggleRegen(Char ch, boolean regen) {
		StaminaRegen regenBuff = ch.buff(StaminaRegen.class);
		if (regenBuff != null) {
			regenBuff.regen = regen;
		}
	}
}

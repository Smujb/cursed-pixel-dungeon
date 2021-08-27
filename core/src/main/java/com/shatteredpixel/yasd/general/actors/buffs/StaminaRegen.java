package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.actors.hero.Hero;

public class StaminaRegen extends Buff {

	{
		actPriority = HERO_PRIO + 1;
	}

	//Track whether regen is enabled. Will need to improve this code if I add stamina for mobs.
	public static boolean regen = true;

	@Override
	public boolean act() {
		if (target.isAlive()) {
			Hero hero = (Hero) target;

			float increase = 4f;
			if (hero.isStarving()) {
				increase /= 2f;
			}
			int max = hero.maxStamina();
			if (hero.stamina < max) {
				hero.stamina = Math.min(max, hero.stamina + increase);
			}
		}
		spend(TICK);
		return true;
	}
}

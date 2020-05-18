package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.actors.hero.Hero;

public class StaminaRegen extends Buff {

	{
		actPriority = HERO_PRIO - 1;
	}

	@Override
	public boolean act() {
		if (target.isAlive() && target instanceof Hero) {
			Hero hero = (Hero) target;

			float increase = 1f;
			if (hero.isStarving()) {
				increase /= 2f;
			}
			int max = hero.maxStamina();
			if (hero.stamina < max) {
				hero.stamina = Math.min(max, hero.stamina + increase);
			}
		}
		spend( TICK );
		return true;
	}
}

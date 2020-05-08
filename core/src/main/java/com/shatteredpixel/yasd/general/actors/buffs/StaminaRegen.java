package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.actors.hero.Hero;

public class StaminaRegen extends Buff {

	{
		actPriority = HERO_PRIO - 1;
	}

	private static final float REGENERATION_DELAY = 20;

	@Override
	public boolean act() {
		if (target.isAlive() && target instanceof Hero) {
			//TODO
			spend( REGENERATION_DELAY );
		} else {
			diactivate();
		}
		return true;
	}
}

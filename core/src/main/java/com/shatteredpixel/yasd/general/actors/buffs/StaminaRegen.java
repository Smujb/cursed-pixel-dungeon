package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.actors.hero.Hero;

public class StaminaRegen extends Buff {

	{
		actPriority = HERO_PRIO - 1;
	}

	@Override
	public boolean act() {
		if (target.isAlive() && target instanceof Hero && !((Hero) target).isStarving()) {
			if (((Hero) target).stamina < ((Hero) target).maxStamina()) {
				((Hero) target).stamina++;
			}
			spend( TICK );
		} else {
			diactivate();
		}
		return true;
	}
}

package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroAction;

public class StaminaRegen extends Buff {

	{
		actPriority = HERO_PRIO - 1;
	}

	@Override
	public boolean act() {
		if (target.isAlive() && target instanceof Hero && !((Hero) target).isStarving()) {
			Hero hero = (Hero) target;
			int max = hero.maxStamina();
			if (hero.stamina < max && !(hero.curAction instanceof HeroAction.Attack)) {
				hero.stamina = Math.min(max, hero.stamina + 1);
			}
			spend( TICK );
		} else {
			diactivate();
		}
		return true;
	}
}

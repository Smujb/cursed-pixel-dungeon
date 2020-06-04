package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroAction;

public class StaminaRegen extends Buff {

	{
		actPriority = HERO_PRIO + 1;
	}

	//FIXME messy way to prevent HP regenerating while the hero attacks. Probably should improve this.
	public static boolean regen = true;

	@Override
	public boolean act() {
		if (target.isAlive() && target instanceof Hero && !(((Hero) target).curAction instanceof HeroAction.Attack)) {
			if (!regen) {
				regen = true;
			} else {
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
		}
		spend( TICK );
		return true;
	}
}

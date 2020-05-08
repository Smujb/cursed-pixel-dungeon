package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.actors.hero.Hero;

public class MpRegen extends Buff {

	{
		actPriority = HERO_PRIO - 1;
	}

	private static final float REGENERATION_DELAY = 20;

	@Override
	public boolean act() {
		if (target.isAlive() && target instanceof Hero) {
			Hero hero = ((Hero)target);
			if (hero.mp < hero.maxMP() && !hero.isStarving()) {
				LockedFloor lock = hero.buff(LockedFloor.class);
				if ((lock == null || lock.regenOn()) && hero.buff(MagicImmune.class) == null) {
					hero.mp++;
				}
			}
			spend( REGENERATION_DELAY );
		} else {
			diactivate();
		}
		return true;
	}
}

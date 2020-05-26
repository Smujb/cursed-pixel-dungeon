package com.shatteredpixel.yasd.general.items.rings;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.messages.Messages;

public abstract class HeroStatRing extends Ring {
	public String statsInfo() {
		if (isIdentified()){
			return Messages.get(this, "stats", Math.round(10 * soloMultiplier()));
		} else {
			return Messages.get(this, "typical_stats", Math.round(10 * multiplier(0)));
		}
	}

	public static int statBonus(Char target, Class<? extends RingBuff> buffClass) {
		return (int) (10 * multiplier(target, buffClass));
	}
}
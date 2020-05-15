package com.shatteredpixel.yasd.general.ui.attack;

import com.shatteredpixel.yasd.general.Dungeon;

public class NormalAttackIndicator extends AttackIndicator {

	@Override
	protected void onClick() {
		if (enabled) {
			if (Dungeon.hero.handle( lastTarget.pos )) {
				Dungeon.hero.next();
			}
		}
	}
}

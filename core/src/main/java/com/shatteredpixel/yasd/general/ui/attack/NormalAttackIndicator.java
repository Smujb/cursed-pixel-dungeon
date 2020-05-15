package com.shatteredpixel.yasd.general.ui.attack;

import com.shatteredpixel.yasd.general.CPDAction;
import com.shatteredpixel.yasd.general.Dungeon;
import com.watabou.input.GameAction;

public class NormalAttackIndicator extends AttackIndicator {

	@Override
	public GameAction keyAction() {
		return CPDAction.TAG_ATTACK;
	}

	@Override
	protected void onClick() {
		if (enabled) {
			if (Dungeon.hero.handle( lastTarget.pos )) {
				Dungeon.hero.next();
			}
		}
	}
}

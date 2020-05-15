package com.shatteredpixel.yasd.general.ui.attack;

import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;

public class CrushAttackIndicator extends AttackIndicator {

	public CrushAttackIndicator() {
		color(0x0000FF);
	}

	@Override
	protected void onClick() {
		if (enabled && lastTarget != null) {
			CPDGame.shake(3);
			Dungeon.hero.doAttack(lastTarget, Char.AttackType.CRUSH);
		}
	}
}

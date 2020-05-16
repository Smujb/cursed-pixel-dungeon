package com.shatteredpixel.yasd.general.ui.attack;

import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.HeroAction;

public class CrushAttackIndicator extends AttackIndicator {

	public CrushAttackIndicator() {
		color(0x0000FF);
	}

	@Override
	protected void onClick() {
		if (enabled && lastTarget != null) {
			if (Dungeon.hero.canAttack(lastTarget)){
				CPDGame.shake(3);
				Dungeon.hero.curAction = new HeroAction.Attack( lastTarget, Char.AttackType.CRUSH );
				Dungeon.hero.next();
			}
		}
	}
}

package com.shatteredpixel.yasd.general.ui.attack;

import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.hero.HeroAction;

public class CrushAttackIndicator extends AttackIndicator {
	{
	}

	public CrushAttackIndicator() {
		color(Constants.Colours.PURE_BLUE);
	}

	@Override
	protected void onClick() {
		if (enabled && lastTarget != null) {
			if (Dungeon.hero.canAttack(lastTarget)){
				Dungeon.hero.curAction = new HeroAction.Attack( lastTarget, Dungeon.hero.curItem());
				Dungeon.hero.next();
			}
		}
	}
}

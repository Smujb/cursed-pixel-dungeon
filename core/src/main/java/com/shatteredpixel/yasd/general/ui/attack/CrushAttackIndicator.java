package com.shatteredpixel.yasd.general.ui.attack;

import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.HeroAction;

public class CrushAttackIndicator extends AttackIndicator {
	{
		attackType = Char.AttackType.CRUSH;
	}

	public CrushAttackIndicator() {
		color(Constants.Colours.PURE_BLUE);
	}

	@Override
	protected void onClick() {
		if (enabled && lastTarget != null) {
			if (Dungeon.hero.canAttack(lastTarget)){
				Dungeon.hero.curAction = new HeroAction.Attack( lastTarget, attackType );
				Dungeon.hero.next();
			}
		}
	}
}

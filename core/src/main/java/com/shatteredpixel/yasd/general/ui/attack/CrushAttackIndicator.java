package com.shatteredpixel.yasd.general.ui.attack;

import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.watabou.utils.Callback;

public class CrushAttackIndicator extends AttackIndicator {
	@Override
	protected void onClick() {
		if (enabled && lastTarget != null) {
			Dungeon.hero.sprite.attack(lastTarget.pos, new Callback() {
				@Override
				public void call() {
					CPDGame.shake(3);
					Dungeon.hero.attack(lastTarget, true, Char.AttackType.CRUSH);
				}
			});
		}
	}
}

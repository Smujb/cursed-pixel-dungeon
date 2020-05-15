package com.shatteredpixel.yasd.general.ui.attack;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class SpinAttackIndicator extends AttackIndicator {

	@Override
	protected void onClick() {
		if (enabled && lastTarget != null) {
			final Hero hero = Dungeon.hero;
			hero.sprite.centerEmitter().start( Speck.factory( Speck.REPAIR ), 0.02f, 20 );
			hero.sprite.attack(lastTarget.pos, new Callback() {
				@Override
				public void call() {
					for (int i : PathFinder.NEIGHBOURS8) {
						Char ch = Actor.findChar(hero.pos + i);
						if (ch != null) {
							hero.attack(ch);
						}
					}
				}
			});
		}
	}
}

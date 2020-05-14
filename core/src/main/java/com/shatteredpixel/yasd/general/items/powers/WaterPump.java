package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.levels.SewerLevel;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.noosa.Camera;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WaterPump extends Power {

	{
		image = ItemSpriteSheet.WATERPUMP;
		mp_cost = 7;
	}

	@Override
	public void onZap(Ballistica shot) {
		super.onZap(shot);
		int pos = shot.collisionPos;
		boolean[] passable = Dungeon.level.passable();
		PathFinder.buildDistanceMap(pos, passable, 3);
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (PathFinder.distance[mob.pos] < Integer.MAX_VALUE) {
				mob.damage(Random.IntRange(4 + Dungeon.getScaleFactor() * 2, 10 + Dungeon.getScaleFactor() * 6), new Char.DamageSrc(Element.WATER, this));
				mob.sprite.emitter().burst(SewerLevel.WaterParticle.FACTORY, 30);
			}
		}
		Camera.main.shake(3, 1f);
	}
}

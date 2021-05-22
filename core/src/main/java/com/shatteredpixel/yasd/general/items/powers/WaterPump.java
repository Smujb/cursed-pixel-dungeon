package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.levels.chapters.sewers.SewerLevel;
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
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				Char ch = Actor.findChar(i);
				if (ch != null && ch.alignment == Char.Alignment.ENEMY) {
					ch.damage(Random.IntRange(4 + Dungeon.getScaling() * 2, 10 + Dungeon.getScaling() * 6), new Char.DamageSrc(Element.WATER, this));
				}
				CellEmitter.get(i).burst(SewerLevel.WaterParticle.FACTORY, 10);
			}
		}
		Camera.main.shake(3, 1f);
	}
}

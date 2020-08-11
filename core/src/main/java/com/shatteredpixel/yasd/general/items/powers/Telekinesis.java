package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.wands.WandOfBlastWave;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.utils.Callback;

public class Telekinesis extends Power {
	{
		image = ItemSpriteSheet.TELEKINESIS;
		mp_cost = 4;

		usesTargeting = true;
	}

	@Override
	public void onZap(Ballistica shot) {
		if (!(curUser instanceof Hero)) return;
		int level = ((Hero) curUser).lvl;

		int cell = shot.collisionPos;

		int dist = shot.dist;
		if (shot.dist > level/5 + 4) {
			dist = level + 4;
		} else if (Actor.findChar(cell) != null) {
			dist--;
		}
		shot = new Ballistica(shot.sourcePos, shot.path.get(dist), Ballistica.PROJECTILE);

		for (Integer affectedCell : shot.path) {
			Char ch = Actor.findChar(affectedCell);
			if (ch != null && ch != curUser) {
				WandOfBlastWave.throwChar(ch, shot, 3 );
			}
			Dungeon.level.pressCell(affectedCell);
			Heap heap = Dungeon.level.heaps.get(affectedCell);
			if (heap != null && heap.type == Heap.Type.HEAP) {
				for (Item item : heap.items) {
					if (!item.collect()) {
						Dungeon.level.drop(item, heap.pos).sprite.drop();
					}
				}
				heap.pickUp();
			}
		}
	}

	@Override
	public void fx(Ballistica shot, Callback callback) {
		MagicMissile.boltFromChar(
				curUser.sprite.parent,
				MagicMissile.SPIRAL,
				curUser.sprite,
				shot.collisionPos,
				callback);
	}
}

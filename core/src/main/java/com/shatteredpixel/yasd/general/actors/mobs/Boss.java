package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.items.CrimsonFlask;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfUpgrade;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Boss extends Mob {

	{
		properties.add(Property.BOSS);
	}

	@Override
	public void die(DamageSrc cause) {
		int blobs = Random.chances(new  float[]{0, 0, 6, 3, 1});
		for (int i = 0; i < blobs; i++){
			int ofs;
			do {
				ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
			} while (!Dungeon.level.passable(pos + ofs));
			Dungeon.level.drop( new ScrollOfUpgrade(), pos + ofs ).sprite.drop( pos );
		}
		Dungeon.level.drop(new CrimsonFlask.Charge(), pos);
		super.die(cause);
	}
}

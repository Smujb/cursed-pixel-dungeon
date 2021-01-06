package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Challenges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.items.CrimsonFlask;
import com.shatteredpixel.yasd.general.items.powers.LuckyBadge;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfUpgrade;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class Boss extends Mob {

	{
		properties.add(Property.BOSS);
	}

	protected String rematchLevel = null;

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
		if (!Dungeon.isChallenged(Challenges.NO_HEALING)) {
			Dungeon.level.drop(new CrimsonFlask.Charge().quantity(Dungeon.difficulty.flaskChargesPerBoss()), pos);
		}
		if (rematchLevel != null && !LuckyBadge.rematchLevels.contains(rematchLevel)) LuckyBadge.rematchLevels.add(rematchLevel);
		super.die(cause);
	}

	public static String rematchLevel(Class<? extends Boss> cl) {
		return Reflection.forceNewInstance(cl).rematchLevel;
	}
}

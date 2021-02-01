package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Challenges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.items.CrimsonFlask;
import com.shatteredpixel.yasd.general.items.powers.LuckyBadge;
import com.shatteredpixel.yasd.general.levels.GrindLevel;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class Boss extends Mob {

	{
		properties.add(Property.BOSS);
	}

	@Override
	protected void onCreate() {
		super.onCreate();
		if (isRematch()) {
			Buff.affect(this, GrindLevel.LootBuff.class).setLoot(Random.IntRange(30, 50));
		}
	}

	protected String rematchLevel = null;

	@Override
	public void die(DamageSrc cause) {
		if (rematchLevel != null && !isRematch()) {
			LuckyBadge.addRematch(rematchLevel);
			if (!Dungeon.isChallenged(Challenges.NO_HEALING)) {
				Dungeon.level.drop(new CrimsonFlask.Charge().quantity(Dungeon.difficulty.flaskChargesPerBoss()), pos);
			}
		}
		super.die(cause);
	}

	protected boolean isRematch() {
		return LuckyBadge.rematchLevels().contains(rematchLevel);
	}

	public static String rematchLevel(Class<? extends Boss> cl) {
		return Reflection.forceNewInstance(cl).rematchLevel;
	}
}

package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.items.Gold;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import org.jetbrains.annotations.NotNull;

public class Greed extends Power {
	{
		mp_cost = 5;
		image = ItemSpriteSheet.GREED;
	}

	@Override
	public void onZap(Ballistica shot) {
		super.onZap(shot);
		int pos = shot.collisionPos;
		Char ch = Actor.findChar(pos);
		if (ch == null) {
			return;
		}
		Buff.affect(ch, GreedBuff.class);
	}

	public void fx(Ballistica shot, Callback callback) {
		MagicMissile.boltFromChar( curUser.sprite.parent,
				MagicMissile.COIN,
				curUser.sprite,
				shot.collisionPos,
				callback);
		Sample.INSTANCE.play( Assets.SND_GOLD );
	}


	public static class GreedBuff extends Buff {

		private float factor;

		private static final String FACTOR = "factor";

		{
			type = buffType.NEGATIVE;
		}

		@Override
		public boolean attachTo(@NotNull Char target) {
			factor = target.hpPercent();
			return super.attachTo(target);
		}

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(FACTOR, factor);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			factor = bundle.getFloat(FACTOR);
		}

		@Override
		public String toString() {
			return Messages.get(this, "name");
		}

		@Override
		public void detach() {
			Dungeon.level.drop(new Gold().random(4*factor), target.pos).sprite.drop();
			super.detach();
		}

		@Override
		public boolean act() {
			spend( TICK );
			return true;
		}
	}
}

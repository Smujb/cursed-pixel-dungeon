package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;

public class BubbleShield extends Power {
	{
		mp_cost = 5;
		image = ItemSpriteSheet.BUBBLESHIELD;
	}

	@Override
	protected void onUse(Hero hero) {
		super.onUse(hero);
		Buff.affect(hero, BubbleShieldBuff.class);
	}

	public static class BubbleShieldBuff extends Buff {

		{
			type = buffType.POSITIVE;
		}

		@Override
		public String toString() {
			return Messages.get(this, "name");
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc");
		}

		@Override
		public void fx(boolean on) {
			if (on) target.sprite.add(CharSprite.State.SHIELDED);
			else target.sprite.remove(CharSprite.State.SHIELDED);
		}

		@Override
		public int icon() {
			return BuffIndicator.ARMOR;
		}
	}
}

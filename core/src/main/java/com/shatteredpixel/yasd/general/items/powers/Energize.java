package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Recharging;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class Energize extends Power {

	{
		image = ItemSpriteSheet.ENERGIZE;
		mp_cost = 5;

		usesTargeting = false;
	}

	@Override
	protected void onUse(Hero hero) {
		super.onUse(hero);
		Buff.affect(hero, Recharging.class, ScrollOfRecharging.BUFF_DURATION/4f);
	}
}

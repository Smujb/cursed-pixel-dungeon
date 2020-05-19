package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;

public abstract class CorruptedSpell extends Power {
	{
		mp_cost = -1;
	}

	protected int hp_cost = -1;

	@Override
	protected boolean use() {
		curUser.damage((int) (hp_cost/100f * curUser.HT), new Char.DamageSrc(Element.SPIRIT, this));
		return true;
	}
}

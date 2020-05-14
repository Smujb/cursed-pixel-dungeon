package com.shatteredpixel.yasd.general.items.bags;

import com.shatteredpixel.yasd.general.actors.hero.Belongings;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.powers.Power;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class PowerHolder extends Bag {
	{
		image = ItemSpriteSheet.SPELLBOOK;

		size = Belongings.BACKPACK_SIZE;

		unique = true;

		bones = false;
	}

	@Override
	public boolean grab( Item item ) {
		return item instanceof Power;
	}

}

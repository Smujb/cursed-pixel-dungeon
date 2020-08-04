package com.shatteredpixel.yasd.general.items.bags;

import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.powers.Power;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class PowerHolder extends Bag {
	{
		image = ItemSpriteSheet.SPELLBOOK;

		unique = true;

		bones = false;
	}

	@Override
	public boolean canHold( Item item ) {
		if (item instanceof Power){
			return super.canHold(item);
		} else {
			return false;
		}
	}

}

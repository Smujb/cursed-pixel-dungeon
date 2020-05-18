package com.shatteredpixel.yasd.general.items.bags;

import com.shatteredpixel.yasd.general.actors.hero.Belongings;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.bombs.Bomb;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class BombBag extends Bag {
	{
		image = ItemSpriteSheet.POUCH;

		size = Belongings.BACKPACK_SIZE;
	}

	@Override
	public boolean grab(Item item) {
		return item instanceof Bomb;
	}
}

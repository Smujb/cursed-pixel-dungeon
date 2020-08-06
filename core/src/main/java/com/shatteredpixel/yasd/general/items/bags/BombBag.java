package com.shatteredpixel.yasd.general.items.bags;

import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.bombs.Bomb;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class BombBag extends Bag {
	{
		image = ItemSpriteSheet.POUCH;
	}

	@Override
	public int capacity() {
		return super.capacity()-1;
	}

	@Override
	public boolean canHold( Item item ) {
		if (item instanceof Bomb){
			return super.canHold(item);
		} else {
			return false;
		}
	}
}

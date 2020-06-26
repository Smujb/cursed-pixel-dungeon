package com.shatteredpixel.yasd.general.items.allies;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.sprites.FireDragonSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class FireDragonCrystal extends DragonCrystal {
	{
		image = ItemSpriteSheet.RED_DRAGON_CRYSTAL;
	}

	@Override
	protected Class<? extends Dragon> dragonType() {
		return FireDragon.class;
	}

	public static class FireDragon extends DragonCrystal.Dragon {
		{
			spriteClass = FireDragonSprite.class;
		}

		@Override
		public Element elementalType() {
			return Element.FIRE;
		}

		@Override
		protected Class<? extends DragonCrystal> crystalType() {
			return FireDragonCrystal.class;
		}
	}
}

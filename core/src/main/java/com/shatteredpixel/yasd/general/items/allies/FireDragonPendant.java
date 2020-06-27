package com.shatteredpixel.yasd.general.items.allies;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.sprites.FireDragonSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class FireDragonPendant extends DragonPendant {
	{
		image = ItemSpriteSheet.RED_DRAGON_CRYSTAL;
	}

	@Override
	protected Class<? extends Dragon> dragonType() {
		return FireDragon.class;
	}

	public static class FireDragon extends DragonPendant.Dragon {
		{
			spriteClass = FireDragonSprite.class;
			damageFactor = 1.5f;
			healthFactor = 2/3f;
			baseSpeed = 1.2f;
			accuracyFactor = 0.7f;
		}

		@Override
		public Element elementalType() {
			return Element.FIRE;
		}

		@Override
		protected Class<? extends DragonPendant> pendantType() {
			return FireDragonPendant.class;
		}
	}
}

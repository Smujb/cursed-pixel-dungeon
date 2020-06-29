package com.shatteredpixel.yasd.general.items.allies;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.sprites.DragonSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class DarkDragonPendant extends DragonPendant {

	{
		image = ItemSpriteSheet.ADORNED_DRAGON_CRYSTAL;
	}

	@Override
	protected Class<? extends Dragon> dragonType() {
		return DarkDragon.class;
	}

	public static class DarkDragon extends DragonPendant.Dragon {
		{
			spriteClass = DragonSprite.Dark.class;
			damageFactor = 1.5f;
			baseSpeed = 1.5f;
			healthFactor = 0.5f;
			resistances.put(Element.SHADOW, 0f);
		}

		@Override
		public Element elementalType() {
			return Element.SHADOW;
		}

		@Override
		protected Class<? extends DragonPendant> pendantType() {
			return DarkDragonPendant.class;
		}
	}
}

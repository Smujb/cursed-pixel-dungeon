package com.shatteredpixel.yasd.general.items.allies;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.sprites.VampiricDragonSprite;

public class VampiricDragonPendant extends DragonPendant {

	{
		image = ItemSpriteSheet.GREEN_DRAGON_CRYSTAL;
	}

	@Override
	protected Class<? extends Dragon> dragonType() {
		return VampiricDragon.class;
	}

	public static class VampiricDragon extends DragonPendant.Dragon {
		{
			spriteClass = VampiricDragonSprite.class;
			healthFactor = 0.7f;
			damageFactor = 0.8f;
			resistances.put(Element.DRAIN, 0f);
		}

		@Override
		public Element elementalType() {
			return Element.DRAIN;
		}

		@Override
		protected Class<? extends DragonPendant> pendantType() {
			return VampiricDragonPendant.class;
		}
	}
}
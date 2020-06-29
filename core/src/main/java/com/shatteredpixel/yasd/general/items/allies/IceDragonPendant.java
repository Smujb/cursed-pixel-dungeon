package com.shatteredpixel.yasd.general.items.allies;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.sprites.DragonSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class IceDragonPendant extends DragonPendant {

	{
		image = ItemSpriteSheet.LIGHT_BLUE_DRAGON_CRYSTAL;
	}

	@Override
	protected Class<? extends Dragon> dragonType() {
		return IceDragon.class;
	}

	public static class IceDragon extends DragonPendant.Dragon {
		{
			spriteClass = DragonSprite.Ice.class;
			resistances.put(Element.COLD, 0f);
		}

		@Override
		public Element elementalType() {
			return Element.COLD;
		}

		@Override
		protected Class<? extends DragonPendant> pendantType() {
			return IceDragonPendant.class;
		}
	}
}
package com.shatteredpixel.yasd.general.items.allies;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.sprites.DragonSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class GrassDragonPendant extends DragonPendant {

	{
		image = ItemSpriteSheet.GREEN_DRAGON_CRYSTAL;
	}

	@Override
	protected Class<? extends Dragon> dragonType() {
		return GrassDragon.class;
	}

	public static class GrassDragon extends DragonPendant.Dragon {
		{
			spriteClass = DragonSprite.Grass.class;
		}

		@Override
		public Element elementalType() {
			return Element.GRASS;
		}

		@Override
		protected Class<? extends DragonPendant> pendantType() {
			return GrassDragonPendant.class;
		}
	}
}

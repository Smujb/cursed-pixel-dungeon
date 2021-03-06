package com.shatteredpixel.yasd.general.items.relics.dragonpendants;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.items.relics.DragonPendant;
import com.shatteredpixel.yasd.general.sprites.DragonSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class WaterDragonPendant extends DragonPendant {

	{
		image = ItemSpriteSheet.BLUE_DRAGON_CRYSTAL;
	}

	@Override
	protected Class<? extends Dragon> dragonType() {
		return WaterDragon.class;
	}

	public static class WaterDragon extends DragonPendant.Dragon {
		{
			spriteClass = DragonSprite.Water.class;
			evasionFactor = 1.5f;
			baseSpeed = 1.5f;
			damageFactor = 2/3f;
			drFactor = 0f;
			elementaldrFactor = 0f;
			resistances.put(Element.WATER, 0f);
		}

		@Override
		public Element elementalType() {
			return Element.WATER;
		}

		@Override
		protected Class<? extends DragonPendant> pendantType() {
			return WaterDragonPendant.class;
		}
	}
}

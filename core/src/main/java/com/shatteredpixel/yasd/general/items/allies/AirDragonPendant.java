package com.shatteredpixel.yasd.general.items.allies;

import com.shatteredpixel.yasd.general.sprites.DragonSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class AirDragonPendant extends DragonPendant {
	{
		image = ItemSpriteSheet.WHITE_DRAGON_CRYSTAL;
	}

	@Override
	protected Class<? extends Dragon> dragonType() {
		return AirDragon.class;
	}

	public static class AirDragon extends DragonPendant.Dragon {
		{
			spriteClass = DragonSprite.Air.class;
			baseSpeed = 1.5f;
			healthFactor = 0.5f;
			evasionFactor = 1.5f;
		}

		@Override
		protected Class<? extends DragonPendant> pendantType() {
			return AirDragonPendant.class;
		}
	}
}

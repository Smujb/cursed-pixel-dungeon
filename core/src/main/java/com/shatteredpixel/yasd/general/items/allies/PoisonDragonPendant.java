package com.shatteredpixel.yasd.general.items.allies;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.sprites.PoisonDragonSprite;

public class PoisonDragonPendant extends DragonPendant {

	{
		image = ItemSpriteSheet.PURPLE_DRAGON_CRYSTAL;
	}

	@Override
	protected Class<? extends Dragon> dragonType() {
		return PoisonDragon.class;
	}

	public static class PoisonDragon extends DragonPendant.Dragon {
		{
			spriteClass = PoisonDragonSprite.class;
			damageFactor = 1.3f;
			evasionFactor = 0.7f;
			resistances.put(Element.TOXIC, 0f);
		}

		@Override
		public Element elementalType() {
			return Element.TOXIC;
		}

		@Override
		protected Class<? extends DragonPendant> pendantType() {
			return PoisonDragonPendant.class;
		}
	}
}
package com.shatteredpixel.yasd.general.items.allies;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.DragonSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class StoneDragonPendant extends DragonPendant {

	{
		image = ItemSpriteSheet.YELLOW_DRAGON_CRYSTAL;

		statScaling.add(Hero.HeroStat.RESILIENCE);
	}

	@Override
	protected Class<? extends Dragon> dragonType() {
		return StoneDragon.class;
	}

	public static class StoneDragon extends DragonPendant.Dragon {

		{
			spriteClass = DragonSprite.Stone.class;
			damageFactor = 2/3f;
			healthFactor = 1.5f;
			resistances.put(Element.STONE, 0f);
		}

		@Override
		public Element elementalType() {
			return Element.STONE;
		}

		@Override
		protected Class<? extends DragonPendant> pendantType() {
			return StoneDragonPendant.class;
		}
	}
}

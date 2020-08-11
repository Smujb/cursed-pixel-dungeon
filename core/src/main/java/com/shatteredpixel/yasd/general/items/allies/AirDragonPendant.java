package com.shatteredpixel.yasd.general.items.allies;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.DragonSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class AirDragonPendant extends DragonPendant {
	{
		image = ItemSpriteSheet.WHITE_DRAGON_CRYSTAL;

		statScaling.add(Hero.HeroStat.ASSAULT);
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
		public Element elementalType() {
			return Element.AIR;
		}

		@Override
		protected Class<? extends DragonPendant> pendantType() {
			return AirDragonPendant.class;
		}
	}
}

package com.shatteredpixel.yasd.general.items.allies;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.DragonSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class FireDragonPendant extends DragonPendant {
	{
		image = ItemSpriteSheet.RED_DRAGON_CRYSTAL;

		statScaling.add(Hero.HeroStat.EXECUTION);
	}

	@Override
	protected Class<? extends Dragon> dragonType() {
		return FireDragon.class;
	}

	public static class FireDragon extends DragonPendant.Dragon {
		{
			spriteClass = DragonSprite.Fire.class;
			damageFactor = 1.5f;
			healthFactor = 2/3f;
			baseSpeed = 1.2f;
			accuracyFactor = 0.7f;
			resistances.put(Element.FIRE, 0f);
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

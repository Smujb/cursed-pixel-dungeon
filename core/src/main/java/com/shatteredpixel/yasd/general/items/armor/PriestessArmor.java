package com.shatteredpixel.yasd.general.items.armor;

import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class PriestessArmor extends ClothArmor {

	{
		image = ItemSpriteSheet.ARMOR_PRIESTESS;
		EVA = 1.25f;
		magicalDRFactor = 1.5f;
		DRfactor = 0.75f;

		statScaling.add(Hero.HeroStat.SUPPORT);
	}

	@Override
	public int appearance() {
		return 6;
	}
}
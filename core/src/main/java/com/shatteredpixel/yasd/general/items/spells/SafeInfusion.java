package com.shatteredpixel.yasd.general.items.spells;

import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class SafeInfusion extends MagicalInfusion {

	{
		image = ItemSpriteSheet.SAFE_INFUSION;
	}

	@Override
	protected void removeUpgrades(Item item) {

	}

	public static class Recipe extends com.shatteredpixel.yasd.general.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{MagicalInfusion.class, SafeUpgrade.class, ArcaneInfusion.class};
			inQuantity = new int[]{1, 1, 1};

			cost = 12;

			output = SafeInfusion.class;
			outQuantity = 1;
		}
	}
}

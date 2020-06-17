package com.shatteredpixel.yasd.general.items.spells;

import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;

public class Degrade extends InventorySpell {

	{
		image = ItemSpriteSheet.DEGRADE;
	}

	@Override
	protected void onItemSelected(Item item) {
		item.degrade();
		GLog.n( Messages.get(this, "degrade", item.name()) );
	}

	@Override
	public int price() {
		return Math.round(quantity * 10);
	}

	public static class Recipe extends com.shatteredpixel.yasd.general.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{ScrollOfUpgrade.class, ScrollOfRetribution.class};
			inQuantity = new int[]{1, 1};

			cost = 4;

			output = Degrade.class;
			outQuantity = 5;
		}

	}
}

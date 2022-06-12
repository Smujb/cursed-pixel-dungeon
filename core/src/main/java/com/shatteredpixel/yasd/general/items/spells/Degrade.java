package com.shatteredpixel.yasd.general.items.spells;

import com.shatteredpixel.yasd.general.items.EquipableItem;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfGreed;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndBag;

public class Degrade extends InventorySpell {

	{
		image = ItemSpriteSheet.DEGRADE;

		mode = WndBag.Mode.EQUIPMENT;
	}

	@Override
	protected void onItemSelected(Item item) {
		if (item instanceof EquipableItem) {
			//Specify in terms of max durability constant so I can tweak durability speed without messing with repair/degrade amounts
			item.reduceDurability((int) (Item.MAX_DURABILITY*0.3f));
			GLog.negative(Messages.get(this, "degrade", item.name()));
		}
	}

	@Override
	public int price() {
		return Math.round(quantity * 10);
	}

	public static class Recipe extends com.shatteredpixel.yasd.general.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{ScrollOfGreed.class, ScrollOfGreed.class};
			inQuantity = new int[]{1, 1};

			cost = 4;

			output = Degrade.class;
			outQuantity = 5;
		}

	}
}

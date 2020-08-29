package com.shatteredpixel.yasd.general.items.spells;

import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Statistics;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndBag;

public class SafeUpgrade extends InventorySpell {

	{
		mode = WndBag.Mode.UPGRADEABLE;
		image = ItemSpriteSheet.SAFE_UPGRADE;
	}

	@Override
	protected void onItemSelected( Item item ) {

		if (item instanceof Weapon && ((Weapon) item).enchantment != null) {
			((Weapon) item).upgrade(true);
		} else {
			item.upgrade();
		}

		GLog.positive( Messages.get(this, "infuse", item.name()) );

		Badges.validateItemLevelAquired(item);

		curUser.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);
		Statistics.upgradesUsed++;
	}

	@Override
	public int price() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * 150);
	}

	public static class Recipe extends com.shatteredpixel.yasd.general.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{ScrollOfUpgrade.class, ArcaneCatalyst.class};
			inQuantity = new int[]{2, 1};

			cost = 4;

			output = SafeUpgrade.class;
			outQuantity = 1;
		}

	}
}

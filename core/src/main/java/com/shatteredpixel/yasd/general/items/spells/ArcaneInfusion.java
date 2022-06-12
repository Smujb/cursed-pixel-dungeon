package com.shatteredpixel.yasd.general.items.spells;

import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Statistics;
import com.shatteredpixel.yasd.general.effects.Enchanting;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.Enchantable;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfGreed;
import com.shatteredpixel.yasd.general.items.stones.StoneOfEnchantment;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndBag;
import com.watabou.utils.Random;

public class ArcaneInfusion extends InventorySpell {

	{
		mode = WndBag.Mode.ENCHANTABLE;
		image = ItemSpriteSheet.ARCANE_INFUSION;
	}

	@Override
	protected void onItemSelected( Item item ) {

		//Restore a bit of durability on highly damaged items
		int restore = (int) (0.2*MAX_DURABILITY);
		restore -= item.getDurability();
		if (restore > 0) {
			item.restoreDurability(restore);
		}

		if (item instanceof Enchantable) {
			((Enchantable) item).enchant(Random.Int(2) == 0 ? Weapon.Enchantment.randomUncommon() : Weapon.Enchantment.randomRare());
		}

		GLog.positive( Messages.get(this, "infuse", item.name()) );

		Badges.validateItemLevelAquired(item);

		curUser.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);
		Enchanting.show( curUser, item );
		Statistics.upgradesUsed++;
	}

	@Override
	public int price() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * 150);
	}

	public static class Recipe extends com.shatteredpixel.yasd.general.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{ScrollOfGreed.class, StoneOfEnchantment.class};
			inQuantity = new int[]{1, 1};

			cost = 4;

			output = ArcaneInfusion.class;
			outQuantity = 1;
		}
	}
}

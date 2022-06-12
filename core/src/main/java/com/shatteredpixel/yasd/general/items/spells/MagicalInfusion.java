/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Cursed Pixel Dungeon
 *  * Copyright (C) 2014-2020 Samuel Braithwaite
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.items.spells;

import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Statistics;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.Enchantable;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfGreed;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Random;

public class MagicalInfusion extends InventorySpell {

	{
		image = ItemSpriteSheet.MAGIC_INFUSE;
	}

	@Override
	protected void onItemSelected( Item item ) {

		//Specify in terms of max durability constant so I can tweak durability speed without messing with repair/degrade amounts
		item.restoreDurability((int) (MAX_DURABILITY*0.3f));

		curseChance(item);

		GLog.positive( Messages.get(this, "infuse", item.name()) );

		Badges.validateItemLevelAquired(item);

		curUser.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);
		Statistics.upgradesUsed++;
	}

	protected void curseChance(Item item) {
		int risk = 3;
		if (item instanceof Enchantable && ((Enchantable) item).getEnchantment() != null) {
			risk = 5;
		}
		if (Random.Int(10) < risk) {
			item.curse();
		}
	}

	@Override
	public int price() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((50 + 40) / 1f));
	}

	public static class Recipe extends com.shatteredpixel.yasd.general.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{ScrollOfGreed.class, ScrollOfTransmutation.class};//Scroll of Upgrade x2 + Scroll of Transmutation
			inQuantity = new int[]{ 2, 1 };

			cost = 4;

			output = MagicalInfusion.class;
			outQuantity = 1;
		}
	}
}


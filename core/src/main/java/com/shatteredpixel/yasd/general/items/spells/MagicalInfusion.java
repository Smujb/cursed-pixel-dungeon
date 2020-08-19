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
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfUpgrade;
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

		//removeUpgrades(item);

		GLog.positive( Messages.get(this, "infuse", item.name()) );

		Badges.validateItemLevelAquired(item);

		curUser.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);
		Statistics.upgradesUsed++;
	}

	protected void removeUpgrades(Item item) {
		//Removes 1 to all upgrades randomly. Use with caution.
		if (item.level() > 0) {
			item.level(Random.Int(item.level()));
		}
	}

	@Override
	public int price() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((50 + 40) / 1f));
	}

	public static class Recipe extends com.shatteredpixel.yasd.general.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{ScrollOfUpgrade.class, ScrollOfTransmutation.class};//Scroll of Upgrade + Scroll of Transmutation
			inQuantity = new int[]{1, 1};

			cost = 4;

			output = MagicalInfusion.class;
			outQuantity = 1;
		}
	}
}


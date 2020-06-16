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

package com.shatteredpixel.yasd.general.items.quest;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Hunger;
import com.shatteredpixel.yasd.general.actors.buffs.Ooze;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.Dewdrop;
import com.shatteredpixel.yasd.general.items.food.Food;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class GooBlob extends Food {
	
	{
		image = ItemSpriteSheet.BLOB;

		energy = Hunger.HUNGRY/4f;
	}

	@Override
	protected void satisfy(Hero hero) {
		super.satisfy(hero);
		affect(hero);
	}

	protected void affect(Char ch) {
		Buff.affect(ch, Ooze.class).set(20f);
		Buff.affect(ch, ElixirOfAquaticRejuvenation.AquaHealing.class).set(50);
	}

	@Override
	public int price() {
		return quantity * 60;
	}

	public static class PurifiedGooBlob extends GooBlob {

		@Override
		protected void affect(Char ch) {
			Buff.affect(ch, ElixirOfAquaticRejuvenation.AquaHealing.class).set(50);
		}

		@Override
		public int price() {
			return (int) (super.price()*1.5f);
		}
	}

	public static class Recipe extends com.shatteredpixel.yasd.general.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{GooBlob.class, Dewdrop.class};
			inQuantity = new int[]{1, 1};

			cost = 3;

			output = PurifiedGooBlob.class;
			outQuantity = 1;
		}
	}
}

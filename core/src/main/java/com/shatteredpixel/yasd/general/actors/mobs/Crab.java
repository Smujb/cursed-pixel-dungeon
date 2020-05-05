/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Yet Another Shattered Dungeon
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

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.food.MysteryMeat;
import com.shatteredpixel.yasd.general.items.potions.PotionOfHealing;
import com.shatteredpixel.yasd.general.sprites.CrabSprite;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class Crab extends Mob {

	{
		spriteClass = CrabSprite.class;

		healthFactor = 0.5f;
		baseSpeed = 2f;
		damageFactor = 2f;
		perceptionFactor = 2f;
		stealthFactor = 0.5f;

		EXP = 4;
		
		loot = new PotionOfHealing();
		lootChance = 0.5f;

		properties.add(Property.WATERY);//Can't drown.
	}

	@Override
	protected Item createLoot() {
		//(5-count) / 5 chance of getting healing, otherwise mystery meat
		if (Random.Float() < ((5f - Dungeon.LimitedDrops.CRAB_HP.count) / 5f)/2) {
			Dungeon.LimitedDrops.CRAB_HP.count++;
			return (Item) loot;
		} else {
			return Reflection.newInstance( MysteryMeat.class );
		}
	}
}

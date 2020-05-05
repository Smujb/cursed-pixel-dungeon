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

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.items.food.MysteryMeat;
import com.shatteredpixel.yasd.general.sprites.AlbinoSprite;
import com.shatteredpixel.yasd.general.sprites.RatSprite;

public class Rat extends Mob {

	{
		spriteClass = RatSprite.class;

		drFactor = 0.4f;
		evasionFactor = 0.5f;
		accuracyFactor = 0.8f;
		healthFactor = 0.7f;
		damageFactor = 0.8f;
	}

	public static class Albino extends Rat {

		{
			spriteClass = AlbinoSprite.class;

			healthFactor = 1.2f;
			EXP = 2;

			loot = new MysteryMeat();
			lootChance = 1f;
		}

		@Override
		public Element elementalType() {
			return Element.SHARP;
		}

		/*@Override
		public int attackProc(Char enemy, int damage ) {
			damage = super.attackProc( enemy, damage );
			if (Random.Int( 2 ) == 0) {
				Buff.affect( enemy, Bleeding.class ).set( Math.max(1, damage/2) );
			}

			return damage;
		}*/
	}
}

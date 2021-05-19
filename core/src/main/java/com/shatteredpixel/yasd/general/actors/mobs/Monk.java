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

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.actors.mobs.npcs.Imp;
import com.shatteredpixel.yasd.general.items.food.Food;
import com.shatteredpixel.yasd.general.items.food.Pasty;
import com.shatteredpixel.yasd.general.sprites.MonkSprite;
import com.shatteredpixel.yasd.general.sprites.SeniorSprite;
import com.watabou.utils.Random;

public class Monk extends Mob {

	{
		spriteClass = MonkSprite.class;


		healthFactor = 0.8f;
		damageFactor = 0.7f;
		attackDelay = 0.5f;

		EXP = 11;

        loot = new Food();
		lootChance = 0.083f;

		properties.add(Property.UNDEAD);
	}

	@Override
	public void rollToDropLoot() {
		Imp.Quest.process(this);

		super.rollToDropLoot();
	}

	@Override
	protected boolean canParry() {
		//Randomly parry when charge >= 50%, with chance increasing with charge
		return parryCharge/MAX_PARRY_CHARGE >= Random.Float(0.5f, 1f);
	}

	public static class Senior extends Monk {

		{
			spriteClass = SeniorSprite.class;
			damageFactor = 0.8f;

			loot = new Pasty();
			lootChance = 0.2f;
			//Parry god
			maxParryDefenseFactor = 1.5f;
		}
	}
}

/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Yet Another Shattered Dungeon
 *   Copyright (C) 2014-2020 Samuel Braithwaite
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ShamanSprite;
import com.watabou.utils.Random;

//TODO stats on these might be a bit weak
public abstract class Shaman extends Mob {

	{
		EXP = 6;

		damageFactor = 0.75f;
		baseSpeed = 1.5f;
		accuracyFactor = 2f;

		loot = Generator.Category.WAND;
		lootChance = 0.05f; //initially, see rollToDropLoot
	}


	//used so resistances can differentiate between melee and magical attacks

	@Override
	public String description() {
		return super.description() + "\n\n" + Messages.get(this, "spell_desc");
	}

	@Override
	public void rollToDropLoot() {
		//each drop makes future drops 1/3 as likely
		// so loot chance looks like: 1/33, 1/100, 1/300, 1/900, etc.
		lootChance *= Math.pow(1/3f, Dungeon.LimitedDrops.SHAMAN_WAND.count);
		super.rollToDropLoot();
		super.rollToDropLoot();
	}

	@Override
	protected Item createLoot() {
		Dungeon.LimitedDrops.SHAMAN_WAND.count++;
		return super.createLoot();
	}

	public static class RedShaman extends Shaman {
		{
			spriteClass = ShamanSprite.Red.class;
		}

		@Override
		public Element elementalType() {
			return Element.SPIRIT;
		}
	}

	public static class BlueShaman extends Shaman {
		{
			spriteClass = ShamanSprite.Blue.class;
		}

		@Override
		public Element elementalType() {
			return Element.DESTRUCTION;
		}
	}

	public static class PurpleShaman extends Shaman {
		{
			spriteClass = ShamanSprite.Purple.class;
		}

		@Override
		public Element elementalType() {
			return Element.AIR;
		}
	}

	//TODO a rare variant that helps brutes?

	public static Class<? extends Shaman> random(){
		float roll = Random.Float();
		if (roll < 0.4f){
			return RedShaman.class;
		} else if (roll < 0.8f){
			return BlueShaman.class;
		} else {
			return PurpleShaman.class;
		}
	}
}

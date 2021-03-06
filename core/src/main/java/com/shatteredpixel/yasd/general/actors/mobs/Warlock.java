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

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.WarlockSprite;

public class Warlock extends Mob {
	
	{
		spriteClass = WarlockSprite.class;


		healthFactor = 0.8f;
		evasionFactor = 0.6f;
		accuracyFactor = 1.2f;
		damageFactor = 1.3f;
		elementaldrFactor = 2f;

		numTypes = 4;

		range = -1;
		shotType = Ballistica.MAGIC_BOLT;

        attackDelay = 2f;

		lootChance = 0.5f;

		properties.add(Property.UNDEAD);
	}

	@Override
	public Element elementalType() {
		Element element;
		switch (type) {
			case 0: default:
				element = Element.SHADOW;
				break;
			case 1:
				element = Element.DESTRUCTION;
				break;
			case 2:
				element = Element.EARTH;
				break;
			case 3:
				element = Element.COLD;
				break;
		}
		return element;
	}

	@Override
	public Item createLoot(){
		return Generator.random(Generator.Category.POTION);
	}
}

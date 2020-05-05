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

package com.shatteredpixel.yasd.general.items.armor;

import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class HeavyArmor extends Armor {

	{
		image = ItemSpriteSheet.ARMOR_PLATE;

		EVA = 2/3f;
		magicalDRFactor = 1.4f;
		DRfactor = 1.25f;
		speedFactor = 2/3f;
	}

	@Override
	public int image() {
		if (tier < 4) {
			return ItemSpriteSheet.ARMOR_PLATE;
		} else  {
			return ItemSpriteSheet.ARMOR_BANDED;
		}
	}

	@Override
	public String desc() {
		if (tier < 4) {
			return Messages.get(Plate.class, "desc");
		} else {
			return Messages.get(Lead.class, "desc");
		}
	}

	@Override
	public String name() {
		if (tier < 4) {
			return Glyph.getName(Plate.class, glyph, cursedKnown);
		} else  {
			return Glyph.getName(Lead.class, glyph, cursedKnown);
		}
	}

	private static class Plate extends Armor {}
	private static class Lead extends Armor {}

	@Override
	public int appearance() {
		return 5;
	}
}

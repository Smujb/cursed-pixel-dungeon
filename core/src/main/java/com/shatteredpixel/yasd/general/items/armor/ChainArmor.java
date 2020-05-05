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

public class ChainArmor extends Armor {

	{
		image = ItemSpriteSheet.ARMOR_MAIL;

		magicalDRFactor = 2/3f;
		DRfactor = 1.5f;
		STE = 0.75f;
		EVA = 0.75f;
	}

	@Override
	public int image() {
		if (tier < 3) {
			return ItemSpriteSheet.ARMOR_STUDDED;
		} else if (tier < 5) {
			return ItemSpriteSheet.ARMOR_RINGMAIL;
		} else {
			return ItemSpriteSheet.ARMOR_MAIL;
		}
	}

	@Override
	public String desc() {
		if (tier < 3) {
			return Messages.get(Studded.class, "desc");
		} else if (tier < 5) {
			return Messages.get(RingMail.class, "desc");
		} else {
			return Messages.get(Mail.class, "desc");
		}
	}

	@Override
	public String name() {
		if (tier < 3) {
			return Glyph.getName(Studded.class, glyph, cursedKnown);
		} else if (tier < 5) {
			return Glyph.getName(RingMail.class, glyph, cursedKnown);
		} else {
			return Glyph.getName(Mail.class, glyph, cursedKnown);
		}
	}

	private static class Studded extends Armor {}
	private static class RingMail extends Armor {}
	private static class Mail extends Armor {}

	@Override
	public int appearance() {
		if (tier < 3) {
			return 2;
		} else {
			return 3;
		}
	}
}

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

package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class Polearm extends MeleeWeapon {

	{
		tier = 1;
		DLY = 1.5f;   //0.67x speed
		RCH = 2;    //extra reach

		damageMultiplier = 1.5f;

		dualWieldpenalty = true;
	}

	@Override
	public int image() {
		if (tier >= 4) {
			return ItemSpriteSheet.GLAIVE;
		} else if (tier > 1) {
			return ItemSpriteSheet.SPEAR;
		} else {
			return ItemSpriteSheet.PRIMITIVE_SPEAR;
		}
	}

	@Override
	public String desc() {
		if (tier >= 4) {
			return Messages.get(Glaive.class, "desc");
		} else if (tier > 1) {
			return Messages.get(Spear.class, "desc");
		} else {
			return Messages.get(PrimitiveSpear.class, "desc");
		}
	}

	@Override
	public String name() {
		if (tier >= 4) {
			return Enchantment.getName(Glaive.class, enchantment, cursedKnown);
		} else if (tier > 1) {
			return Enchantment.getName(Spear.class, enchantment, cursedKnown);
		} else {
			return Enchantment.getName(PrimitiveSpear.class, enchantment, cursedKnown);
		}
	}

	//Placeholders for tiers.
	private static class Glaive extends MeleeWeapon {}

	private static class Spear extends MeleeWeapon {}

	private static class PrimitiveSpear extends MeleeWeapon {}
}

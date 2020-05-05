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

public class Basic extends MeleeWeapon {

	{
		image = ItemSpriteSheet.GREATSWORD;

		tier=1;
	}

	@Override
	public int image() {
		switch (tier) {
			case 1:
				return ItemSpriteSheet.WORN_SHORTSWORD;
			case 2:
				return ItemSpriteSheet.SHORTSWORD;
			case 3:
				return ItemSpriteSheet.SWORD;
			case 4:
				return ItemSpriteSheet.LONGSWORD;
			case 5: default:
				return ItemSpriteSheet.GREATSWORD;

		}
	}

	@Override
	public String desc() {
		switch (tier) {
			case 1:
				return Messages.get(WornShortsword.class, "desc");
			case 2:
				return Messages.get(Shortsword.class, "desc");
			case 3:
				return Messages.get(Sword.class, "desc");
			case 4:
				return Messages.get(Longsword.class, "desc");
			case 5: default:
				return Messages.get(Greatsword.class, "desc");

		}
	}

	@Override
	public String name() {
		switch (tier) {
			case 1:
				return Enchantment.getName(WornShortsword.class, enchantment, cursedKnown);
			case 2:
				return Enchantment.getName(Shortsword.class, enchantment, cursedKnown);
			case 3:
				return Enchantment.getName(Sword.class, enchantment, cursedKnown);
			case 4:
				return Enchantment.getName(Longsword.class, enchantment, cursedKnown);
			case 5: default:
				return Enchantment.getName(Greatsword.class, enchantment, cursedKnown);

		}
	}

	//Placeholders for tiers
	private class WornShortsword extends MeleeWeapon {}
	private class Shortsword extends MeleeWeapon {}
	private class Sword extends MeleeWeapon {}
	private class Longsword extends MeleeWeapon {}
	private class Greatsword extends MeleeWeapon {}

}

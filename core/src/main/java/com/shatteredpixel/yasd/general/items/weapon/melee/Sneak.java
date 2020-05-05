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

public class Sneak extends MeleeWeapon {

	{
		image = ItemSpriteSheet.ASSASSINS_BLADE;

		tier = 1;
		damageMultiplier = 0.80f;
		sneakBenefit = true;
	}

	@Override
	public int image() {
		if (tier < 2) {
			return ItemSpriteSheet.DAGGER;
		} else if (tier < 3) {
			return ItemSpriteSheet.DIRK;
		} else {
			return ItemSpriteSheet.ASSASSINS_BLADE;
		}
	}

	@Override
	public String desc() {
		if (tier < 2) {
			return Messages.get(Dagger.class, "desc");
		} else if (tier < 3) {
			return Messages.get(Dirk.class, "desc");
		} else {
			return Messages.get(AssassinsBlade.class, "desc");
		}
	}

	@Override
	public String name() {
		if (tier < 2) {
			return Enchantment.getName(Dagger.class, enchantment, cursedKnown);
		} else if (tier < 3) {
			return Enchantment.getName(Dirk.class, enchantment, cursedKnown);
		} else {
			return Enchantment.getName(AssassinsBlade.class, enchantment, cursedKnown);
		}
	}

	private static class Dirk extends MeleeWeapon {}
	private static class Dagger extends MeleeWeapon {}
	private static class AssassinsBlade extends MeleeWeapon {}

}
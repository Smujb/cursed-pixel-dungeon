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

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class Staff extends MeleeWeapon {

	{
		//image = ItemSpriteSheet.QUARTERSTAFF;

		tier = 1;

		damageMultiplier = 0.7f;
	}

	@Override
	public int image() {
		if (tier < 4) {
			return ItemSpriteSheet.QUARTERSTAFF;
		} else  {
			return ItemSpriteSheet.ROD;
		}
	}

	@Override
	public String desc() {
		if (tier < 4) {
			return Messages.get(Quarterstaff.class, "desc");
		} else {
			return Messages.get(Rod.class, "desc");
		}
	}

	@Override
	public String name() {
		if (tier < 4) {
			return Enchantment.getName(Quarterstaff.class, enchantment, cursedKnown);
		} else  {
			return Enchantment.getName(Rod.class, enchantment, cursedKnown);
		}
	}

	@Override
	public int defenseFactor( Char owner ) {
		return 2*tier;	//2*tier extra defence
	}

	private static class Rod extends MeleeWeapon {}
	private static class Quarterstaff extends MeleeWeapon {}
}

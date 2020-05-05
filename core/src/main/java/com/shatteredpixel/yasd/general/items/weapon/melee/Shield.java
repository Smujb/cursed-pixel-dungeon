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

public class Shield extends MeleeWeapon {

	{
		//image = ItemSpriteSheet.GREATSHIELD;

		tier = 1;

		damageMultiplier = 0.6f;
	}

	@Override
	public int image() {
		if (tier >= 4) {
			return ItemSpriteSheet.GREATSHIELD;
		} else {
			return ItemSpriteSheet.ROUND_SHIELD;
		}
	}

	@Override
	public String desc() {
		if (tier >= 4) {
			return Messages.get(Greatshield.class, "desc");
		} else {
			return Messages.get(RoundShield.class, "desc");
		}
	}

	@Override
	public String name() {
		if (tier >= 4) {
			return Enchantment.getName(Greatshield.class, enchantment, cursedKnown);
		} else {
			return Enchantment.getName(RoundShield.class, enchantment, cursedKnown);
		}
	}

	@Override
	public int defenseFactor( Char owner ) {
		return defenseFactor();
	}

	public int defenseFactor() {
		return Math.round(tier*2 + (tier/2f)*level());    //2*tier extra defence, plus tier/2 per level;
	}

	//Placeholders for tiers.
	private static class Greatshield extends MeleeWeapon {}
	private static class RoundShield extends MeleeWeapon {}
}

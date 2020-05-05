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

package com.shatteredpixel.yasd.general.items.weapon.enchantments;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Vampiric extends Weapon.Enchantment {

	private static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0x660022 );

	@Override
	public int proc( Weapon weapon, Char attacker, Char defender, int damage ) {

		//heals for up to 30% of damage dealt, based on missing HP, ultimately normally distributed
		float 	missingPercent = (attacker.HT - attacker.HP) / (float)attacker.HT,
				maxHeal = (.025f + missingPercent * .125f) * 2, // min max heal is .025%, consistent with shattered.
				healPercent = 0;
		int tries = weapon.level();
		do {
			healPercent = Math.max(healPercent, Random.NormalFloat(0,maxHeal));
		} while(tries-- > 0);
		int healAmt = Math.min( Math.round(healPercent*damage), attacker.HT - attacker.HP );

		if (healAmt > 0 && attacker.isAlive()) {
			attacker.heal(healAmt, false);
		}

		return damage;
	}

	@Override
	public Glowing glowing() {
		return RED;
	}
}
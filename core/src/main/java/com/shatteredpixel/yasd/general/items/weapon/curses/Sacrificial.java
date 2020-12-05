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

package com.shatteredpixel.yasd.general.items.weapon.curses;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.items.Enchantable;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Random;

public class Sacrificial extends Weapon.Enchantment {

	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );

	@Override
	public int proc(Enchantable weapon, Char attacker, Char defender, int damage ) {

		if (Random.Int(5+weapon.enchPower()) <= weapon.enchPower()) {
			int procDMG = defender.HP;
			if (Math.round(procDMG/2f) >= attacker.HP) {//Use Math.round rather than integer division so that odd damage doesn't have a small chance of killing the player
				procDMG = (attacker.HP - 1)*2;
			}
			if (procDMG > 0) {
				GLog.negative(Messages.get(this,"proc"));
				attacker.damage(procDMG / 2, new Char.DamageSrc(Element.SHADOW, this));
				if (defender.properties().contains(Char.Property.BOSS)) {
					procDMG /= 2;//Doesn't one shot bosses
				}
				defender.damage(procDMG, new Char.DamageSrc(Element.SHADOW, this));
			}
		}
		return damage;
	}

	@Override
	public boolean curse() {
		return true;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}

}

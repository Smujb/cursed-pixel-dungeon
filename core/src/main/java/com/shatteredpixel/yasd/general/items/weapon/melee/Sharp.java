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
import com.shatteredpixel.yasd.general.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Sharp extends MeleeWeapon {

	{
		image = ItemSpriteSheet.SCIMITAR;

		tier = 1;
		DLY = 1f;

		damageMultiplier = 0.80f;
	}

	@Override
	public String desc() {
		return Messages.get(Scimitar.class, "desc");
	}

	@Override
	public String name() {
		return Enchantment.getName(Scimitar.class, enchantment, cursedKnown);
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (Random.Int(3) == 0) {
			Buff.affect( defender, Bleeding.class ).set( damage/3 );
		}
		return super.proc(attacker, defender, damage);
	}

	private static class Scimitar extends MeleeWeapon {}
}

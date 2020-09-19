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

package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Greataxe extends MeleeWeapon {

	{
		image = ItemSpriteSheet.Weapons.GREATAXE;
		hitSound = Assets.Sounds.HIT_CRUSH;
		hitSoundPitch = 0.7f;

		DLY = 2f;
		ACC = 1.5f;

		statScaling.add(Hero.HeroStat.RESILIENCE);
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (Random.Int(2) == 0 && defender.buff(Paralysis.class) == null) {
			Buff.affect(defender, Paralysis.class, Paralysis.DURATION/2f);
		}
		return super.proc(attacker, defender, damage);
	}

	@Override
	protected boolean hasProperties() {
		return true;
	}

	@Override
	protected String propsDesc() {
		return super.propsDesc() + "\n" + Messages.get(this, "paralysis");
	}
}

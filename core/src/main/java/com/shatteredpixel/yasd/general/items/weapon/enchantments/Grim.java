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

package com.shatteredpixel.yasd.general.items.weapon.enchantments;

import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.general.items.Enchantable;
import com.shatteredpixel.yasd.general.items.wands.WandOfDamnation;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Grim extends Weapon.Enchantment {
	
	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );

	@Override
	public int proc(Enchantable weapon, Char attacker, Char defender, int damage ) {

		int enemyHealth = defender.HP - damage;
		if (enemyHealth <= 0) return damage; //no point in proccing if they're already dead.

		if (!defender.isImmune(this.getClass())
				&& Random.Int( weapon.enchPower()/2 + 15 ) > 12) {

			Buff.affect(defender, DeferredDeath.class, Math.max(10, 50-weapon.enchPower()));
			int level = 0;
			defender.sprite.emitter().burst( ShadowParticle.UP, 5 + level );
		}

		return damage;
	}
	
	@Override
	public Glowing glowing() {
		return BLACK;
	}

	public static class DeferredDeath extends WandOfDamnation.DeferredDeath {

		@Override
		protected void killTarget() {
			super.killTarget();
			if (!target.isAlive()) {
				Badges.validateGrimWeapon();
			}
		}
	}
}

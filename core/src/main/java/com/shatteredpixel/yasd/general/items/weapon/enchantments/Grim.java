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
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Grim extends Weapon.Enchantment {
	
	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );

	@Override
	public int proc(Weapon weapon, Char attacker, Char defender, int damage ) {

		int enemyHealth = defender.HP - damage;
		if (enemyHealth <= 0) return damage; //no point in proccing if they're already dead.

		float chance = ((float)(damage/2))/defender.HP;//Chance is now half of damage dealt out of enemy hp. This is a massive nerf for fast weapons but a smaller one for slow weapons.

		if (Random.Float() < chance) {

			defender.damage( defender.HP, new Char.DamageSrc(Element.SPIRIT, this).ignoreDefense());
			int level = 0;
			defender.sprite.emitter().burst( ShadowParticle.UP, 5 + level );

			if (!defender.isAlive() && attacker instanceof Hero
					//this prevents unstable from triggering grim achievement
					&& weapon.hasEnchant(Grim.class, attacker)) {
				Badges.validateGrimWeapon();
			}
		}

		return damage;
	}
	
	@Override
	public Glowing glowing() {
		return BLACK;
	}

}

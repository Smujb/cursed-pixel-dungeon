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
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class Crossbow extends MeleeWeapon {
	
	{
		image = ItemSpriteSheet.Weapons.CROSSBOW;
		hitSound = Assets.Sounds.HIT;
		hitSoundPitch = 1f;
		
		//check Dart.class for additional properties
		damageMultiplier = 0.75f;

		statScaling.add(Hero.HeroStat.ASSAULT);
	}

	private Dart dart = null;

	@Override
	public boolean canReach(Char owner, int target) {
		dart = null;
		if (curUser != null && (dart = curUser.belongings.getItem(Dart.class)) != null) {
			if (Ballistica.canHit(curUser, target, Ballistica.PROJECTILE)) {
				dart.setCrossbow(this);
				return true;
			}
		}
		return super.canReach(owner, target);
	}

	@Override
	public boolean doAttack(Char attacker, Char enemy) {
		if (dart != null && !Dungeon.level.adjacent(curUser.pos, enemy.pos)) {
			dart.cast(curUser, enemy.pos);
			return false;
		}
		return super.doAttack(attacker, enemy);
	}
}

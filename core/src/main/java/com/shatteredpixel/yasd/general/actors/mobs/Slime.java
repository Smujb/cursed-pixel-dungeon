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

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.quest.GooBlob;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.sprites.CausticSlimeSprite;
import com.shatteredpixel.yasd.general.sprites.SlimeSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class Slime extends Mob {
	
	{
		spriteClass = SlimeSprite.class;

		healthFactor = 1 + 2/3f;
		damageFactor = 2/3f;

        lootChance = 0.2f; //by default, see rollToDropLoot()
	}
	
	@Override
	public void damage(int dmg,  DamageSrc src) {
		float threshold = HT/3f;
		if (dmg >= threshold){
			dmg = (int) ((threshold-1) + Math.sqrt((24/threshold)*(dmg - (threshold-1)) + 1) - 1)/2;
		}
		super.damage(dmg, src);
	}

	@Override
	public void rollToDropLoot() {
		//each drop makes future drops 1/3 as likely
		// so loot chance looks like: 1/5, 1/15, 1/45, 1/135, etc.
		lootChance *= Math.pow(1/3f, Dungeon.LimitedDrops.SLIME_WEP.count);
		super.rollToDropLoot();
	}
	
	@Override
	protected Item createLoot() {
		Dungeon.LimitedDrops.SLIME_WEP.increaseCount();
		MeleeWeapon w = Generator.randomWeapon();
		w.random();
		return w;
	}

	public static class CausticSlime extends Slime {

		{
			spriteClass = CausticSlimeSprite.class;

			properties.add(Property.ACIDIC);
		}

		@Override
		public Element elementalType() {
			return Element.ACID;
		}

		@Override
		public void rollToDropLoot() {
			super.rollToDropLoot();

			int ofs;
			do {
				ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
			} while (!Dungeon.level.passable(pos + ofs));
			Dungeon.level.drop( Reflection.newInstance(  GooBlob.class ), pos + ofs ).sprite.drop( pos );
		}
	}
}

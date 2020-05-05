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

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.levels.features.Chasm;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.SkeletonSprite;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

public class Skeleton extends Mob {
	
	{
		spriteClass = SkeletonSprite.class;

		healthFactor = 0.8f;
		evasionFactor = 2/3f;
		accuracyFactor = 1.25f;

		numTypes = 2;
		
		EXP = 5;

		loot = Generator.Category.WEAPON;
		lootChance = 0.125f;

		properties.add(Property.UNDEAD);
		properties.add(Property.INORGANIC);
	}

	@Override
	public Element elementalType() {
		Element element;
		switch (type) {
			case 0: default:
				element = Element.PHYSICAL;
				break;
			case 1:
				element = Element.EARTH;
				break;
		}
		return element;
	}

	@Override
	public void die( DamageSrc cause ) {
		
		super.die( cause );
		
		if (cause.getCause() == Chasm.class) return;
		
		boolean heroKilled = false;
		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			Char ch = findChar( pos + PathFinder.NEIGHBOURS8[i] );
			if (ch != null && ch.isAlive()) {
				int damage = Random.NormalIntRange(6, 12);
				damage = Math.max( 0,  damage - ch.drRoll(Element.PHYSICAL) );
				ch.damage( damage, new DamageSrc(Element.PHYSICAL, this));
				if (ch == Dungeon.hero && !ch.isAlive()) {
					heroKilled = true;
				}
			}
		}
		
		if (Dungeon.level.heroFOV[pos]) {
			Sample.INSTANCE.play( Assets.SND_BONES );
		}
		
		if (heroKilled) {
			Dungeon.fail( getClass() );
			GLog.n( Messages.get(this, "explo_kill") );
		}
	}
	
	@Override
	protected Item createLoot() {
		MeleeWeapon loot;
		do {
			loot = Generator.randomWeapon();
		//50% chance of re-rolling tier 4 or 5 melee weapons
		} while (loot.tier >= 4 && Random.Int(2) == 0);
		loot.level(0);
		return loot;
	}
	
	/*@Override
	public int attackSkill( Char target ) {
		return 16;
	}
	
	@Override
	public int drRoll(Element element) {
		return Random.NormalIntRange(0, 5);
	}*/

}

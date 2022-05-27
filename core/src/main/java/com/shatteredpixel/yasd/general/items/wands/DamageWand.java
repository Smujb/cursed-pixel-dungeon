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

package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.items.EquipableItem;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

//for wands that directly damage a target
//wands with AOE effects count here (e.g. fireblast, blast wave), but wands with indrect damage do not (e.g. corrosion, transfusion)
public abstract class DamageWand extends Wand{

	protected float damageFactor = 1f;

	protected float damageMultiplier() {
		return damageFactor;
	}

	public final int hit(Char enemy) {
		return hit(enemy, damageRoll());
	}

	public int hit(Char enemy, int damage) {
		if (curUser == null) {
			curUser = Dungeon.hero;
		}
		damage = element.attackProc(damage, curUser, enemy);
		damage = enemy.defenseProc(curUser, damage);
		if (damage > 0) {
			enemy.damage(damage, new Char.DamageSrc(element, this));
		}
		Sample.INSTANCE.play( Assets.Sounds.HIT_MAGIC, 1, 0.8f * Random.Float(0.87f, 1.15f) );
		return damage;
	}




	final int defaultMin() {
		return (int) min(1f);
	}

	public final int min() {
		return Math.round(min(power()));
	}

	public final int max(){
		return Math.round(max(power()));
	}

	public float min(float lvl) {
		return (int) Math.max(0, 15 * lvl - damageReduction()) * damageFactor;   //level scaling
	}

	public float max(float lvl) {
		return (int) Math.max(0, 20 * lvl - damageReduction()) * damageFactor;   //level scaling
	}

	final int defaultMax() {
		return (int) max(1f);
	}

	@Override
	protected int initialCharges() {
		return 3;
	}

	public int damageRoll(){
		return damageRoll(power());
	}

	public int damageRoll(float lvl){
		int damage = Random.NormalIntRange((int)min(lvl), (int)max(lvl));
		if (broken()) {
			damage *= EquipableItem.BROKEN_DAMAGE_MODIFIER;
		}
		return damage;
	}

	@Override
	public String statsDesc() {
		if (levelKnown)
			return Messages.get(this, "stats_desc",  min(), max());
		else
			return Messages.get(this, "stats_desc", defaultMin(), defaultMax());
	}
}

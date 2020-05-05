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

import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Statistics;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Corruption;
import com.shatteredpixel.yasd.general.actors.buffs.PinCushion;
import com.shatteredpixel.yasd.general.actors.buffs.SoulMark;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Corrupting extends Weapon.Enchantment {
	
	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x440066 );
	
	@Override
	public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
		if (defender.buff(Corruption.class) != null || !(defender instanceof Mob)) return damage;
		
		int level = Math.max( 0, weapon.level() );
		
		// lvl 0 - 20%
		// lvl 1 ~ 22.5%
		// lvl 2 ~ 25%
		if (damage >= defender.HP
				&& !defender.isImmune(Corruption.class)
				&& Random.Int( level + 15 ) >= 12){
			
			Mob enemy = (Mob) defender;
			Hero hero = (attacker instanceof Hero) ? (Hero) attacker : Dungeon.hero;
			
			enemy.HP = enemy.HT;
			for (Buff buff : enemy.buffs()) {
				if (buff.type == Buff.buffType.NEGATIVE
						&& !(buff instanceof SoulMark)) {
					buff.detach();
				} else if (buff instanceof PinCushion){
					buff.detach();
				}
			}
			if (enemy.alignment == Char.Alignment.ENEMY){
				enemy.rollToDropLoot();
			}
			
			Buff.affect(enemy, Corruption.class);
			
			Statistics.enemiesSlain++;
			Badges.validateMonstersSlain();
			Statistics.qualifiedForNoKilling = false;
			if (enemy.EXP > 0 && hero.lvl <= enemy.maxLvl) {
				hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(enemy, "exp", enemy.EXP));
				hero.earnExp(enemy.EXP, enemy.getClass());
			} else {
				hero.earnExp(0, enemy.getClass());
			}
			
			return 0;
		}
		
		return damage;
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}
}

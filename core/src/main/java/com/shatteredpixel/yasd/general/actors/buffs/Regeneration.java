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

package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.Challenges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.utils.GLog;

public class Regeneration extends Buff {
	
	{
		//unlike other buffs, this one acts after the hero and takes priority against other effects
		//healing is much more useful if you get some of it off before taking damage
		actPriority = HERO_PRIO - 1;
	}
	
	private static final float REGENERATION_DELAY = 10;
	
	@Override
	public boolean act() {
		if (Dungeon.isChallenged(Challenges.BLOODLUST)) {
			if (Dungeon.depth > 0) target.damage(target.HT/100, new Char.DamageSrc(Element.META, this).ignoreDefense());
			spend(1f);
			if (!target.isAlive()) {
				GLog.negative(Messages.get(this, "death"));
				Dungeon.fail(Regeneration.class);
			}
			return true;
		}
		if (target.isAlive()) {

			if (target.HP < regencap() && !(target instanceof Hero && ((Hero)target).isStarving())) {
				LockedFloor lock = target.buff(LockedFloor.class);
				if (target.HP > 0 && (lock == null || lock.regenOn())) {
					target.heal(1);
					if (target.HP == regencap() && target instanceof Hero) {
						((Hero) target).resting = false;
					}
				}
			}

			ChaliceOfBlood.chaliceRegen regenBuff = Dungeon.hero.buff( ChaliceOfBlood.chaliceRegen.class);

			if (regenBuff != null) {
				if (regenBuff.isCursed()) {
					spend( REGENERATION_DELAY * 1.5f );
				} else {
					spend( REGENERATION_DELAY - regenBuff.itemLevel()*0.9f );
				}
			} else {
				spend( REGENERATION_DELAY/Math.max(1, target.level()));
			}
			
		} else {
			
			diactivate();
			
		}
		
		return true;
	}
	
	public int regencap(){
		return target.HT;
	}
}

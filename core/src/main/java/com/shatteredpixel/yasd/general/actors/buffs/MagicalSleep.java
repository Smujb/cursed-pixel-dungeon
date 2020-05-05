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

package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.shatteredpixel.yasd.general.utils.GLog;

import org.jetbrains.annotations.NotNull;

public class MagicalSleep extends Buff {

	private static final float STEP = 1f;

	@Override
	public boolean attachTo(@NotNull Char target ) {
		if (!target.isImmune(Sleep.class) && super.attachTo( target )) {
			
			target.paralysed++;
			
			if (target.alignment == Char.Alignment.ALLY) {
				if (target.HP == target.HT) {
					if (target instanceof  Hero) GLog.i(Messages.get(this, "toohealthy"));
					detach();
				} else {
					if (target instanceof  Hero) GLog.i(Messages.get(this, "fallasleep"));
				}
			}

			if (target instanceof Mob) {
				((Mob) target).state = ((Mob) target).SLEEPING;
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean act(){
		if (target instanceof Mob && ((Mob) target).state != ((Mob) target).SLEEPING){
			detach();
			return true;
		}
		if (target.alignment == Char.Alignment.ALLY) {
			target.HP = Math.min(target.HP+1, target.HT);
			if (target instanceof  Hero) ((Hero) target).resting = true;
			if (target.HP == target.HT) {
				if (target instanceof  Hero) {
					GLog.p(Messages.get(this, "wakeup"));
					((Hero)target).gainMorale(5f);
				}
				detach();

			}
		}
		spend( STEP );
		return true;
	}

	@Override
	public void detach() {
		if (target.paralysed > 0)
			target.paralysed--;
		if (target instanceof Hero)
			((Hero) target).resting = false;
		super.detach();
	}

	@Override
	public int icon() {
		return BuffIndicator.MAGIC_SLEEP;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
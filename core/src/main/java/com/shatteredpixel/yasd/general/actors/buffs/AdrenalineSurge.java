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

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.watabou.utils.Bundle;

import org.jetbrains.annotations.NotNull;

public class AdrenalineSurge extends FlavourBuff {

	public static float DURATION = 800f;
	
	{
		type = buffType.POSITIVE;
	}

	@Override
	public boolean attachTo(@NotNull Char target) {
		boolean attach = super.attachTo(target);
		if (attach) target.updateHT(true);
		return attach;
	}

	private float interval;

	public static int statBoost(Char ch) {
		if (ch.buffs() == null) return 0;
		AdrenalineSurge surge = ch.buff(AdrenalineSurge.class);
		if (surge != null) return 1;
		return 0;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.FURY;
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - visualcooldown()) / DURATION);
	}
	
	@Override
	public String toString() {
		return Messages.get(this, "name");
	}
	
	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns(visualcooldown()));
	}

	private static final String INTERVAL	    = "interval";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( INTERVAL, interval );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if (bundle.contains(INTERVAL)) {
			interval = bundle.getFloat(INTERVAL);
		} else {
			interval = 800f;
		}
	}
}

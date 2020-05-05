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

import com.watabou.utils.Random;

public class Bestiary {

	public static Class<? extends Mob> swapMobAlt( Class<? extends Mob> mob ) {
		Class<? extends Mob> cl = mob;
		if (Random.Int( 5 ) == 0) {
			if (cl == Rat.class) {
				cl = Rat.Albino.class;
			} else if (cl == Slime.class) {
				cl = Slime.CausticSlime.class;
			} else if (cl == Thief.class) {
				cl = Thief.Bandit.class;
			} else if (cl == DM200.class) {
				cl = DM200.DM201.class;
			} else if (cl == Brute.class) {
				cl = Brute.ArmoredBrute.class;
			} else if (cl == Monk.class) {
				cl = Monk.Senior.class;
			} else if (cl == Scorpio.class) {
				cl = Scorpio.Acidic.class;
			}
		}
		return cl;
	}
}

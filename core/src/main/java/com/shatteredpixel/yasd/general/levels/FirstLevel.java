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

package com.shatteredpixel.yasd.general.levels;

import com.shatteredpixel.yasd.general.actors.mobs.Rat;
import com.shatteredpixel.yasd.general.actors.mobs.Snake;
import com.shatteredpixel.yasd.general.levels.traps.WornDartTrap;

public class FirstLevel extends SewerLevel {//First depth has different mobs

	@Override
	public int getScaleFactor() {
		return 1;
	}

	@Override
	public Class<?>[] mobClasses() {
			return new Class[]{Rat.class, Snake.class};
	}

	@Override
	public float[] mobChances() {
		return new float[]{3, 1};
	}

	@Override
	protected Class<?>[] trapClasses() {
		return new Class<?>[]{WornDartTrap.class};
	}

	@Override
	protected float[] trapChances() {
		return new float[]{1};
	}

	@Override
	protected float[] connectionRoomChances() {
		return new float[]{
				20,
				1,
				0,
				2,
				2,
				1,
				0};
	}

	@Override
	protected float[] standardRoomChances() {
		return new float[]{20, 15, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	}
}

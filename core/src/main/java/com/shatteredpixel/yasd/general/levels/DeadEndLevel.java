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

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.levels.interactive.Entrance;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;

public class DeadEndLevel extends Level {

	private static final int SIZE = 5;
	
	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_CAVES;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_HALLS;
	}
	
	@Override
	protected boolean build() {
		
		setSize(7, 7);
		
		for (int i=2; i < SIZE; i++) {
			for (int j=2; j < SIZE; j++) {
				map[i * width() + j] = Terrain.EMPTY;
			}
		}
		
		for (int i=1; i <= SIZE; i++) {
			map[width() + i] =
			map[width() * SIZE + i] =
			map[width() * i + 1] =
			map[width() * i + SIZE] =
				Terrain.WATER;
		}
		
		//entrance = SIZE * width() + SIZE / 2 + 1;
		interactiveAreas.add(new Entrance().setPos(this, SIZE * width() + SIZE / 2 + 1));
		map[getEntrancePos()] = Terrain.ENTRANCE;

		//exit = 0;
		
		return true;
	}
	
	@Override
	public Mob createMob() {
		return null;
	}
	
	@Override
	protected void createMobs() {
	}

	public Actor respawner() {
		return null;
	}

	@Override
	protected void createItems() {
	}
	
	@Override
	public int randomRespawnCell(Char target) {
		return getEntrancePos()-width();
	}

}

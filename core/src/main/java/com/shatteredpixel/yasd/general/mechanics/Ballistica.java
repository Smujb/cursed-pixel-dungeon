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

package com.shatteredpixel.yasd.general.mechanics;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.watabou.noosa.Game;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Ballistica {



	//note that the path is the FULL path of the projectile, including tiles after collision.

	//make sure to generate a subPath for the common case of going source to collision.

	public ArrayList<Integer> path = new ArrayList<>();
	public Integer sourcePos = null;
	public Integer collisionPos = null;
	public ArrayList<Integer> reflectPositions = new ArrayList<>();

	public Integer dist = 0;

	private static final int MAX_REFLECT_TIMES = 2;

	private int reflectTimes = 0;

	//parameters to specify the colliding cell
	public static final int STOP_TARGET = 1; //ballistica will stop at the target cell
	public static final int STOP_CHARS = 2; //ballistica will stop on first char hit
	public static final int STOP_TERRAIN = 4; //ballistica will stop on terrain(LOS blocking, impassable, etc.)
	public static final int REFLECT = 8; //ballistica will reflect instead of stopping


	public static final int PROJECTILE = STOP_TARGET | STOP_CHARS | STOP_TERRAIN;
	public static final int MAGIC_BOLT = STOP_CHARS | STOP_TERRAIN;
	public static final int WONT_STOP = 0;
	public static final int LASER = STOP_TERRAIN | REFLECT;

	public static boolean canHit(@NotNull Char source, @NotNull Char target, int params) {
		return new Ballistica(source.pos, target.pos, params).path.contains(target.pos);
	}

	public Ballistica(int from, int to, int params) {

		sourcePos = from;

		build(from, to, (params & STOP_TARGET) > 0, (params & STOP_CHARS) > 0, (params & STOP_TERRAIN) > 0, (params & REFLECT) > 0);

		if (collisionPos != null) {

			dist = path.lastIndexOf(collisionPos);

		} else if (!path.isEmpty()) {

			collisionPos = path.get(dist = path.size() - 1);

		} else {

			path.add(from);

			collisionPos = from;

			dist = 0;

		}

	}



	private void build(int from, int to, boolean stopTarget, boolean stopChars, boolean stopTerrain, boolean reflect) {

		int w = Dungeon.level.width();



		int x0 = from % w;

		int x1 = to % w;

		int y0 = from / w;

		int y1 = to / w;



		int dx = x1 - x0;

		int dy = y1 - y0;



		int stepX = dx > 0 ? +1 : -1;

		int stepY = dy > 0 ? +1 : -1;



		dx = Math.abs(dx);

		dy = Math.abs(dy);



		int stepA;

		int stepB;

		int dA;

		int dB;



		if (dx > dy) {



			stepA = stepX;

			stepB = stepY * w;

			dA = dx;

			dB = dy;



		} else {



			stepA = stepY * w;

			stepB = stepX;

			dA = dy;

			dB = dx;



		}



		int cell = from;



		int err = dA / 2;



		boolean alreadyReflected = false;

		while (Dungeon.level.insideMap(cell)) {

			// Wall case is treated differently by stopping one early

			if (stopTerrain && cell != sourcePos && !Dungeon.level.passable(cell) && !Dungeon.level.avoid(cell)) {

				int cellBeforeWall = path.get(path.size() - 1);



				if (!reflect) {

					collide(cellBeforeWall);

				} else if (!alreadyReflected) {

					alreadyReflected = true;



					int cellAdjacentHorizontal = cellBeforeWall + stepX;

					int cellAdjacentVertical = cellBeforeWall + stepY * w;

					if (Dungeon.level.solid(cellAdjacentVertical) && Dungeon.level.solid(cellAdjacentHorizontal)) {

						// found a corner?

						collide(cellBeforeWall);

					} else if (!Dungeon.level.solid(cellAdjacentVertical) && !Dungeon.level.solid(cellAdjacentHorizontal)) {

						// both sides open? how did we get this case?

						collide(cellBeforeWall);

					} else {

						// actual reflection

						int destinationX, destinationY;

						if (!Dungeon.level.solid(cellAdjacentVertical)) {

							// bounce against vertical wall, keep x

							destinationX = x0;

							destinationY = y0 + stepY * Math.abs(y0 - cell / w) * 2;

						} else {

							// bounce against horizontal wall, keep y

							destinationY = y0;

							destinationX = x0 + stepX * Math.abs(x0 - cell % w) * 2;

						}

						reflectPositions.add(cellBeforeWall);

						build(cellBeforeWall, destinationX + destinationY * w, stopTarget, stopChars, stopTerrain, ++reflectTimes < MAX_REFLECT_TIMES);

					}

				}

			}



			path.add(cell);



			if ((stopTerrain && cell != sourcePos && Dungeon.level.solid(cell))

					|| (cell != sourcePos && stopChars && Actor.findChar(cell) != null)

					|| (cell == to && stopTarget)) {

				collide(cell);

			}



			cell += stepA;



			err += dB;

			if (err >= dA) {

				err = err - dA;

				cell = cell + stepB;

			}

		}

	}



	//we only want to record the first position collision occurs at.

	private void collide(int cell) {

		if (collisionPos == null)

			collisionPos = cell;

	}



	//returns a segment of the path from start to end, inclusive.

	//if there is an error, returns an empty arraylist instead.

	public List<Integer> subPath(int start, int end) {

		try {

			end = Math.min(end, path.size() - 1);

			return path.subList(start, end + 1);

		} catch (Exception e) {

			Game.reportException(e);
			return new ArrayList<>();

		}

	}

}

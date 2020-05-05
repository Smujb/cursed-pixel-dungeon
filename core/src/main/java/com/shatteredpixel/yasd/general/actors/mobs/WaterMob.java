/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Yet Another Shattered Dungeon
 *   Copyright (C) 2014-2020 Samuel Braithwaite
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Vertigo;
import com.watabou.utils.PathFinder;

public abstract class WaterMob extends Mob {

	{
		SLEEPING = new WaterSleeping();
		WANDERING = new WaterWandering();
		HUNTING = new WaterHunting();

		properties.add(Property.BLOB_IMMUNE);
		properties.add(Property.WATERY);
	}

	@Override
	protected boolean act() {
		if (!Dungeon.level.liquid(pos)) {
			die( new DamageSrc(Element.META, null) );
			return true;
		} else {
			return super.act();
		}
	}

	@Override
	public int defenseSkill( Char enemy ) {
		enemySeen = state != SLEEPING
				&& this.enemy != null
				&& fieldOfView != null
				&& fieldOfView[this.enemy.pos]
				&& this.enemy.invisible == 0;
		return super.defenseSkill( enemy );
	}

	@Override
	protected boolean getCloser( int target ) {

		if (rooted) {
			return false;
		}

		int step = Dungeon.findStep( this, target, Dungeon.level.liquid(), fieldOfView, true );
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean getFurther( int target ) {
		int step = Dungeon.flee( this, target, Dungeon.level.liquid(), fieldOfView, true );
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}

	{
		immunities.add( Burning.class );
		immunities.add( Vertigo.class );
	}

	//if there is not a path to the enemy, piranhas act as if they can't see them
	private class WaterSleeping extends Mob.Sleeping{
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			if (enemyInFOV) {
				PathFinder.buildDistanceMap(enemy.pos, Dungeon.level.liquid(), viewDistance);
				enemyInFOV = PathFinder.distance[pos] != Integer.MAX_VALUE;
			}

			return super.act(enemyInFOV, justAlerted);
		}
	}

	private class WaterWandering extends Mob.Wandering{
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			if (enemyInFOV) {
				PathFinder.buildDistanceMap(enemy.pos, Dungeon.level.liquid(), viewDistance);
				enemyInFOV = PathFinder.distance[pos] != Integer.MAX_VALUE;
			}

			return super.act(enemyInFOV, justAlerted);
		}
	}

	private class WaterHunting extends Mob.Hunting{

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			if (enemyInFOV) {
				PathFinder.buildDistanceMap(enemy.pos, Dungeon.level.liquid(), viewDistance);
				enemyInFOV = PathFinder.distance[pos] != Integer.MAX_VALUE;
			}

			return super.act(enemyInFOV, justAlerted);
		}
	}
}

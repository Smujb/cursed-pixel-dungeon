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

package com.shatteredpixel.yasd.general.levels.traps;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.levels.RegularLevel;
import com.shatteredpixel.yasd.general.levels.rooms.Room;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.utils.BArray;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RockfallTrap extends Trap {

	{
		color = GREY;
		shape = DIAMOND;
		
		canBeHidden = false;
	}
	
	@Override
	public void activate() {
		
		ArrayList<Integer> rockCells = new ArrayList<>();
		
		//determines if the trap is actually in the world, or if it is being spawned for its effect
		boolean onGround = Dungeon.level.traps.get(pos) == this;
		
		if (onGround && Dungeon.level instanceof RegularLevel){
			Room r = ((RegularLevel) Dungeon.level).room(pos);
			int cell;
			for (Point p : r.getPoints()){
				cell = Dungeon.level.pointToCell(p);
				if (!Dungeon.level.solid(cell)){
					rockCells.add(cell);
				}
			}
			
		//if we don't have rooms, then just do 5x5
		} else {
			PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid(), null ), 2 );
			for (int i = 0; i < PathFinder.distance.length; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE) {
					rockCells.add(i);
				}
			}
		}
		
		boolean seen = false;
		for (int cell : rockCells){

			if (Dungeon.level.heroFOV[ cell ]){
				CellEmitter.get( cell - Dungeon.level.width() ).start(Speck.factory(Speck.ROCK), 0.07f, 10);
				seen = true;
			}

			Char ch = Actor.findChar( cell );

			if (ch != null && ch.isAlive()){
				int damage = Random.NormalIntRange(5+Dungeon.getScaleFactor(), 10+Dungeon.getScaleFactor() *2);
				ch.damage( Math.max(damage, 0) , new Char.DamageSrc(Element.PHYSICAL, this) );

				Buff.prolong( ch, Paralysis.class, Paralysis.DURATION );

				if (!ch.isAlive() && ch == Dungeon.hero){
					Dungeon.fail( getClass() );
					GLog.n( Messages.get(this, "ondeath") );
				}
			}
		}
		
		if (seen){
			Camera.main.shake(3, 0.7f);
			Sample.INSTANCE.play(Assets.SND_ROCKS);
		}

	}
}

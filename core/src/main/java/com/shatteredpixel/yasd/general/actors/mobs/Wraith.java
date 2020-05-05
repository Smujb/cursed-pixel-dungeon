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

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.PinCushion;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Grim;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.WraithSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Wraith extends Mob {

	private static final float SPAWN_DELAY	= 2f;


	private static final float BLINK_CHANCE	= 0.125f;
	
	{
		spriteClass = WraithSprite.class;
		EXP = 0;

		maxLvl = -2;
		range = 4;
		shotType = Ballistica.MAGIC_BOLT;

		damageFactor = 2/3f;
		healthFactor = 0.5f;
		
		flying = true;

		properties.add(Property.UNDEAD);
		properties.add(Property.BLOB_IMMUNE);
		//Resistant to physical weapons.
		resistances.put(Element.PHYSICAL, 0.5f);
	}

	@Override
	public Element elementalType() {
		return Element.DRAIN;
	}

	@Override
	public void add(Buff buff) {
		if (!(buff.type == Buff.buffType.NEGATIVE)) {
			super.add( buff );
		}
		if (buff instanceof PinCushion) {
			buff.detach();
		}
	}

	@Override
	public boolean reset() {
		state = WANDERING;
		return true;
	}
	
	private static Wraith spawnNeighbor(int pos) {
		ArrayList<Integer> locations = new  ArrayList<>();
		for (int n : PathFinder.NEIGHBOURS8) {
			int cell = pos + n;
			if (Dungeon.level.passable(cell) && Actor.findChar( cell ) == null) {
				locations.add(cell);
			}
		}
		if (locations.size() > 0) {
			return spawnAt(Random.element(locations), false);
		} else {
			return null;
		}
	}

	public static Wraith spawnAt( int pos ) {
		return spawnAt(pos, true);
	}
	
	public static Wraith spawnAt( int pos, boolean useNeighbors ) {
		if (Dungeon.level.passable(pos) && Actor.findChar( pos ) == null) {
			
			Wraith w = Mob.create(Wraith.class);
			w.enemySeen = true;
			w.pos = pos;
			w.state = w.HUNTING;
			GameScene.add( w, SPAWN_DELAY );
			
			w.sprite.alpha( 0 );
			w.sprite.parent.add( new  AlphaTweener( w.sprite, 1, 0.5f ) );
			
			w.sprite.emitter().burst( ShadowParticle.CURSE, 5 );
			
			return w;
		} else {
			if (useNeighbors) {
				return spawnNeighbor(pos);
			} else {
				return null;
			}
		}
	}
	
	{
		immunities.add( Grim.class );
	}

	private void blink() {

		ArrayList<Integer> cells = new  ArrayList<>();

		for( Integer cell : Dungeon.level.getPassableCellsList() ){
			if( pos != cell && enemy.fieldOfView[ cell ] ) {
				cells.add( cell );
			}
		}

		int newPos = !cells.isEmpty() ? Random.element( cells ) : pos ;

		if (enemy.fieldOfView[pos]) {
			CellEmitter.get(pos).start( ShadowParticle.UP, 0.01f, Random.IntRange(5, 10) );
		}

		if (enemy.fieldOfView[newPos]) {
			CellEmitter.get(newPos).start(ShadowParticle.MISSILE, 0.01f, Random.IntRange(5, 10));
		}

		ScrollOfTeleportation.appear( this, newPos );

		move( newPos );
	}

	@Override
	protected boolean doAttack( Char enemy ) {

		if (!rooted && Random.Float() < BLINK_CHANCE) {
			blink();
		}

		return super.doAttack(enemy);
	}
}

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

package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.Web;
import com.shatteredpixel.yasd.general.effects.Beam;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.particles.PurpleParticle;
import com.shatteredpixel.yasd.general.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfDisintegration extends DamageWand {

	{
		image = ItemSpriteSheet.WAND_DISINTEGRATION_YAPD;

		collisionProperties = Ballistica.WONT_STOP;

		element = Element.DESTRUCTION;
	}

	@Override
	public float min(float lvl){
		return 2+lvl;
	}

	@Override
	public float max(float lvl){
		return 8+4*lvl;
	}
	
	@Override
	public void onZap(Ballistica beam) {
		
		boolean terrainAffected = false;
		
		int maxDistance = Math.min(distance(), beam.dist);
		
		ArrayList<Char> chars = new ArrayList<>();

		Blob web = Dungeon.level.blobs.get(Web.class);

		int terrainPassed = 2, terrainBonus = 0;
		for (int c : beam.subPath(1, maxDistance)) {
			
			Char ch;
			if ((ch = Actor.findChar( c )) != null) {

				//we don't want to count passed terrain after the last enemy hit. That would be a lot of bonus levels.
				//terrainPassed starts at 2, equivalent of rounding up when /3 for integer arithmetic.
				terrainBonus += terrainPassed/3;
				terrainPassed = terrainPassed%3;

				chars.add( ch );
			}

			if (Dungeon.level.solid(c)) {
				terrainPassed++;
				if (web != null) web.clear(c);
			}

			if (Dungeon.level.flammable(c)) {

				Dungeon.level.destroy( c );
				GameScene.updateMap( c );
				terrainAffected = true;
				
			}
			
			CellEmitter.center( c ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
		}
		
		if (terrainAffected) {
			Dungeon.observe();
		}

		float multiplier = (float) Math.pow(1.1, (chars.size()-1) + terrainBonus);
		for (Char ch : chars) {
			processSoulMark(ch, chargesPerCast());
			hit(ch, (int) (damageRoll()*multiplier));
			ch.sprite.centerEmitter().burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
			ch.sprite.flash();
		}
	}

	private int getReflectTo( int sourcePos, int targetPos ) {

		int sourceX = sourcePos % Dungeon.level.width();
		int sourceY = sourcePos / Dungeon.level.width();

		int targetX = targetPos % Dungeon.level.width();
		int targetY = targetPos / Dungeon.level.width();

		int reflectX = targetX;
		int reflectY = targetY;

		int deltaX = targetX - sourceX;
		int deltaY = targetY - sourceY;

		// right angles would reflect everything right back at ya so they are ignored
		if( deltaX != 0 && deltaY != 0 ){

			boolean horizontWall = Dungeon.level.solid( targetPos - ( deltaX > 0 ? 1 : -1 ) );
			boolean verticalWall = Dungeon.level.solid( targetPos - ( deltaY > 0 ? Dungeon.level.width() : -Dungeon.level.width() ) );

			if( !horizontWall || !verticalWall ) {

				// convex corners reflect in random direction
				boolean reflectHorizontally = horizontWall || ( !verticalWall && Random.Int( 2 ) == 0 );

				if( reflectHorizontally ) {
					// perform horizontal reflection
					reflectX += deltaX;
					reflectY -= deltaY;
				} else {
					// perform vertical reflection
					reflectX -= deltaX;
					reflectY += deltaY;
				}
			} else {

				// concave corners reflect everything by both axes, unless hit from 45 degrees angle
				if( Math.abs( deltaX ) != Math.abs( deltaY ) ){

					if( deltaX > 0 == deltaY > 0 ){
						reflectX -= deltaY;
						reflectY -= deltaX;
					} else {
						reflectX += deltaY;
						reflectY += deltaX;
					}
				}
			}
		}

		reflectX = (int) GameMath.gate( 0, reflectX, Dungeon.level.width() );
		reflectY = (int) GameMath.gate( 0, reflectY, Dungeon.level.height() );

		return reflectX + reflectY * Dungeon.level.width();

	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//no direct effect, see magesStaff.reachfactor
	}

	private int distance() {
		return level()*2 + 4;
	}
	
	@Override
	protected void fx( Ballistica beam, Callback callback ) {
		
		int cell = beam.path.get(Math.min(beam.dist, distance()));
		curUser.sprite.parent.add(new Beam.DeathRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld( cell )));
		callback.call();
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color(0x220022);
		particle.am = 0.6f;
		particle.setLifespan(1f);
		particle.acc.set(10, -10);
		particle.setSize( 0.5f, 3f);
		particle.shuffleXY(1f);
	}

}

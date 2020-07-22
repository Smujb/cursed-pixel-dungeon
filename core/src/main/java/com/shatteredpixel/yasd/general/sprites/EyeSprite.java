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

package com.shatteredpixel.yasd.general.sprites;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.mobs.Eye;
import com.shatteredpixel.yasd.general.effects.Beam;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public abstract class EyeSprite extends MobSprite {

	private int zapPos;

	private Animation charging;
	private Emitter chargeParticles;
	
	public EyeSprite(int offset) {
		super();
		
		texture( Assets.Sprites.EYE );
		
		TextureFilm frames = new TextureFilm( texture, 16, 18 );
		
		idle = new Animation( 8, true );
		idle.frames( frames, 0+offset, 1+offset, 2+offset );

		charging = new Animation( 12, true);
		charging.frames( frames, 3+offset, 4+offset );
		
		run = new Animation( 12, true );
		run.frames( frames, 5+offset, 6+offset );
		
		attack = new Animation( 8, false );
		attack.frames( frames, 4+offset, 3+offset );
		zap = attack.clone();
		
		die = new Animation( 8, false );
		die.frames( frames, 7+offset, 8+offset, 9+offset );
		
		play( idle );
	}

	@Override
	public void killAndErase() {
		super.killAndErase();
		if (chargeParticles != null) {
			chargeParticles.killAndErase();
		}
	}

	@Override
	public void link(Char ch) {
		super.link(ch);

		chargeParticles = centerEmitter();
		chargeParticles.autoKill = false;
		chargeParticles.pour(MagicMissile.MagicParticle.ATTRACTING, 0.05f);
		chargeParticles.on = false;

		if (((Eye)ch).beamCharged()) play(charging);
	}

	@Override
	public void update() {
		super.update();
		if (chargeParticles != null){
			chargeParticles.pos( center() );
			chargeParticles.visible = visible;
		}
	}

	@Override
	public void die() {
		super.die();
		if (chargeParticles != null){
			chargeParticles.on = false;
		}
	}

	@Override
	public void kill() {
		super.kill();
		if (chargeParticles != null){
			chargeParticles.killAndErase();
		}
	}

	public void charge( int pos ){
		turnTo(ch.pos, pos);
		play(charging);
	}

	@Override
	public void play(Animation anim) {
		if (chargeParticles != null) chargeParticles.on = anim == charging;
		super.play(anim);
	}

	@Override
	public void zap(int cell) {
		zapPos = cell;
		play(zap);
	}

	@Override
	public void onComplete( Animation anim ) {
		super.onComplete( anim );
		
		if (anim == zap) {
			idle();
			if (Actor.findChar(zapPos) != null) {
				parent.add(new Beam.DeathRay(center(), Actor.findChar(zapPos).sprite.center()));
				ch.attack(Actor.findChar(zapPos));
			} else {
				parent.add(new Beam.DeathRay(center(), DungeonTilemap.raisedTileCenterToWorld(zapPos)));
			}
			ch.next();
			((Eye)ch).beamUsed();
		} else if (anim == die){
			chargeParticles.killAndErase();
		}
	}

	public static class Purple extends EyeSprite {
		public Purple() {
			super(0);
		}
	}

	public static class Red extends EyeSprite {
		public Red() {
			super(32);
		}
	}
}

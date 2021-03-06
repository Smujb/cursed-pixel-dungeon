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
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.watabou.noosa.TextureFilm;

public class WraithSprite extends MobSprite {
	private Animation blink;
	
	public WraithSprite() {
		super();
		
		texture( Assets.Sprites.WRAITH );
		
		TextureFilm frames = new TextureFilm( texture, 14, 15 );
		
		idle = new Animation( 5, true );
		idle.frames( frames, 0, 1 );
		
		run = new Animation( 10, true );
		run.frames( frames, 0, 1 );
		
		attack = new Animation( 10, false );
		attack.frames( frames, 0, 2, 3 );
		
		die = new Animation( 8, false );
		die.frames( frames, 0, 4, 5, 6, 7 );

		blink = new Animation( 15, false );
		blink.frames( frames, 7, 6, 5, 4, 0 );
		
		play( idle );
	}
	
	@Override
	public int blood() {
		return 0x88000000;
	}

	@Override
	public void die() {
		super.die();
		if (Dungeon.hero.fieldOfView[ch.pos]) {
			emitter().burst( ShadowParticle.CURSE, 10 );
		}
	}

	public void blink( int from, int to ) {

		place( to );

		play( blink );
		turnTo( from , to );

		isMoving = true;

		ch.onMotionComplete();
	}
}

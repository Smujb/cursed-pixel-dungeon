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
import com.shatteredpixel.yasd.general.effects.Speck;
import com.watabou.noosa.TextureFilm;

public class SkeletonSprite extends MobSprite {

	protected int offset = 0;
	
	public SkeletonSprite() {
		super();
		
		texture( Assets.Sprites.SKELETON );
		
		TextureFilm frames = new TextureFilm( texture, 12, 15 );
		
		idle = new Animation( 12, true );
		idle.frames( frames, 0+offset, 0+offset, 0+offset, 0+offset, 0+offset, 0+offset, 0+offset, 0+offset, 0+offset, 0+offset, 0+offset, 0+offset, 0+offset, 1+offset, 2+offset, 3+offset );
		
		run = new Animation( 15, true );
		run.frames( frames, 4+offset, 5+offset, 6+offset, 7+offset, 8+offset, 9+offset );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 14+offset, 15+offset, 16+offset );
		
		die = new Animation( 12, false );
		die.frames( frames, 10+offset, 11+offset, 12+offset, 13+offset);
		
		play( idle );
	}
	
	@Override
	public void die() {
		super.die();
		if (Dungeon.level.heroFOV[ch.pos]) {
			emitter().burst( Speck.factory( Speck.BONE ), 6 );
		}
	}
	
	@Override
	public int blood() {
		return 0xFFcccccc;
	}

	public static class Old extends SkeletonSprite {
		{
			offset = 20;
		}
	}
}

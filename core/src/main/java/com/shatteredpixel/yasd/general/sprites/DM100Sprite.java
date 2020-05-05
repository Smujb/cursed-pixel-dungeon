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

package com.shatteredpixel.yasd.general.sprites;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.watabou.noosa.TextureFilm;

public class DM100Sprite extends MobSprite {
	
	public DM100Sprite() {
		super();

		texture( Assets.DM100 );

		TextureFilm frames = new TextureFilm( texture, 16, 14 );

		idle = new Animation( 1, true );
		idle.frames( frames, 0, 1 );

		attack = new Animation( 12, false );
		attack.frames( frames, 2, 3, 4, 0 );

		zap = new Animation( 8, false );
		zap.frames( frames, 5, 5, 1 );

		die = new Animation( 12, false );
		die.frames( frames, 10, 11, 12, 13, 14, 15 );
		
		play( idle );
	}

	@Override
	public void die() {
		emitter().burst( Speck.factory( Speck.WOOL ), 5 );
		super.die();
	}
}

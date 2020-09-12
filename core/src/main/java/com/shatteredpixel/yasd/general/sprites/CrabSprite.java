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
import com.watabou.noosa.TextureFilm;

public abstract class CrabSprite extends MobSprite {
	private static final int WIDTH = 16;

	public CrabSprite(int row) {
		super();

		texture( Assets.Sprites.CRAB );

		TextureFilm frames = new TextureFilm( texture, 16, 16 );

		int offset = row*WIDTH;

		idle = new Animation( 5, true );
		idle.frames( frames, 0+offset, 1+offset, 0+offset, 2+offset );

		run = new Animation( 15, true );
		run.frames( frames, 3+offset, 4+offset, 5+offset, 6+offset );

		attack = new Animation( 12, false );
		attack.frames( frames, 7+offset, 8+offset, 9+offset );

		die = new Animation( 12, false );
		die.frames( frames, 10+offset, 11+offset, 12+offset, 13+offset );

		play( idle );
	}

	@Override
	public int blood() {
		return 0xFFFFEA80;
	}

	public static class Crab extends CrabSprite {
		public Crab() {
			super(0);
		}
	}

	public static class GreatCrab extends CrabSprite {
		public GreatCrab() {
			super(1);
		}
	}

	public static class MagicCrab extends CrabSprite {
		public MagicCrab() {
			super(2);
			TextureFilm frames = new TextureFilm( texture, 16, 16 );
			int offset = 2*WIDTH;

			run.frames( frames, 3+offset, 4+offset, 5+offset, 6+offset, 7+offset);

			attack = new Animation( 12, false );
			attack.frames( frames, 8+offset, 9+offset, 10+offset );

			die = new Animation( 12, false );
			die.frames( frames, 11+offset, 12+offset, 13+offset, 14+offset, 15+offset );
		}
	}
}

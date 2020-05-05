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

package com.shatteredpixel.yasd.general.ui;

import com.shatteredpixel.yasd.general.Dungeon;
import com.watabou.noosa.Image;

public class IconDifficulty extends Image {

	public IconDifficulty() {
		super();

		switch( Dungeon.difficulty ){
			case IMPOSSIBLE:
				copy( Icons.WARNING.get() );
				break;
			case HARD:
				copy( Icons.DIFF3.get() );
				break;
			case MEDIUM:
				copy( Icons.DIFF2.get() );
				break;
			default:
				copy( Icons.DIFF1.get() );
				break;
		}

		origin.set( width / 2, height / 2 );
	}
}

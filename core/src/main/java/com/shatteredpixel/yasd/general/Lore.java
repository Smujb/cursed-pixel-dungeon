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

package com.shatteredpixel.yasd.general;

import com.shatteredpixel.yasd.general.levels.CavesLevel;
import com.shatteredpixel.yasd.general.levels.CityLevel;
import com.shatteredpixel.yasd.general.levels.FirstLevel;
import com.shatteredpixel.yasd.general.levels.HallsLevel;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.PrisonLevel;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.TextScene;
import com.watabou.utils.Callback;

import java.util.HashMap;

public class Lore {

	private static final HashMap<Class<? extends Level>, String> CHAPTERS = new HashMap<>();

	static {
		CHAPTERS.put( FirstLevel.class, "sewers" );
		CHAPTERS.put( PrisonLevel.class, "prison" );
		CHAPTERS.put( CavesLevel.class, "caves" );
		CHAPTERS.put( CityLevel.class, "city" );
		CHAPTERS.put( HallsLevel.class, "halls" );
	}

	public static void showChapter( Level level ) {

		String text = Messages.get(Lore.class, CHAPTERS.get(level.getClass()));
		if (!text.contains("missed_string")) {
			TextScene.init(text, null, level.loadImg(), 5, 0.67f, new Callback() {
				@Override
				public void call() {
					MainGame.switchScene(GameScene.class);
				}
			}, null, false);
		}
	}
}

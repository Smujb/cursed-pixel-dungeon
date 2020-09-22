/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Cursed Pixel Dungeon
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

import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.SurfaceLevel;
import com.shatteredpixel.yasd.general.levels.chapters.airtrial.AirTrialLevel;
import com.shatteredpixel.yasd.general.levels.chapters.caves.CavesLevel;
import com.shatteredpixel.yasd.general.levels.chapters.city.CityLevel;
import com.shatteredpixel.yasd.general.levels.chapters.earthtrial.EarthTrialLevel;
import com.shatteredpixel.yasd.general.levels.chapters.firetrial.FireTrialLevel;
import com.shatteredpixel.yasd.general.levels.chapters.halls.HallsLevel;
import com.shatteredpixel.yasd.general.levels.chapters.halls.LastLevel;
import com.shatteredpixel.yasd.general.levels.chapters.prison.PrisonLevel;
import com.shatteredpixel.yasd.general.levels.chapters.sewers.FirstLevel;
import com.shatteredpixel.yasd.general.levels.chapters.watertrial.WaterTrialLevel;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.TextScene;
import com.watabou.utils.Callback;

import java.util.HashMap;

public class Lore {

	private static final HashMap<Class<? extends Level>, String> CHAPTERS = new HashMap<>();

	static {
		CHAPTERS.put( SurfaceLevel.class, "intro" );
		CHAPTERS.put( FirstLevel.class, "sewers" );
		CHAPTERS.put( PrisonLevel.class, "prison" );
		CHAPTERS.put( CavesLevel.class, "caves" );
		CHAPTERS.put( CityLevel.class, "city" );
		CHAPTERS.put( HallsLevel.class, "halls" );
		CHAPTERS.put( LastLevel.class, "outro" );
		CHAPTERS.put( WaterTrialLevel.class, "water_trial" );
		CHAPTERS.put( FireTrialLevel.class, "fire_trial" );
		CHAPTERS.put( AirTrialLevel.class, "air_trial" );
		CHAPTERS.put( EarthTrialLevel.class, "earth_trial" );
	}

	public static void showChapter( Level level ) {
		showChapter(level.getClass(), level.loadImg(), null);
	}


	public static void showChapter( Class<? extends Level> levelClass, String img, Callback callback ) {

		String key = CHAPTERS.get(levelClass);
		if (levelClass == SurfaceLevel.class || levelClass == LastLevel.class) {
			key += "_" + CPDSettings.storyChapter().name();
		}
		String text = Messages.get(Lore.class, key);
		if ((!CPDSettings.watchedCutscene(key) || CPDSettings.cutscenes()) && !text.contains("missed_string") && !text.equals("TODO")) {
			String finalKey = key;
			TextScene.init(text, null, img, 5, 0.67f, new Callback() {
				@Override
				public void call() {
					CPDSettings.watchedCutscene(finalKey, true);
					CPDGame.switchScene(GameScene.class);
					if (callback != null) {
						callback.call();
					}
				}
			}, null, false, CPDSettings.watchedCutscene(key));
		} else {
			if (callback != null) {
				callback.call();
			}
		}
	}
}

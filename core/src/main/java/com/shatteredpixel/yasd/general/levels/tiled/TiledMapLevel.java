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

package com.shatteredpixel.yasd.general.levels.tiled;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.MapHandler;
import com.shatteredpixel.yasd.general.levels.Level;
import com.watabou.utils.Callback;

public abstract class TiledMapLevel extends Level {

	private Map map;

	@Override
	protected boolean build() {
		//FIXME this is a horrible solution, but it may be the only option on current engine. I should possibly look into refactoring code around creating levels.
		final Level _this = this;
		//Setup map to be loaded on render thread
		CPDGame.runOnRenderThread(new Callback() {
			public void call() {
				setMap();
				synchronized (_this) {
					_this.notify();
				}
			}
		});
		//Pause the current thread until map is loaded. This *must* be executed after the previous line.
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		return MapHandler.build(this, map);
	}

	protected abstract String mapName();

	private void setMap() {
		map = new TmxMapLoader().load(mapName());
	}

	@Override
	protected final void createMobs() {
		MapHandler.createMobs(this, map);
	}

	@Override
	protected final void createItems() {
		MapHandler.createItems(this, map);
	}

	@Override
	protected final void createAreas() {
		MapHandler.createAreas(this, map);
	}
}

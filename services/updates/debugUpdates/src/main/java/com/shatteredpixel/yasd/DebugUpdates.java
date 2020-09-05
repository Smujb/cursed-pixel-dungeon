package com.shatteredpixel.yasd;/*
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

import com.watabou.noosa.Game;
import com.watabou.utils.DeviceCompat;

public class DebugUpdates extends UpdateService {

	private static AvailableUpdateData debugUpdateInfo;

	public void checkForUpdate(boolean useMetered, UpdateResultCallback callback) {

		if (!useMetered && !Game.platform.connectedToUnmeteredNetwork()){
			callback.onConnectionFailed();
			return;
		}

		//turn on to test update UI
		if (false){
			debugUpdateInfo = new AvailableUpdateData();
			debugUpdateInfo.versionCode = Game.versionCode+1;
			debugUpdateInfo.URL = "http://www.google.com";

			callback.onUpdateAvailable(debugUpdateInfo);
		} else {
			debugUpdateInfo = null;

			callback.onNoUpdateFound();
		}

	}

	@Override
	public void initializeUpdate(AvailableUpdateData update) {
		DeviceCompat.openURI( update.URL );
	}

}
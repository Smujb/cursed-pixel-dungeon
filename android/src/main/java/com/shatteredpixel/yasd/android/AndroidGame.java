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

package com.shatteredpixel.yasd.android;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.rohitss.uceh.UCEHandler;
import com.shatteredpixel.yasd.UpdateImpl;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.CPDSettings;
import com.shatteredpixel.yasd.general.services.Updates;
import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;

public class AndroidGame extends AndroidApplication {
	
	public static AndroidApplication instance;
	public static AndroidApplicationConfiguration config;
	protected static GLSurfaceView view;

	private static AndroidPlatformSupport support;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		
		try {
			Game.version = getPackageManager().getPackageInfo( getPackageName(), 0 ).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			Game.version = "???";
		}
		try {
			Game.versionCode = getPackageManager().getPackageInfo( getPackageName(), 0 ).versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			Game.versionCode = 0;
		}

		if (UpdateImpl.supportsUpdates()){
			Updates.service = UpdateImpl.getUpdateService();
		}


		FileUtils.setDefaultFileProperties( Files.FileType.Local, "" );

		UCEHandler.Builder builder = new UCEHandler.Builder(this);
		builder.build();
		// grab preferences directly using our instance first
		// so that we don't need to rely on Gdx.app, which isn't initialized yet.
		// Note that we use a different prefs name on android for legacy purposes,
		// this is the default prefs filename given to an android app (.xml is automatically added to it)
		CPDSettings.set(instance.getPreferences("ShatteredPixelDungeon"));
		
		//set desired orientation (if it exists) before initializing the app.
		if (CPDSettings.landscape() != null) {
			AndroidGame.instance.setRequestedOrientation( CPDSettings.landscape() ?
					ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE :
					ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT );
		}
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.depth = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			//use rgb888 on more modern devices for better visuals
			config.r = config.g = config.b = 8;
		} else {
			//and rgb565 (default) on older ones for better performance
		}
		
		config.useCompass = false;
		config.useAccelerometer = false;

		if (support == null) support = new AndroidPlatformSupport();
		else                 support.resetGenerators();
		
		support.updateSystemUI();

		CPDGame cpdGame = new CPDGame(support);
		initialize(cpdGame, config);
		
		view = (GLSurfaceView)graphics.getView();
	}

	@Override
	public View initializeForView(ApplicationListener listener) {
		config = new AndroidApplicationConfiguration();
		config.useGyroscope = true;  //default is false

		//you may want to switch off sensors that are on by default if they are no longer needed.
		config.useAccelerometer = false;
		config.useCompass = false;
		return initializeForView(listener, config);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		support.updateSystemUI();
	}
	
	@Override
	public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
		super.onMultiWindowModeChanged(isInMultiWindowMode);
		support.updateSystemUI();
	}
}
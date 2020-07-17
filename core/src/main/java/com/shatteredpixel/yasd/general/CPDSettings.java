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

package com.shatteredpixel.yasd.general;

import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.RelicMeleeWeapon;
import com.shatteredpixel.yasd.general.messages.Languages;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Point;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class CPDSettings extends com.watabou.utils.GameSettings {
	
	//Version info
	
	public static final String KEY_VERSION      = "version";
	
	public static void version( int value)  {
		put( KEY_VERSION, value );
	}
	
	public static int version() {
		return getInt( KEY_VERSION, 0 );
	}
	
	//Graphics

	private static final String KEY_DARK	= "dark_ui";
	private static final String KEY_FULLSCREEN	= "fullscreen";
	private static final String KEY_LANDSCAPE	= "landscape";
	public static final String KEY_POWER_SAVER 	= "power_saver";
	private static final String KEY_SCALE		= "scale";
	private static final String KEY_ZOOM			= "zoom";
	private static final String KEY_BRIGHTNESS	= "brightness";
	private static final String KEY_GRID 	    = "visual_grid";
	private static final String KEY_CUTSCENES 	    	= "cutscenes";
	private static final String KEY_FAST_INTERLEVEL_SCENE 	    	= "fast-scenes";
	private static final String KEY_CUTSCENE 	    	= "watched-cutscene-%s";

	public static void darkUI(boolean value) {
		put( KEY_DARK, value );
	}

	public static boolean darkUI() {
		return getBoolean(KEY_DARK, false);
	}
	
	public static void fullscreen( boolean value ) {
		put( KEY_FULLSCREEN, value );
		
		CPDGame.updateSystemUI();
	}
	
	public static boolean fullscreen() {
		return getBoolean( KEY_FULLSCREEN, false );
	}
	
	public static void landscape( boolean value ){
		put( KEY_LANDSCAPE, value );
		((CPDGame) CPDGame.instance).updateDisplaySize();
	}

	//can return null because we need to directly handle the case of landscape not being set
	// as there are different defaults for different devices
	public static Boolean landscape(){
		if (contains(KEY_LANDSCAPE)){
			return getBoolean(KEY_LANDSCAPE, false);
		} else {
			return null;
		}
	}
	
	public static void powerSaver( boolean value ){
		put( KEY_POWER_SAVER, value );
		((CPDGame) CPDGame.instance).updateDisplaySize();
	}
	
	public static boolean powerSaver(){
		return getBoolean( KEY_POWER_SAVER, false );
	}
	
	public static void scale( int value ) {
		put( KEY_SCALE, value );
	}
	
	public static int scale() {
		return getInt( KEY_SCALE, 0 );
	}
	
	public static void zoom( int value ) {
		put( KEY_ZOOM, value );
	}
	
	public static int zoom() {
		return getInt( KEY_ZOOM, 0 );
	}
	
	public static void brightness( int value ) {
		put( KEY_BRIGHTNESS, value );
		GameScene.updateFog();
	}
	
	public static int brightness() {
		return getInt( KEY_BRIGHTNESS, 0, -1, 1 );
	}
	
	public static void visualGrid( int value ){
		put( KEY_GRID, value );
		GameScene.updateMap();
	}
	
	public static int visualGrid(){		return getInt( KEY_GRID, 0, -1, 2 );	}

	public static void cutscenes( boolean value ){
		put( KEY_CUTSCENES, value );
	}

	public static boolean cutscenes() {
		return getBoolean( KEY_CUTSCENES, true );
	}

	public static void watchedCutscene(String sceneID, boolean value) {
		put( Messages.format(KEY_CUTSCENE, sceneID), value );
	}

	public static boolean watchedCutscene(String sceneID) {
		return getBoolean(Messages.format(KEY_CUTSCENE, sceneID), false );
	}

	public static void fastInterlevelScene( boolean value ){
		put( KEY_FAST_INTERLEVEL_SCENE, value );
	}

	public static boolean fastInterlevelScene() {
		return getBoolean( KEY_FAST_INTERLEVEL_SCENE, false );
	}

	//Interface
	
	private static final String KEY_QUICKSLOTS	= "quickslots";
	private static final String KEY_PARTICLES	= "particles";
	private static final String KEY_FLIPTOOLBAR	= "flipped_ui";
	private static final String KEY_FLIPTAGS 	= "flip_tags";
	private static final String KEY_BARMODE		= "toolbar_mode";

	public static void quickslots( int value ){
		put( KEY_QUICKSLOTS, value );
	}

	public static int quickslots(){
		return getInt( KEY_QUICKSLOTS, 4, Constants.MIN_QUICKSLOTS, Constants.MAX_QUICKSLOTS);
	}
	
	public static void particles( int value ){
		put( KEY_PARTICLES, value );
	}
	
	public static int particles(){
		return getInt( KEY_PARTICLES, 1, 0, 6);
	}
	
	public static void flipToolbar( boolean value) {
		put(KEY_FLIPTOOLBAR, value );
	}
	
	public static boolean flipToolbar(){ return getBoolean(KEY_FLIPTOOLBAR, false); }
	
	public static void flipTags( boolean value) {
		put(KEY_FLIPTAGS, value );
	}
	
	public static boolean flipTags(){ return getBoolean(KEY_FLIPTAGS, false); }
	
	public static void toolbarMode( String value ) {
		put( KEY_BARMODE, value );
	}
	
	public static String toolbarMode() {

		return getString(KEY_BARMODE, PixelScene.landscape() ? "GROUP" : "SPLIT");
	}
	
	//Game State
	
	public static final String KEY_LAST_CLASS	= "last_class";
	public static final String KEY_CHALLENGES	= "challenges";
	public static final String KEY_INTRO		= "intro";
	public static final String KEY_TESTING		= "testing";
	public static final String KEY_CHAPTER		= "chapter";
	public static final String KEY_RELIC_WEAPON		= "relic_weapon %s";
	public static final String KEY_GRINDING_HIGH_SCORE	= "grinding_high_score";

	public static boolean validateGrindingHighScore( int value ) {
		if (value <= getGrindingHighScore()) {
			return false;
		}
		put( KEY_GRINDING_HIGH_SCORE, value );
		return true;
	}

	public static int getGrindingHighScore() {
		return getInt( KEY_GRINDING_HIGH_SCORE, 0);
	}
	
	public static void intro( boolean value ) {
		put( KEY_INTRO, value );
	}
	
	public static boolean intro() {
		return getBoolean( KEY_INTRO, true );
	}

	private static void lastClass(int value) {
		put( KEY_LAST_CLASS, value );
	}

	public static void lastClass( HeroClass value ) {
		int cl = 0;
		switch (value) {
			case WARRIOR:
				cl = 0;
				break;

			case MAGE:
				cl = 1;
				break;

			case ROGUE:
				cl = 2;
				break;

			case HUNTRESS:
				cl = 3;
				break;

			case PRIESTESS:
				cl = 4;
				break;
		}
		lastClass(cl);
	}
	
	public static int lastClass() {
		return getInt( KEY_LAST_CLASS, 0, 0, 4 );
	}
	
	public static void challenges( int value ) {
		put( KEY_CHALLENGES, value );
	}
	
	public static int challenges() {
		return getInt( KEY_CHALLENGES, 0, 0, Challenges.MAX_VALUE );
	}


	public static boolean testing() {
		return getBoolean( KEY_TESTING, true);
	}

	public static void testing( boolean value ) {
		put( KEY_TESTING, value);
	}

	public static void storyChapter(@NotNull StoryChapter value) {
		put(KEY_CHAPTER, value.name());
	}

	public static StoryChapter storyChapter() {
		try {
			return Enum.valueOf(StoryChapter.class, getString(KEY_CHAPTER, StoryChapter.FIRST.name()));
		} catch (Exception e) {
			return StoryChapter.FIRST;
		}
	}

	public static void unlockRelicWep(Class<? extends RelicMeleeWeapon> wepClass, boolean value) {
		String name = wepClass.getName();
		put(Messages.format(KEY_RELIC_WEAPON, name), value);
	}

	public static boolean unlockedRelicWep(Class<? extends RelicMeleeWeapon> wepClass) {
		String name = wepClass.getName();
		return getBoolean(Messages.format(KEY_RELIC_WEAPON, name), false);
	}

	//Audio
	
	public static final String KEY_MUSIC		= "music";
	public static final String KEY_MUSIC_VOL    = "music_vol";
	public static final String KEY_SOUND_FX		= "soundfx";
	public static final String KEY_SFX_VOL      = "sfx_vol";
	public static final String VIBRATE          = "vibrate";
	public static boolean vibrate = false;
	
	public static void music( boolean value ) {
		Music.INSTANCE.enable( value );
		put( KEY_MUSIC, value );
	}
	
	public static boolean music() {
		return getBoolean( KEY_MUSIC, true );
	}
	
	public static void musicVol( int value ){
		Music.INSTANCE.volume(value/10f);
		put( KEY_MUSIC_VOL, value );
	}
	
	public static int musicVol(){
		return getInt( KEY_MUSIC_VOL, 10, 0, 10 );
	}
	
	public static void soundFx( boolean value ) {
		Sample.INSTANCE.enable( value );
		put( KEY_SOUND_FX, value );
	}
	
	public static boolean soundFx() {
		return getBoolean( KEY_SOUND_FX, true );
	}

	public static void vibrate(boolean value) {
		vibrate = value;
		put( VIBRATE, value);
	}

	public static boolean vibrate() {
		return getBoolean(VIBRATE,true);
	}
	
	public static void SFXVol( int value ) {
		Sample.INSTANCE.volume(value/10f);
		put( KEY_SFX_VOL, value );
	}

	public static int SFXVol() {
		return getInt( KEY_SFX_VOL, 10, 0, 10 );
	}
	
	//Languages and Font
	
	public static final String KEY_LANG         = "language";
	public static final String KEY_SYSTEMFONT	= "system_font";
	public static final String KEY_DIFFICULTY	= "difficulty";
	
	public static void language(Languages lang) {
		put( KEY_LANG, lang.code());
	}
	
	public static Languages language() {
		String code = getString(KEY_LANG, null);
		if (code == null){
			return Languages.matchLocale(Locale.getDefault());
		} else {
			return Languages.matchCode(code);
		}
	}
	
	public static void systemFont(boolean value){
		put(KEY_SYSTEMFONT, value);
	}
	
	public static boolean systemFont(){
		return getBoolean(KEY_SYSTEMFONT,
				(language() == Languages.KOREAN || language() == Languages.CHINESE || language() == Languages.JAPANESE));
	}

	public static void difficulty( int value ) {
		put( KEY_DIFFICULTY, value );
	}

	public static int difficulty() {
		return getInt( KEY_DIFFICULTY, 2, 0, 4 );
	}

	//Developer options
	private static final String MAP_CACHE  	  = "map_cache";
	private static final String DEBUG_REPORT  = "debug_report";

	public static void mapCache(boolean value) {
		put( MAP_CACHE, value);
	}

	public static boolean mapCache() {
		return getBoolean(MAP_CACHE,true);
	}

	public static void debugReport(boolean value) {
		put( DEBUG_REPORT, value);
	}

	public static boolean debugReport() {
		return getBoolean(DEBUG_REPORT,false);
	}

	//Window management (desktop only atm)

	public static final String KEY_WINDOW_WIDTH     = "window_width";
	public static final String KEY_WINDOW_HEIGHT    = "window_height";
	public static final String KEY_WINDOW_MAXIMIZED = "window_maximized";

	public static void windowResolution( Point p ){
		put(KEY_WINDOW_WIDTH, p.x);
		put(KEY_WINDOW_HEIGHT, p.y);
	}

	public static Point windowResolution(){
		return new Point(

				getInt( KEY_WINDOW_WIDTH, 960, 480, Integer.MAX_VALUE ),
				getInt( KEY_WINDOW_HEIGHT, 640, 320, Integer.MAX_VALUE )
		);
	}

	public static void windowMaximized( boolean value ){
		put( KEY_WINDOW_MAXIMIZED, value );
	}

	public static boolean windowMaximized(){
		return getBoolean( KEY_WINDOW_MAXIMIZED, false );
	}
	
}

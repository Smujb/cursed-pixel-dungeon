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

package com.shatteredpixel.yasd.general;

import com.shatteredpixel.yasd.general.actors.mobs.Brute;
import com.shatteredpixel.yasd.general.items.rings.RingOfEvasion;
import com.shatteredpixel.yasd.general.items.rings.RingOfPerception;
import com.shatteredpixel.yasd.general.items.weapon.melee.Fist;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.scenes.WelcomeScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PlatformSupport;

public class MainGame extends Game {
	
	//variable constants for specific older versions of shattered, used for data conversion
	//versions older than v0.6.5c are no longer supported, and data from them is ignored
	public static final int v0_6_5c = 264;
	public static final int v0_2_0  = 400;
	public static final int v0_2_1  = 401;
	public static final int v0_2_2  = 402;
	public static final int v0_2_3  = 403;
	public static final int v0_2_4  = 404;
	public static final int v0_2_5  = 405;
	public static final int v0_2_6  = 406;
	public static final int v0_2_7  = 407;
	public static final int v0_2_8  = 409;
	public static final int v0_2_10  = 411;
	public static final int v0_2_11  = 412;
	public static final int v0_2_12  = 413;
	public static final int v0_2_13  = 414;
	public static final int v0_2_14  = 415;
	public static final int v0_2_15  = 416;
	public static final int v0_2_16  = 417;
	public static final int v0_2_17  = 418;
	public static final int v0_2_18  = 419;
	public static final int v0_2_19  = 422;
	public static final int v0_2_20  = 423;
	public static final int v0_2_21  = 424;
	public static final int v0_2_21B  = 425;
	public static final int v0_2_22  = 426;

	public static final int v0_3_0  = 427;
	public static final int v0_3_1  = 428;
	public static final int v0_3_2  = 429;
	public static final int v0_3_3  = 430;
	
	public MainGame(PlatformSupport platform ) {
		super( sceneClass == null ? WelcomeScene.class : sceneClass, platform );
		
		//v0.7.0
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.general.items.bombs.Bomb.class,
				"com.shatteredpixel.yasd.general.items.Bomb" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRetribution.class,
				"com.shatteredpixel.yasd.general.items.scrolls.ScrollOfPsionicBlast" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfMight.class,
				"com.shatteredpixel.yasd.general.items.potions.PotionOfMight" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.general.items.spells.MagicalInfusion.class,
				"com.shatteredpixel.yasd.general.items.scrolls.ScrollOfMagicalInfusion" );
		
		//v0.7.1
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.general.items.weapon.SpiritBow.class,
				"com.shatteredpixel.yasd.general.items.getWeapons.missiles.Boomerang" );
		
		com.watabou.utils.Bundle.addAlias(
				Fist.class,
				"com.shatteredpixel.yasd.general.items.getWeapons.melee.Knuckles" );
		
		//v0.7.2
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.general.items.stones.StoneOfDisarming.class,
				"com.shatteredpixel.yasd.general.items.stones.StoneOfDetectCurse" );

		com.watabou.utils.Bundle.addAlias(RingOfEvasion.class,
				"com.shatteredpixel.yasd.general.items.rings.RingOfForce");

		com.watabou.utils.Bundle.addAlias(RingOfPerception.class,
				"com.shatteredpixel.yasd.general.items.rings.RingOfAccuracy");

		com.watabou.utils.Bundle.addAlias(RingOfPerception.class,
				"com.shatteredpixel.yasd.general.items.rings._Unused");
		
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.general.items.weapon.enchantments.Elastic.class,
				"com.shatteredpixel.yasd.general.items.getWeapons.curses.Elastic" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.general.items.weapon.enchantments.Elastic.class,
				"com.shatteredpixel.yasd.general.items.getWeapons.enchantments.Dazzling" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.general.items.weapon.enchantments.Elastic.class,
				"com.shatteredpixel.yasd.general.items.getWeapons.enchantments.Eldritch" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.general.items.weapon.enchantments.Grim.class,
				"com.shatteredpixel.yasd.general.items.getWeapons.enchantments.Stunning" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.general.items.weapon.enchantments.Chilling.class,
				"com.shatteredpixel.yasd.general.items.getWeapons.enchantments.Venomous" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.general.items.weapon.enchantments.Kinetic.class,
				"com.shatteredpixel.yasd.general.items.getWeapons.enchantments.Vorpal" );
		
		//v0.7.3
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.general.items.weapon.enchantments.Kinetic.class,
				"com.shatteredpixel.yasd.general.items.getWeapons.enchantments.Precise" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.general.items.weapon.enchantments.Kinetic.class,
				"com.shatteredpixel.yasd.general.items.getWeapons.enchantments.Swift" );
		
		//v0.7.5
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.general.levels.rooms.sewerboss.SewerBossEntranceRoom.class,
				"com.shatteredpixel.yasd.general.levels.rooms.standard.SewerBossEntranceRoom" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.general.levels.OldPrisonBossLevel.class,
				"com.shatteredpixel.yasd.general.levels.PrisonBossLevel" );
		com.watabou.utils.Bundle.addAlias(
				com.shatteredpixel.yasd.general.actors.mobs.OldTengu.class,
				"com.shatteredpixel.yasd.general.actors.mobs.Tengu" );

		//v0.7.6
		com.watabou.utils.Bundle.addAlias(
				Brute.ArmoredBrute.class,
				"com.shatteredpixel.yasd.general.actors.mobs.Brute.Shielded");
	}
	
	@Override
	public void create() {
		super.create();

		updateSystemUI();
		YASDAction.loadBindings();
		
		Music.INSTANCE.enable( YASDSettings.music() );
		Music.INSTANCE.volume( YASDSettings.musicVol()/10f );
		Sample.INSTANCE.enable( YASDSettings.soundFx() );
		Sample.INSTANCE.volume( YASDSettings.SFXVol()/10f );

		Sample.INSTANCE.load(
				Assets.SND_CLICK,
				Assets.SND_BADGE,
				Assets.SND_GOLD,

				Assets.SND_STEP,
				Assets.SND_WATER,
				Assets.SND_OPEN,
				Assets.SND_UNLOCK,
				Assets.SND_ITEM,
				Assets.SND_DEWDROP,
				Assets.SND_HIT,
				Assets.SND_MISS,

				Assets.SND_DESCEND,
				Assets.SND_EAT,
				Assets.SND_READ,
				Assets.SND_LULLABY,
				Assets.SND_DRINK,
				Assets.SND_SHATTER,
				Assets.SND_ZAP,
				Assets.SND_LIGHTNING,
				Assets.SND_LEVELUP,
				Assets.SND_DEATH,
				Assets.SND_CHALLENGE,
				Assets.SND_CURSED,
				Assets.SND_EVOKE,
				Assets.SND_TRAP,
				Assets.SND_TOMB,
				Assets.SND_ALERT,
				Assets.SND_MELD,
				Assets.SND_BOSS,
				Assets.SND_BLAST,
				Assets.SND_PLANT,
				Assets.SND_RAY,
				Assets.SND_BEACON,
				Assets.SND_TELEPORT,
				Assets.SND_CHARMS,
				Assets.SND_MASTERY,
				Assets.SND_PUFF,
				Assets.SND_ROCKS,
				Assets.SND_BURNING,
				Assets.SND_FALLING,
				Assets.SND_GHOST,
				Assets.SND_SECRET,
				Assets.SND_BONES,
				Assets.SND_BEE,
				Assets.SND_DEGRADE,
				Assets.SND_MIMIC );

		
	}

	@Override
	public void destroy(){
		super.destroy();
		GameScene.endActorThread();
	}

	public static void switchNoFade(Class<? extends PixelScene> c){
		switchNoFade(c, null);
	}

	public static void switchNoFade(Class<? extends PixelScene> c, SceneChangeCallback callback) {
		PixelScene.noFade = true;
		switchScene( c, callback );
	}
	
	public static void seamlessResetScene(SceneChangeCallback callback) {
		if (scene() instanceof PixelScene){
			((PixelScene) scene()).saveWindows();
			switchNoFade((Class<? extends PixelScene>) sceneClass, callback );
		} else {
			resetScene();
		}
	}
	
	public static void seamlessResetScene(){
		seamlessResetScene(null);
	}
	
	@Override
	protected void switchScene() {
		super.switchScene();
		if (scene instanceof PixelScene){
			((PixelScene) scene).restoreWindows();
		}
	}
	
	@Override
	public void resize( int width, int height ) {
		if (width == 0 || height == 0){
			return;
		}

		if (scene instanceof PixelScene &&
				(height != Game.height || width != Game.width)) {
			PixelScene.noFade = true;
			((PixelScene) scene).saveWindows();
		}

		super.resize( width, height );

		updateDisplaySize();

	}

	public void updateDisplaySize(){
		platform.updateDisplaySize();
	}

	public static void updateSystemUI() {
		platform.updateSystemUI();
	}
}
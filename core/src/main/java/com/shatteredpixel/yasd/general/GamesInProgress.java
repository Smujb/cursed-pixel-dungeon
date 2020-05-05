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

import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.hero.HeroSubClass;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class GamesInProgress {
	
	public static final int MAX_SLOTS = 4;
	
	//null means we have loaded info and it is empty, no entry means unknown.
	private static HashMap<Integer, Info> slotStates = new HashMap<>();
	public static int curSlot;
	
	public static HeroClass selectedClass;

	private static String gameFolder() {
		return DATA_FOLDER;
	}

	private static final String DATA_FOLDER = "data/";
	private static final String SLOT_FOLDER = "slot%d";
	private static final String XPOS = "xPos%d";
	private static final String YPOS = "depth%d";
	private static final String ZPOS = "zPos%d.dat";
	private static final String GAME_FILE	= "game.dat";
	
	public static boolean gameExists( int slot ){
		return FileUtils.dirExists(gameFolder() + Messages.format(SLOT_FOLDER, slot));
	}
	
	public static String slotFolder(int slot ){
		return gameFolder() + Messages.format(SLOT_FOLDER, slot);
	}

	/*public static String xFolder(int x ){
		return Messages.format(XPOS, x);
	}

	public static String yFolder(int y ){
		return Messages.format(YPOS, y);
	}*/
	

	public static String gameFile(int slot ){
		return slotFolder(slot) + "/" + GAME_FILE;
	}

	/*public static String depthFile( int slot, int x, int y, int z ) {
		return slotFolder(slot) + "/" + xFolder(x) + "/" + yFolder(y) + "/"  + Messages.format(ZPOS, z);
	}*/
	
	public static int firstEmpty(){
		for (int i = 1; i <= MAX_SLOTS; i++){
			if (check(i) == null) return i;
		}
		return -1;
	}
	
	public static ArrayList<Info> checkAll(){
		ArrayList<Info> result = new ArrayList<>();
		for (int i = 0; i <= MAX_SLOTS; i++){
			Info curr = check(i);
			if (curr != null) result.add(curr);
		}
		Collections.sort(result, scoreComparator);
		return result;
	}
	
	public static Info check( int slot ) {
		
		/*if (slotStates.containsKey( slot )) {
			
			return slotStates.get( slot );
			
		} else*/ if (!gameExists( slot )) {
			
			slotStates.put(slot, null);
			return null;
			
		} else {
			
			Info info;
			try {
				
				Bundle bundle = FileUtils.bundleFromFile(gameFile(slot));
				info = new Info();
				info.slot = slot;
				Dungeon.preview(info, bundle);
				
				//saves from before 0.6.5c are not supported
				if (info.version < MainGame.v0_6_5c) {
					info = null;
				}

			} catch (IOException e) {
				info = null;
			} catch (Exception e){
				MainGame.reportException( e );
				info = null;
			}
			
			slotStates.put( slot, info );
			return info;
			
		}
	}

	public static void set(int slot, int depth, int challenges,
	                       Hero hero) {
		Info info = new Info();
		info.slot = slot;
		info.depth = depth;

		info.challenges = challenges;
		
		info.level = hero.lvl;
		info.str = hero.STR;
		info.exp = hero.exp;
		info.hp = hero.HP;
		info.ht = hero.HT;
		info.shld = hero.shielding();
		info.heroClass = hero.heroClass;
		info.subClass = hero.subClass;
		info.armorTier = hero.tier();
		
		info.goldCollected = Statistics.goldCollected;
		info.maxDepth = Statistics.deepestFloor;
		
		slotStates.put( slot, info );
	}
	
	public static void setUnknown( int slot ) {
		slotStates.remove( slot );
	}
	
	public static void delete( int slot ) {
		slotStates.put( slot, null );
	}
	
	public static class Info {
		public int slot;
		
		public int depth;
		public int path;
		public int version;
		public int challenges;
		
		public int level;
		public int str;
		public int exp;
		public int hp;
		public int ht;
		public int shld;
		public HeroClass heroClass;
		public HeroSubClass subClass;
		public int armorTier;
		
		public int goldCollected;
		public int maxDepth;
	}
	
	public static final Comparator<GamesInProgress.Info> scoreComparator = new Comparator<GamesInProgress.Info>() {
		@Override
		public int compare(GamesInProgress.Info lhs, GamesInProgress.Info rhs ) {
			int lScore = (lhs.level * lhs.maxDepth * 100) + lhs.goldCollected;
			int rScore = (rhs.level * rhs.maxDepth * 100) + rhs.goldCollected;
			return (int)Math.signum( rScore - lScore );
		}
	};
}

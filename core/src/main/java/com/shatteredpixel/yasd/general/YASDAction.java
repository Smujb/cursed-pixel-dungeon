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

import com.badlogic.gdx.Input;
import com.watabou.input.GameAction;
import com.watabou.input.KeyBindings;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.LinkedHashMap;

public class YASDAction extends GameAction {

	//--Existing actions from GameAction
	public static final GameAction NONE  = GameAction.NONE;
	public static final GameAction BACK  = GameAction.BACK;
	//--

	protected YASDAction( String name ){
		super( name );
	}

	public static final GameAction HERO_INFO   = new YASDAction("hero_info");
	public static final GameAction JOURNAL     = new YASDAction("journal");

	public static final GameAction WAIT        = new YASDAction("wait");
	public static final GameAction SEARCH      = new YASDAction("search");

	public static final GameAction INVENTORY   = new YASDAction("inventory");
	public static final GameAction QUICKSLOT_1 = new YASDAction("quickslot_1");
	public static final GameAction QUICKSLOT_2 = new YASDAction("quickslot_2");
	public static final GameAction QUICKSLOT_3 = new YASDAction("quickslot_3");
	public static final GameAction QUICKSLOT_4 = new YASDAction("quickslot_4");

	public static final GameAction TAG_ATTACK  = new YASDAction("tag_attack");
	public static final GameAction TAG_DANGER  = new YASDAction("tag_danger");
	public static final GameAction TAG_ACTION  = new YASDAction("tag_action");
	public static final GameAction TAG_LOOT    = new YASDAction("tag_loot");
	public static final GameAction TAG_RESUME  = new YASDAction("tag_resume");

	public static final GameAction ZOOM_IN     = new YASDAction("zoom_in");
	public static final GameAction ZOOM_OUT    = new YASDAction("zoom_out");

	public static final GameAction N           = new YASDAction("n");
	public static final GameAction E           = new YASDAction("e");
	public static final GameAction S           = new YASDAction("s");
	public static final GameAction W           = new YASDAction("w");
	public static final GameAction NE          = new YASDAction("ne");
	public static final GameAction SE          = new YASDAction("se");
	public static final GameAction SW          = new YASDAction("sw");
	public static final GameAction NW          = new YASDAction("nw");

	private static final LinkedHashMap<Integer, GameAction> defaultBindings = new LinkedHashMap<>();
	static {
		defaultBindings.put( Input.Keys.ESCAPE,      YASDAction.BACK );
		defaultBindings.put( Input.Keys.BACKSPACE,   YASDAction.BACK );

		defaultBindings.put( Input.Keys.H,           YASDAction.HERO_INFO );
		defaultBindings.put( Input.Keys.J,           YASDAction.JOURNAL );

		defaultBindings.put( Input.Keys.SPACE,       YASDAction.WAIT );
		defaultBindings.put( Input.Keys.S,           YASDAction.SEARCH );

		defaultBindings.put( Input.Keys.I,           YASDAction.INVENTORY );
		defaultBindings.put( Input.Keys.Q,           YASDAction.QUICKSLOT_1 );
		defaultBindings.put( Input.Keys.W,           YASDAction.QUICKSLOT_2 );
		defaultBindings.put( Input.Keys.E,           YASDAction.QUICKSLOT_3 );
		defaultBindings.put( Input.Keys.R,           YASDAction.QUICKSLOT_4 );

		defaultBindings.put( Input.Keys.A,           YASDAction.TAG_ATTACK );
		defaultBindings.put( Input.Keys.TAB,         YASDAction.TAG_DANGER );
		defaultBindings.put( Input.Keys.D,           YASDAction.TAG_ACTION );
		defaultBindings.put( Input.Keys.ENTER,       YASDAction.TAG_LOOT );
		defaultBindings.put( Input.Keys.T,           YASDAction.TAG_RESUME );

		defaultBindings.put( Input.Keys.PLUS,        YASDAction.ZOOM_IN );
		defaultBindings.put( Input.Keys.EQUALS,      YASDAction.ZOOM_IN );
		defaultBindings.put( Input.Keys.MINUS,       YASDAction.ZOOM_OUT );

		defaultBindings.put( Input.Keys.UP,          YASDAction.N );
		defaultBindings.put( Input.Keys.RIGHT,       YASDAction.E );
		defaultBindings.put( Input.Keys.DOWN,        YASDAction.S );
		defaultBindings.put( Input.Keys.LEFT,        YASDAction.W );

		defaultBindings.put( Input.Keys.NUMPAD_5,    YASDAction.WAIT );
		defaultBindings.put( Input.Keys.NUMPAD_8,    YASDAction.N );
		defaultBindings.put( Input.Keys.NUMPAD_9,    YASDAction.NE );
		defaultBindings.put( Input.Keys.NUMPAD_6,    YASDAction.E );
		defaultBindings.put( Input.Keys.NUMPAD_3,    YASDAction.SE );
		defaultBindings.put( Input.Keys.NUMPAD_2,    YASDAction.S );
		defaultBindings.put( Input.Keys.NUMPAD_1,    YASDAction.SW );
		defaultBindings.put( Input.Keys.NUMPAD_4,    YASDAction.W );
		defaultBindings.put( Input.Keys.NUMPAD_7,    YASDAction.NW );
	}


	@Contract(" -> new")
	public static LinkedHashMap<Integer, GameAction> getDefaults() {
		return new LinkedHashMap<>(defaultBindings);
	}

	//hard bindings for android devices
	static {
		KeyBindings.addHardBinding( Input.Keys.BACK, YASDAction.BACK );
		KeyBindings.addHardBinding( Input.Keys.MENU, YASDAction.INVENTORY );
	}

	//we only save/loads keys which differ from the default configuration.
	private static final String BINDINGS_FILE = "keybinds.dat";

	public static void loadBindings(){
		try {
			Bundle b = FileUtils.bundleFromFile(BINDINGS_FILE);

			Bundle firstKeys = b.getBundle("first_keys");
			Bundle secondKeys = b.getBundle("second_keys");

			LinkedHashMap<Integer, GameAction> defaults = getDefaults();
			LinkedHashMap<Integer, GameAction> custom = new LinkedHashMap<>();

			for (GameAction a : allActions()) {
				if (firstKeys.contains(a.name())) {
					if (firstKeys.getInt(a.name()) == 0){
						for (int i : defaults.keySet()){
							if (defaults.get(i) == a){
								defaults.remove(i);
								break;
							}
						}
					} else {
						custom.put(firstKeys.getInt(a.name()), a);
						defaults.remove(firstKeys.getInt(a.name()));
					}
				}

				//we store any custom second keys in defaults for the moment to preserve order
				//incase the 2nd key is custom but the first one isn't
				if (secondKeys.contains(a.name())) {
					if (secondKeys.getInt(a.name()) == 0){
						int last = 0;
						for (int i : defaults.keySet()){
							if (defaults.get(i) == a){
								last = i;
							}
						}
						defaults.remove(last);
					} else {
						defaults.remove(secondKeys.getInt(a.name()));
						defaults.put(secondKeys.getInt(a.name()), a);
					}
				}

			}

			//now merge them and store
			for( int i : defaults.keySet()){
				if (i != 0) {
					custom.put(i, defaults.get(i));
				}
			}

			KeyBindings.setAllBindings(custom);

		} catch (Exception e){
			KeyBindings.setAllBindings(getDefaults());
		}
	}

	public static void saveBindings(){
		Bundle b = new Bundle();

		Bundle firstKeys = new Bundle();
		Bundle secondKeys = new Bundle();

		for (GameAction a : allActions()){
			int firstCur = 0;
			int secondCur = 0;
			int firstDef = 0;
			int secondDef = 0;

			for (int i : defaultBindings.keySet()){
				if (defaultBindings.get(i) == a){
					if(firstDef == 0){
						firstDef = i;
					} else {
						secondDef = i;
					}
				}
			}

			LinkedHashMap<Integer, GameAction> curBindings = KeyBindings.getAllBindings();
			for (int i : curBindings.keySet()){
				if (curBindings.get(i) == a){
					if(firstCur == 0){
						firstCur = i;
					} else {
						secondCur = i;
					}
				}
			}

			if (firstCur != firstDef){
				firstKeys.put(a.name(), firstCur);
			}
			if (secondCur != secondDef){
				secondKeys.put(a.name(), secondCur);
			}

		}

		b.put("first_keys", firstKeys);
		b.put("second_keys", secondKeys);

		try {
			FileUtils.bundleToFile(BINDINGS_FILE, b);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

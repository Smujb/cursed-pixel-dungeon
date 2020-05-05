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

package com.shatteredpixel.yasd.general.items.keys;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.journal.Notes;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.windows.WndJournal;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import org.jetbrains.annotations.NotNull;

public abstract class Key extends Item {

	public static final float TIME_TO_UNLOCK = 1f;
	
	{
		stackable = true;
		unique = true;
	}

	public String levelKey;

	//public int depth;
	//public int xPos;
	//public int zPos;
	
	@Override
	public boolean isSimilar( Item item ) {
		return super.isSimilar(item) && ((Key) item).levelKey.equals(levelKey);
	}

	@Override
	public boolean doPickUp(Hero hero) {
		GameScene.pickUpJournal(this, hero.pos);
		WndJournal.last_index = 2;
		Notes.add(this);
		Sample.INSTANCE.play( Assets.SND_ITEM );
		hero.spendAndNext( TIME_TO_PICK_UP );
		GameScene.updateKeyDisplay();
		return true;
	}

	//private static final String YPOS = "depth";
	//private static final String XPOS = "xPos";
	//private static final String ZPOS = "zPos";
	private static final String KEY = "key";
	
	@Override
	public void storeInBundle(  Bundle bundle ) {
		super.storeInBundle( bundle );
		//bundle.put( YPOS, depth );
		//bundle.put( XPOS, xPos );
		//bundle.put( ZPOS, zPos );
		bundle.put(KEY, levelKey);
	}
	
	@Override
	public void restoreFromBundle(  Bundle bundle ) {
		super.restoreFromBundle( bundle );
		//depth = bundle.getInt( YPOS );
		//xPos = bundle.getInt( XPOS );
		//zPos = bundle.getInt( ZPOS );
		levelKey = bundle.getString(KEY);
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}

}

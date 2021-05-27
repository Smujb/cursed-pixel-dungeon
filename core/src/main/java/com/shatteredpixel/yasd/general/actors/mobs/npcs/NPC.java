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

package com.shatteredpixel.yasd.general.actors.mobs.npcs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.Heap;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class NPC extends Mob {

	{
		HP = HT = 1;

		alignment = Alignment.NEUTRAL;
		state = PASSIVE;
	}

	private static final String QUEST_FLAGS = "quest_flags";

	private ArrayList<String> questlineFlags = new ArrayList<>();

	public void addQuestFlag(String flag) {
		questlineFlags.add(flag);
	}

	public boolean questlineFlagCompleted(String flag) {
		return questlineFlags.contains(flag);
	}

	protected void throwItem() {
		Heap heap = Dungeon.level.heaps.get( pos );
		if (heap != null && heap.type == Heap.Type.HEAP) {
			int n;
			do {
				n = pos + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
			} while (!Dungeon.level.passable(n) && !Dungeon.level.avoid(n));
			Dungeon.level.drop( heap.pickUp(), n ).sprite.drop( pos );
		}
	}

	@Override
	public void beckon( int cell ) {
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(QUEST_FLAGS, questlineFlags.toArray(new String[0]));
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(QUEST_FLAGS)) questlineFlags = new ArrayList<>(Arrays.asList(bundle.getStringArray(QUEST_FLAGS)));
	}
}
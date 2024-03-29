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

import com.shatteredpixel.yasd.general.items.CrimsonFlask;
import com.shatteredpixel.yasd.general.items.Dewdrop;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.yasd.general.items.artifacts.HornOfPlenty;
import com.shatteredpixel.yasd.general.items.food.Food;
import com.shatteredpixel.yasd.general.items.food.SmallRation;

public class Challenges {

	//Some of these internal IDs are outdated and don't represent what these challenges do
	public static final int NO_FOOD				= 1;
	public static final int NO_HEALING			= 2;
	public static final int NO_HERBALISM		= 4;
	public static final int SWARM_INTELLIGENCE	= 8;
	public static final int DARKNESS			= 16;
	public static final int COLLAPSING_FLOOR    = 32;
	public static final int CORROSION			= 64;
	public static final int BLOODLUST			= 128;

	public static final int MAX_VALUE           = 255;

	public static final String[] NAME_IDS = {
			"no_food",
			"no_healing",
			"no_herbalism",
			"swarm_intelligence",
			"darkness",
			"collapsing_floor",
			"corrosion",
			"bloodlust"
	};

	public static final int[] MASKS = {
			NO_FOOD, NO_HEALING, NO_HERBALISM, SWARM_INTELLIGENCE, DARKNESS, COLLAPSING_FLOOR, CORROSION, BLOODLUST
	};

	public static boolean isItemBlocked( Item item ){
		if (Dungeon.isChallenged(NO_FOOD)){
			if (item instanceof Food && !(item instanceof SmallRation)) {
				return true;
			} else if (item instanceof HornOfPlenty){
				return true;
			}
		}

		if (Dungeon.isChallenged(NO_HEALING)){
			if (item instanceof CrimsonFlask.Charge){
				return true;
			}
		}

		if (Dungeon.isChallenged(NO_HERBALISM)){
			if (item instanceof Dewdrop) {
				return true;
			}
		}

		if (Dungeon.isChallenged(BLOODLUST)) {
			return item instanceof ChaliceOfBlood;
		}

		return false;

	}

}
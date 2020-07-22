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

package com.shatteredpixel.yasd.general.items.rings;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

import java.text.DecimalFormat;

public class RingOfTenacity extends Ring {

	{
		icon = ItemSpriteSheet.Icons.RING_TENACITY;
	}
	
	public String statsInfo() {
		if (isIdentified()){
			return Messages.get(this, "stats", new DecimalFormat("#.##").format(100f * (0.5f * soloMultiplier())));
		} else {
			return Messages.get(this, "typical_stats", new DecimalFormat("#.##").format(100f * (0.5f * multiplier(0))));
		}
	}

	@Override
	protected RingBuff buff( ) {
		return new Tenacity();
	}
	
	public static float damageMultiplier( Char t ){
		//(HT - HP)/HT = heroes current % missing health.
		return 1f - ((0.5f * multiplier(t, Tenacity.class))*((float)(t.HT - t.HP)/t.HT));
	}

	public class Tenacity extends RingBuff {
	}
}


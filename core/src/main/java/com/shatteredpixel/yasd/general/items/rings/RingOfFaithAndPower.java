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
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

import java.text.DecimalFormat;

public class RingOfFaithAndPower extends Ring {

	{
		icon = ItemSpriteSheet.Icons.RING_SHARPSHOOT;
	}
	
	public String statsInfo() {
		if (isIdentified()){
			return Messages.get(this, "stats", (int) (10 * soloMultiplier()), new DecimalFormat("#.##").format(100f * (5f * soloMultiplier())));
		} else {
			return Messages.get(this, "typical_stats", (int) (10 * multiplier(0)), new DecimalFormat("#.##").format(100f * (5f * multiplier(0))));
		}
	}
	
	@Override
	protected RingBuff buff( ) {
		return new FaithAndPower();
	}
	
	public static float hpModifier( Char target ){
		return 1 + 0.2f * multiplier(target, FaithAndPower.class);
	}
	
	public static float mpModifier( Char target ){
		return 1 + 0.3f * multiplier(target, FaithAndPower.class);
	}

	@Override
	public boolean doEquip(Hero hero) {
		if (super.doEquip(hero)){
			hero.updateHT( false );
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean doUnequip(Char hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){
			hero.updateHT( false );
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Item upgrade() {
		super.upgrade();
		updateTargetHT();
		return this;
	}

	@Override
	public Item level(int value) {
		super.level(value);
		updateTargetHT();
		return this;
	}

	private void updateTargetHT(){
		if (buff != null && buff.target != null) {
			buff.target.updateHT( false );
		}
	}

	public class FaithAndPower extends RingBuff {
	}
}

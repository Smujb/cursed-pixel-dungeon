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

package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;

public class Healing extends Buff {

	private int healingLeft;
	
	private float percentHealPerTick;
	private int flatHealPerTick;

	private float storedHealing = 0;
	
	{
		//unlike other buffs, this one acts after the hero and takes priority against other effects
		//healing is much more useful if you get some of it off before taking damage
		actPriority = HERO_PRIO - 1;
		
		type = buffType.POSITIVE;
	}
	
	@Override
	public boolean act() {

		storedHealing += healingThisTick();
		int toHeal = (int) storedHealing;
		if (toHeal > 0) {
			target.heal(toHeal, false, true);
			storedHealing -= toHeal;
			healingLeft -= toHeal;
		}
		
		if (healingLeft <= 0){
			detach();
		}
		
		spend( TICK );
		
		return true;
	}
	
	private float healingThisTick(){
		return GameMath.gate(0.05f, healingLeft * percentHealPerTick + flatHealPerTick,
				healingLeft);
	}
	
	public void setHeal(int amount, float percentPerTick, int flatPerTick){
		healingLeft = amount;
		percentHealPerTick = percentPerTick;
		flatHealPerTick = flatPerTick;
	}
	
	public void increaseHeal( int amount ){
		healingLeft += amount;
	}
	
	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add( CharSprite.State.HEALING );
		else    target.sprite.remove( CharSprite.State.HEALING );
	}
	
	private static final String LEFT = "left";
	private static final String PERCENT = "percent";
	private static final String FLAT = "flat";
	private static final String STORED = "stored";
	
	@Override
	public void storeInBundle( Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(LEFT, healingLeft);
		bundle.put(PERCENT, percentHealPerTick);
		bundle.put(FLAT, flatHealPerTick);
		bundle.put(STORED, storedHealing);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle) {
		super.restoreFromBundle(bundle);
		healingLeft = bundle.getInt(LEFT);
		percentHealPerTick = bundle.getFloat(PERCENT);
		flatHealPerTick = bundle.getInt(FLAT);
		storedHealing = bundle.getFloat(STORED);
	}
	
	@Override
	public int icon() {
		return BuffIndicator.HEALING;
	}
	
	@Override
	public String toString() {
		return Messages.get(this, "name");
	}
	
	@Override
	public String desc() {
		return Messages.get(this, "desc", Math.round(healingThisTick()), healingLeft);
	}
}

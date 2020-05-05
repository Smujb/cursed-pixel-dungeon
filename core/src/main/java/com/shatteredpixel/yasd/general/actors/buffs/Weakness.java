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

//TODO need to re-evaluate various cases cases where this was used
// now that warlocks give a different debuff and shamen have 3 different ones
package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;

public class Weakness extends FlavourBuff {

	public static final float DURATION = 40f;

	{
		type = buffType.NEGATIVE;
		announced = true;
	}

	public float damageFactor() {
		return (float) Math.pow( 0.98, cooldown());
	}

	@Override
	public int icon() {
		return BuffIndicator.WEAKNESS;
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add( CharSprite.State.WEAKENED );
		else if (target.invisible == 0) target.sprite.remove( CharSprite.State.WEAKENED );
	}
	
	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String heroMessage() {
		return Messages.get(this, "heromsg");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", damageFactor(), dispTurns());
	}
}

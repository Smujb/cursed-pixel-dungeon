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

package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Ooze extends Buff {

	{
		type = buffType.NEGATIVE;
		announced = true;

		element = Element.ACID;
	}
	
	private float left;
	private static final String LEFT	= "left";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEFT, left );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		//pre-0.7.0
		if (bundle.contains( LEFT )) {
			left = bundle.getFloat(LEFT);
		} else {
			left = 20;
		}
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add( CharSprite.State.OOZE );
		else if (target.invisible == 0) target.sprite.remove( CharSprite.State.OOZE );
	}
	
	@Override
	public int icon() {
		return BuffIndicator.OOZE;
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
		return Messages.get(this, "desc", dispTurns(left));
	}
	
	public void set(float left){
		this.left = left;
	}

	@Override
	public boolean act() {
		if (target.isAlive()) {
			if (Dungeon.getScaleFactor() > 4)
				target.damage( Dungeon.getScaleFactor()/Constants.CHAPTER_LENGTH, this);
			else if (Random.Int(2) == 0)
				target.damage( 1, this );
			if (!target.isAlive() && target == Dungeon.hero) {
				Dungeon.fail( getClass() );
				GLog.n( Messages.get(this, "ondeath") );
			}
			spend( TICK );
			left -= TICK;
			if (left <= 0){
				detach();
			}
		} else {
			detach();
		}
		if (Dungeon.level.liquid(target.pos)) {
			detach();
		}
		return true;
	}
}

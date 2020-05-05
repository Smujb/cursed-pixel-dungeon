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

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Ghost;
import com.shatteredpixel.yasd.general.items.food.MysteryMeat;
import com.shatteredpixel.yasd.general.items.wands.Wand;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.GreatCrabSprite;
import com.shatteredpixel.yasd.general.utils.GLog;

import org.jetbrains.annotations.NotNull;

public class GreatCrab extends Crab {

	{
		spriteClass = GreatCrabSprite.class;

		//HP = HT = 25;
		//defenseSkill = 0; //see damage()
		baseSpeed = 1f;

		healthFactor = 1.5f;
		damageFactor = 1.2f;

		EXP = 6;

		state = WANDERING;

		properties.add(Property.MINIBOSS);
	}

	private int moving = 0;

	@Override
	protected boolean getCloser( int target ) {
		//this is used so that the crab remains slower, but still detects the player at the expected rate.
		moving++;
		if (moving < 3) {
			return super.getCloser( target );
		} else {
			moving = 0;
			return true;
		}

	}

	@Override
	public void damage(int dmg,  DamageSrc src){
		//crab blocks all attacks originating from its current enemy if it sees them.
		//All direct damage is negated, no exceptions. environmental effects go through as normal.
		if ((enemySeen && state != SLEEPING && paralysed == 0)
				&& ((src.getCause() instanceof Wand && enemy == Dungeon.hero)
				|| (src.getCause() instanceof Char && enemy == src.getCause()))){
			GLog.n( Messages.get(this, "noticed") );
			sprite.showStatus( CharSprite.NEUTRAL, Messages.get(this, "blocked") );
		} else {
			super.damage( dmg, src);
		}
	}

	@Override
	public void die( DamageSrc cause ) {
		super.die( cause );

		Ghost.Quest.process();

		Dungeon.level.drop( new  MysteryMeat(), pos );
		Dungeon.level.drop( new  MysteryMeat(), pos ).sprite.drop();
	}
}

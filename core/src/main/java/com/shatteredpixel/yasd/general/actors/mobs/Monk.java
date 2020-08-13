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

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Focus;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Imp;
import com.shatteredpixel.yasd.general.items.food.Food;
import com.shatteredpixel.yasd.general.items.food.Pasty;
import com.shatteredpixel.yasd.general.sprites.MonkSprite;
import com.shatteredpixel.yasd.general.sprites.SeniorSprite;

public class Monk extends Mob {

	{
		spriteClass = MonkSprite.class;


		healthFactor = 0.8f;
		damageFactor = 0.7f;
		attackDelay = 0.5f;

		EXP = 11;

        loot = new Food();
		lootChance = 0.083f;

		properties.add(Property.UNDEAD);
	}

	@Override
	public void rollToDropLoot() {
		Imp.Quest.process(this);

		super.rollToDropLoot();
	}

	protected boolean act() {
		boolean result = super.act();
		if (buff(Focus.class) == null && state == HUNTING/* && focusCooldown <= 0*/) {
			Buff.affect(this, Focus.class);
		}
		return result;
	}

	@Override
	public void move(int step) {
		// moving reduces cooldown by an additional 0.67, giving a total reduction of 1.67f.
		// basically monks will become focused notably faster if you kite them.
		Buff.affect(this, Focus.class).loseCooldown(0.67f);
		super.move(step);
	}

	public static class Senior extends Monk {

		{
			spriteClass = SeniorSprite.class;
			damageFactor = 0.8f;

			loot = new Pasty();
			lootChance = 0.2f;
		}

		@Override
		public void move( int step ) {
			// on top of the existing move bonus, senior monks get a further 1.66 cooldown reduction
			// for a total of 3.33, double the normal 1.67 for regular monks
			Buff.affect(this, Focus.class).loseCooldown(1.33f);
			super.move(step);
		}

	}
}

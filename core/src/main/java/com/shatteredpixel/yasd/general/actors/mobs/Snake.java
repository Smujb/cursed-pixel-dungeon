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

import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.SnakeSprite;
import com.shatteredpixel.yasd.general.utils.GLog;

public class Snake extends Mob {
	
	{
		spriteClass = SnakeSprite.class;

		healthFactor = 0.4f;
		numTypes = 2;
		evasionFactor = 2f;
		
		EXP = 2;
		
		loot = Generator.random(Generator.Category.SEED);
		lootChance = 0.25f;
	}

	@Override
	public Element elementalType() {
		switch (type) {
			case 0: default:
				return Element.PHYSICAL;
			case 1:
				return Element.TOXIC;
		}
	}

	private static int dodges = 0;

	@Override
	public String defenseVerb() {
		dodges++;
		if (dodges >= 5 && !Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_1)){
			GLog.w(Messages.get(this, "hint"));
			dodges = 0;
		}
		return super.defenseVerb();
	}
}

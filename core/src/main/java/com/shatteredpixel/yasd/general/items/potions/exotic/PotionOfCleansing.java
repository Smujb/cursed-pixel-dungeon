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

package com.shatteredpixel.yasd.general.items.potions.exotic;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Corruption;
import com.shatteredpixel.yasd.general.actors.buffs.Hunger;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.watabou.noosa.audio.Sample;

public class PotionOfCleansing extends ExoticPotion {
	
	{
		initials = 9;
	}
	
	@Override
	public void apply( Hero hero ) {
		setKnown();
		
		cleanse( hero );
	}
	
	@Override
	public void shatter(int cell) {
		if (Actor.findChar(cell) == null){
			super.shatter(cell);
		} else {
			if (Dungeon.level.heroFOV[cell]) {
				Sample.INSTANCE.play(Assets.SND_SHATTER);
				splash(cell);
				setKnown();
			}
			
			if (Actor.findChar(cell) != null){
				cleanse(Actor.findChar(cell));
			}
		}
	}
	
	public static void cleanse(Char ch){
		for (Buff b : ch.buffs()){
			if (b.type == Buff.buffType.NEGATIVE && !(b instanceof Corruption)){
				b.detach();
			}
			if (b instanceof Hunger){
				((Hunger) b).satisfy(Hunger.STARVING);
			}
		}
	}
}

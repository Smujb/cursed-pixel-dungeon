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

package com.shatteredpixel.yasd.general.items.food;

import com.shatteredpixel.yasd.general.actors.buffs.Barkskin;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Hunger;
import com.shatteredpixel.yasd.general.actors.buffs.Invisibility;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.CrimsonFlask;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Random;

public class FrozenCarpaccio extends Food {

	{
		image = ItemSpriteSheet.CARPACCIO;
		energy = Hunger.HUNGRY/2f;
	}
	
	@Override
	protected void satisfy(Hero hero) {
		super.satisfy(hero);
		effect(hero);
	}
	
	public int price() {
		return 10 * quantity;
	}

	public static void effect(Hero hero){
		switch (Random.Int( 5 )) {
			case 0:
				GLog.info( Messages.get(FrozenCarpaccio.class, "invis") );
				Buff.affect( hero, Invisibility.class, Invisibility.DURATION );
				break;
			case 1:
				GLog.info( Messages.get(FrozenCarpaccio.class, "hard") );
				Buff.affect( hero, Barkskin.class ).set( hero.lvl, 1 );
				break;
			case 2: default:
				GLog.info( Messages.get(FrozenCarpaccio.class, "refresh") );
				CrimsonFlask.cure(hero);
				break;
			case 3:
				GLog.info( Messages.get(FrozenCarpaccio.class, "better") );
				hero.heal(hero.HT/4);
				break;
		}
	}
	
	public static Food cook( MysteryMeat ingredient ) {
		FrozenCarpaccio result = new FrozenCarpaccio();
		result.quantity = ingredient.quantity();
		return result;
	}
}

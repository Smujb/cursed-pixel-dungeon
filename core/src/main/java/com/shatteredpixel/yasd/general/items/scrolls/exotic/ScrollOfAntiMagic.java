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

package com.shatteredpixel.yasd.general.items.scrolls.exotic;

import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Invisibility;
import com.shatteredpixel.yasd.general.actors.buffs.MagicImmune;
import com.shatteredpixel.yasd.general.effects.Flare;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class ScrollOfAntiMagic extends ExoticScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_ANTIMAGIC;

		mpCost = 9;
	}
	
	@Override
	public void doRead() {
		
		Invisibility.dispel();

		Buff.affect( curUser, MagicImmune.class, MagicImmune.DURATION );
		new Flare( 5, 32 ).color( Constants.Colours.PURE_RED, true ).show( curUser.sprite, 2f );
		
		setKnown();
		
		readAnimation();
	}
}

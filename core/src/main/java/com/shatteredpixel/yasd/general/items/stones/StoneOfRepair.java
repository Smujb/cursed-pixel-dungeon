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

package com.shatteredpixel.yasd.general.items.stones;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.windows.WndBag;
import com.watabou.noosa.audio.Sample;

public class StoneOfRepair extends InventoryStone {

	{
		mode = WndBag.Mode.REPAIRABLE;
		image = ItemSpriteSheet.STONE_REPAIR;
	}

	private final float TIME_TO_REPAIR = 3f;

	@Override
	protected void onItemSelected(Item item) {
		item.fullyRepair();
		curUser.sprite.centerEmitter().start( Speck.factory( Speck.REPAIR ), 0.05f, 10 );
		curUser.spend( TIME_TO_REPAIR );
		curUser.busy();
		curUser.sprite.operate( curUser.pos );
		Sample.INSTANCE.play( Assets.SND_EVOKE );
	}

	@Override
	public int price() {
		return 30 * quantity;
	}
}

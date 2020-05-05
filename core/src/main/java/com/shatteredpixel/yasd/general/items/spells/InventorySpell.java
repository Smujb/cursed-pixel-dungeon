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

package com.shatteredpixel.yasd.general.items.spells;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.buffs.Invisibility;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.windows.WndBag;
import com.watabou.noosa.audio.Sample;

public abstract class InventorySpell extends Spell {
	
	protected String inventoryTitle = Messages.get(this, "inv_title");
	protected WndBag.Mode mode = WndBag.Mode.ALL;
	
	@Override
	protected void onCast(Hero hero) {
		//curItem = detach( hero.belongings.backpack );
		GameScene.selectItem( itemSelector, mode, inventoryTitle );
	}
	
	protected abstract void onItemSelected( Item item );
	
	protected WndBag.Listener itemSelector = new WndBag.Listener(this) {
		@Override
		public void onSelect( Item item ) {

			
			if (item != null) {
				
				((InventorySpell)source).onItemSelected( item );
				curUser.spend( 1f );
				curUser.busy();
				(curUser.sprite).operate( curUser.pos );
				
				Sample.INSTANCE.play( Assets.SND_READ );
				Invisibility.dispel();
				
			} else {
				((Item)source).collect( curUser.belongings.backpack, curUser);
			}
		}
	};
}

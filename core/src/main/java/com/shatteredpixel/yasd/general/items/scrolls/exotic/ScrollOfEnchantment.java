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

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.buffs.Invisibility;
import com.shatteredpixel.yasd.general.effects.Enchanting;
import com.shatteredpixel.yasd.general.items.Enchantable;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.stones.StoneOfEnchantment;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndBag;
import com.shatteredpixel.yasd.general.windows.WndOptions;
import com.watabou.noosa.audio.Sample;

public class ScrollOfEnchantment extends ExoticScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_ENCHANT;

		mpCost = 5;
	}
	
	@Override
	public void doRead() {
		setKnown();
		
		GameScene.selectItem( itemSelector, WndBag.Mode.ENCHANTABLE, Messages.get(this, "inv_title"));
	}
	
	protected WndBag.Listener itemSelector = new WndBag.Listener(this) {
		@Override
		public void onSelect(final Item item) {
			
			if (item instanceof Enchantable) {

				final Weapon.Enchantment[] enchants = new Weapon.Enchantment[3];
				
				Class<? extends Weapon.Enchantment> existing = ((Enchantable) item).getEnchantment() != null ? ((Enchantable) item).getEnchantment().getClass() : null;
				enchants[0] = Weapon.Enchantment.randomCommon( existing );
				enchants[1] = Weapon.Enchantment.randomUncommon( existing );
				enchants[2] = Weapon.Enchantment.random( existing, enchants[0].getClass(), enchants[1].getClass());
				
				GameScene.show(new WndOptions(Messages.titleCase(ScrollOfEnchantment.this.name()),
						Messages.get(ScrollOfEnchantment.class, "getWeapons") +
						"\n\n" +
						Messages.get(ScrollOfEnchantment.class, "cancel_warn"),
						enchants[0].name(),
						enchants[1].name(),
						enchants[2].name(),
						Messages.get(ScrollOfEnchantment.class, "cancel")){
					
					@Override
					protected void onSelect(int index) {
						if (index < 3) {
							((Enchantable) item).enchant(enchants[index]);
							GLog.positive(Messages.get(StoneOfEnchantment.class, "getWeapons"));
							((ScrollOfEnchantment)source).readAnimation();
							
							Sample.INSTANCE.play( Assets.Sounds.READ );
							Invisibility.dispel();
							Enchanting.show(curUser, item);
						}
					}
					
					@Override
					public void onBackPressed() {
						//do nothing, reader has to cancel
					}
				});
			
			} else {
				//TODO if this can ever be found un-IDed, need logic for that
				((Item)source).collect();
			}
		}
	};
}

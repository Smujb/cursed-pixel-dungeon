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

package com.shatteredpixel.yasd.general.items.scrolls.exotic;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.buffs.Invisibility;
import com.shatteredpixel.yasd.general.effects.Enchanting;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.armor.Armor;
import com.shatteredpixel.yasd.general.items.stones.StoneOfEnchantment;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndBag;
import com.shatteredpixel.yasd.general.windows.WndOptions;
import com.watabou.noosa.audio.Sample;

public class ScrollOfEnchantment extends ExoticScroll {
	
	{
		initials = 11;
	}
	
	@Override
	public void doRead() {
		setKnown();
		
		GameScene.selectItem( itemSelector, WndBag.Mode.ENCHANTABLE, Messages.get(this, "inv_title"));
	}
	
	protected WndBag.Listener itemSelector = new WndBag.Listener(this) {
		@Override
		public void onSelect(final Item item) {
			
			if (item instanceof Weapon){

				final Weapon.Enchantment[] enchants = new Weapon.Enchantment[3];
				
				Class<? extends Weapon.Enchantment> existing = ((Weapon) item).enchantment != null ? ((Weapon) item).enchantment.getClass() : null;
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
							((Weapon) item).enchant(enchants[index]);
							GLog.p(Messages.get(StoneOfEnchantment.class, "getWeapons"));
							((ScrollOfEnchantment)source).readAnimation();
							
							Sample.INSTANCE.play( Assets.SND_READ );
							Invisibility.dispel();
							Enchanting.show(curUser, item);
						}
					}
					
					@Override
					public void onBackPressed() {
						//do nothing, reader has to cancel
					}
				});
			
			} else if (item instanceof Armor) {

				final Armor.Glyph[] glyphs = new Armor.Glyph[3];
				
				Class<? extends Armor.Glyph> existing = ((Armor) item).glyph != null ? ((Armor) item).glyph.getClass() : null;
				glyphs[0] = Armor.Glyph.randomCommon( existing );
				glyphs[1] = Armor.Glyph.randomUncommon( existing );
				glyphs[2] = Armor.Glyph.random( existing, glyphs[0].getClass(), glyphs[1].getClass());
				
				GameScene.show(new WndOptions(Messages.titleCase(ScrollOfEnchantment.this.name()),
						Messages.get(ScrollOfEnchantment.class, "getArmors") +
						"\n\n" +
						Messages.get(ScrollOfEnchantment.class, "cancel_warn"),
						glyphs[0].name(),
						glyphs[1].name(),
						glyphs[2].name(),
						Messages.get(ScrollOfEnchantment.class, "cancel")){
					
					@Override
					protected void onSelect(int index) {
						if (index < 3) {
							((Armor) item).inscribe(glyphs[index]);
							GLog.p(Messages.get(StoneOfEnchantment.class, "getArmors"));
							((ScrollOfEnchantment)source).readAnimation();
							
							Sample.INSTANCE.play( Assets.SND_READ );
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

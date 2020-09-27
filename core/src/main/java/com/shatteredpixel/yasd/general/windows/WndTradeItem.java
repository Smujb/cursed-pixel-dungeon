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

package com.shatteredpixel.yasd.general.windows;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.yasd.general.items.EquipableItem;
import com.shatteredpixel.yasd.general.items.Gold;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.ui.RedButton;

public class WndTradeItem extends WndInfoItem {

	private static final float GAP		= 2;
	private static final int BTN_HEIGHT	= 16;

	private WndBag owner;

	//selling
	public WndTradeItem( final Item item, WndBag owner ) {

		super(item);

		this.owner = owner;

		float pos = height;

		if (item.quantity() == 1) {

			RedButton btnSell = new RedButton( Messages.get(this, "sell", item.price()) ) {
				@Override
				protected void onClick() {
					sell( item );
					hide();
				}
			};
			btnSell.setRect( 0, pos + GAP, width, BTN_HEIGHT );
			add( btnSell );

			pos = btnSell.bottom();

		} else {

			int priceAll= item.price();
			RedButton btnSell1 = new RedButton( Messages.get(this, "sell_1", priceAll / item.quantity()) ) {
				@Override
				protected void onClick() {
					sellOne( item );
					hide();
				}
			};
			btnSell1.setRect( 0, pos + GAP, width, BTN_HEIGHT );
			add( btnSell1 );
			RedButton btnSellAll = new RedButton( Messages.get(this, "sell_all", priceAll ) ) {
				@Override
				protected void onClick() {
					sell( item );
					hide();
				}
			};
			btnSellAll.setRect( 0, btnSell1.bottom() + 1, width, BTN_HEIGHT );
			add( btnSellAll );

			pos = btnSellAll.bottom();

		}

		resize( width, (int)pos );
	}

	//buying
	public WndTradeItem( final Heap heap ) {

		super(heap);

		Item item = heap.peek();

		float pos = height;

		final int price = price(item);

		RedButton btnBuy = new RedButton(Messages.get(this, "buy", price)) {
			@Override
			protected void onClick() {
				hide();
				buy(heap);
			}
		};
		btnBuy.setRect(0, pos + GAP, width, BTN_HEIGHT);
		btnBuy.enable(price <= Dungeon.gold);
		add(btnBuy);

		//Check for shopkeeper
		Shopkeeper shopkeeper = null;
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (mob instanceof Shopkeeper) {
				shopkeeper = (Shopkeeper) mob;
				break;
			}
		}
		//If there's no shopkeeper, stealing has 100% chance of working.
		boolean steal = true;
		float chance = 1f;
		if (shopkeeper != null) {
			chance = 1f - shopkeeper.noticeChance(Dungeon.hero);
			steal = !shopkeeper.notice(Dungeon.hero);
		}
		boolean finalSteal = steal;
		RedButton btnSteal = new RedButton(Messages.get(this, "steal", Math.min(100, (int) (chance * 100)))) {
			@Override
			protected void onClick() {
				if (finalSteal) {
					Hero hero = Dungeon.hero;
					Item item = heap.pickUp();
					hide();

					if (!item.doPickUp(hero)) {
						Dungeon.level.drop(item, heap.pos).sprite.drop();
					}
				} else {
					for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
						if (mob instanceof Shopkeeper) {
							mob.yell(Messages.get(mob, "thief"));
							((Shopkeeper) mob).flee();
							break;
						}
					}
					hide();
				}
			}
		};
		btnSteal.setRect(0, btnBuy.bottom() + GAP, WIDTH, BTN_HEIGHT);
		add(btnSteal);

		resize(width, (int) btnSteal.bottom());
	}

	@Override
	public void hide() {

		super.hide();

		if (owner != null) {
			owner.hide();
			Shopkeeper.sell();
		}
	}

	private void sell( Item item ) {

		Hero hero = Dungeon.hero;

		if (item.isEquipped( hero ) && !((EquipableItem)item).doUnequip( hero, false )) {
			return;
		}
		item.detachAll( hero.belongings.backpack );

		new Gold( item.price() ).doPickUp( hero );

		//selling items in the sell interface doesn't spend time
		hero.spend(-hero.cooldown());
	}

	private void sellOne( Item item ) {

		if (item.quantity() <= 1) {
			sell( item );
		} else {

			Hero hero = Dungeon.hero;

			item = item.detach( hero.belongings.backpack );

			new Gold( item.price() ).doPickUp( hero );

			//selling items in the sell interface doesn't spend time
			hero.spend(-hero.cooldown());
		}
	}

	private int price( Item item ) {
		return item.price() * 5 * (Dungeon.getScaleFactor() / 6 + 1);
	}

	private void buy( Heap heap ) {

		Item item = heap.pickUp();
		if (item == null) return;

		int price = price( item );
		Dungeon.gold -= price;

		if (!item.doPickUp( Dungeon.hero )) {
			Dungeon.level.drop( item, heap.pos ).sprite.drop();
		}
	}
}

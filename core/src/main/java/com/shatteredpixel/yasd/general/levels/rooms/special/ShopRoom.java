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

package com.shatteredpixel.yasd.general.levels.rooms.special;

import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.hero.Belongings;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.yasd.general.items.Enchantable;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Honeypot;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.MerchantsBeacon;
import com.shatteredpixel.yasd.general.items.Torch;
import com.shatteredpixel.yasd.general.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.general.items.bags.Bag;
import com.shatteredpixel.yasd.general.items.bags.MagicalHolster;
import com.shatteredpixel.yasd.general.items.bags.PotionBandolier;
import com.shatteredpixel.yasd.general.items.bags.ScrollHolder;
import com.shatteredpixel.yasd.general.items.bags.VelvetPouch;
import com.shatteredpixel.yasd.general.items.bombs.Bomb;
import com.shatteredpixel.yasd.general.items.food.SmallRation;
import com.shatteredpixel.yasd.general.items.potions.PotionOfOvergrowth;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.yasd.general.items.stones.StoneOfAugmentation;
import com.shatteredpixel.yasd.general.items.stones.StoneOfEnchantment;
import com.shatteredpixel.yasd.general.items.stones.StoneOfProtection;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopRoom extends SpecialRoom {

	private ArrayList<Item> itemsToSpawn;
	
	@Override
	public int minWidth() {
		return Math.max(7, (int)(Math.sqrt(itemCount())+3));
	}
	
	@Override
	public int minHeight() {
		return Math.max(7, (int)(Math.sqrt(itemCount())+3));
	}

	public int itemCount() {
		if (itemsToSpawn == null) itemsToSpawn = generateItems();
		return itemsToSpawn.size();
	}
	
	public void paint( Level level ) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY_SP );

		placeShopkeeper( level );

		placeItems( level );
		
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

	}

	protected void placeShopkeeper( Level level ) {

		int pos = level.pointToCell(center());

		Mob shopkeeper = Mob.create(Shopkeeper.class, level);
		shopkeeper.pos = pos;
		if (CPDGame.scene() instanceof GameScene) {
			GameScene.add(shopkeeper);
		} else {
			level.mobs.add(shopkeeper);
		}
	}

	protected void placeItems( Level level ){

		if (itemsToSpawn == null)
			itemsToSpawn = generateItems();

		TimekeepersHourglass hourglass = Dungeon.hero.belongings.getItem(TimekeepersHourglass.class);
		if (hourglass != null){
			int bags = 0;
			//creates the given float percent of the remaining bags to be dropped.
			//this way players who get the hourglass late can still max it, usually.
			if (level.key.contains(Dungeon.SEWERS_ID)) {
				bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.20f );
			} else if (level.key.contains(Dungeon.PRISON_ID)) {
				bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.25f );
			} else if (level.key.contains(Dungeon.CAVES_ID)) {
				bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.50f );
			} else if (level.key.contains(Dungeon.CITY_ID)) {
				bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.80f );
			} else if (level.key.contains(Dungeon.HALLS_ID)) {
				bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.80f );
			}

			for(int i = 1; i <= bags; i++){
				itemsToSpawn.add( new TimekeepersHourglass.sandBag());
				hourglass.sandBags ++;
			}
		}

		Point itemPlacement = new Point(entrance());
		if (itemPlacement.y == top){
			itemPlacement.y++;
		} else if (itemPlacement.y == bottom) {
			itemPlacement.y--;
		} else if (itemPlacement.x == left){
			itemPlacement.x++;
		} else {
			itemPlacement.x--;
		}

		for (Item item : itemsToSpawn) {

			if (itemPlacement.x == left+1 && itemPlacement.y != top+1){
				itemPlacement.y--;
			} else if (itemPlacement.y == top+1 && itemPlacement.x != right-1){
				itemPlacement.x++;
			} else if (itemPlacement.x == right-1 && itemPlacement.y != bottom-1){
				itemPlacement.y++;
			} else {
				itemPlacement.x--;
			}

			int cell = level.pointToCell(itemPlacement);

			if (level.heaps.get( cell ) != null) {
				do {
					cell = level.pointToCell(random());
				} while (level.heaps.get( cell ) != null || level.findMob( cell ) != null);
			}

			level.drop( item, cell ).type = Heap.Type.FOR_SALE;
		}

	}

	private ArrayList<Item> generateItems() {

		ArrayList<Item> itemsToSpawn = new ArrayList<>();

		if (Dungeon.depth == 24) {
			itemsToSpawn.add(new Torch());
			itemsToSpawn.add(new Torch());
			itemsToSpawn.add(new Torch());
		}

		for (int a = 0; a < Random.IntRange(3, 6); a++) {
			Item toAdd;
			switch (Random.Int(5)) {
				default: case 0:
					toAdd = Generator.random(Generator.Category.WEAPON);
					break;
				case 1:
					toAdd = Generator.random(Generator.Category.SHIELD);
					break;
				case 2:
					toAdd = Generator.random(Generator.Category.WAND);
					break;
				case 3:
					toAdd = Generator.random(Generator.Category.RANGED);
					break;
				case 4:
					toAdd = Generator.random(Generator.Category.DRAGON_PENDANT);
					break;
			}
			toAdd.level(0);
			toAdd.randomHigh();
			toAdd.identify();
			if (toAdd.cursed()) {
				toAdd.uncurse();
				if (toAdd instanceof Enchantable) {
					((Enchantable) toAdd).enchant();
				}
			}
			itemsToSpawn.add( toAdd );
		}

		itemsToSpawn.add(new StoneOfProtection());

		itemsToSpawn.add( new MerchantsBeacon() );


		itemsToSpawn.add(ChooseBag(Dungeon.hero.belongings));


		itemsToSpawn.add( new PotionOfOvergrowth() );

		itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.POTION ) );
		itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.POTION ) );
		itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.POTION ) );

		itemsToSpawn.add( new ScrollOfIdentify() );
		itemsToSpawn.add( new ScrollOfRemoveCurse() );
		itemsToSpawn.add( new ScrollOfMagicMapping() );
		itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.SCROLL ) );

		for (int i=0; i < 2; i++)
			itemsToSpawn.add( Random.Int(2) == 0 ?
					Generator.randomUsingDefaults( Generator.Category.POTION ) :
					Generator.randomUsingDefaults( Generator.Category.SCROLL ) );


		itemsToSpawn.add( new SmallRation() );
		itemsToSpawn.add( new SmallRation() );
		
		switch (Random.Int(4)){
			case 0:
				itemsToSpawn.add( new Bomb() );
				break;
			case 1:
				itemsToSpawn.add( new StoneOfEnchantment() );
				break;
			case 2:
				itemsToSpawn.add( new Bomb.DoubleBomb() );
				break;
			case 3:
				itemsToSpawn.add( new Honeypot() );
				break;
		}

		//itemsToSpawn.add( new Ankh() );
		itemsToSpawn.add( new StoneOfAugmentation() );

		for (int i = 0; i < 2; i++) {
			Item rare;
			switch (Random.Int(3)) {
				case 1: case 0:
					rare = Generator.random(Generator.Category.RING);
					break;
				case 2: default:
					rare = Generator.random(Generator.Category.ARTIFACT);
					break;
			}
			rare.uncurse();
			rare.cursedKnown = true;
			rare.levelKnown = true;
			rare.randomHigh();
			itemsToSpawn.add(rare);
		}

		//hard limit is 63 items + 1 shopkeeper, as shops can't be bigger than 8x8=64 internally
		if (itemsToSpawn.size() > 63)
			throw new RuntimeException("Shop attempted to carry more than 63 items!");

		Random.shuffle(itemsToSpawn);
		return itemsToSpawn;
	}

	protected static Bag ChooseBag(Belongings pack){


		//generate a hashmap of all valid bags.
		HashMap<Bag, Integer> bags = new HashMap<>();
		if (!Dungeon.LimitedDrops.VELVET_POUCH.dropped()) bags.put(new VelvetPouch(), 1);
		if (!Dungeon.LimitedDrops.SCROLL_HOLDER.dropped()) bags.put(new ScrollHolder(), 0);
		if (!Dungeon.LimitedDrops.POTION_BANDOLIER.dropped()) bags.put(new PotionBandolier(), 0);
		if (!Dungeon.LimitedDrops.MAGICAL_HOLSTER.dropped()) bags.put(new MagicalHolster(), 0);

		if (bags.isEmpty()) return null;

		//count up items in the main bag
		for (Item item : pack.backpack.items) {
			for (Bag bag : bags.keySet()){
				if (bag.canHold(item)){
					bags.put(bag, bags.get(bag)+1);
				}
			}
		}
		
		//drop it, or return nothing if no bag works
		//find which bag will result in most inventory savings, drop that.
		Bag bestBag = null;
		for (Bag bag : bags.keySet()){
			if (bestBag == null){
				bestBag = bag;
			} else if (bags.get(bag) > bags.get(bestBag)){
				bestBag = bag;
			}
		}

		if (bestBag instanceof VelvetPouch){
			Dungeon.LimitedDrops.VELVET_POUCH.drop();
		} else if (bestBag instanceof ScrollHolder){
			Dungeon.LimitedDrops.SCROLL_HOLDER.drop();
		} else if (bestBag instanceof PotionBandolier){
			Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
		} else if (bestBag instanceof MagicalHolster){
			Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();
		}

		return bestBag;
	}

}

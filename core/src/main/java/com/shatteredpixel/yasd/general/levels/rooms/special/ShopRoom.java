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

package com.shatteredpixel.yasd.general.levels.rooms.special;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.actors.hero.Belongings;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Honeypot;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.MerchantsBeacon;
import com.shatteredpixel.yasd.general.items.Torch;
import com.shatteredpixel.yasd.general.items.alcohol.Beer;
import com.shatteredpixel.yasd.general.items.alcohol.Whiskey;
import com.shatteredpixel.yasd.general.items.armor.Armor;
import com.shatteredpixel.yasd.general.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.general.items.bags.Bag;
import com.shatteredpixel.yasd.general.items.bags.MagicalHolster;
import com.shatteredpixel.yasd.general.items.bags.PotionBandolier;
import com.shatteredpixel.yasd.general.items.bags.ScrollHolder;
import com.shatteredpixel.yasd.general.items.bags.VelvetPouch;
import com.shatteredpixel.yasd.general.items.bombs.Bomb;
import com.shatteredpixel.yasd.general.items.food.SmallRation;
import com.shatteredpixel.yasd.general.items.potions.PotionOfHealing;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.yasd.general.items.stones.StoneOfAugmentation;
import com.shatteredpixel.yasd.general.items.stones.StoneOfEnchantment;
import com.shatteredpixel.yasd.general.items.stones.StoneOfRepair;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.items.weapon.missiles.darts.TippedDart;
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
		if (itemsToSpawn == null) itemsToSpawn = generateItems(Dungeon.getScaleFactor());
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
		if (MainGame.scene() instanceof GameScene) {
			GameScene.add(shopkeeper);
		} else {
			level.mobs.add(shopkeeper);
		}
	}

	protected void placeItems( Level level ){

		if (itemsToSpawn == null)
			itemsToSpawn = generateItems(level.getScaleFactor());

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

	private Armor generateArmor(int level) {
		Armor armor;
		int minTier = level/6;
		int maxTier = minTier + 2;
		armor = (Armor) Generator.randomArmor().identify().upgrade();
		armor.cursed = false;
		if (armor.hasCurseGlyph()) {
			armor.inscribe(Armor.Glyph.random());
		}
		armor.setTier(Math.min(5, Random.Int(minTier, maxTier)));
		if (armor.tier < 1) {
			armor.setTier(1);
		}
		if (armor.tier == minTier & armor.isUpgradable()) {//Extra upgrade if possible and Armour is at minimum amount
			armor.upgrade();
		} else if (armor.tier == maxTier & armor.level() > 1) {
			armor.degrade();
		}
		return armor;
	}

	private MeleeWeapon generateWeapon(int level) {
		MeleeWeapon weapon;
		int minTier = level/6;
		int maxTier = minTier + 2;

		weapon = (MeleeWeapon) Generator.random(Generator.Category.WEAPON).identify().upgrade();
		weapon.cursed = false;
		if (weapon.hasCurseEnchant()) {
			weapon.enchant(Weapon.Enchantment.random());
		}
		if (weapon.tier < 1) {
			weapon.setTier(1);
		}
		weapon.setTier(Math.min(5, Random.Int(minTier, maxTier)));
		if (weapon.tier == minTier & weapon.isUpgradable()) {//Extra upgrade if possible and Armour is at minimum amount
			weapon.upgrade();
		} else if (weapon.tier == maxTier & weapon.level() > 1) {
			weapon.degrade();
		}
		return weapon;
	}

	private ArrayList<Item> generateItems(int level) {

		ArrayList<Item> itemsToSpawn = new ArrayList<>();
		
		switch (Dungeon.depth) {
			case 5:
				itemsToSpawn.add(Generator.random(Generator.misTiers[1]).quantity(2).identify());
				break;

			case 11:
				itemsToSpawn.add(Generator.random(Generator.misTiers[2]).quantity(2).identify());
				break;

			case 17:
				itemsToSpawn.add(Generator.random(Generator.misTiers[3]).quantity(2).identify());
				break;

			case 23:
			case 24:
				itemsToSpawn.add(Generator.random(Generator.misTiers[4]).quantity(2).identify());
				itemsToSpawn.add( new Torch() );
				itemsToSpawn.add( new Torch() );
				itemsToSpawn.add( new Torch() );
				break;
		}
		for (int a = 0; a < Random.IntRange(1,2); a++) {
			Armor armor = generateArmor(level);
			itemsToSpawn.add( armor );
		}

		for (int a = 0; a < Random.IntRange(1,2); a++) {
			MeleeWeapon weapon = generateWeapon(level);
			itemsToSpawn.add( weapon );
		}

		itemsToSpawn.add(new StoneOfRepair());
		
		itemsToSpawn.add( TippedDart.randomTipped(2) );

		itemsToSpawn.add( Random.oneOf(new Whiskey(), new Beer()));

		itemsToSpawn.add( new MerchantsBeacon() );


		itemsToSpawn.add(ChooseBag(Dungeon.hero.belongings));


		itemsToSpawn.add( new PotionOfHealing() );
		for (int i=0; i < 3; i++)
			itemsToSpawn.add( Generator.random( Generator.Category.POTION ) );

		itemsToSpawn.add( new ScrollOfIdentify() );
		itemsToSpawn.add( new ScrollOfRemoveCurse() );
		itemsToSpawn.add( new ScrollOfMagicMapping() );
		itemsToSpawn.add( Generator.random( Generator.Category.SCROLL ) );

		for (int i=0; i < 2; i++)
			itemsToSpawn.add( Random.Int(2) == 0 ?
					Generator.random( Generator.Category.POTION ) :
					Generator.random( Generator.Category.SCROLL ) );


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

		TimekeepersHourglass hourglass = Dungeon.hero.belongings.getItem(TimekeepersHourglass.class);
		if (hourglass != null){
			int bags = 0;
			//creates the given float percent of the remaining bags to be dropped.
			//this way players who get the hourglass late can still max it, usually.
			switch (Dungeon.depth) {
				case 5:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.20f ); break;
				case 11:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.25f ); break;
				case 18:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.50f ); break;
				case 23: case 24:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.80f ); break;
			}

			for(int i = 1; i <= bags; i++){
				itemsToSpawn.add( new TimekeepersHourglass.sandBag());
				hourglass.sandBags ++;
			}
		}

		Item rare;
		switch (Random.Int(2)){
			case 0: default:
				rare = Generator.random( Generator.Category.WAND );
				break;
			case 1:
				rare = Generator.random(Generator.Category.RING);
				break;
			case 2:
				rare = Generator.random( Generator.Category.ARTIFACT );
				break;
		}
		rare.cursed = false;
		rare.cursedKnown = true;
		rare.levelKnown = true;
		itemsToSpawn.add( rare );

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
				if (bag.grab(item)){
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

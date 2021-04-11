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

package com.shatteredpixel.yasd.general.items.scrolls;

import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.items.EquipableItem;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.artifacts.Artifact;
import com.shatteredpixel.yasd.general.items.potions.AlchemicalCatalyst;
import com.shatteredpixel.yasd.general.items.potions.Potion;
import com.shatteredpixel.yasd.general.items.potions.brews.Brew;
import com.shatteredpixel.yasd.general.items.potions.elixirs.Elixir;
import com.shatteredpixel.yasd.general.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.yasd.general.items.relics.DragonPendant;
import com.shatteredpixel.yasd.general.items.relics.Relic;
import com.shatteredpixel.yasd.general.items.rings.Ring;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.yasd.general.items.shield.Shield;
import com.shatteredpixel.yasd.general.items.stones.Runestone;
import com.shatteredpixel.yasd.general.items.unused.missiles.MissileWeapon;
import com.shatteredpixel.yasd.general.items.unused.missiles.darts.Dart;
import com.shatteredpixel.yasd.general.items.wands.Wand;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.journal.Catalog;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.plants.Plant;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndBag;
import com.watabou.utils.Reflection;

public class ScrollOfTransmutation extends InventoryScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_TRANSMUTE;
		mode = WndBag.Mode.TRANMSUTABLE;
		
		bones = true;

		mpCost = 10;
	}
	
	public static boolean canTransmute(Item item){
		return item instanceof MeleeWeapon ||
				item instanceof Shield ||
				(item instanceof MissileWeapon && !(item instanceof Dart)) ||
				(item instanceof Potion && !(item instanceof Elixir || item instanceof Brew || item instanceof AlchemicalCatalyst)) ||
				item instanceof Scroll ||
				item instanceof Ring ||
				item instanceof Wand ||
				item instanceof Plant.Seed ||
				item instanceof Runestone ||
				item instanceof DragonPendant ||
				item instanceof Artifact;
	}
	
	@Override
	protected void onItemSelected(Item item) {
		
		Item result;
		
		if (item instanceof MagesStaff) {
			result = changeStaff( (MagesStaff)item );
		} else if (item instanceof MeleeWeapon || item instanceof MissileWeapon) {
			result = changeWeapon( (Weapon)item );
		} else if (item instanceof Scroll) {
			result = changeScroll( (Scroll)item );
		} else if (item instanceof Potion) {
			result = changePotion( (Potion)item );
		} else if (item instanceof Ring) {
			result = changeRing( (Ring)item );
		} else if (item instanceof Wand) {
			result = changeWand( (Wand)item );
		} else if (item instanceof Plant.Seed) {
			result = changeSeed((Plant.Seed) item);
		} else if (item instanceof Runestone) {
			result = changeStone((Runestone) item);
		} else if (item instanceof Artifact) {
			result = changeArtifact( (Artifact)item );
		} else if (item instanceof Shield) {
			result = changeShield((Shield) item );
		} else if (item instanceof Relic) {
			result = changeRelic((Relic) item);
		} else {
			result = null;
		}
		
		if (result == null){
			//This shouldn't ever interact
			GLog.negative( Messages.get(this, "nothing") );
			this.collect( curUser.belongings.backpack, curUser);
		} else {
			if (item.isEquipped(Dungeon.hero)){
				item.uncurse(); //to allow it to be unequipped
				((EquipableItem)item).doUnequip(Dungeon.hero, false);
				((EquipableItem)result).doEquip(Dungeon.hero);
			} else {
				item.detach(Dungeon.hero.belongings.backpack);
				if (!result.collect()){
					Dungeon.level.drop(result, curUser.pos).sprite.drop();
				}
			}
			if (result.isIdentified()){
				Catalog.setSeen(result.getClass());
			}
			result.timesUpgraded = item.timesUpgraded;
			//TODO visuals
			GLog.positive( Messages.get(this, "morph") );
		}
		
	}
	
	private MagesStaff changeStaff( MagesStaff staff ){
		Class<?extends Wand> wandClass = staff.wandClass();
		
		if (wandClass == null){
			return null;
		} else {
			Wand n;
			do {
				n = (Wand) Generator.random(Generator.Category.WAND);
			} while (!n.canSpawn() || n.getClass() == wandClass);
			n.level(0);
			n.identify();
			staff.imbueWand(n, null);
		}
		
		return staff;
	}

	private com.shatteredpixel.yasd.general.items.shield.Shield changeShield(com.shatteredpixel.yasd.general.items.shield.Shield a ) {
		com.shatteredpixel.yasd.general.items.shield.Shield s;
		do {
			s = Generator.randomShield();
		} while (s.getClass() == a.getClass());

		int level = a.trueLevel();

		s.level(level);

		s.levelKnown = a.levelKnown;
		s.cursedKnown = a.cursedKnown;
		s.curseIntensity = a.curseIntensity;
		return s;
	}

	/*private Armor changeArmor(Armor a ) {
		Armor n;
		do {
			n = Generator.randomShield();
		} while (n.getClass() == a.getClass());

		int level = a.trueLevel();

		n.level(level);

		n.glyph = a.glyph;
		n.curseInfusionBonus = a.curseInfusionBonus;
		n.levelKnown = a.levelKnown;
		n.cursedKnown = a.cursedKnown;
		n.cursed = a.cursed;
		n.augment = a.augment;
		return n;
	}*/

	private Relic changeRelic(Relic r ) {
		Relic n;
		do {
			n = (Relic) Generator.random(Generator.Category.RELIC);
		} while (!n.canSpawn() || n.getClass() == r.getClass());
		int level = r.trueLevel();
		n.level(level);
		n.levelKnown = r.levelKnown;
		n.cursedKnown = r.cursedKnown;
		n.curseIntensity = r.curseIntensity;
		return n;
	}
	
	private Weapon changeWeapon( Weapon w ) {
		
		Weapon n;
		do {
			if (w instanceof MeleeWeapon) {
				n = Generator.randomWeapon();
			} else {
				n = Generator.randomRanged();
			}
		} while (!n.canSpawn() || n.getClass() == w.getClass());
		
		int level = w.level();
		if (w.curseInfusionBonus) level -= Constants.CURSE_INFUSION_BONUS_AMT;


		n.level(level);
		n.enchantment = w.enchantment;
		n.curseInfusionBonus = w.curseInfusionBonus;
		n.levelKnown = w.levelKnown;
		n.cursedKnown = w.cursedKnown;
		n.curseIntensity = w.curseIntensity;
		n.augment = w.augment;
		
		return n;
		
	}
	
	private Ring changeRing( Ring r ) {
		Ring n;
		do {
			n = (Ring)Generator.random( Generator.Category.RING );
		} while (!n.canSpawn() || n.getClass() == r.getClass());
		
		n.level(0);
		
		int level = r.level();
		if (level > 0) {
			n.upgrade( level );
		} else if (level < 0) {
			n.degrade( -level );
		}
		
		n.levelKnown = r.levelKnown;
		n.cursedKnown = r.cursedKnown;
		n.curseIntensity = r.curseIntensity;
		
		return n;
	}
	
	private Artifact changeArtifact( Artifact a ) {
		Artifact n = Generator.randomArtifact();
		
		if (n != null && n.canSpawn()){
			n.cursedKnown = a.cursedKnown;
			n.curseIntensity = a.curseIntensity;
			n.levelKnown = a.levelKnown;
			n.transferUpgrade(a.visiblyUpgraded());
			return n;
		}
		
		return null;
	}
	
	private Wand changeWand( Wand w ) {
		
		Wand n;
		do {
			n = (Wand)Generator.random( Generator.Category.WAND );
		} while ( !n.canSpawn() || n.getClass() == w.getClass());
		
		n.level( 0 );
		int level = w.level();
		if (w.curseInfusionBonus) level -= Constants.CURSE_INFUSION_BONUS_AMT;
		n.upgrade( level );
		
		n.levelKnown = w.levelKnown;
		n.cursedKnown = w.cursedKnown;
		n.curseIntensity = w.curseIntensity;
		n.curseInfusionBonus = w.curseInfusionBonus;
		
		return n;
	}
	
	private Plant.Seed changeSeed( Plant.Seed s ) {
		
		Plant.Seed n;
		
		do {
			n = (Plant.Seed)Generator.random( Generator.Category.SEED );
		} while (n.getClass() == s.getClass());
		
		return n;
	}
	
	private Runestone changeStone( Runestone r ) {
		
		Runestone n;
		
		do {
			n = (Runestone) Generator.random( Generator.Category.STONE );
		} while (n.getClass() == r.getClass());
		
		return n;
	}
	
	private Scroll changeScroll( Scroll s ) {
		if (s instanceof ExoticScroll) {
			return Reflection.newInstance(ExoticScroll.exoToReg.get(s.getClass()));
		} else {
			return Reflection.newInstance(ExoticScroll.regToExo.get(s.getClass()));
		}
	}
	
	private Potion changePotion( Potion p ) {
		if	(p instanceof ExoticPotion) {
			return Reflection.newInstance(ExoticPotion.exoToReg.get(p.getClass()));
		} else {
			return Reflection.newInstance(ExoticPotion.regToExo.get(p.getClass()));
		}
	}
	
	@Override
	public void empoweredRead() {
		//does nothing, this shouldn't happen
	}
	
	@Override
	public int price() {
		return isKnown() ? 100 * quantity : super.price();
	}
}

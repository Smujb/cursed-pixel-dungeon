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

package com.shatteredpixel.yasd.general.journal;

import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.yasd.general.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.yasd.general.items.artifacts.CloakOfShadows;
import com.shatteredpixel.yasd.general.items.artifacts.EtherealChains;
import com.shatteredpixel.yasd.general.items.artifacts.HornOfPlenty;
import com.shatteredpixel.yasd.general.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.yasd.general.items.artifacts.SandalsOfNature;
import com.shatteredpixel.yasd.general.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.yasd.general.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.general.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.yasd.general.items.potions.PotionOfExperience;
import com.shatteredpixel.yasd.general.items.potions.PotionOfFrost;
import com.shatteredpixel.yasd.general.items.potions.PotionOfHaste;
import com.shatteredpixel.yasd.general.items.potions.PotionOfInvisibility;
import com.shatteredpixel.yasd.general.items.potions.PotionOfLevitation;
import com.shatteredpixel.yasd.general.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.yasd.general.items.potions.PotionOfMana;
import com.shatteredpixel.yasd.general.items.potions.PotionOfMindVision;
import com.shatteredpixel.yasd.general.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.yasd.general.items.potions.PotionOfPurity;
import com.shatteredpixel.yasd.general.items.potions.PotionOfRestoration;
import com.shatteredpixel.yasd.general.items.potions.PotionOfToxicGas;
import com.shatteredpixel.yasd.general.items.rings.RingOfAssault;
import com.shatteredpixel.yasd.general.items.rings.RingOfElements;
import com.shatteredpixel.yasd.general.items.rings.RingOfExecution;
import com.shatteredpixel.yasd.general.items.rings.RingOfFaithAndPower;
import com.shatteredpixel.yasd.general.items.rings.RingOfFocus;
import com.shatteredpixel.yasd.general.items.rings.RingOfFuror;
import com.shatteredpixel.yasd.general.items.rings.RingOfHaste;
import com.shatteredpixel.yasd.general.items.rings.RingOfResilience;
import com.shatteredpixel.yasd.general.items.rings.RingOfTenacity;
import com.shatteredpixel.yasd.general.items.rings.RingOfWealth;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfGreed;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRage;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.yasd.general.items.unused.armor.BasicArmor;
import com.shatteredpixel.yasd.general.items.unused.armor.ChainArmor;
import com.shatteredpixel.yasd.general.items.unused.armor.ClothArmor;
import com.shatteredpixel.yasd.general.items.unused.armor.HeavyArmor;
import com.shatteredpixel.yasd.general.items.unused.armor.HuntressArmor;
import com.shatteredpixel.yasd.general.items.unused.armor.LightArmor;
import com.shatteredpixel.yasd.general.items.unused.armor.MageArmor;
import com.shatteredpixel.yasd.general.items.unused.armor.RogueArmor;
import com.shatteredpixel.yasd.general.items.wands.WandOfBlastWave;
import com.shatteredpixel.yasd.general.items.wands.WandOfCorrosion;
import com.shatteredpixel.yasd.general.items.wands.WandOfCorruption;
import com.shatteredpixel.yasd.general.items.wands.WandOfDisintegration;
import com.shatteredpixel.yasd.general.items.wands.WandOfFireblast;
import com.shatteredpixel.yasd.general.items.wands.WandOfFrost;
import com.shatteredpixel.yasd.general.items.wands.WandOfLightning;
import com.shatteredpixel.yasd.general.items.wands.WandOfLivingEarth;
import com.shatteredpixel.yasd.general.items.wands.WandOfMagicMissile;
import com.shatteredpixel.yasd.general.items.wands.WandOfPrismaticLight;
import com.shatteredpixel.yasd.general.items.wands.WandOfRegrowth;
import com.shatteredpixel.yasd.general.items.wands.WandOfTransfusion;
import com.shatteredpixel.yasd.general.items.wands.WandOfWarding;
import com.shatteredpixel.yasd.general.items.weapon.melee.Axe;
import com.shatteredpixel.yasd.general.items.weapon.melee.Dagger;
import com.shatteredpixel.yasd.general.items.weapon.melee.Flail;
import com.shatteredpixel.yasd.general.items.weapon.melee.Glove;
import com.shatteredpixel.yasd.general.items.weapon.melee.Greataxe;
import com.shatteredpixel.yasd.general.items.weapon.melee.Katana;
import com.shatteredpixel.yasd.general.items.weapon.melee.Mace;
import com.shatteredpixel.yasd.general.items.weapon.melee.Polearm;
import com.shatteredpixel.yasd.general.items.weapon.melee.RunicBlade;
import com.shatteredpixel.yasd.general.items.weapon.melee.Scimitar;
import com.shatteredpixel.yasd.general.items.weapon.melee.Staff;
import com.shatteredpixel.yasd.general.items.weapon.melee.Sword;
import com.shatteredpixel.yasd.general.items.weapon.melee.Whip;
import com.shatteredpixel.yasd.general.items.weapon.melee.hybrid.MagesStaff;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public enum Catalog {
	
	WEAPONS,
	ARMOR,
	WANDS,
	RINGS,
	ARTIFACTS,
	POTIONS,
	SCROLLS;
	
	private LinkedHashMap<Class<? extends Item>, Boolean> seen = new LinkedHashMap<>();
	
	public Collection<Class<? extends Item>> items(){
		return seen.keySet();
	}
	
	public boolean allSeen(){
		for (Class<?extends Item> item : items()){
			if (!seen.get(item)){
				return false;
			}
		}
		return true;
	}
	
	static {
		WEAPONS.seen.put( Glove.class,                     false);
		WEAPONS.seen.put( MagesStaff.class,                 false);
		WEAPONS.seen.put( Staff.class,               false);
		WEAPONS.seen.put( Scimitar.class,                   false);
		WEAPONS.seen.put( Katana.class,                        false);
		WEAPONS.seen.put( Whip.class,                       false);
		WEAPONS.seen.put( Axe.class,                  false);
		WEAPONS.seen.put( Flail.class,                      false);
		WEAPONS.seen.put( RunicBlade.class,                 false);
		WEAPONS.seen.put( Dagger.class,             false);
		WEAPONS.seen.put( Sword.class,                 false);
		WEAPONS.seen.put( Mace.class,                  false);
		WEAPONS.seen.put( Polearm.class,                     false);
		WEAPONS.seen.put( Greataxe.class,                   false);
	
		ARMOR.seen.put( ClothArmor.class,                   false);
		ARMOR.seen.put( LightArmor.class,                 false);
		ARMOR.seen.put( ChainArmor.class,                    false);
		ARMOR.seen.put( BasicArmor.class,                   false);
		ARMOR.seen.put( HeavyArmor.class,                   false);
		ARMOR.seen.put( MageArmor.class,                    false);
		ARMOR.seen.put( RogueArmor.class,                   false);
		ARMOR.seen.put( HuntressArmor.class,                false);
	
		WANDS.seen.put( WandOfMagicMissile.class,           false);
		WANDS.seen.put( WandOfLightning.class,              false);
		WANDS.seen.put( WandOfDisintegration.class,         false);
		WANDS.seen.put( WandOfFireblast.class,              false);
		WANDS.seen.put( WandOfCorrosion.class,              false);
		WANDS.seen.put( WandOfBlastWave.class,              false);
		WANDS.seen.put( WandOfLivingEarth.class,            false);
		WANDS.seen.put( WandOfFrost.class,                  false);
		WANDS.seen.put( WandOfPrismaticLight.class,         false);
		WANDS.seen.put( WandOfWarding.class,                false);
		WANDS.seen.put( WandOfTransfusion.class,            false);
		WANDS.seen.put( WandOfCorruption.class,             false);
		WANDS.seen.put( WandOfRegrowth.class,               false);
	
		RINGS.seen.put( RingOfResilience.class,               false );
		RINGS.seen.put( RingOfFocus.class,                 false );
		RINGS.seen.put( RingOfElements.class,               false );
		RINGS.seen.put( RingOfAssault.class,                   false );
		RINGS.seen.put( RingOfFuror.class,                  false );
		RINGS.seen.put( RingOfHaste.class,                  false );
		RINGS.seen.put( RingOfExecution.class,                  false );
		RINGS.seen.put( RingOfFaithAndPower.class,          false );
		RINGS.seen.put( RingOfTenacity.class,               false );
		RINGS.seen.put( RingOfWealth.class,                 false );
	
		ARTIFACTS.seen.put( AlchemistsToolkit.class,        false);
		ARTIFACTS.seen.put( ChaliceOfBlood.class,           false);
		ARTIFACTS.seen.put( CloakOfShadows.class,           false);
		ARTIFACTS.seen.put( EtherealChains.class,           false);
		ARTIFACTS.seen.put( HornOfPlenty.class,             false);
		ARTIFACTS.seen.put( MasterThievesArmband.class,     false);
		ARTIFACTS.seen.put( SandalsOfNature.class,          false);
		ARTIFACTS.seen.put( TalismanOfForesight.class,      false);
		ARTIFACTS.seen.put( TimekeepersHourglass.class,     false);
		ARTIFACTS.seen.put( UnstableSpellbook.class,        false);
	
		POTIONS.seen.put( PotionOfRestoration.class,            false);
		POTIONS.seen.put( PotionOfMana.class,           false);
		POTIONS.seen.put( PotionOfLiquidFlame.class,        false);
		POTIONS.seen.put( PotionOfFrost.class,              false);
		POTIONS.seen.put( PotionOfToxicGas.class,           false);
		POTIONS.seen.put( PotionOfParalyticGas.class,       false);
		POTIONS.seen.put( PotionOfPurity.class,             false);
		POTIONS.seen.put( PotionOfLevitation.class,         false);
		POTIONS.seen.put( PotionOfMindVision.class,         false);
		POTIONS.seen.put( PotionOfInvisibility.class,       false);
		POTIONS.seen.put( PotionOfExperience.class,         false);
		POTIONS.seen.put( PotionOfHaste.class,              false);
	
		SCROLLS.seen.put( ScrollOfIdentify.class,           false);
		SCROLLS.seen.put( ScrollOfGreed.class,            false);
		SCROLLS.seen.put( ScrollOfRemoveCurse.class,        false);
		SCROLLS.seen.put( ScrollOfMagicMapping.class,       false);
		SCROLLS.seen.put( ScrollOfTeleportation.class,      false);
		SCROLLS.seen.put( ScrollOfRecharging.class,         false);
		SCROLLS.seen.put( ScrollOfMirrorImage.class,        false);
		SCROLLS.seen.put( ScrollOfTerror.class,             false);
		SCROLLS.seen.put( ScrollOfLullaby.class,            false);
		SCROLLS.seen.put( ScrollOfRage.class,               false);
		SCROLLS.seen.put( ScrollOfRetribution.class,        false);
		SCROLLS.seen.put( ScrollOfTransmutation.class,      false);
	}
	
	public static LinkedHashMap<Catalog, Badges.Badge> catalogBadges = new LinkedHashMap<>();
	static {
		catalogBadges.put(WEAPONS, Badges.Badge.ALL_WEAPONS_IDENTIFIED);
		catalogBadges.put(ARMOR, Badges.Badge.ALL_ARMOR_IDENTIFIED);
		catalogBadges.put(WANDS, Badges.Badge.ALL_WANDS_IDENTIFIED);
		catalogBadges.put(RINGS, Badges.Badge.ALL_RINGS_IDENTIFIED);
		catalogBadges.put(ARTIFACTS, Badges.Badge.ALL_ARTIFACTS_IDENTIFIED);
		catalogBadges.put(POTIONS, Badges.Badge.ALL_POTIONS_IDENTIFIED);
		catalogBadges.put(SCROLLS, Badges.Badge.ALL_SCROLLS_IDENTIFIED);
	}
	
	public static boolean isSeen(Class<? extends Item> itemClass){
		for (Catalog cat : values()) {
			if (cat.seen.containsKey(itemClass)) {
				return cat.seen.get(itemClass);
			}
		}
		return false;
	}
	
	public static void setSeen(Class<? extends Item> itemClass){
		for (Catalog cat : values()) {
			if (cat.seen.containsKey(itemClass) && !cat.seen.get(itemClass)) {
				cat.seen.put(itemClass, true);
				Journal.saveNeeded = true;
			}
		}
		Badges.validateItemsIdentified();
	}
	
	private static final String CATALOGS = "catalogs";
	
	public static void store( Bundle bundle ){
		
		Badges.loadGlobal();
		
		ArrayList<String> seen = new ArrayList<>();
		
		//if we have identified all items of a set, we use the badge to keep track instead.
		if (!Badges.isUnlocked(Badges.Badge.ALL_ITEMS_IDENTIFIED)) {
			for (Catalog cat : values()) {
				if (!Badges.isUnlocked(catalogBadges.get(cat))) {
					for (Class<? extends Item> item : cat.items()) {
						if (cat.seen.get(item)) seen.add(item.getSimpleName());
					}
				}
			}
		}
		
		bundle.put( CATALOGS, seen.toArray(new String[0]) );
		
	}
	
	public static void restore( Bundle bundle ){
		
		Badges.loadGlobal();
		
		//logic for if we have all badges
		if (Badges.isUnlocked(Badges.Badge.ALL_ITEMS_IDENTIFIED)){
			for ( Catalog cat : values()){
				for (Class<? extends Item> item : cat.items()){
					cat.seen.put(item, true);
				}
			}
			return;
		}
		
		//catalog-specific badge logic
		for (Catalog cat : values()){
			if (Badges.isUnlocked(catalogBadges.get(cat))){
				for (Class<? extends Item> item : cat.items()){
					cat.seen.put(item, true);
				}
			}
		}
		
		//general save/load
		if (bundle.contains(CATALOGS)) {
			List<String> seen = Arrays.asList(bundle.getStringArray(CATALOGS));
			
			//TODO should adjust this to tie into the bundling system's class array
			for (Catalog cat : values()) {
				for (Class<? extends Item> item : cat.items()) {
					if (seen.contains(item.getSimpleName())) {
						cat.seen.put(item, true);
					}
				}
			}
		}
	}
	
}

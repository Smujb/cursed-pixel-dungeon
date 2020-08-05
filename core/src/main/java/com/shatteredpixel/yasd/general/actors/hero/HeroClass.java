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

package com.shatteredpixel.yasd.general.actors.hero;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Challenges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.items.BrokenSeal;
import com.shatteredpixel.yasd.general.items.DeveloperItem;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindOfWeapon;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.items.TomeOfMastery;
import com.shatteredpixel.yasd.general.items.allies.AirDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.DarkDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.EarthenDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.FireDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.GrassDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.IceDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.PoisonDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.StoneDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.VampiricDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.WaterDragonPendant;
import com.shatteredpixel.yasd.general.items.armor.ChainArmor;
import com.shatteredpixel.yasd.general.items.armor.ClothArmor;
import com.shatteredpixel.yasd.general.items.armor.HuntressArmor;
import com.shatteredpixel.yasd.general.items.armor.MageArmor;
import com.shatteredpixel.yasd.general.items.armor.PriestessArmor;
import com.shatteredpixel.yasd.general.items.armor.RogueArmor;
import com.shatteredpixel.yasd.general.items.bags.MagicalHolster;
import com.shatteredpixel.yasd.general.items.bags.PotionBandolier;
import com.shatteredpixel.yasd.general.items.bags.PowerHolder;
import com.shatteredpixel.yasd.general.items.bags.ScrollHolder;
import com.shatteredpixel.yasd.general.items.bags.VelvetPouch;
import com.shatteredpixel.yasd.general.items.food.Food;
import com.shatteredpixel.yasd.general.items.food.SmallRation;
import com.shatteredpixel.yasd.general.items.potions.PotionOfHealing;
import com.shatteredpixel.yasd.general.items.potions.PotionOfInvisibility;
import com.shatteredpixel.yasd.general.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.yasd.general.items.potions.PotionOfMindVision;
import com.shatteredpixel.yasd.general.items.powers.Blink;
import com.shatteredpixel.yasd.general.items.powers.LuckyBadge;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRage;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.yasd.general.items.wands.WandOfMagicMissile;
import com.shatteredpixel.yasd.general.items.weapon.SpiritBow;
import com.shatteredpixel.yasd.general.items.weapon.melee.Basic;
import com.shatteredpixel.yasd.general.items.weapon.melee.Fist;
import com.shatteredpixel.yasd.general.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.general.items.weapon.melee.Magical;
import com.shatteredpixel.yasd.general.items.weapon.melee.Sneak;
import com.shatteredpixel.yasd.general.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.yasd.general.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.HeroSelectScene;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

public enum HeroClass {

	WARRIOR( "warrior"),
	MAGE( "mage"),
	ROGUE( "rogue"),
	HUNTRESS( "huntress"),
	PRIESTESS( "priestess");

	private String title;

	HeroClass(String title) {
		this.title = title;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;

		initCommon( hero );

		switch (this) {
			case WARRIOR:
				initWarrior( hero );
				break;

			case MAGE:
				initMage( hero );
				break;

			case ROGUE:
				initRogue( hero );
				break;

			case HUNTRESS:
				initHuntress( hero );
				break;

			case PRIESTESS:
				initPriestess( hero );
				break;
		}
		
	}

	private static void initCommon( Hero hero ) {
		hero.belongings.miscs = new KindofMisc[hero.miscSlots()];
		Item i = new ClothArmor().identify();
		if (!Challenges.isItemBlocked(i))

		i = new Food();
		if (!Challenges.isItemBlocked(i)) i.collect();

		if (Dungeon.isChallenged(Challenges.NO_FOOD)){
			new SmallRation().collect();
		}

		if (HeroSelectScene.curWeapon != null) {
			hero.belongings.setWeapon((KindOfWeapon) HeroSelectScene.curWeapon.upgrade().identify());
			hero.belongings.getWeapon().activate(hero);
		}

		new PotionBandolier().collect();
		Dungeon.LimitedDrops.POTION_BANDOLIER.drop();

		new ScrollHolder().collect();
		Dungeon.LimitedDrops.SCROLL_HOLDER.drop();

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();

		new MagicalHolster().collect();
		Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();

		new PowerHolder().collect();

		//new FoodHolder().collect();

		new LuckyBadge().collect();

		new ScrollOfIdentify().identify().collect();

		if (Dungeon.testing) {
			initTest(hero);
		}
	}

	public static void initTest(Hero hero) {
		new TomeOfMastery().collect();
		new FireDragonPendant().identify().collect();
		new WaterDragonPendant().identify().collect();
		new VampiricDragonPendant().identify().collect();
		new EarthenDragonPendant().identify().collect();
		new PoisonDragonPendant().identify().collect();
		new IceDragonPendant().identify().collect();
		new StoneDragonPendant().identify().collect();
		new DarkDragonPendant().identify().collect();
		new GrassDragonPendant().identify().collect();
		new AirDragonPendant().identify().collect();
		new DeveloperItem().collect(hero.belongings.backpack, hero);
	}

	@Contract(pure = true)
	public Badges.Badge masteryBadge() {
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
			case PRIESTESS:
				return Badges.Badge.MASTERY_PRIESTESS;
		}
		return null;
	}

	private static void initWarrior( Hero hero ) {
		if (hero.belongings.getWeapon() == null) {
			hero.belongings.setWeapon((KindOfWeapon) new Basic().upgrade().identify());
		}
		(hero.belongings.armor = new ChainArmor()).identify();
		ThrowingStone stones = new ThrowingStone();
		stones.quantity(3).collect();
		Dungeon.quickslot.setSlot(0, stones);

		new BrokenSeal().collect();
		
		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
		hero.setPower(5);
		hero.STR();
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff;

		staff = (MagesStaff) new MagesStaff(new WandOfMagicMissile()).upgrade().identify();

		if (hero.belongings.getWeapon() == null) {
			hero.belongings.setWeapon(staff);
		} else {
			staff.collect();
		}
		(hero.belongings.armor = new MageArmor()).identify();
		staff.activate(hero);

		new Blink().collect();

		Dungeon.quickslot.setSlot(0, staff);
		
		new ScrollOfUpgrade().identify();
		new PotionOfLiquidFlame().identify();
		hero.setFocus(5);
	}

	private static void initRogue( Hero hero ) {
		if (hero.belongings.getWeapon() == null) {
			hero.belongings.setWeapon((KindOfWeapon) new Sneak().upgrade().identify());
		}
		(hero.belongings.armor = new RogueArmor()).identify();
		//CloakOfShadows cloak = new CloakOfShadows();
		//(hero.belongings.miscs[2] = cloak).identify();
		//hero.belongings.miscs[2].activate( hero );

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		//Dungeon.quickslot.setSlot(0, cloak);
		Dungeon.quickslot.setSlot(1, knives);
		
		new ScrollOfMagicMapping().identify();
		new PotionOfInvisibility().identify();
		hero.setEvasion(5);
	}

	private static void initHuntress( Hero hero ) {

		if (hero.belongings.getWeapon() == null) {
			hero.belongings.setWeapon((KindOfWeapon) new Fist().upgrade().identify());
		}
		(hero.belongings.armor = new HuntressArmor()).identify();
		SpiritBow bow = new SpiritBow();
		bow.identify().collect();

		Dungeon.quickslot.setSlot(0, bow);
		
		new PotionOfMindVision().identify();
		new ScrollOfLullaby().identify();

		hero.setPerception(5);
	}

	private static void initPriestess( Hero hero ) {

		if (hero.belongings.getWeapon() == null) {
			hero.belongings.setWeapon((KindOfWeapon) new Magical().upgrade().identify());
		}
		(hero.belongings.armor = new PriestessArmor()).identify();
		(hero.belongings.miscs[0] = new PoisonDragonPendant()).upgrade().identify();
		hero.belongings.miscs[0].activate(hero);

		new PotionOfMindVision().identify();
		new ScrollOfLullaby().identify();

		hero.setAttunement(5);
	}
	
	public String title() {
		return Messages.get(HeroClass.class, title);
	}
	
	public HeroSubClass[] subClasses() {
		ArrayList<HeroSubClass> subClasses = new ArrayList<>();
		switch (this) {
			case MAGE:
				subClasses.add(HeroSubClass.BATTLEMAGE);//NORMAL
				subClasses.add(HeroSubClass.WARLOCK);//MAGE + WARRIOR
				subClasses.add(HeroSubClass.NECROMANCER);//MAGE + PRIESTESS
				subClasses.add(HeroSubClass.WARDEN);//MAGE + HUNTRESS
				subClasses.add(HeroSubClass.ENCHANTER);//MAGE + ROGUE
				break;
			case WARRIOR:
				subClasses.add(HeroSubClass.BERSERKER);//NORMAL
				subClasses.add(HeroSubClass.GLADIATOR);//WARRIOR + HUNTRESS
				subClasses.add(HeroSubClass.WARLOCK);//WARRIOR + MAGE
				subClasses.add(HeroSubClass.BRAWLER);//WARRIOR + ROGUE
				subClasses.add(HeroSubClass.PALADIN);//WARRIOR + PRIESTESS
				break;
			case ROGUE:
				subClasses.add(HeroSubClass.ASSASSIN);//NORMAL
				subClasses.add(HeroSubClass.FREERUNNER);//ROGUE + HUNTRESS
				subClasses.add(HeroSubClass.BRAWLER);//ROGUE + WARRIOR
				subClasses.add(HeroSubClass.ENCHANTER);//ROGUE + MAGE
				subClasses.add(HeroSubClass.CULTIST);//ROGUE + PRIESTESS
				break;
			case HUNTRESS:
				subClasses.add(HeroSubClass.SNIPER);//NORMAL
				subClasses.add(HeroSubClass.WARDEN);//HUNTRESS + MAGE
				subClasses.add(HeroSubClass.GLADIATOR);//HUNTRESS + WARRIOR
				subClasses.add(HeroSubClass.FREERUNNER);//HUNTRESS + ROGUE
				subClasses.add(HeroSubClass.VALKYRIE);//HUNTRESS + PRIESTESS
				break;
			case PRIESTESS:
				subClasses.add(HeroSubClass.MEDIC);//NORMAL
				subClasses.add(HeroSubClass.NECROMANCER);//PRIESTESS + MAGE
				subClasses.add(HeroSubClass.VALKYRIE);//PRIESTESS + HUNTRESS
				subClasses.add(HeroSubClass.PALADIN);//PRIESTESS + WARRIOR
				subClasses.add(HeroSubClass.CULTIST);//PRIESTESS + ROGUE
				break;
		}
		return subClasses.toArray(new HeroSubClass[0]);
	}
	
	public String spritesheet() {
		switch (this) {
			case WARRIOR: default:
				return Assets.Sprites.WARRIOR;
			case MAGE:
				return Assets.Sprites.MAGE;
			case ROGUE:
				return Assets.Sprites.ROGUE;
			case HUNTRESS:
				return Assets.Sprites.HUNTRESS;
			case PRIESTESS:
				return Assets.Sprites.PRIESTESS;
		}
	}

	public String splashArt(){
		switch (this) {
			case WARRIOR: default:
				return Assets.Splashes.WARRIOR;
			case MAGE:
				return Assets.Splashes.MAGE;
			case ROGUE:
				return Assets.Splashes.ROGUE;
			case HUNTRESS:
				return Assets.Splashes.HUNTRESS;
			//FIXME splash for Priestess
			case PRIESTESS:
				return Assets.Interfaces.SHADOW;
		}
	}

	public Image icon() {
		return new Image(spritesheet(), 0, 90, 12, 15);
	}
	
	public String[] perks() {
		switch (this) {
			case WARRIOR: default:
				return new String[]{
						Messages.get(HeroClass.class, "warrior_perk1"),
						Messages.get(HeroClass.class, "warrior_perk2"),
						Messages.get(HeroClass.class, "warrior_perk3"),
						Messages.get(HeroClass.class, "warrior_perk4"),
						Messages.get(HeroClass.class, "warrior_perk5"),
				};
			case MAGE:
				return new String[]{
						Messages.get(HeroClass.class, "mage_perk1"),
						Messages.get(HeroClass.class, "mage_perk2"),
						Messages.get(HeroClass.class, "mage_perk3"),
						Messages.get(HeroClass.class, "mage_perk4"),
						Messages.get(HeroClass.class, "mage_perk5"),
				};
			case ROGUE:
				return new String[]{
						Messages.get(HeroClass.class, "rogue_perk1"),
						Messages.get(HeroClass.class, "rogue_perk2"),
						Messages.get(HeroClass.class, "rogue_perk3"),
						Messages.get(HeroClass.class, "rogue_perk4"),
						Messages.get(HeroClass.class, "rogue_perk5"),
				};
			case HUNTRESS:
				return new String[]{
						Messages.get(HeroClass.class, "huntress_perk1"),
						Messages.get(HeroClass.class, "huntress_perk2"),
						Messages.get(HeroClass.class, "huntress_perk3"),
						Messages.get(HeroClass.class, "huntress_perk4"),
						Messages.get(HeroClass.class, "huntress_perk5"),
				};
			case PRIESTESS:
				return new String[]{
						Messages.get(HeroClass.class, "priestess_perk1"),
						Messages.get(HeroClass.class, "priestess_perk2"),
						Messages.get(HeroClass.class, "priestess_perk3"),
						Messages.get(HeroClass.class, "priestess_perk4"),
						Messages.get(HeroClass.class, "priestess_perk5"),
				};
		}
	}
	
	public boolean locked(){
		//always unlock on debug builds
		if (DeviceCompat.isDebug()) return false;
		
		switch (this){
			case WARRIOR: default:
				return false;
			case MAGE:
				return !Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE);
			case ROGUE:
				return !Badges.isUnlocked(Badges.Badge.UNLOCK_ROGUE);
			case HUNTRESS:
				return !Badges.isUnlocked(Badges.Badge.UNLOCK_HUNTRESS);
			//TODO
			case PRIESTESS:
				return true;
		}
	}
	
	public String unlockMsg() {
		switch (this){
			case WARRIOR: default:
				return "";
			case MAGE:
				return Messages.get(HeroClass.class, "mage_unlock");
			case ROGUE:
				return Messages.get(HeroClass.class, "rogue_unlock");
			case HUNTRESS:
				return Messages.get(HeroClass.class, "huntress_unlock");
		}
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : ROGUE;
	}
}

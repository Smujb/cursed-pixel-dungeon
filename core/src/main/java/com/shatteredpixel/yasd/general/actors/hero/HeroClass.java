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
import com.shatteredpixel.yasd.general.CPDSettings;
import com.shatteredpixel.yasd.general.Challenges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.StoryChapter;
import com.shatteredpixel.yasd.general.items.Amulet;
import com.shatteredpixel.yasd.general.items.BrokenSeal;
import com.shatteredpixel.yasd.general.items.CrimsonFlask;
import com.shatteredpixel.yasd.general.items.DeveloperItem;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindOfWeapon;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.items.TomeOfMastery;
import com.shatteredpixel.yasd.general.items.bags.MagicalHolster;
import com.shatteredpixel.yasd.general.items.bags.PotionBandolier;
import com.shatteredpixel.yasd.general.items.bags.ScrollHolder;
import com.shatteredpixel.yasd.general.items.bags.VelvetPouch;
import com.shatteredpixel.yasd.general.items.food.Food;
import com.shatteredpixel.yasd.general.items.food.SmallRation;
import com.shatteredpixel.yasd.general.items.potions.PotionOfInvisibility;
import com.shatteredpixel.yasd.general.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.yasd.general.items.potions.PotionOfMindVision;
import com.shatteredpixel.yasd.general.items.potions.PotionOfRestoration;
import com.shatteredpixel.yasd.general.items.powers.LuckyBadge;
import com.shatteredpixel.yasd.general.items.relics.DragonPendant;
import com.shatteredpixel.yasd.general.items.relics.dragonpendants.EarthenDragonPendant;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRage;
import com.shatteredpixel.yasd.general.items.shield.ParryingDagger;
import com.shatteredpixel.yasd.general.items.shield.ProphetShield;
import com.shatteredpixel.yasd.general.items.shield.ReflexShield;
import com.shatteredpixel.yasd.general.items.shield.RoundShield;
import com.shatteredpixel.yasd.general.items.shield.SorcererShield;
import com.shatteredpixel.yasd.general.items.wands.WandOfMagicMissile;
import com.shatteredpixel.yasd.general.items.wands.WandOfTransfusion;
import com.shatteredpixel.yasd.general.items.weapon.SpiritBow;
import com.shatteredpixel.yasd.general.items.weapon.melee.Dagger;
import com.shatteredpixel.yasd.general.items.weapon.melee.FoolsBlade;
import com.shatteredpixel.yasd.general.items.weapon.melee.Glove;
import com.shatteredpixel.yasd.general.items.weapon.melee.Greataxe;
import com.shatteredpixel.yasd.general.items.weapon.melee.InscribedKnife;
import com.shatteredpixel.yasd.general.items.weapon.melee.MegaStick;
import com.shatteredpixel.yasd.general.items.weapon.melee.Whip;
import com.shatteredpixel.yasd.general.items.weapon.melee.hybrid.MagesStaff;
import com.shatteredpixel.yasd.general.items.weapon.ranged.ImpactCrossbow;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.HeroSelectScene;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Reflection;

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

		if (CPDSettings.storyChapter() == StoryChapter.SECOND) {
			new Amulet.EmptyAmulet().collect();
		}
	}

	private static void initCommon( Hero hero ) {
		hero.belongings.miscs = new KindofMisc[hero.miscSlots()];

		Item i = new Food();
		if (i.canSpawn()) i.collect();

		if (Dungeon.isChallenged(Challenges.NO_FOOD)){
			new SmallRation().collect();
		}

		if (HeroSelectScene.curWeapon != null) {
			hero.belongings.miscs[0] = (KindOfWeapon) HeroSelectScene.curWeapon.identify();
		}

		new PotionBandolier().collect();
		Dungeon.LimitedDrops.POTION_BANDOLIER.drop();

		new ScrollHolder().collect();
		Dungeon.LimitedDrops.SCROLL_HOLDER.drop();

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();

		new MagicalHolster().collect();
		Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();

		new LuckyBadge().collect();

		new ScrollOfIdentify().identify().collect();

		CrimsonFlask flask = new CrimsonFlask();
		flask.collect();
		Dungeon.quickslot.setSlot(0, flask);

		if (Dungeon.testing) {
			initTest(hero);
		}
	}

	public static void initTest(Hero hero) {
		new TomeOfMastery().collect();
		new MegaStick().identify().collect();
		new DeveloperItem().collect();
		new FoolsBlade().identify().collect();

		for (Class<?> itemClass : Generator.Category.RING.classes) {
			Item item = (Item) Reflection.newInstance(itemClass);
			if (item != null) {
				item.random();
				item.uncurse();
				item.identify()./*level(59).*/collect();
			}
		}
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
		if (hero.belongings.miscs[0] == null) {
			hero.belongings.miscs[0] = (KindofMisc) new Greataxe().identify();
		}
		RoundShield shield = (RoundShield) new RoundShield().identify();
		hero.belongings.miscs[1] = shield;
		Dungeon.quickslot.setSlot(1, shield);

		ImpactCrossbow crossbow = (ImpactCrossbow) new ImpactCrossbow().identify();
		hero.belongings.miscs[2] = crossbow;
		Dungeon.quickslot.setSlot(2, crossbow);

		new BrokenSeal().collect();
		
		new PotionOfRestoration().identify();
		new ScrollOfRage().identify();
		hero.setResilience(3);
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff;

		staff = (MagesStaff) new MagesStaff(new WandOfMagicMissile()).identify();

		if (hero.belongings.miscs[0] == null) {
			hero.belongings.miscs[0] = staff;
		} else {
			staff.collect();
		}

		Dungeon.quickslot.setSlot(1, staff.getWand());

		SorcererShield shield = (SorcererShield) new SorcererShield().identify();
		hero.belongings.miscs[1] = shield;
		Dungeon.quickslot.setSlot(1, shield);

		new PotionOfLiquidFlame().identify();
		hero.setFocus(3);
	}

	private static void initRogue( Hero hero ) {
		if (hero.belongings.miscs[0] == null) {
			hero.belongings.miscs[0] = (KindofMisc) new Dagger().identify();
		}

		Whip whip = (Whip) new Whip().identify();
		hero.belongings.miscs[1] = whip;
		Dungeon.quickslot.setSlot(1, whip);

		ParryingDagger dagger = (ParryingDagger) new ParryingDagger().identify();
		hero.belongings.miscs[2] = dagger;
		Dungeon.quickslot.setSlot(2, dagger);
		
		new ScrollOfMagicMapping().identify();
		new PotionOfInvisibility().identify();
		hero.setExecution(3);
	}

	private static void initHuntress( Hero hero ) {

		if (hero.belongings.miscs[0] == null) {
			hero.belongings.miscs[0] = (KindofMisc) new Glove().identify();
		}

		SpiritBow bow = (SpiritBow) new SpiritBow().identify();
		hero.belongings.miscs[1] = bow;
		Dungeon.quickslot.setSlot(1, bow);

		ReflexShield shield = (ReflexShield) new ReflexShield().identify();
		hero.belongings.miscs[2] = shield;
		Dungeon.quickslot.setSlot(2, shield);
		
		new PotionOfMindVision().identify();
		new ScrollOfLullaby().identify();

		hero.setAssault(3);
	}

	private static void initPriestess( Hero hero ) {

		InscribedKnife knife = (InscribedKnife) new InscribedKnife().identify();
		if (hero.belongings.miscs[0] == null) {
			hero.belongings.miscs[0] = knife;
		} else {
			knife.collect();
		}

		DragonPendant pendant = (DragonPendant) new EarthenDragonPendant().identify();
		hero.belongings.miscs[1] = pendant;
		Dungeon.quickslot.setSlot(1, pendant);

		ProphetShield shield = (ProphetShield) new ProphetShield().identify();
		hero.belongings.miscs[2] = shield;
		Dungeon.quickslot.setSlot(2, shield);

		WandOfTransfusion wand = (WandOfTransfusion) new WandOfTransfusion().identify();
		hero.belongings.miscs[3] = wand;
		Dungeon.quickslot.setSlot(3, wand);

		new PotionOfMindVision().identify();
		new ScrollOfLullaby().identify();

		hero.setSupport(3);
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

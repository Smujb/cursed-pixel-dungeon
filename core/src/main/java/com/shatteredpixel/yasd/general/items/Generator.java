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

package com.shatteredpixel.yasd.general.items;

import com.shatteredpixel.yasd.general.items.bags.Bag;
import com.shatteredpixel.yasd.general.items.food.Food;
import com.shatteredpixel.yasd.general.items.food.MysteryMeat;
import com.shatteredpixel.yasd.general.items.food.Pasty;
import com.shatteredpixel.yasd.general.items.potions.Potion;
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
import com.shatteredpixel.yasd.general.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.yasd.general.items.potions.brews.CausticBrew;
import com.shatteredpixel.yasd.general.items.potions.brews.InfernalBrew;
import com.shatteredpixel.yasd.general.items.potions.brews.ShockingBrew;
import com.shatteredpixel.yasd.general.items.potions.elixirs.Elixir;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfArcaneArmor;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfDragonsBlood;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfIcyTouch;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfToxicEssence;
import com.shatteredpixel.yasd.general.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.yasd.general.items.potions.exotic.PotionOfAdrenalineSurge;
import com.shatteredpixel.yasd.general.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.yasd.general.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.yasd.general.items.potions.exotic.PotionOfDragonsBreath;
import com.shatteredpixel.yasd.general.items.potions.exotic.PotionOfEarthenArmor;
import com.shatteredpixel.yasd.general.items.potions.exotic.PotionOfHolyFuror;
import com.shatteredpixel.yasd.general.items.potions.exotic.PotionOfMagicalSight;
import com.shatteredpixel.yasd.general.items.potions.exotic.PotionOfShielding;
import com.shatteredpixel.yasd.general.items.potions.exotic.PotionOfShroudingFog;
import com.shatteredpixel.yasd.general.items.potions.exotic.PotionOfSnapFreeze;
import com.shatteredpixel.yasd.general.items.potions.exotic.PotionOfStamina;
import com.shatteredpixel.yasd.general.items.potions.exotic.PotionOfStormClouds;
import com.shatteredpixel.yasd.general.items.powers.Alchemy;
import com.shatteredpixel.yasd.general.items.powers.Blink;
import com.shatteredpixel.yasd.general.items.powers.BubbleShield;
import com.shatteredpixel.yasd.general.items.powers.Energize;
import com.shatteredpixel.yasd.general.items.powers.HeroicLeap;
import com.shatteredpixel.yasd.general.items.powers.LuckyBadge;
import com.shatteredpixel.yasd.general.items.powers.MoltenEarth;
import com.shatteredpixel.yasd.general.items.powers.PoisonBurst;
import com.shatteredpixel.yasd.general.items.powers.Power;
import com.shatteredpixel.yasd.general.items.powers.RaiseDead;
import com.shatteredpixel.yasd.general.items.powers.SmokeBomb;
import com.shatteredpixel.yasd.general.items.powers.SpectralBlades;
import com.shatteredpixel.yasd.general.items.powers.Surprise;
import com.shatteredpixel.yasd.general.items.powers.Telekinesis;
import com.shatteredpixel.yasd.general.items.powers.WaterPump;
import com.shatteredpixel.yasd.general.items.relics.CupOfSuffering;
import com.shatteredpixel.yasd.general.items.relics.GarbOfRetribution;
import com.shatteredpixel.yasd.general.items.relics.KarahBracelet;
import com.shatteredpixel.yasd.general.items.relics.LloydsBeacon;
import com.shatteredpixel.yasd.general.items.relics.Relic;
import com.shatteredpixel.yasd.general.items.relics.WarpedPrayerBook;
import com.shatteredpixel.yasd.general.items.relics.dragonpendants.AirDragonPendant;
import com.shatteredpixel.yasd.general.items.relics.dragonpendants.EarthenDragonPendant;
import com.shatteredpixel.yasd.general.items.relics.dragonpendants.FireDragonPendant;
import com.shatteredpixel.yasd.general.items.relics.dragonpendants.WaterDragonPendant;
import com.shatteredpixel.yasd.general.items.rings.Ring;
import com.shatteredpixel.yasd.general.items.rings.RingOfAssault;
import com.shatteredpixel.yasd.general.items.rings.RingOfElements;
import com.shatteredpixel.yasd.general.items.rings.RingOfExecution;
import com.shatteredpixel.yasd.general.items.rings.RingOfFaithAndPower;
import com.shatteredpixel.yasd.general.items.rings.RingOfFocus;
import com.shatteredpixel.yasd.general.items.rings.RingOfFuror;
import com.shatteredpixel.yasd.general.items.rings.RingOfHaste;
import com.shatteredpixel.yasd.general.items.rings.RingOfResilience;
import com.shatteredpixel.yasd.general.items.rings.RingOfSupport;
import com.shatteredpixel.yasd.general.items.rings.RingOfTenacity;
import com.shatteredpixel.yasd.general.items.rings.RingOfWealth;
import com.shatteredpixel.yasd.general.items.scrolls.Scroll;
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
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfAffection;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfAntiMagic;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfConfusion;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfDivination;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfForesight;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfMysticalEnergy;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfPassage;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfPetrification;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfPolymorph;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfPrismaticImage;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.yasd.general.items.shield.BarbedShield;
import com.shatteredpixel.yasd.general.items.shield.GreatShield;
import com.shatteredpixel.yasd.general.items.shield.HeroShield;
import com.shatteredpixel.yasd.general.items.shield.LaceratingShield;
import com.shatteredpixel.yasd.general.items.shield.LightShield;
import com.shatteredpixel.yasd.general.items.shield.MidasShield;
import com.shatteredpixel.yasd.general.items.shield.PanicShield;
import com.shatteredpixel.yasd.general.items.shield.ParryingDagger;
import com.shatteredpixel.yasd.general.items.shield.PestilentShield;
import com.shatteredpixel.yasd.general.items.shield.ProphetShield;
import com.shatteredpixel.yasd.general.items.shield.ReflexShield;
import com.shatteredpixel.yasd.general.items.shield.ReinforcedShield;
import com.shatteredpixel.yasd.general.items.shield.RoundShield;
import com.shatteredpixel.yasd.general.items.shield.RunicShield;
import com.shatteredpixel.yasd.general.items.shield.Shield;
import com.shatteredpixel.yasd.general.items.shield.SorcererShield;
import com.shatteredpixel.yasd.general.items.shield.SpiritualShield;
import com.shatteredpixel.yasd.general.items.shield.SwiftShield;
import com.shatteredpixel.yasd.general.items.shield.TitanShield;
import com.shatteredpixel.yasd.general.items.shield.WarpShield;
import com.shatteredpixel.yasd.general.items.shield.WoodenShield;
import com.shatteredpixel.yasd.general.items.spells.Alchemize;
import com.shatteredpixel.yasd.general.items.spells.AquaBlast;
import com.shatteredpixel.yasd.general.items.spells.CurseInfusion;
import com.shatteredpixel.yasd.general.items.spells.FeatherFall;
import com.shatteredpixel.yasd.general.items.spells.MagicalInfusion;
import com.shatteredpixel.yasd.general.items.spells.MagicalPorter;
import com.shatteredpixel.yasd.general.items.spells.PhaseShift;
import com.shatteredpixel.yasd.general.items.spells.ReclaimTrap;
import com.shatteredpixel.yasd.general.items.spells.Recycle;
import com.shatteredpixel.yasd.general.items.spells.Spell;
import com.shatteredpixel.yasd.general.items.spells.WildEnergy;
import com.shatteredpixel.yasd.general.items.stones.Runestone;
import com.shatteredpixel.yasd.general.items.stones.StoneOfAffection;
import com.shatteredpixel.yasd.general.items.stones.StoneOfAugmentation;
import com.shatteredpixel.yasd.general.items.stones.StoneOfBlast;
import com.shatteredpixel.yasd.general.items.stones.StoneOfBlink;
import com.shatteredpixel.yasd.general.items.stones.StoneOfClairvoyance;
import com.shatteredpixel.yasd.general.items.stones.StoneOfDeepenedSleep;
import com.shatteredpixel.yasd.general.items.stones.StoneOfDisarming;
import com.shatteredpixel.yasd.general.items.stones.StoneOfEnchantment;
import com.shatteredpixel.yasd.general.items.stones.StoneOfFlock;
import com.shatteredpixel.yasd.general.items.stones.StoneOfIntuition;
import com.shatteredpixel.yasd.general.items.stones.StoneOfProtection;
import com.shatteredpixel.yasd.general.items.stones.StoneOfShock;
import com.shatteredpixel.yasd.general.items.wands.ChaosWand;
import com.shatteredpixel.yasd.general.items.wands.Wand;
import com.shatteredpixel.yasd.general.items.wands.WandOfAcid;
import com.shatteredpixel.yasd.general.items.wands.WandOfBinding;
import com.shatteredpixel.yasd.general.items.wands.WandOfBlastWave;
import com.shatteredpixel.yasd.general.items.wands.WandOfCorrosion;
import com.shatteredpixel.yasd.general.items.wands.WandOfCorruption;
import com.shatteredpixel.yasd.general.items.wands.WandOfDamnation;
import com.shatteredpixel.yasd.general.items.wands.WandOfDarkness;
import com.shatteredpixel.yasd.general.items.wands.WandOfDemonicMissile;
import com.shatteredpixel.yasd.general.items.wands.WandOfDisintegration;
import com.shatteredpixel.yasd.general.items.wands.WandOfFireblast;
import com.shatteredpixel.yasd.general.items.wands.WandOfFlow;
import com.shatteredpixel.yasd.general.items.wands.WandOfFrost;
import com.shatteredpixel.yasd.general.items.wands.WandOfLifeDrain;
import com.shatteredpixel.yasd.general.items.wands.WandOfLightning;
import com.shatteredpixel.yasd.general.items.wands.WandOfLivingEarth;
import com.shatteredpixel.yasd.general.items.wands.WandOfMagicMissile;
import com.shatteredpixel.yasd.general.items.wands.WandOfPlasmaBolt;
import com.shatteredpixel.yasd.general.items.wands.WandOfPrismaticLight;
import com.shatteredpixel.yasd.general.items.wands.WandOfRegrowth;
import com.shatteredpixel.yasd.general.items.wands.WandOfShadowEmbrace;
import com.shatteredpixel.yasd.general.items.wands.WandOfThornvines;
import com.shatteredpixel.yasd.general.items.wands.WandOfTransfusion;
import com.shatteredpixel.yasd.general.items.wands.WandOfVoltage;
import com.shatteredpixel.yasd.general.items.wands.WandOfWarding;
import com.shatteredpixel.yasd.general.items.weapon.melee.Arachia;
import com.shatteredpixel.yasd.general.items.weapon.melee.Axe;
import com.shatteredpixel.yasd.general.items.weapon.melee.ButchersKnife;
import com.shatteredpixel.yasd.general.items.weapon.melee.ChainWhip;
import com.shatteredpixel.yasd.general.items.weapon.melee.Dagger;
import com.shatteredpixel.yasd.general.items.weapon.melee.Estoc;
import com.shatteredpixel.yasd.general.items.weapon.melee.Falchion;
import com.shatteredpixel.yasd.general.items.weapon.melee.Flail;
import com.shatteredpixel.yasd.general.items.weapon.melee.FoolsBlade;
import com.shatteredpixel.yasd.general.items.weapon.melee.Glove;
import com.shatteredpixel.yasd.general.items.weapon.melee.Greataxe;
import com.shatteredpixel.yasd.general.items.weapon.melee.Greatsword;
import com.shatteredpixel.yasd.general.items.weapon.melee.HeroSword;
import com.shatteredpixel.yasd.general.items.weapon.melee.Hoe;
import com.shatteredpixel.yasd.general.items.weapon.melee.Katana;
import com.shatteredpixel.yasd.general.items.weapon.melee.Mace;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.items.weapon.melee.MidnightCutlass;
import com.shatteredpixel.yasd.general.items.weapon.melee.MundaneSword;
import com.shatteredpixel.yasd.general.items.weapon.melee.Pitchfork;
import com.shatteredpixel.yasd.general.items.weapon.melee.Polearm;
import com.shatteredpixel.yasd.general.items.weapon.melee.Rapier;
import com.shatteredpixel.yasd.general.items.weapon.melee.RoyalHalberd;
import com.shatteredpixel.yasd.general.items.weapon.melee.RunicBlade;
import com.shatteredpixel.yasd.general.items.weapon.melee.Scimitar;
import com.shatteredpixel.yasd.general.items.weapon.melee.Sickle;
import com.shatteredpixel.yasd.general.items.weapon.melee.Staff;
import com.shatteredpixel.yasd.general.items.weapon.melee.Sword;
import com.shatteredpixel.yasd.general.items.weapon.melee.Tachi;
import com.shatteredpixel.yasd.general.items.weapon.melee.Wakizashi;
import com.shatteredpixel.yasd.general.items.weapon.melee.Whip;
import com.shatteredpixel.yasd.general.items.weapon.melee.hybrid.MagesStaff;
import com.shatteredpixel.yasd.general.items.weapon.ranged.Bow;
import com.shatteredpixel.yasd.general.items.weapon.ranged.CrescentMoonGreatbow;
import com.shatteredpixel.yasd.general.items.weapon.ranged.Crossbow;
import com.shatteredpixel.yasd.general.items.weapon.ranged.Firearm;
import com.shatteredpixel.yasd.general.items.weapon.ranged.HeavyPistol;
import com.shatteredpixel.yasd.general.items.weapon.ranged.ImpactCrossbow;
import com.shatteredpixel.yasd.general.items.weapon.ranged.Longbow;
import com.shatteredpixel.yasd.general.items.weapon.ranged.MarksmansBow;
import com.shatteredpixel.yasd.general.items.weapon.ranged.OldSunCrossbow;
import com.shatteredpixel.yasd.general.items.weapon.ranged.PrecisionRifle;
import com.shatteredpixel.yasd.general.items.weapon.ranged.RangedWeapon;
import com.shatteredpixel.yasd.general.items.weapon.ranged.ShredderCrossbow;
import com.shatteredpixel.yasd.general.plants.Blindweed;
import com.shatteredpixel.yasd.general.plants.Dreamfoil;
import com.shatteredpixel.yasd.general.plants.Earthroot;
import com.shatteredpixel.yasd.general.plants.Fadeleaf;
import com.shatteredpixel.yasd.general.plants.Firebloom;
import com.shatteredpixel.yasd.general.plants.Icecap;
import com.shatteredpixel.yasd.general.plants.Plant;
import com.shatteredpixel.yasd.general.plants.Rotberry;
import com.shatteredpixel.yasd.general.plants.Sorrowmoss;
import com.shatteredpixel.yasd.general.plants.Starflower;
import com.shatteredpixel.yasd.general.plants.Stormvine;
import com.shatteredpixel.yasd.general.plants.Sungrass;
import com.shatteredpixel.yasd.general.plants.Swiftthistle;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Generator {

	public enum Category {
		SHIELD			( 3,    Shield.class ),
		WEAPON			( 3,    MeleeWeapon.class),
		WAND			( 3,    Wand.class ),
		RANGED 			( 3, 	  RangedWeapon.class),
		RELIC			( 3,    Relic.class ),

		RING	( 1,    Ring.class ),

		FOOD	( 0,    Food.class ),

		POTION	( 20,   Potion.class ),
		POTION_EXOTIC	( 1,   ExoticPotion.class ),
		ELIXIR( 1,   Elixir.class ),
		SEED	( 0,    Plant.Seed.class ), //dropped by grass

		SCROLL	( 20,   Scroll.class ),
		SPELL ( 0,   Spell.class ),
		SCROLL_EXOTIC (1,   ExoticScroll.class),
		STONE   ( 2,    Runestone.class),

		POWER (0, Power.class),

		GOLD	( 18,   Gold.class );

		public Class<?>[] classes;

		//some item types use a deck-based system, where the probs decrement as items are picked
		// until they are all 0, and then they reset. Those generator classes should define
		// defaultProbs. If defaultProbs is null then a deck system isn't used.
		//Artifacts in particular don't reset, no duplicates!
		public float[] probs;
		public float[] defaultProbs = null;

		public float prob;
		public Class<? extends Item> superClass;

		private Category( float prob, Class<? extends Item> superClass ) {
			this.prob = prob;
			this.superClass = superClass;
		}

		public static int order( Item item ) {
			for (int i=0; i < values().length; i++) {
				if (values()[i].superClass.isInstance( item )) {
					return i;
				}
			}

			return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
		}

		static {
			GOLD.classes = new Class<?>[]{
					Gold.class};
			GOLD.probs = new float[]{1};

			POWER.classes = new Class<?>[] {
					Alchemy.class,
					Blink.class,
					BubbleShield.class,
					Energize.class,
					HeroicLeap.class,
					LuckyBadge.class,
					MoltenEarth.class,
					PoisonBurst.class,
					RaiseDead.class,
					SmokeBomb.class,
					SpectralBlades.class,
					Surprise.class,
					Telekinesis.class,
					WaterPump.class
			};

			POWER.probs = new float[] {
					1,
					1,
					1,
					1,
					0,
					0,
					0,
					1,
					0,
					0,
					0,
					1,
					1,
					1
			};

			POTION.classes = new Class<?>[]{
					PotionOfMana.class,
					PotionOfRestoration.class,
					PotionOfMindVision.class,
					PotionOfFrost.class,
					PotionOfLiquidFlame.class,
					PotionOfToxicGas.class,
					PotionOfHaste.class,
					PotionOfInvisibility.class,
					PotionOfLevitation.class,
					PotionOfParalyticGas.class,
					PotionOfPurity.class,
					PotionOfExperience.class};
			POTION.defaultProbs = new float[]{3, 6, 4, 3, 3, 3, 2, 2, 2, 2, 2, 0};
			POTION.probs = POTION.defaultProbs.clone();

			POTION_EXOTIC.classes = new Class<?>[]{
					PotionOfAdrenalineSurge.class, //2 drop every chapter, see Dungeon.posNeeded()
					PotionOfShielding.class,
					PotionOfMagicalSight.class,
					PotionOfSnapFreeze.class,
					PotionOfDragonsBreath.class,
					PotionOfCorrosiveGas.class,
					PotionOfStamina.class,
					PotionOfShroudingFog.class,
					PotionOfStormClouds.class,
					PotionOfEarthenArmor.class,
					PotionOfCleansing.class,
					PotionOfHolyFuror.class};
			POTION_EXOTIC.defaultProbs = new float[]{ 2, 6, 4, 3, 3, 3, 2, 2, 2, 2, 2, 1 };
			POTION_EXOTIC.probs = POTION_EXOTIC.defaultProbs.clone();

			ELIXIR.classes = new Class<?>[] {
					BlizzardBrew.class,
					CausticBrew.class,
					InfernalBrew.class,
					ShockingBrew.class,
					ElixirOfAquaticRejuvenation.class,
					ElixirOfArcaneArmor.class,
					ElixirOfDragonsBlood.class,
					ElixirOfHoneyedHealing.class,
					ElixirOfIcyTouch.class,
					ElixirOfMight.class,
					ElixirOfToxicEssence.class
			};

			ELIXIR.defaultProbs = new float[] {1,1,1,1,1,1,1,1,1,1,1};
			ELIXIR.probs = ELIXIR.defaultProbs.clone();

			SEED.classes = new Class<?>[]{
					Rotberry.Seed.class, //quest item
					Sungrass.Seed.class,
					Fadeleaf.Seed.class,
					Icecap.Seed.class,
					Firebloom.Seed.class,
					Sorrowmoss.Seed.class,
					Stormvine.Seed.class,
					Sungrass.Seed.class,
					Swiftthistle.Seed.class,
					Blindweed.Seed.class,
					Stormvine.Seed.class,
					Earthroot.Seed.class,
					Dreamfoil.Seed.class,
					Starflower.Seed.class};
			SEED.defaultProbs = new float[]{ 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 2 };
			SEED.probs = SEED.defaultProbs.clone();

			SPELL.classes = new Class<?>[]{
					CurseInfusion.class,
					MagicalInfusion.class,
					MagicalPorter.class,
					PhaseShift.class,
					WildEnergy.class,
					AquaBlast.class,
					FeatherFall.class,
					ReclaimTrap.class,
					Alchemize.class,
					Recycle.class
			};
			SPELL.defaultProbs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			SPELL.probs = SPELL.defaultProbs.clone();

			SCROLL.classes = new Class<?>[]{
					ScrollOfGreed.class,
					ScrollOfIdentify.class,
					ScrollOfRemoveCurse.class,
					ScrollOfMirrorImage.class,
					ScrollOfRecharging.class,
					ScrollOfTeleportation.class,
					ScrollOfLullaby.class,
					ScrollOfMagicMapping.class,
					ScrollOfRage.class,
					ScrollOfRetribution.class,
					ScrollOfTerror.class,
					ScrollOfTransmutation.class
			};
			SCROLL.defaultProbs = new float[]{1, 6, 4, 3, 3, 3, 2, 2, 2, 2, 2, 1};
			SCROLL.probs = SCROLL.defaultProbs.clone();

			SCROLL_EXOTIC.classes = new Class<?>[]{
					ScrollOfEnchantment.class,
					ScrollOfDivination.class,
					ScrollOfAntiMagic.class,
					ScrollOfPrismaticImage.class,
					ScrollOfMysticalEnergy.class,
					ScrollOfPassage.class,
					ScrollOfAffection.class,
					ScrollOfForesight.class,
					ScrollOfConfusion.class,
					ScrollOfPsionicBlast.class,
					ScrollOfPetrification.class,
					ScrollOfPolymorph.class
			};
			SCROLL_EXOTIC.defaultProbs = SCROLL_EXOTIC.probs = SCROLL.defaultProbs.clone();

			STONE.classes = new Class<?>[]{
					StoneOfEnchantment.class,   //1 drops per chapter, can rarely find more
					StoneOfIntuition.class,     //1 additional stone is also dropped on floors 1-3
					StoneOfDisarming.class,
					StoneOfFlock.class,
					StoneOfShock.class,
					StoneOfBlink.class,
					StoneOfDeepenedSleep.class,
					StoneOfClairvoyance.class,
					StoneOfProtection.class,
					StoneOfBlast.class,
					StoneOfAffection.class,
					StoneOfAugmentation.class  //1 is sold in each shop
			};
			STONE.defaultProbs = new float[]{ 0, 5, 5, 5, 5, 5, 5, 5, 0, 5, 5, 0 };
			STONE.probs = STONE.defaultProbs.clone();

			WAND.classes = new Class<?>[]{
					WandOfMagicMissile.class,
					WandOfLightning.class,
					WandOfDisintegration.class,
					WandOfFireblast.class,
					WandOfCorrosion.class,
					WandOfBlastWave.class,
					WandOfLivingEarth.class,
					WandOfFrost.class,
					WandOfPrismaticLight.class,
					WandOfWarding.class,
					WandOfTransfusion.class,
					WandOfCorruption.class,
					WandOfRegrowth.class,
					WandOfLifeDrain.class,
					WandOfAcid.class,
					WandOfDamnation.class,
					WandOfThornvines.class,
					WandOfPlasmaBolt.class,
					WandOfFlow.class,
					WandOfDarkness.class,
					WandOfVoltage.class,
					WandOfBinding.class,
					WandOfDemonicMissile.class,
					WandOfShadowEmbrace.class,
					ChaosWand.class
			};
			WAND.probs = new float[]{ 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 4, 3, 2, 3, 2, 2, 8, 1 };

			//see generator.randomWeapon
			WEAPON.classes = new Class<?>[]{
					Staff.class,
					Glove.class,
					MagesStaff.class,
					Scimitar.class,
					Katana.class,
					Whip.class,
					Axe.class,
					Flail.class,
					RunicBlade.class,
					Dagger.class,
					Sword.class,
					Mace.class,
					Polearm.class,
					Greataxe.class,
					ChainWhip.class,
					Sickle.class,
					Hoe.class,
					ButchersKnife.class,
					Rapier.class,
					RoyalHalberd.class,
					MundaneSword.class,
					Pitchfork.class,
					HeroSword.class,
					Estoc.class,
					Falchion.class,
					Wakizashi.class,
					Tachi.class,
					MidnightCutlass.class,
					Arachia.class,
					FoolsBlade.class,
					Greatsword.class

			};
			WEAPON.probs = new float[]{ 1, 1, 0, 4, 4, 4, 6, 5, 5, 4, 4, 6, 4, 3, 3, 2, 2, 3, 1, 1, 4, 2, 2, 2, 2, 3, 2, 10, 3, 10, 6 };

			SHIELD.classes = new Class<?>[] {
					RoundShield.class,
					SpiritualShield.class,
					WoodenShield.class,
					GreatShield.class,
					PanicShield.class,
					SwiftShield.class,
					WarpShield.class,
					LightShield.class,
					BarbedShield.class,
					ParryingDagger.class,
					MidasShield.class,
					LaceratingShield.class,
					HeroShield.class,
					ReinforcedShield.class,
					RunicShield.class,
					PestilentShield.class,
					SorcererShield.class,
					TitanShield.class,
					ReflexShield.class,
					ProphetShield.class
			};
			SHIELD.probs = new float[] { 3, 2, 1, 2, 2, 2, 3, 2, 2, 3, 1, 2, 2, 3, 1, 2, 3, 1, 1, 3 };

			RANGED.classes = new Class<?>[] {
					Bow.class,
					Firearm.class,
					Crossbow.class,
					MarksmansBow.class,
					ShredderCrossbow.class,
					ImpactCrossbow.class,
					Longbow.class,
					PrecisionRifle.class,
					HeavyPistol.class,
					CrescentMoonGreatbow.class,
					OldSunCrossbow.class
			};
			RANGED.probs = new float[]{3, 3, 3, 2, 1, 3, 2, 2, 2, 4, 1};

			RELIC.classes = new Class<?>[]{
					com.shatteredpixel.yasd.general.items.relics.CloakOfShadows.class,
					CupOfSuffering.class,
					com.shatteredpixel.yasd.general.items.relics.EtherealChains.class,
					GarbOfRetribution.class,
					WarpedPrayerBook.class,//TODO add functionality
					com.shatteredpixel.yasd.general.items.relics.SandalsOfNature.class,
					LloydsBeacon.class,
					AirDragonPendant.class,
					FireDragonPendant.class,
					WaterDragonPendant.class,
					EarthenDragonPendant.class,
					KarahBracelet.class
			};
			RELIC.probs = new float[]{
					1,
					1,
					1,
					1,
					0,
					1,
					1,
					1,
					1,
					1,
					1,
					1
			};

			FOOD.classes = new Class<?>[]{
					Food.class,
					Pasty.class,
					MysteryMeat.class};
			FOOD.probs = new float[]{4, 1, 0};

			RING.classes = new Class<?>[]{
					RingOfResilience.class,
					RingOfSupport.class,
					RingOfElements.class,
					RingOfAssault.class,
					RingOfFuror.class,
					RingOfHaste.class,
					RingOfFocus.class,
					RingOfExecution.class,
					RingOfFaithAndPower.class,
					RingOfTenacity.class,
					RingOfWealth.class};
			RING.defaultProbs = new float[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
			RING.probs = RING.defaultProbs.clone();
		}
	}

	private static final String CATEGORY_PROBS = "_probs";

	private static HashMap<Category,Float> categoryProbs = new LinkedHashMap<>();

	public static void reset() {
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );
			if (cat.defaultProbs != null) cat.probs = cat.defaultProbs.clone();
		}
	}

	public static void reset(Category cat){
		cat.probs = cat.defaultProbs.clone();
	}

	public static Item random() {
		Category cat = Random.chances( categoryProbs );
		do {
			reset();
			cat = Random.chances( categoryProbs );
		} while (cat == null);
		categoryProbs.put( cat, categoryProbs.get( cat ) - 1);
		return random( cat );
	}

	public static Item random( Category cat ) {
		if (cat == null) return random();
		switch (cat) {
			case SHIELD:
				return randomShield();
			case WEAPON:
				return randomWeapon();
			default:
				if (cat.probs == null) {
					cat.probs = cat.defaultProbs;
				}
				int i = Random.chances(cat.probs);
				if (i == -1) {
					reset(cat);
					i = Random.chances(cat.probs);
				}
				if (cat.defaultProbs != null) cat.probs[i]--;
				return ((Item) Reflection.newInstance(cat.classes[i])).random();
		}
	}

	//overrides any deck systems and always uses default probs
	public static Item randomUsingDefaults( Category cat ){
		if (cat.defaultProbs == null) {
			return random(cat); //currently covers weapons/armor/missiles
		} else {
			return ((Item) Reflection.newInstance(cat.classes[Random.chances(cat.defaultProbs)])).random();
		}
	}

	public static Item random( Class<? extends Item> cl ) {
		return Reflection.newInstance(cl).random();
	}

	@NotNull
	public static com.shatteredpixel.yasd.general.items.shield.Shield randomShield() {
		com.shatteredpixel.yasd.general.items.shield.Shield s = (com.shatteredpixel.yasd.general.items.shield.Shield) Reflection.newInstance(Category.SHIELD.classes[Random.chances(Category.SHIELD.probs)]);
		s.random();
		return s;
	}

	public static MeleeWeapon randomWeapon() {
		MeleeWeapon w = (MeleeWeapon) Reflection.newInstance(Category.WEAPON.classes[Random.chances( Category.WEAPON.probs )]);
		w.random();
		return w;
	}

	public static RangedWeapon randomRanged() {
		return (RangedWeapon) random(Category.RANGED);
	}

	private static final String GENERAL_PROBS = "general_probs";

	public static void storeInBundle(Bundle bundle) {
		Float[] genProbs = categoryProbs.values().toArray(new Float[0]);
		float[] storeProbs = new float[genProbs.length];
		for (int i = 0; i < storeProbs.length; i++){
			storeProbs[i] = genProbs[i];
		}
		bundle.put( GENERAL_PROBS, storeProbs);

		for (Category cat : Category.values()){
			if (cat.defaultProbs == null) continue;
			boolean needsStore = false;
			for (int i = 0; i < cat.probs.length; i++){
				if (cat.probs[i] != cat.defaultProbs[i]){
					needsStore = true;
					break;
				}
			}

			if (needsStore){
				bundle.put(cat.name().toLowerCase() + CATEGORY_PROBS, cat.probs);
			}
		}
	}

	public static void restoreFromBundle(Bundle bundle) {
		reset();
		Category[] values = Category.values();

		if (bundle.contains(GENERAL_PROBS)){
			float[] probs = bundle.getFloatArray(GENERAL_PROBS);
			for (int i = 0; i < probs.length; i++){
				if (i < values.length) {
					categoryProbs.put(values[i], probs[i]);
				}
			}
		}

		for (Category cat : Category.values()){
			if (bundle.contains(cat.name().toLowerCase() + CATEGORY_PROBS)){
				float[] probs = bundle.getFloatArray(cat.name().toLowerCase() + CATEGORY_PROBS);
				if (cat.defaultProbs != null && probs.length == cat.defaultProbs.length){
					cat.probs = probs;
				}
			}
		}

	}
}

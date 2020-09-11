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

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.items.allies.AirDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.DarkDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.DragonPendant;
import com.shatteredpixel.yasd.general.items.allies.EarthenDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.FireDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.GrassDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.IceDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.PoisonDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.StoneDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.VampiricDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.WaterDragonPendant;
import com.shatteredpixel.yasd.general.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.yasd.general.items.artifacts.Artifact;
import com.shatteredpixel.yasd.general.items.artifacts.CapeOfThorns;
import com.shatteredpixel.yasd.general.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.yasd.general.items.artifacts.CloakOfShadows;
import com.shatteredpixel.yasd.general.items.artifacts.EtherealChains;
import com.shatteredpixel.yasd.general.items.artifacts.HornOfPlenty;
import com.shatteredpixel.yasd.general.items.artifacts.LloydsBeacon;
import com.shatteredpixel.yasd.general.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.yasd.general.items.artifacts.SandalsOfNature;
import com.shatteredpixel.yasd.general.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.yasd.general.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.general.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.yasd.general.items.bags.Bag;
import com.shatteredpixel.yasd.general.items.food.Food;
import com.shatteredpixel.yasd.general.items.food.MysteryMeat;
import com.shatteredpixel.yasd.general.items.food.Pasty;
import com.shatteredpixel.yasd.general.items.potions.Potion;
import com.shatteredpixel.yasd.general.items.potions.PotionOfExperience;
import com.shatteredpixel.yasd.general.items.potions.PotionOfFrost;
import com.shatteredpixel.yasd.general.items.potions.PotionOfHaste;
import com.shatteredpixel.yasd.general.items.potions.PotionOfHealing;
import com.shatteredpixel.yasd.general.items.potions.PotionOfInvisibility;
import com.shatteredpixel.yasd.general.items.potions.PotionOfLevitation;
import com.shatteredpixel.yasd.general.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.yasd.general.items.potions.PotionOfMana;
import com.shatteredpixel.yasd.general.items.potions.PotionOfMindVision;
import com.shatteredpixel.yasd.general.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.yasd.general.items.potions.PotionOfPurity;
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
import com.shatteredpixel.yasd.general.items.rings.Ring;
import com.shatteredpixel.yasd.general.items.rings.RingOfAssault;
import com.shatteredpixel.yasd.general.items.rings.RingOfElements;
import com.shatteredpixel.yasd.general.items.rings.RingOfExecution;
import com.shatteredpixel.yasd.general.items.rings.RingOfFocus;
import com.shatteredpixel.yasd.general.items.rings.RingOfFuror;
import com.shatteredpixel.yasd.general.items.rings.RingOfHaste;
import com.shatteredpixel.yasd.general.items.rings.RingOfResilience;
import com.shatteredpixel.yasd.general.items.rings.RingOfSharpshooting;
import com.shatteredpixel.yasd.general.items.rings.RingOfSupport;
import com.shatteredpixel.yasd.general.items.rings.RingOfTenacity;
import com.shatteredpixel.yasd.general.items.rings.RingOfWealth;
import com.shatteredpixel.yasd.general.items.scrolls.Scroll;
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
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfUpgrade;
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
import com.shatteredpixel.yasd.general.items.shield.LightShield;
import com.shatteredpixel.yasd.general.items.shield.PanicShield;
import com.shatteredpixel.yasd.general.items.shield.ParryShield;
import com.shatteredpixel.yasd.general.items.shield.RoundShield;
import com.shatteredpixel.yasd.general.items.shield.SpiritualShield;
import com.shatteredpixel.yasd.general.items.shield.SwiftShield;
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
import com.shatteredpixel.yasd.general.items.stones.StoneOfRepair;
import com.shatteredpixel.yasd.general.items.stones.StoneOfShock;
import com.shatteredpixel.yasd.general.items.wands.Wand;
import com.shatteredpixel.yasd.general.items.wands.WandOfAcid;
import com.shatteredpixel.yasd.general.items.wands.WandOfBlastWave;
import com.shatteredpixel.yasd.general.items.wands.WandOfCorrosion;
import com.shatteredpixel.yasd.general.items.wands.WandOfCorruption;
import com.shatteredpixel.yasd.general.items.wands.WandOfDamnation;
import com.shatteredpixel.yasd.general.items.wands.WandOfDarkness;
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
import com.shatteredpixel.yasd.general.items.wands.WandOfThornvines;
import com.shatteredpixel.yasd.general.items.wands.WandOfTransfusion;
import com.shatteredpixel.yasd.general.items.wands.WandOfWarding;
import com.shatteredpixel.yasd.general.items.weapon.melee.Axe;
import com.shatteredpixel.yasd.general.items.weapon.melee.Crossbow;
import com.shatteredpixel.yasd.general.items.weapon.melee.Dagger;
import com.shatteredpixel.yasd.general.items.weapon.melee.Flail;
import com.shatteredpixel.yasd.general.items.weapon.melee.Glove;
import com.shatteredpixel.yasd.general.items.weapon.melee.Katana;
import com.shatteredpixel.yasd.general.items.weapon.melee.Mace;
import com.shatteredpixel.yasd.general.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.items.weapon.melee.Polearm;
import com.shatteredpixel.yasd.general.items.weapon.melee.RunicBlade;
import com.shatteredpixel.yasd.general.items.weapon.melee.Scimitar;
import com.shatteredpixel.yasd.general.items.weapon.melee.Staff;
import com.shatteredpixel.yasd.general.items.weapon.melee.Sword;
import com.shatteredpixel.yasd.general.items.weapon.melee.Whip;
import com.shatteredpixel.yasd.general.items.weapon.missiles.Bolas;
import com.shatteredpixel.yasd.general.items.weapon.missiles.FishingSpear;
import com.shatteredpixel.yasd.general.items.weapon.missiles.ForceCube;
import com.shatteredpixel.yasd.general.items.weapon.missiles.HeavyBoomerang;
import com.shatteredpixel.yasd.general.items.weapon.missiles.Javelin;
import com.shatteredpixel.yasd.general.items.weapon.missiles.Kunai;
import com.shatteredpixel.yasd.general.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.yasd.general.items.weapon.missiles.Shuriken;
import com.shatteredpixel.yasd.general.items.weapon.missiles.ThrowingClub;
import com.shatteredpixel.yasd.general.items.weapon.missiles.ThrowingHammer;
import com.shatteredpixel.yasd.general.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.yasd.general.items.weapon.missiles.ThrowingSpear;
import com.shatteredpixel.yasd.general.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.yasd.general.items.weapon.missiles.Tomahawk;
import com.shatteredpixel.yasd.general.items.weapon.missiles.Trident;
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
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Generator {

	public enum Category {
		WEAPON	( 3,    MeleeWeapon.class),
		SHIELD	( 3,    com.shatteredpixel.yasd.general.items.shield.Shield.class ),

		MISSILE ( 2,    MissileWeapon.class ),
		MIS_T1  ( 0,    MissileWeapon.class ),
		MIS_T2  ( 0,    MissileWeapon.class ),
		MIS_T3  ( 0,    MissileWeapon.class ),
		MIS_T4  ( 0,    MissileWeapon.class ),
		MIS_T5  ( 0,    MissileWeapon.class ),


		DRAGON_PENDANT  ( 2,    DragonPendant.class ),

		WAND	( 2,    Wand.class ),
		RING	( 1,    Ring.class ),
		ARTIFACT( 1,    Artifact.class),

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

			DRAGON_PENDANT.classes = new Class<?>[]{
					EarthenDragonPendant.class,
					FireDragonPendant.class,
					IceDragonPendant.class,
					PoisonDragonPendant.class,
					VampiricDragonPendant.class,
					WaterDragonPendant.class,
					AirDragonPendant.class,
					DarkDragonPendant.class,
					GrassDragonPendant.class,
					StoneDragonPendant.class
			};
			DRAGON_PENDANT.probs = new float[]{
					1,
					1,
					1,
					1,
					1,
					1,
					1,
					1,
					1,
					1
			};

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
					PotionOfHealing.class,
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
			POTION.defaultProbs = new float[]{3, 6, 4, 3, 3, 3, 2, 2, 2, 2, 2, 1};
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
					ScrollOfUpgrade.class, //3 drop every chapter, see Dungeon.souNeeded()
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
			SCROLL.defaultProbs = new float[]{0, 6, 4, 3, 3, 3, 2, 2, 2, 2, 2, 1};
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
					StoneOfRepair.class,
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
					WandOfDarkness.class};
			WAND.probs = new float[]{ 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 4, 3, 3 };

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
					Crossbow.class,
					Sword.class,
					Mace.class,
					Polearm.class

			};
			WEAPON.probs = new float[]{ 1, 1, 0, 4, 4, 4, 6, 5, 5, 4, 4, 4, 6, 4 };

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
					ParryShield.class
			};
			SHIELD.probs = new float[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

			//see Generator.randomMissile
			MISSILE.classes = new Class<?>[]{};
			MISSILE.probs = new float[]{};

			MIS_T1.classes = new Class<?>[]{
					ThrowingStone.class,
					ThrowingKnife.class
			};
			MIS_T1.probs = new float[]{ 6, 5 };

			MIS_T2.classes = new Class<?>[]{
					FishingSpear.class,
					ThrowingClub.class,
					Shuriken.class
			};
			MIS_T2.probs = new float[]{ 6, 5, 4 };

			MIS_T3.classes = new Class<?>[]{
					ThrowingSpear.class,
					Kunai.class,
					Bolas.class
			};
			MIS_T3.probs = new float[]{ 6, 5, 4 };

			MIS_T4.classes = new Class<?>[]{
					Javelin.class,
					Tomahawk.class,
					HeavyBoomerang.class
			};
			MIS_T4.probs = new float[]{ 6, 5, 4 };

			MIS_T5.classes = new Class<?>[]{
					Trident.class,
					ThrowingHammer.class,
					ForceCube.class
			};
			MIS_T5.probs = new float[]{ 6, 5, 4 };

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
					RingOfSharpshooting.class,
					RingOfTenacity.class,
					RingOfWealth.class};
			RING.defaultProbs = new float[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
			RING.probs = RING.defaultProbs.clone();

			ARTIFACT.classes = new Class<?>[]{
					CapeOfThorns.class,
					ChaliceOfBlood.class,
					CloakOfShadows.class,
					HornOfPlenty.class,
					MasterThievesArmband.class,
					SandalsOfNature.class,
					TalismanOfForesight.class,
					TimekeepersHourglass.class,
					UnstableSpellbook.class,
					AlchemistsToolkit.class,
					//DriedRose.class,
					LloydsBeacon.class,
					EtherealChains.class
			};
			ARTIFACT.defaultProbs = new float[]{0, 1, 1, 1, 1, 1, 1, 1, 1, 0, /*0,*/ 0, 1};
			ARTIFACT.probs = ARTIFACT.defaultProbs.clone();
		}
	}

	private static final String CATEGORY_PROBS = "_probs";

	private static final float[][] floorSetTierProbs = new float[][] {
			{0, 70, 20,  8,  2},
			{0, 25, 50, 20,  5},
			{0,  0, 40, 50, 10},
			{0,  0, 20, 40, 40},
			{0,  0,  0, 20, 80}
	};

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
			case MISSILE:
				return randomMissile();
			case ARTIFACT:
				Item item = randomArtifact();
				//if we're out of artifacts, return a ring instead.
				return item != null ? item : random(Category.RING);
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

	public static final Category[] misTiers = new Category[]{
			Category.MIS_T1,
			Category.MIS_T2,
			Category.MIS_T3,
			Category.MIS_T4,
			Category.MIS_T5
	};

	public static MissileWeapon randomMissile(){
		return randomMissile(Dungeon.depth / 5);
	}

	public static MissileWeapon randomMissile(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);

		Category c = misTiers[Random.chances(floorSetTierProbs[floorSet])];
		MissileWeapon w = (MissileWeapon)Reflection.newInstance(c.classes[Random.chances(c.probs)]);
		w.random();
		return w;
	}

	//enforces uniqueness of artifacts throughout a run.
	public static Artifact randomArtifact() {

		Category cat = Category.ARTIFACT;
		int i = Random.chances( cat.probs );

		//if no artifacts are left, return null
		if (i == -1){
			return null;
		}

		cat.probs[i]--;
		return (Artifact) Reflection.newInstance((Class<? extends Artifact>) cat.classes[i]).random();

	}

	public static boolean removeArtifact(Class<?extends Artifact> artifact) {
		Category cat = Category.ARTIFACT;
		for (int i = 0; i < cat.classes.length; i++){
			if (cat.classes[i].equals(artifact)) {
				cat.probs[i] = 0;
				return true;
			}
		}
		return false;
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

		//pre-0.8.1
		if (bundle.contains("spawned_artifacts")) {
			for (Class<? extends Artifact> artifact : bundle.getClassArray("spawned_artifacts")) {
				Category cat = Category.ARTIFACT;
				for (int i = 0; i < cat.classes.length; i++) {
					if (cat.classes[i].equals(artifact)) {
						cat.probs[i] = 0;
					}
				}
			}
		}

	}
}

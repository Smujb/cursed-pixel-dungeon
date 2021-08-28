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

package com.shatteredpixel.yasd.general.items.weapon;

import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.MagicImmune;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.Enchantable;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindOfWeapon;
import com.shatteredpixel.yasd.general.items.rings.RingOfFuror;
import com.shatteredpixel.yasd.general.items.weapon.curses.Annoying;
import com.shatteredpixel.yasd.general.items.weapon.curses.Displacing;
import com.shatteredpixel.yasd.general.items.weapon.curses.Exhausting;
import com.shatteredpixel.yasd.general.items.weapon.curses.Fragile;
import com.shatteredpixel.yasd.general.items.weapon.curses.Friendly;
import com.shatteredpixel.yasd.general.items.weapon.curses.Polarized;
import com.shatteredpixel.yasd.general.items.weapon.curses.Sacrificial;
import com.shatteredpixel.yasd.general.items.weapon.curses.Sapping;
import com.shatteredpixel.yasd.general.items.weapon.curses.Thunderous;
import com.shatteredpixel.yasd.general.items.weapon.curses.Wayward;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Blazing;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Blocking;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Blooming;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Chilling;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Corrupting;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Elastic;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Grim;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Lucky;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Projecting;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Shocking;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Unstable;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Vampiric;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

abstract public class Weapon extends KindOfWeapon implements Enchantable {

	{
		usesTargeting = true;
	}

	public float attackDelay = 1f;	// Speed modifier
	public int reach = 1;    // Reach modifier (only applies to melee hits)
	protected float damageFactor = 1f; //Percentage of regular damage that this weapon deals

	public enum Augment {
		SPEED   (0.7f, 0.6667f),
		DAMAGE  (1.5f, 1.6667f),
		NONE	(1.0f, 1.0000f);

		private float damageFactor;
		private float delayFactor;

		Augment(float dmg, float dly){
			damageFactor = dmg;
			delayFactor = dly;
		}

		public int damageFactor(int dmg){
			return Math.round(dmg * damageFactor);
		}

		public float delayFactor(float dly){
			return dly * delayFactor;
		}
	}
	
	public Augment augment = Augment.NONE;
	
	private static final int USES_TO_ID = 20;
	private int usesLeftToID = USES_TO_ID;
	private float availableUsesToID = USES_TO_ID/2f;
	
	public Enchantment enchantment;
	public boolean curseInfusionBonus = false;

	protected float damageFactor() {
		return damageFactor;
	}

	@Override
	public int staminaConsumption() {
		int cons = super.staminaConsumption();
		if (hasEnchant(Exhausting.class, curUser)) {
			cons *= 2;
		}
		return cons;
	}

	@Override
	public int proc( Char attacker, Char defender, int damage ) {
		
		if (enchantment != null && attacker.buff(MagicImmune.class) == null) {
			damage = enchantment.proc( this, attacker, defender, damage );
		}
		
		if (!levelKnown && attacker == Dungeon.hero && availableUsesToID >= 1) {
			availableUsesToID--;
			usesLeftToID--;
			if (usesLeftToID <= 0) {
				identify();
				GLog.positive( Messages.get(Weapon.class, "identify") );
				Badges.validateItemLevelAquired( this );
			}
		}

		return damage;
	}
	
	public void onHeroGainExp( float levelPercent, Hero hero ){
		if (!levelKnown && isEquipped(hero) && availableUsesToID <= USES_TO_ID/2f) {
			//gains enough uses to ID over 0.5 levels
			availableUsesToID = Math.min(USES_TO_ID/2f, availableUsesToID + levelPercent * USES_TO_ID);
		}
	}

	@Override
	public int price() {
		int price = super.price();
		if (hasGoodEnchant()) {
			price *= 1.5;
		}
		return price;
	}

	@Override
	public Item curse() {
		super.curse();
		enchant(Enchantment.randomCurse());
		return this;
	}

	@Override
	public Item uncurse() {
		if (hasCurseEnchant()) {
			enchant(null);
		}
		return super.uncurse();
	}

	@Override
	public void setupEmitters(ItemSprite sprite) {
		super.setupEmitters(sprite);
		Emitter emitter = emitter(sprite);
		if (enchantment != null && !enchantment.curse() && cursedKnown) {
			emitter.pour(Speck.factory(Speck.HALO), 0.15f);
		}
	}

	private static final String USES_LEFT_TO_ID = "uses_left_to_id";
	private static final String AVAILABLE_USES  = "available_uses";
	private static final String ENCHANTMENT	    = "enchantment";
	private static final String CURSE_INFUSION_BONUS = "curse_infusion_bonus";
	private static final String AUGMENT	        = "augment";

	@Override
	public void storeInBundle(  Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( USES_LEFT_TO_ID, usesLeftToID );
		bundle.put( AVAILABLE_USES, availableUsesToID );
		bundle.put( ENCHANTMENT, enchantment );
		bundle.put( CURSE_INFUSION_BONUS, curseInfusionBonus );
		bundle.put( AUGMENT, augment );
	}
	
	@Override
	public void restoreFromBundle(  Bundle bundle ) {
		super.restoreFromBundle( bundle );
		usesLeftToID = bundle.getInt( USES_LEFT_TO_ID );
		availableUsesToID = bundle.getInt( AVAILABLE_USES );
		enchantment = (Enchantment)bundle.get( ENCHANTMENT );
		curseInfusionBonus = bundle.getBoolean( CURSE_INFUSION_BONUS );
		
		//pre-0.7.2 saves
		if (bundle.contains( "unfamiliarity" )){
			usesLeftToID = bundle.getInt( "unfamiliarity" );
			availableUsesToID = USES_TO_ID/2f;
		}
		
		augment = bundle.getEnum(AUGMENT, Augment.class);
	}
	
	@Override
	public void reset() {
		super.reset();
		usesLeftToID = USES_TO_ID;
		availableUsesToID = USES_TO_ID/2f;
	}

	@Override
	public float speedFactor( Char owner ) {

		float DLY = augment.delayFactor(this.attackDelay);

		DLY *= RingOfFuror.attackDelayMultiplier(owner);

		return DLY;
	}

	@Override
	public int reachFactor(Char owner) {
		return hasEnchant(Projecting.class, owner) ? reach +1 : reach;
	}

	@Override
	public int level() {
		return super.level() + (curseInfusionBonus ? Constants.CURSE_INFUSION_BONUS_AMT : 0);
	}
	
	@Override
	public Item upgrade() {
		return upgrade(false);
	}
	
	public Item upgrade(boolean enchant ) {

		if (enchant){
			if (enchantment == null || hasCurseEnchant()){
				enchant(Enchantment.random());
			}
		} else {
			if (hasCurseEnchant()){
				if (Random.Int(3) == 0) enchant(null);
			} else if (level() >= 4 && Random.Float(10) < Math.pow(2, level()-4)){
				enchant(null);
			}
		}
		
		uncurse();
		
		return super.upgrade();
	}


	
	@Override
	public String name() {
		//return enchantment != null && (cursedKnown || !enchantment.curse()) ? enchantment.name( super.name() ) : super.name();
		return Enchantment.getName(this.getClass(), enchantment, cursedKnown);
	}
	
	@Override
	public Item random() {
		//+0: 75% (3/4)
		//+1: 20% (4/20)
		//+2: 5%  (1/20)
		int n = Dungeon.getScaling()/2;
		if (Random.Int(4) == 0) {
			n++;
			if (Random.Int(5) == 0) {
				n++;
			}
		}
		level(n);
		
		//30% chance to be cursed
		//10% chance to be enchanted
		float effectRoll = Random.Float();
		if (effectRoll < 0.7f) {
			enchant(Enchantment.randomCurse());
			curse();
		} else if (effectRoll >= 0.8f){
			enchant();
		}

		return this;
	}

	@Override
	public Weapon enchant( Enchantment ench ) {
		if (ench == null || !ench.curse()) curseInfusionBonus = false;
		enchantment = ench;
		updateQuickslot();
		return this;
	}

	@Override
	public Weapon enchant() {

		Class<? extends Enchantment> oldEnchantment = enchantment != null ? enchantment.getClass() : null;
		Enchantment ench = Enchantment.random( oldEnchantment );

		return enchant( ench );
	}

	@Override
	public boolean hasEnchant(Class<?extends Enchantment> type, Char owner) {
		return enchantment != null && enchantment.getClass() == type && owner.buff(MagicImmune.class) == null;
	}

	@Override
	public Enchantment getEnchantment() {
		return enchantment;
	}

	//these are not used to process specific enchant effects, so magic immune doesn't affect them
	@Override
	public boolean hasGoodEnchant(){
		return enchantment != null && !enchantment.curse();
	}

	@Override
	public boolean hasCurseEnchant(){
		return enchantment != null && enchantment.curse();
	}

	@Override
	public int enchPower() {
		return cursed() ? curseIntensity : level();
	}

	@Override
	public boolean isSimilar(Item item) {
		boolean similar = super.isSimilar(item);
		if (similar && item instanceof Weapon) {
			if (enchantment == null && ((Weapon) item).enchantment == null) {
				return true;
			} else if (enchantment != null && ((Weapon)item).enchantment != null) {
				return enchantment.getClass() == ((Weapon) item).enchantment.getClass();
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return enchantment != null && cursedKnown ? enchantment.glowing() : null;
	}

	public static abstract class Enchantment implements Bundlable {

		//Why is this static when it takes an enchant as an argument? Well, it prevents me doing a null-check every time I want to use it (as enchantment can be null and often is)
		public static String getName(Class<? extends Item> itemClass, Enchantment ench, boolean showEnchant) {
			String name = Messages.get(itemClass, "name");
			if (ench != null && showEnchant) {
				name = ench.name(name);
			}
			return name;
		}
		
		private static final Class<?>[] common = new Class<?>[]{
				Blazing.class, Chilling.class, Kinetic.class, Shocking.class};
		
		private static final Class<?>[] uncommon = new Class<?>[]{
				Blocking.class, Blooming.class, Elastic.class,
				Lucky.class, Projecting.class, Unstable.class};
		
		private static final Class<?>[] rare = new Class<?>[]{
				Corrupting.class, Grim.class, Vampiric.class};
		
		private static final float[] typeChances = new float[]{
				50, //12.5% each
				40, //6.67% each
				10  //3.33% each
		};
		
		private static final Class<?>[] curses = new Class<?>[]{
				Annoying.class, Displacing.class, Exhausting.class, Fragile.class, Sapping.class,
				Sacrificial.class, Wayward.class, Polarized.class, Friendly.class, Thunderous.class
		};
		
			
		public abstract int proc(Enchantable weapon, Char attacker, Char defender, int damage );

		public String name() {
			if (!curse())
				return name( Messages.get(this, "enchant"));
			else
				return name( Messages.get(Item.class, "curse"));
		}

		public String name( String weaponName ) {
			return Messages.get(this, "name", weaponName);
		}

		public String desc() {
			return Messages.get(this, "desc");
		}

		public boolean curse() {
			return false;
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
		}
		
		public abstract ItemSprite.Glowing glowing();
		
		@SuppressWarnings("unchecked")
		public static Enchantment random( Class<? extends Enchantment> ... toIgnore ) {
			switch(Random.chances(typeChances)){
				case 0: default:
					return randomCommon( toIgnore );
				case 1:
					return randomUncommon( toIgnore );
				case 2:
					return randomRare( toIgnore );
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Enchantment randomCommon( Class<? extends Enchantment> ... toIgnore ) {
			ArrayList<Class<?>> enchants = new ArrayList<>(Arrays.asList(common));
			enchants.removeAll(Arrays.asList(toIgnore));
			if (enchants.isEmpty()) {
				return random();
			} else {
				return (Enchantment) Reflection.newInstance(Random.element(enchants));
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Enchantment randomUncommon( Class<? extends Enchantment> ... toIgnore ) {
			ArrayList<Class<?>> enchants = new ArrayList<>(Arrays.asList(uncommon));
			enchants.removeAll(Arrays.asList(toIgnore));
			if (enchants.isEmpty()) {
				return random();
			} else {
				return (Enchantment) Reflection.newInstance(Random.element(enchants));
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Enchantment randomRare( Class<? extends Enchantment> ... toIgnore ) {
			ArrayList<Class<?>> enchants = new ArrayList<>(Arrays.asList(rare));
			enchants.removeAll(Arrays.asList(toIgnore));
			if (enchants.isEmpty()) {
				return random();
			} else {
				return (Enchantment) Reflection.newInstance(Random.element(enchants));
			}
		}

		@SuppressWarnings("unchecked")
		public static Enchantment randomCurse( Class<? extends Enchantment> ... toIgnore ){
			ArrayList<Class<?>> enchants = new ArrayList<>(Arrays.asList(curses));
			enchants.removeAll(Arrays.asList(toIgnore));
			if (enchants.isEmpty()) {
				return random();
			} else {
				return (Enchantment) Reflection.newInstance(Random.element(enchants));
			}
		}
		
	}
}

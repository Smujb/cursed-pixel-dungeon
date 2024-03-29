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

package com.shatteredpixel.yasd.general.items.unused.armor;

import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.MagicImmune;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.BrokenSeal;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.items.unused.armor.curses.AntiEntropy;
import com.shatteredpixel.yasd.general.items.unused.armor.curses.Bulk;
import com.shatteredpixel.yasd.general.items.unused.armor.curses.Corrosion;
import com.shatteredpixel.yasd.general.items.unused.armor.curses.Displacement;
import com.shatteredpixel.yasd.general.items.unused.armor.curses.Explosive;
import com.shatteredpixel.yasd.general.items.unused.armor.curses.Metabolism;
import com.shatteredpixel.yasd.general.items.unused.armor.curses.Multiplicity;
import com.shatteredpixel.yasd.general.items.unused.armor.curses.Overgrowth;
import com.shatteredpixel.yasd.general.items.unused.armor.curses.Stench;
import com.shatteredpixel.yasd.general.items.unused.armor.glyphs.Affection;
import com.shatteredpixel.yasd.general.items.unused.armor.glyphs.AntiMagic;
import com.shatteredpixel.yasd.general.items.unused.armor.glyphs.Brimstone;
import com.shatteredpixel.yasd.general.items.unused.armor.glyphs.Camouflage;
import com.shatteredpixel.yasd.general.items.unused.armor.glyphs.Entanglement;
import com.shatteredpixel.yasd.general.items.unused.armor.glyphs.Flow;
import com.shatteredpixel.yasd.general.items.unused.armor.glyphs.Obfuscation;
import com.shatteredpixel.yasd.general.items.unused.armor.glyphs.Potential;
import com.shatteredpixel.yasd.general.items.unused.armor.glyphs.Repulsion;
import com.shatteredpixel.yasd.general.items.unused.armor.glyphs.Swiftness;
import com.shatteredpixel.yasd.general.items.unused.armor.glyphs.Thorns;
import com.shatteredpixel.yasd.general.items.unused.armor.glyphs.Viscosity;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class Armor extends KindofMisc {
	{
		statScaling = new ArrayList<>(Arrays.asList(Hero.HeroStat.RESILIENCE));
	}

	public float EVA = 1f;
	public float STE = 1f;
	public float speedFactor = 1f;
	public float DRfactor = 1f;
	public float magicalDRFactor = 1f;

	protected static final String AC_DETACH       = "DETACH";
	
	public enum Augment {
		EVASION (2f , -1f),
		DEFENSE (-2f, 1f),
		NONE	(0f   ,  0f);
		
		private float evasionFactor;
		private float defenceFactor;
		
		Augment(float eva, float df){
			evasionFactor = eva;
			defenceFactor = df;
		}
		
		public int evasionFactor(int level){
			return Math.round((2 + level) * evasionFactor);
		}
		
		public int defenseFactor(int level){
			return Math.round((2 + level) * defenceFactor);
		}
	}

	public Augment augment = Augment.NONE;
	
	public Glyph glyph;
	public boolean curseInfusionBonus = false;
	
	private BrokenSeal seal;
	
	private static final int USES_TO_ID = 10;
	private int usesLeftToID = USES_TO_ID;
	private float availableUsesToID = USES_TO_ID/2f;
	
	private static final String USES_LEFT_TO_ID = "uses_left_to_id";
	private static final String AVAILABLE_USES  = "available_uses";
	private static final String GLYPH			= "glyph";
	private static final String CURSE_INFUSION_BONUS = "curse_infusion_bonus";
	private static final String SEAL            = "seal";
	private static final String AUGMENT			= "augment";
	private static final String TIER = "tier";

	@Override
	public void storeInBundle(  Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( USES_LEFT_TO_ID, usesLeftToID );
		bundle.put( AVAILABLE_USES, availableUsesToID );
		bundle.put( GLYPH, glyph );
		bundle.put( CURSE_INFUSION_BONUS, curseInfusionBonus );
		bundle.put( SEAL, seal);
		bundle.put( AUGMENT, augment);
	}

	@Override
	public void restoreFromBundle(  Bundle bundle ) {
		super.restoreFromBundle(bundle);
		usesLeftToID = bundle.getInt( USES_LEFT_TO_ID );
		availableUsesToID = bundle.getInt( AVAILABLE_USES );
		inscribe((Glyph) bundle.get(GLYPH));
		curseInfusionBonus = bundle.getBoolean( CURSE_INFUSION_BONUS );
		seal = (BrokenSeal)bundle.get(SEAL);
		
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
		//armours can be kept in bones between runs, the seal cannot.
		seal = null;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (seal != null) actions.add(AC_DETACH);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_DETACH) && seal != null){
			GLog.info( Messages.get(this, "detach_seal") );
			hero.sprite.operate(hero.pos);
			if (!seal.collect()){
				Dungeon.level.drop(seal, hero.pos);
			}
			seal = null;
		}
	}

	@Override
	public boolean isEquipped(@NotNull Char owner) {
		return owner.belongings.getArmor() == this;
	}

	@Override
	public void activate(Char ch) {
		super.activate(ch);
	}

	public void affixSeal(BrokenSeal seal){
		this.seal = seal;
		/*if (seal.level() > 0){
			//doesn't interact upgrading logic such as affecting curses/glyphs
			level(Math.min(level()+1,3));
			Badges.validateItemLevelAquired(this);
		}*/
		if (isEquipped(Dungeon.hero)){
		}
	}

	@Override
	public boolean doEquip(Hero hero) {
		if (hero.belongings.getArmor() != null) {
			GLog.negative(Messages.get(this, "only_wear_one"));
			return false;
		}
		return super.doEquip(hero);
	}

	public int appearance() {
		return 1;
	}

	public BrokenSeal checkSeal(){
		return seal;
	}

	@Override
	protected float time2equip( Char hero ) {
		return 2 / hero.speed();
	}

	public final int DRMax(){
		return DRMax(power());
	}

	public int DRMax(float lvl){
		return Math.round(lvl * 7 * DRfactor);
	}

	public final int DRMin(){
		return DRMin(power());
	}

	public int DRMin(float lvl){
		return Math.round(lvl);
	}

	public int DRRoll() {
		return DRRoll(power());
	}

	public int DRRoll(float lvl) {
		return Random.NormalIntRange(DRMin(lvl), DRMax(lvl));
	}

	public final int magicalDRMax(){
		return magicalDRMax(power());
	}

	public int magicalDRMax(float lvl){
		return Math.round(lvl * 7 * magicalDRFactor);
	}

	public final int magicalDRMin(){
		return magicalDRMin(power());
	}

	public int magicalDRMin(float lvl){
		return Math.round(lvl);
	}

	public int magicalDRRoll() {
		return magicalDRRoll(power());
	}

	public int magicalDRRoll(float lvl) {
		return Random.NormalIntRange(magicalDRMin(lvl), magicalDRMax(lvl));
	}

	@Override
	public int level() {
		int lvl = super.level();
		if (curseInfusionBonus) {
			lvl += Constants.CURSE_INFUSION_BONUS_AMT;
		}
		if (seal != null) {
			lvl += seal.level();
		}
		return lvl;
	}
	
	@Override
	public Item upgrade() {
		return upgrade( false );
	}
	
	public Item upgrade( boolean inscribe ) {

		if (inscribe && (glyph == null || glyph.curse())) {
			inscribe(Glyph.random());
		} else if (!inscribe && level() >= 4 && Random.Float(10) < Math.pow(2, level() - 4)) {
			inscribe(null);
		}

		uncurse();

		if (seal != null && seal.isUpgradable()) {
			seal.upgrade();
			return this;
		}

		return super.upgrade();
	}
	
	public int proc( Char attacker, Char defender, int damage ) {

		if (glyph != null && defender.buff(MagicImmune.class) == null) {
			damage = glyph.proc( this, attacker, defender, damage );
		}

		if (!levelKnown && defender == Dungeon.hero && availableUsesToID >= 1) {
			availableUsesToID--;
			usesLeftToID--;
			if (usesLeftToID <= 0) {
				identify();
				GLog.positive( Messages.get(this, "identify") );
				Badges.validateItemLevelAquired( this );
			}
		}

		return damage;
	}

	public int magicalProc( Char attacker, Char defender, int damage ) {

		if (glyph != null && defender.buff(MagicImmune.class) == null) {
			damage = glyph.magicalProc( this, attacker, defender, damage );
		}

		if (!levelKnown && defender == Dungeon.hero && availableUsesToID >= 1) {
			availableUsesToID--;
			usesLeftToID--;
			if (usesLeftToID <= 0) {
				identify();
				GLog.positive( Messages.get(this, "identify") );
				Badges.validateItemLevelAquired( this );
			}
		}

		return damage;
	}
	
	@Override
	public void onHeroGainExp(float levelPercent, Hero hero) {
		if (!levelKnown && isEquipped(hero) && availableUsesToID <= USES_TO_ID/2f) {
			//gains enough uses to ID over 0.5 levels
			availableUsesToID = Math.min(USES_TO_ID/2f, availableUsesToID + levelPercent * USES_TO_ID);
		}
	}
	
	@Override
	public String name() {
		return Glyph.getName(this.getClass(), glyph, cursedKnown);
	}

	@Override
	public Item curse() {
		super.curse();
		inscribe(Glyph.randomCurse());
		return this;
	}

	@Override
	public Item uncurse() {
		if (hasCurseGlyph()) {
			inscribe(null);
		}
		return super.uncurse();
	}

	@Override
	public String info() {
		String info = desc();
		
		if (levelKnown) {
			info += "\n\n" + Messages.get(Armor.class, "curr_absorb", DRMin(), DRMax());

			info += " " + Messages.get(Armor.class, "curr_absorb_magic",  magicalDRMin(), magicalDRMax());
		} else {
			info += "\n\n" + Messages.get(Armor.class, "avg_absorb", DRMin(0), DRMax(0));

			if (magicalDRMax() > 0) {
				info += " " +  Messages.get(Armor.class, "avg_absorb_magic", magicalDRMin(0), magicalDRMax(0));
			}
		}

		switch (augment) {
			case EVASION:
				info += "\n\n" + Messages.get(Armor.class, "evasion");
				break;
			case DEFENSE:
				info += "\n\n" + Messages.get(Armor.class, "defense");
				break;
			case NONE:
		}

		if (EVA != 1f || STE != 1f || speedFactor != 1f) {

			info += "\n";

			if (EVA > 1f) {
				info += "\n" + Messages.get(Armor.class, "eva_increase", Math.round((EVA-1f)*100));
			} else if (EVA < 1f) {
				info += "\n" + Messages.get(Armor.class, "eva_decrease", Math.round((1f-EVA)*100));
			}

			if (STE > 1f) {
				info += "\n" + Messages.get(Armor.class, "ste_increase", Math.round((STE-1f)*100));
			} else if (STE < 1f) {
				info += "\n" + Messages.get(Armor.class, "ste_decrease", Math.round((1f-STE)*100));
			}

			if (speedFactor > 1f) {
				info += "\n" + Messages.get(Armor.class, "speed_increase", Math.round((speedFactor-1f)*100));
			} else if (speedFactor < 1f) {
				info += "\n" + Messages.get(Armor.class, "speed_decrease", Math.round((1f-speedFactor)*100));
			}
		}
		
		if (glyph != null  && (cursedKnown || !glyph.curse())) {
			info += "\n\n" +  Messages.get(Armor.class, "inscribed", glyph.name());
			info += " " + glyph.desc();
		}
		
		if (seal != null) {
			info += "\n\n" + Messages.get(Armor.class, "seal_attached");
		}
		
		return info + equipableItemDesc();
	}

	@Override
	public void setupEmitters(ItemSprite sprite) {
		super.setupEmitters(sprite);
		Emitter emitter = emitter(sprite);
		if (glyph != null && !glyph.curse() && cursedKnown) {
			emitter.pour(Speck.factory(Speck.HALO), 0.15f);
		}

		if (seal != null) {
			emitter = emitter(sprite);
			emitter.move(ItemSpriteSheet.film.width(image) / 2f + 2f, ItemSpriteSheet.film.height(image) / 3f);
			emitter.fillTarget = false;
			emitter.pour(Speck.factory(Speck.RED_LIGHT), 0.6f);
		}
	}

	@Override
	public Item random() {
		//+0: 75% (3/4)
		//+1: 20% (4/20)
		//+2: 5%  (1/20)
		/*int n = Dungeon.getScaling()/2;
		if (Random.Int(4) == 0) {
			n++;
			if (Random.Int(5) == 0) {
				n++;
			}
		}
		level(n);*/
		
		//30% chance to be cursed
		//15% chance to be inscribed
		float effectRoll = Random.Float();
		if (effectRoll < 0.7f) {
			inscribe(Glyph.randomCurse());
			curse();
		} else if (effectRoll >= 0.8f){
			inscribe();
		}

		return this;
	}
	
	@Override
	public int price() {
		if (seal != null) return 0;

		int price = 100;
		if (hasGoodGlyph()) {
			price *= 1.5;
		}
		if (cursedKnown && (cursed())) {
			price /= 2;
		}
		if (levelKnown && level() > 0) {
			price *= (power() + 1);
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

	@Override
	public boolean cursed() {
		return super.cursed() || hasCurseGlyph();
	}

	public Armor inscribe(Glyph glyph ) {
		if (glyph == null || !glyph.curse()) curseInfusionBonus = false;
		this.glyph = glyph;
		updateQuickslot();
		return this;
	}

	public Armor inscribe() {

		Class<? extends Glyph> oldGlyphClass = glyph != null ? glyph.getClass() : null;
		Glyph gl = Glyph.random( oldGlyphClass );

		return inscribe( gl );
	}

	public boolean hasGlyph(Class<?extends Glyph> type, Char owner) {
		return glyph != null && glyph.getClass() == type && owner.buff(MagicImmune.class) == null;
	}

	//these are not used to process specific glyph effects, so magic immune doesn't affect them
	public boolean hasGoodGlyph(){
		return glyph != null && !glyph.curse();
	}

	public boolean hasCurseGlyph(){
		return glyph != null && glyph.curse();
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return glyph != null && (cursedKnown || !glyph.curse()) ? glyph.glowing() : null;
	}
	
	public static abstract class Glyph implements Bundlable {

		public static String getName(Class<? extends Armor> armClass, Glyph gly, boolean showGlyph) {
			String name = Messages.get(armClass, "name");
			if (gly != null && showGlyph) {
				name = gly.name(name);
			}
			return name;
		}
		
		private static final Class<?>[] common = new Class<?>[]{
				Obfuscation.class, Swiftness.class, Viscosity.class, Potential.class };
		
		private static final Class<?>[] uncommon = new Class<?>[]{
				Brimstone.class, /*Stone.class,*/ Entanglement.class,
				Repulsion.class, Camouflage.class, Flow.class };
		
		private static final Class<?>[] rare = new Class<?>[]{
				Affection.class, AntiMagic.class, Thorns.class };
		
		private static final float[] typeChances = new float[]{
				50, //12.5% each
				40, //6.67% each
				10  //3.33% each
		};

		private static final Class<?>[] curses = new Class<?>[]{
				AntiEntropy.class, Corrosion.class, Displacement.class, Metabolism.class,
				Multiplicity.class, Stench.class, Overgrowth.class, Bulk.class, Explosive.class
		};
		
		public abstract int proc( Armor armor, Char attacker, Char defender, int damage );

		public int magicalProc( Armor armor, Char attacker, Char defender, int damage ) {
			return damage;
		}
		
		public String name() {
			if (!curse())
				return name( Messages.get(this, "glyph") );
			else
				return name( Messages.get(Item.class, "curse"));
		}
		
		public String name( String armorName ) {
			return Messages.get(this, "name", armorName);
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
		public static Glyph random( Class<? extends Glyph> ... toIgnore ) {
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
		public static Glyph randomCommon( Class<? extends Glyph> ... toIgnore ){
			ArrayList<Class<?>> glyphs = new ArrayList<>(Arrays.asList(common));
			glyphs.removeAll(Arrays.asList(toIgnore));
			if (glyphs.isEmpty()) {
				return random();
			} else {
				return (Glyph) Reflection.newInstance(Random.element(glyphs));
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Glyph randomUncommon( Class<? extends Glyph> ... toIgnore ){
			ArrayList<Class<?>> glyphs = new ArrayList<>(Arrays.asList(uncommon));
			glyphs.removeAll(Arrays.asList(toIgnore));
			if (glyphs.isEmpty()) {
				return random();
			} else {
				return (Glyph) Reflection.newInstance(Random.element(glyphs));
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Glyph randomRare( Class<? extends Glyph> ... toIgnore ){
			ArrayList<Class<?>> glyphs = new ArrayList<>(Arrays.asList(rare));
			glyphs.removeAll(Arrays.asList(toIgnore));
			if (glyphs.isEmpty()) {
				return random();
			} else {
				return (Glyph) Reflection.newInstance(Random.element(glyphs));
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Glyph randomCurse( Class<? extends Glyph> ... toIgnore ){
			ArrayList<Class<?>> glyphs = new ArrayList<>(Arrays.asList(curses));
			glyphs.removeAll(Arrays.asList(toIgnore));
			if (glyphs.isEmpty()) {
				return random();
			} else {
				return (Glyph) Reflection.newInstance(Random.element(glyphs));
			}
		}
		
	}
}

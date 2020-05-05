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

package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Invisibility;
import com.shatteredpixel.yasd.general.actors.buffs.LockedFloor;
import com.shatteredpixel.yasd.general.actors.buffs.MagicImmune;
import com.shatteredpixel.yasd.general.actors.buffs.Recharging;
import com.shatteredpixel.yasd.general.actors.buffs.SoulMark;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.hero.HeroSubClass;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.items.armor.MageArmor;
import com.shatteredpixel.yasd.general.items.bags.MagicalHolster;
import com.shatteredpixel.yasd.general.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.CellSelector;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.ui.QuickSlotButton;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class Wand extends KindofMisc {

	public Element element = Element.MAGICAL;

	private static final String AC_ZAP = "ZAP";
	public static final String AC_ZAP_OVERRIDE = "ZAP_OVERRIDE";

	private static final float TIME_TO_ZAP = 1f;

	public int maxCharges = initialCharges();
	public int curCharges = maxCharges;
	public float partialCharge = 0f;

	private Charger charger;

	private boolean curChargeKnown = false;

	public boolean curseInfusionBonus = false;

	private static final int USES_TO_ID = 10;
	private int usesLeftToID = USES_TO_ID;
	private float availableUsesToID = USES_TO_ID / 2f;

	protected int collisionProperties = Ballistica.MAGIC_BOLT;

	{
		defaultAction = AC_ZAP;
		usesTargeting = true;
		bones = true;
	}

	private float effectiveness(Hero hero) {
		return level() + hero.getFocus()/6f;
	}

	public float actualLevel() {
		float level;
		if (curUser instanceof Hero) {
			level = effectiveness((Hero) curUser);
		} else {
			level = 2*level();
		}
		return level;
	}



	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (curCharges > 0 || !curChargeKnown & isEquipped(hero)) {
			actions.add(AC_ZAP);
		}

		return actions;
	}

	@Override
	public int defaultDegradeAmount() {
		return (int) (super.defaultDegradeAmount()*1.5f);
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_ZAP)) {

			if (!isEquipped(hero)) execute(hero, AC_EQUIP);
			else {
				execute(hero, AC_ZAP_OVERRIDE);

			}

		} else if (action.equals(AC_ZAP_OVERRIDE)) {//This is used by Mage's Staff as the Wand in the staff is never equipped.
			curUser = hero;
			//curItem = this;
			GameScene.selectCell(zapper);
		}
	}

	@Override
	public boolean canDegrade() {
		return Constants.DEGRADATION;
	}

	@Override
	public void activate(Char ch) {//When equipped, start charging
		super.activate(ch);
		if (ch instanceof Char) {
			curUser = ch;
		}

		if (ch instanceof Hero && ((Hero) ch).belongings.getItem(MagicalHolster.class) != null) {
			charge(ch, ((Hero) ch).belongings.getItem(MagicalHolster.class).HOLSTER_SCALE_FACTOR);
		} else {
			charge(ch);
		}

	}

	public void zap(int pos) {
		final Ballistica attack = new Ballistica( curUser.pos,pos, this.collisionProperties);
		this.fx(attack, new Callback() {
			public void call() {
				onZap(attack);
				wandUsed();
				curUser.next();
			}
		});
	}

	public abstract void onZap(Ballistica attack );

	public abstract void onHit( MagesStaff staff, Char attacker, Char defender, int damage);

	public boolean tryToZap(Char owner, int target ){

		if (owner.buff(MagicImmune.class) != null){
			GLog.w( Messages.get(this, "no_magic") );
			return false;
		}

		if (Dungeon.underwater()) {
			GLog.w( Messages.get(this, "underwater") );
			return false;
		}

		if ( curCharges >= (cursed ? 1 : chargesPerCast())){
			return true;
		} else {
			GLog.w(Messages.get(this, "fizzles"));
			return false;
		}
	}
	
	public void gainCharge( float amt ){
		partialCharge += amt;
		while (partialCharge >= 1) {
			curCharges = Math.min(maxCharges, curCharges+1);
			partialCharge--;
			updateQuickslot();
		}
	}
	
	public void charge( Char owner ) {
		if (charger == null) charger = new Charger();
		charger.attachTo( owner );
	}

	public void charge( Char owner, float chargeScaleFactor ){
		charge( owner );
		charger.setScaleFactor( chargeScaleFactor );
	}

	protected void processSoulMark(Char target, int chargesUsed){
		processSoulMark(target, level(), chargesUsed);
	}

	protected static void processSoulMark(Char target, int wandLevel, int chargesUsed){
		if (target != Dungeon.hero &&
				Dungeon.hero.subClass == HeroSubClass.WARLOCK &&
				//standard 1 - 0.92^x chance, plus 7%. Starts at 15%
				Random.Float() > (Math.pow(0.92f, (wandLevel*chargesUsed)+1) - 0.07f)){
			SoulMark.prolong(target, SoulMark.class, SoulMark.DURATION + wandLevel);
		}
	}

	@Override
	public void onDetach( ) {
		stopCharging();
	}

	public void stopCharging() {
		if (charger != null) {
			charger.detach();
			charger = null;
		}
	}
	
	public Item level( int value) {
		super.level( value );
		updateLevel();
		return this;
	}
	
	@Override
	public Item identify() {
		
		curChargeKnown = true;
		super.identify();
		
		updateQuickslot();
		
		return this;
	}
	
	public void onHeroGainExp( float levelPercent, Hero hero ){
		if (!isIdentified() && availableUsesToID <= USES_TO_ID/2f) {
			//gains enough uses to ID over 1 level
			availableUsesToID = Math.min(USES_TO_ID/2f, availableUsesToID + levelPercent * USES_TO_ID/2f);
		}
	}

	@Override
	public String info() {
		String desc = desc();

		desc += "\n\n" + statsDesc();

		if (cursed && cursedKnown) {
			desc += "\n\n" + Messages.get(Wand.class, "cursed");
		} else if (!isIdentified() && cursedKnown){
			desc += "\n\n" + Messages.get(Wand.class, "not_cursed");
		}

		return desc;
	}

	public String statsDesc(){
		return Messages.get(this, "stats_desc");
	}
	
	@Override
	public boolean isIdentified() {
		return super.isIdentified() && curChargeKnown;
	}
	
	@Override
	public String status() {
		if (levelKnown) {
			return (curChargeKnown ? curCharges : "?") + "/" + maxCharges;
		} else {
			return null;
		}
	}
	
	@Override
	public int level() {
		if (!cursed && curseInfusionBonus){
			curseInfusionBonus = false;
			updateLevel();
		}
		return super.level() + (curseInfusionBonus ? Constants.CURSE_INFUSION_BONUS_AMT : 0);
	}
	
	@Override
	public Item upgrade() {

		super.upgrade();

		if (Random.Int(3) == 0) {
			cursed = false;
		}

		updateLevel();
		curCharges = Math.min( curCharges + 1, maxCharges );
		updateQuickslot();
		
		return this;
	}
	
	@Override
	public Item degrade() {
		super.degrade();
		
		updateLevel();
		updateQuickslot();
		
		return this;
	}

	public int chargesScaling() {
		return 1;
	}
	
	public void updateLevel() {
		maxCharges = Math.min( initialCharges() + level()*chargesScaling(), Constants.WAND_CHARGE_CAP );
		curCharges = Math.min( curCharges, maxCharges );
	}
	
	protected int initialCharges() {
		return 2;
	}

	protected int chargesPerCast() {
		return 1;
	}
	
	protected void fx( Ballistica bolt, Callback callback ) {
		element.FX( curUser, bolt.collisionPos, callback );
	}

	public void staffFx( MagesStaff.StaffParticle particle ){
		particle.color(0xFFFFFF); particle.am = 0.3f;
		particle.setLifespan( 1f);
		particle.speed.polar( Random.Float(PointF.PI2), 2f );
		particle.setSize( 1f, 2f );
		particle.radiateXY(0.5f);
	}

	protected void wandUsed() {
		if (!isIdentified() && availableUsesToID >= 1) {
			availableUsesToID--;
			usesLeftToID--;
			if (usesLeftToID <= 0) {
				identify();
				GLog.p( Messages.get(Wand.class, "identify") );
				Badges.validateItemLevelAquired( this );
			}
		}
		
		curCharges -= cursed ? 1 : chargesPerCast();
		
		if (curUser instanceof Hero && ((Hero)curUser).heroClass == HeroClass.MAGE) levelKnown = true;
		updateQuickslot();

		use(defaultDegradeAmount()*1.5f);

		curUser.spendAndNext( TIME_TO_ZAP );
	}
	
	@Override
	public Item random() {
		//+0: 66.67% (2/3)
		//+1: 26.67% (4/15)
		//+2: 6.67%  (1/15)
		int n = 0;
		if (Random.Int(3) == 0) {
			n++;
			if (Random.Int(5) == 0){
				n++;
			}
		}
		level(n);

		curCharges += n;
		
		//30% chance to be cursed
		if (Random.Float() < 0.3f) {
			cursed = true;
		}

		return this;
	}
	
	@Override
	public int price() {
		int price = 75;
		if (cursed && cursedKnown) {
			price /= 2;
		}
		if (levelKnown) {
			if (level() > 0) {
				price *= (level() + 1);
			} else if (level() < 0) {
				price /= (1 - level());
			}
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}
	
	private static final String USES_LEFT_TO_ID = "uses_left_to_id";
	private static final String AVAILABLE_USES  = "available_uses";
	private static final String CUR_CHARGES         = "curCharges";
	private static final String CUR_CHARGE_KNOWN    = "curChargeKnown";
	private static final String PARTIALCHARGE       = "partialCharge";
	private static final String CURSE_INFUSION_BONUS = "curse_infusion_bonus";
	
	@Override
	public void storeInBundle(  Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( USES_LEFT_TO_ID, usesLeftToID );
		bundle.put( AVAILABLE_USES, availableUsesToID );
		bundle.put( CUR_CHARGES, curCharges );
		bundle.put( CUR_CHARGE_KNOWN, curChargeKnown );
		bundle.put( PARTIALCHARGE , partialCharge );
		bundle.put(CURSE_INFUSION_BONUS, curseInfusionBonus );
	}
	
	@Override
	public void restoreFromBundle(  Bundle bundle ) {
		super.restoreFromBundle( bundle );
		usesLeftToID = bundle.getInt( USES_LEFT_TO_ID );
		availableUsesToID = bundle.getInt( AVAILABLE_USES );
		
		//pre-0.7.2 saves
		if (bundle.contains( "unfamiliarity" )){
			usesLeftToID = Math.min(10, bundle.getInt( "unfamiliarity" ));
			availableUsesToID = USES_TO_ID/2f;
		}
		curCharges = bundle.getInt( CUR_CHARGES );
		curChargeKnown = bundle.getBoolean( CUR_CHARGE_KNOWN );
		partialCharge = bundle.getFloat( PARTIALCHARGE );
		curseInfusionBonus = bundle.getBoolean(CURSE_INFUSION_BONUS);
	}
	
	@Override
	public void reset() {
		super.reset();
		usesLeftToID = USES_TO_ID;
		availableUsesToID = USES_TO_ID/2f;
	}
	
	private CellSelector.Listener zapper = new CellSelector.Listener(this) {
		
		@Override
		public void onSelect( Integer target ) {
			
			if (target != null) {

				//Source is always Wand in this case.
				final Wand curWand = (Wand) source;

				final Ballistica shot = new Ballistica( curUser.pos, target, curWand.collisionProperties);
				int cell = shot.collisionPos;
				
				if (target == curUser.pos || cell == curUser.pos) {
					GLog.i( Messages.get(Wand.class, "self_target") );
					return;
				}

				curUser.sprite.zap(cell);

				//attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
				if (Actor.findChar(target) != null)
					QuickSlotButton.target(Actor.findChar(target));
				else
					QuickSlotButton.target(Actor.findChar(cell));
				
				if (curWand.tryToZap(curUser, target)) {
					if (curUser instanceof Hero) {
						curUser.busy();
					}

					Invisibility.dispel();
					
					if (curWand.cursed){
						if (!curWand.cursedKnown){
							GLog.n(Messages.get(Wand.class, "curse_discover", curWand.name()));
						}
						CursedWand.cursedZap(curWand,
								curUser,
								new Ballistica(curUser.pos, target, Ballistica.MAGIC_BOLT),
								new Callback() {
									@Override
									public void call() {
										curWand.wandUsed();
									}
								});
					} else {
						curWand.zap(target);
					}
					curWand.cursedKnown = true;
					
				}
				
			}
		}
		
		@Override
		public String prompt() {
			return Messages.get(Wand.class, "prompt");
		}
	};
	
	public class Charger extends Buff {
		
		private static final float BASE_CHARGE_DELAY = 10f;
		private static final float SCALING_CHARGE_ADDITION = 40f;
		private static final float NORMAL_SCALE_FACTOR = 0.875f;

		private static final float CHARGE_BUFF_BONUS = 0.25f;

		float scalingFactor = NORMAL_SCALE_FACTOR;
		
		@Override
		public boolean attachTo(@NotNull Char target ) {
			super.attachTo( target );
			
			return true;
		}
		
		@Override
		public boolean act() {
			if (curCharges < maxCharges)
				recharge();
			
			while (partialCharge >= 1 && curCharges < maxCharges) {
				partialCharge--;
				curCharges++;
				updateQuickslot();
			}
			
			if (curCharges == maxCharges){
				partialCharge = 0;
			}
			
			spend( TICK );
			
			return true;
		}

		private void recharge(){
			float moraleMultiplier = 1f;
			float ArmourMultiplier = 1f;
			if (target instanceof Hero) {
				moraleMultiplier = 1f + (float) ((((Hero)target).morale - ((Hero)target).MAX_MORALE) * 0.04);
				if (((Hero)target).belongings.getItem(MageArmor.class) != null) {
					ArmourMultiplier = 1.25f;
				}
			}


			int missingCharges = maxCharges - curCharges;
			missingCharges = Math.max(0, missingCharges);

			float turnsToCharge = (float) (BASE_CHARGE_DELAY
					+ (SCALING_CHARGE_ADDITION * Math.pow(scalingFactor, missingCharges)));

			LockedFloor lock = target.buff(LockedFloor.class);
			if (lock == null || lock.regenOn())
				partialCharge += (1f/turnsToCharge);// * RingOfFocus.wandChargeMultiplier(target);

			for (Recharging bonus : target.buffs(Recharging.class)){
				if (bonus != null && bonus.remainder() > 0f) {
					partialCharge += CHARGE_BUFF_BONUS * bonus.remainder() * moraleMultiplier * ArmourMultiplier;
				}
			}
		}
		
		public Wand wand(){
			return Wand.this;
		}

		public void gainCharge(float charge){
			partialCharge += charge;
			while (partialCharge >= 1f){
				curCharges++;
				partialCharge--;
			}
			curCharges = Math.min(curCharges, maxCharges);
			updateQuickslot();
		}

		private void setScaleFactor(float value){
			this.scalingFactor = value;
		}
	}
}

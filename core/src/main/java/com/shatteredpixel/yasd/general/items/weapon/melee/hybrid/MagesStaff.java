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

package com.shatteredpixel.yasd.general.items.weapon.melee.hybrid;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroSubClass;
import com.shatteredpixel.yasd.general.effects.particles.ElmoParticle;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.bags.Bag;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.yasd.general.items.wands.Wand;
import com.shatteredpixel.yasd.general.items.wands.WandOfCorrosion;
import com.shatteredpixel.yasd.general.items.wands.WandOfCorruption;
import com.shatteredpixel.yasd.general.items.wands.WandOfDisintegration;
import com.shatteredpixel.yasd.general.items.wands.WandOfLivingEarth;
import com.shatteredpixel.yasd.general.items.wands.WandOfRegrowth;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndBag;
import com.shatteredpixel.yasd.general.windows.WndOptions;
import com.shatteredpixel.yasd.general.windows.WndUseItem;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MagesStaff extends MeleeWeapon {

	private Wand wand;

	public static final String AC_IMBUE = "IMBUE";
	public static final String AC_ZAP	= "ZAP";

	private static final float STAFF_SCALE_FACTOR = 0.750f;

	{
		image = ItemSpriteSheet.Weapons.MAGES_STAFF;
		hitSound = Assets.Sounds.HIT;
		hitSoundPitch = 1.1f;

		usesTargeting = true;

		unique = true;
		bones = false;

		damageFactor = 0.65f;

		statScaling.add(Hero.HeroStat.FOCUS);
	}

	public MagesStaff() {
		wand = null;
	}

	public MagesStaff(Wand wand){
		this();
		wand.identify();
		wand.uncurse();
		this.wand = wand;
		wand.imbuedStaff = this;
		updateWand(false);
		wand.curCharges = wand.maxCharges;
		name = Messages.get(wand, "staff_name");
	}

	public Wand getWand() {
		return wand;
	}

	@Override
	public Item level(int value) {
		updateWand(false);
		super.level(value);
		return this;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		actions.add(AC_IMBUE);
		if (wand!= null && wand.curCharges > 0) {
			actions.add( AC_ZAP );
		}
		return actions;
	}

	@Override
	public void activate( Char ch ) {
		super.activate(ch);
		if(wand != null) wand.charge( ch, STAFF_SCALE_FACTOR );
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_IMBUE)) {

			curUser = hero;
			GameScene.selectItem(itemSelector, WndBag.Mode.WAND, Messages.get(this, "prompt"));

		} else if (action.equals(AC_ZAP)){

			if (wand == null) {
				GameScene.show(new WndUseItem(null, this) );
				return;
			}

			wand.curseIntensity = curseIntensity;
			wand.execute(hero, Wand.AC_ZAP_OVERRIDE);
		}
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (wand != null &&
				attacker instanceof Hero && ((Hero)attacker).subClass == HeroSubClass.BATTLEMAGE) {
			if (wand.curCharges < wand.maxCharges) wand.partialCharge += 0.33f;
			ScrollOfRecharging.charge(attacker);
			damage += Random.NormalIntRange(1, ((Hero)attacker).getFocus()/2);
			wand.onHit(this, attacker, defender, damage);
		}

		return super.proc(attacker, defender, damage);
	}

	@Override
	public int reachFactor(Char owner) {
		int reach = super.reachFactor(owner);
		if (owner instanceof Hero
				&& wand instanceof WandOfDisintegration
				&& ((Hero)owner).subClass == HeroSubClass.BATTLEMAGE){
			reach++;
		}
		return reach;
	}

	@Override
	public boolean collect(Bag container, @NotNull Char ch) {
		if (super.collect(container, ch)) {
			if (container.owner != null && wand != null) {
				wand.charge(container.owner, STAFF_SCALE_FACTOR);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onDetach( ) {
		if (wand != null) wand.stopCharging();
	}

	public Item imbueWand(Wand wand, Char owner) {
		//Ensure old wand can't still be used
		this.wand.imbuedStaff = this;
		Dungeon.quickslot.clearItem(this.wand);

		this.wand = null;

		int targetLevel = getNewLevel(trueLevel(), wand.trueLevel());
		if (owner instanceof Hero && wand.isEquipped(owner)) {
			wand.doUnequip(owner, false);
		}
		level(targetLevel);
		this.wand = wand;
		updateWand(false);
		wand.curCharges = wand.maxCharges;
		if (owner != null) wand.charge(owner);

		name = Messages.get(wand, "staff_name");

		//This is necessary to reset any particles.
		//FIXME this is gross, should implement a better way to fully reset quickslot visuals
		int slot = Dungeon.quickslot.getSlot(this);
		if (slot != -1){
			Dungeon.quickslot.clearSlot(slot);
			updateQuickslot();
			Dungeon.quickslot.setSlot( slot, this );
			updateQuickslot();
		}
		
		Badges.validateItemLevelAquired(this);
		wand.imbuedStaff = this;
		wand.curseIntensity = curseIntensity;

		return this;
	}
	
	public void gainCharge( float amt ){
		if (wand != null){
			wand.gainCharge(amt);
		}
	}

	public Class<?extends Wand> wandClass(){
		return wand != null ? wand.getClass() : null;
	}

	@Override
	public Item upgrade(boolean enchant) {
		super.upgrade( enchant );

		updateWand(true);

		return this;
	}

	@Override
	public Item degrade() {
		super.degrade();

		updateWand(false);

		return this;
	}
	
	public void updateWand(boolean levelled){
		if (wand != null) {
			int curCharges = wand.curCharges;
			wand.level(level());
			//gives the wand one additional max charge
			wand.maxCharges += 1;
			wand.curCharges = Math.min(curCharges + (levelled ? 1 : 0), wand.maxCharges);
			updateQuickslot();
		}
	}

	@Override
	public String status() {
		if (wand == null) return super.status();
		else return wand.status();
	}

	@Override
	public String info() {
		String info = super.info();

		if (wand == null){
			info += "\n\n" + Messages.get(this, "no_wand");
		} else {
			info += "\n\n" + Messages.get(this, "has_wand", Messages.get(wand, "name")) + " " + wand.statsDesc();
		}

		return info;
	}

	@Override
	public void setupEmitters(ItemSprite sprite) {
		super.setupEmitters(sprite);
		if (wand != null) {
			Emitter emitter = emitter(sprite);
			emitter.fillTarget = false;
			emitter.move(12.5f, 3);
			emitter.pour(StaffParticleFactory, 0.1f);
		}
	}

	private static final String WAND = "wand";

	@Override
	public void storeInBundle(  Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(WAND, wand);
	}

	@Override
	public void restoreFromBundle(  Bundle bundle) {
		super.restoreFromBundle(bundle);
		wand = (Wand) bundle.get(WAND);
		if (wand != null) {
			wand.imbuedStaff = this;
			wand.updateLevel();
			wand.maxCharges++;
			name = Messages.get(wand, "staff_name");
		}
	}

	@Override
	public int price() {
		return 0;
	}
	
	@Override
	public Weapon enchant(Enchantment ench) {
		if (curseInfusionBonus && (ench == null || !ench.curse())){
			curseInfusionBonus = false;
			updateWand(false);
		}
		return super.enchant(ench);
	}
	
	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( final Item item ) {
			if (item != null) {

				if (!item.isIdentified()) {
					GLog.warning(Messages.get(MagesStaff.class, "id_first"));
					return;
				} else if (item.cursed()){
					GLog.warning(Messages.get(MagesStaff.class, "cursed"));
					return;
				}

				if (wand == null){
					applyWand((Wand)item);
				} else {
					final int newLevel =
							getNewLevel(trueLevel(), item.trueLevel());
					GameScene.show(
							new WndOptions("",
									Messages.get(MagesStaff.class, "warning", newLevel),
									Messages.get(MagesStaff.class, "yes"),
									Messages.get(MagesStaff.class, "no")) {
								@Override
								protected void onSelect(int index) {
									if (index == 0) {
										applyWand((Wand)item);
									}
								}
							}
					);
				}
			}
		}

		private void applyWand(Wand wand){
			Sample.INSTANCE.play(Assets.Sounds.BURNING);
			curUser.sprite.emitter().burst( ElmoParticle.FACTORY, 12 );
			if (curUser instanceof Hero) {
				evoke((Hero)curUser);
			}


			Dungeon.quickslot.clearItem(wand);

			wand.detach(curUser.belongings.backpack);

			GLog.positive( Messages.get(MagesStaff.class, "imbue", wand.name()));
			imbueWand( wand, curUser );

			updateQuickslot();
		}
	};

	private static int getNewLevel(int staffLvl, int wndLvl) {
		if (staffLvl > 0) wndLvl++;
		return Math.max(staffLvl, wndLvl);
	}

	private final Emitter.Factory StaffParticleFactory = new Emitter.Factory() {
		@Override
		//reimplementing this is needed as instance creation of new staff particles must be within this class.
		public void emit( Emitter emitter, int index, float x, float y ) {
			StaffParticle c = (StaffParticle)emitter.getFirstAvailable(StaffParticle.class);
			if (c == null) {
				c = new StaffParticle();
				emitter.add(c);
			}
			c.reset(x, y);
		}

		@Override
		//some particles need light mode, others don't
		public boolean lightMode() {
			return !((wand instanceof WandOfDisintegration)
					|| (wand instanceof WandOfCorruption)
					|| (wand instanceof WandOfCorrosion)
					|| (wand instanceof WandOfRegrowth)
					|| (wand instanceof WandOfLivingEarth));
		}
	};

	//determines particle effects to use based on wand the staff owns.
	public class StaffParticle extends PixelParticle{

		private float minSize;
		private float maxSize;
		public float sizeJitter = 0;

		public StaffParticle(){
			super();
		}

		public void reset( float x, float y ) {
			revive();

			speed.set(0);

			this.x = x;
			this.y = y;

			if (wand != null)
				wand.staffFx( this );

		}

		public void setSize( float minSize, float maxSize ){
			this.minSize = minSize;
			this.maxSize = maxSize;
		}

		public void setLifespan( float life ){
			lifespan = left = life;
		}

		public void shuffleXY(float amt){
			x += Random.Float(-amt, amt);
			y += Random.Float(-amt, amt);
		}

		public void radiateXY(float amt){
			float hypot = (float)Math.hypot(speed.x, speed.y);
			this.x += speed.x/hypot*amt;
			this.y += speed.y/hypot*amt;
		}

		@Override
		public void update() {
			super.update();
			size(minSize + (left / lifespan)*(maxSize-minSize) + Random.Float(sizeJitter));
		}
	}
}

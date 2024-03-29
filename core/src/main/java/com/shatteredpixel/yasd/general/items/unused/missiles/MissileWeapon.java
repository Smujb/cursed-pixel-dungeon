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

package com.shatteredpixel.yasd.general.items.unused.missiles;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Corruption;
import com.shatteredpixel.yasd.general.actors.buffs.PinCushion;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.hero.HeroSubClass;
import com.shatteredpixel.yasd.general.items.Attackable;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.items.bags.Bag;
import com.shatteredpixel.yasd.general.items.bags.MagicalHolster;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Projecting;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

abstract public class MissileWeapon extends Weapon implements Attackable {

	{
		stackable = true;
		levelKnown = true;
		
		bones = true;

		defaultAction = AC_THROW;
		usesTargeting = true;

		statScaling = new ArrayList<>(Arrays.asList(Hero.HeroStat.ASSAULT));
	}
	
	protected boolean sticky = true;
	
	private static final float MAX_DURABILITY = 100;
	protected float durability = MAX_DURABILITY;
	protected float baseUses = 10;

	public float damageMultiplier = 1f;
	
	public boolean holster;
	
	//used to reduce durability from the source weapon stack, rather than the one being thrown.
	protected MissileWeapon parent;

	@Override
	public boolean breaksArmor(Char owner) {
		return owner instanceof  Hero && ((Hero)owner).subClass == HeroSubClass.SNIPER;
	}

	@Override
	public int min(float lvl) {
		return Math.round(8 * lvl * damageMultiplier);   //level scaling
	}

	@Override
	public int max(float lvl) {
		return Math.round(16 * lvl * damageMultiplier);   //level scaling
	}

	@Override
	public int max() {
		return Math.max(0, max( power()));
	}
	
	@Override
	//FIXME some logic here assumes the items are in the player's inventory. Might need to adjust
	public Item upgrade() {
		if (!bundleRestoring) {
			super.upgrade();

			Item similar = Dungeon.hero.belongings.getSimilar(this);
			if (similar != null) {
				detach(Dungeon.hero.belongings.backpack);
				return similar.merge(this);
			}
			updateQuickslot();
			return this;
		} else {
			return super.upgrade();
		}
	}

	@Override
	public boolean use(Char enemy) {
		if (curUser == null) {
			return false;
		}
		if (Dungeon.level.adjacent(curUser.pos, enemy.pos)) {
			doAttack(curUser, enemy);
			PinCushion pinCushion = enemy.buff(PinCushion.class);
			if (pinCushion != null) {
				MissileWeapon similar = (MissileWeapon) pinCushion.getSimilar(this);
				if (similar != null) {
					if (!similar.mergeWithMisc(curUser)) {
						similar.collect(curUser.belongings.backpack, curUser);
					}
				}
			}
			return true;
		} else if (quantity > 1) {
			cast(curUser, enemy.pos);
			return true;
		}
		return false;
	}

	@Override
	public boolean canUse(Char enemy) {
		if (curUser == null) {
			return false;
		} else if (quantity <= 1) {
			return Dungeon.level.adjacent(curUser.pos, enemy.pos);
		} else {
			return Ballistica.canHit(curUser, enemy, Ballistica.PROJECTILE);
		}
	}


	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped(hero) && quantity <= 1) {
			actions.remove(AC_THROW);
		}
		return actions;
	}
	
	@Override
	public boolean collect(Bag container, @NotNull Char ch) {
		if (container instanceof MagicalHolster) holster = true;
		return super.collect(container, ch);
	}
	
	@Override
	public int throwPos( Char user, int dst) {
		if (hasEnchant(Projecting.class, user)
				&& !Dungeon.level.solid(dst) && Dungeon.level.distance(user.pos, dst) <= 4){
			return dst;
		} else {
			return super.throwPos(user, dst);
		}
	}

	@Override
	public void doThrow(Hero hero) {
		parent = null; //reset parent before throwing, just incase
		super.doThrow(hero);
	}

	@Override
	protected void onThrow( int cell ) {
		Char enemy = Actor.findChar( cell );
		if (enemy == null || enemy == curUser) {
				parent = null;
				super.onThrow( cell );
		} else {
			if (!attack(curUser, enemy, false)) {
				rangedMiss( cell );
			} else {
				
				rangedHit( enemy, cell );

			}
		}

		if (quantity < 1 && isEquipped(curUser)) {
			doUnequip(curUser, false);
		}
	}
	
	@Override
	public Item random() {
		if (!stackable) return this;

		//3: 66.67% (2/3)
		//4: 26.67% (4/15)
		//5: 6.67%  (1/15)
		quantity = 3;
		if (Random.Int(3) == 0) {
			quantity++;
			if (Random.Int(5) == 0) {
				quantity++;
			}
		}
		return this;
	}
	
	@Override
	public float castDelay(Char user, int dst) {
		return speedFactor( user );
	}
	
	protected void rangedHit( Char enemy, int cell ){
		decrementDurability();
		if (durability > 0){
			//attempt to stick the missile getWeapons to the enemy, just drop it if we can't.
			if (sticky && enemy != null && enemy.isAlive() && enemy.buff(Corruption.class) == null){
				PinCushion p = Buff.affect(enemy, PinCushion.class);
				if (p.target == enemy){
					p.stick(this);
					return;
				}
			}
			Dungeon.level.drop( this, cell ).sprite.drop();
		}
	}

	protected void rangedMiss( int cell ) {
		parent = null;
		super.onThrow(cell);
	}
	
	protected float durabilityPerUse(){
		float usages = baseUses * (float)(Math.pow(3, level()));
		
		if (Dungeon.hero.heroClass == HeroClass.HUNTRESS)   usages *= 1.5f;
		if (holster)                                        usages *= MagicalHolster.HOLSTER_DURABILITY_FACTOR;
		
		//when upgraded, items just last forever.
		if (baseUses >= INFINITE_USES) return 0;
		
		//add a tiny amount to account for rounding error for calculations like 1/3
		return (MAX_DURABILITY/usages) + 0.001f;
	}

	protected static final int INFINITE_USES = 100_000_000;
	
	protected void decrementDurability(){
		//if this getWeapons was thrown from a source stack, degrade that stack.
		//unless a getWeapons is about to break, then break the one being thrown
		if (parent != null){
			if (parent.durability <= parent.durabilityPerUse()){
				durability = 0;
				parent.durability = MAX_DURABILITY;
			} else {
				parent.durability -= parent.durabilityPerUse();
				if (parent.durability > 0 && parent.durability <= parent.durabilityPerUse()){
					if (level() <= 0)GLog.warning(Messages.get(this, "about_to_break"));
					else             GLog.negative(Messages.get(this, "about_to_break"));
				}
			}
			parent = null;
		} else {
			durability -= durabilityPerUse();
			if (durability > 0 && durability <= durabilityPerUse()){
				if (level() <= 0)GLog.warning(Messages.get(this, "about_to_break"));
				else             GLog.negative(Messages.get(this, "about_to_break"));
			}
		}
	}
	
	@Override
	public int damageRoll(Char owner) {
		return augment.damageFactor(super.damageRoll( owner ));
	}
	
	@Override
	public void reset() {
		super.reset();
		durability = MAX_DURABILITY;
	}
	
	@Override
	public Item merge(Item other) {
		super.merge(other);
		if (isSimilar(other)) {
			durability += ((MissileWeapon)other).durability;
			durability -= MAX_DURABILITY;
			while (durability <= 0){
				quantity -= 1;
				durability += MAX_DURABILITY;
			}
		}
		return this;
	}
	
	@Override
	public Item split(int amount) {
		bundleRestoring = true;
		Item split = super.split(amount);
		bundleRestoring = false;
		
		//unless the thrown getWeapons will break, split off a max durability item and
		//have it reduce the durability of the main stack. Cleaner to the player this way
		if (split != null){
			MissileWeapon m = (MissileWeapon)split;
			m.durability = MAX_DURABILITY;
			m.parent = this;
			m.curUser = curUser;
		}
		
		return split;
	}

	@Override
	public void activate(Char ch) {
		super.activate(ch);
		if (parent != null) {
			parent.curUser = ch;
		}
	}

	private boolean mergeWithMisc(Char hero) {
		KindofMisc[] miscs = hero.belongings.miscs;
		for (int i = 0; i < miscs.length; i++) {
			KindofMisc misc = miscs[i];
			if (misc != null && misc.isSimilar(this)) {
				Item merged = misc.merge(this);
				if (merged != null) {
					miscs[i] = (KindofMisc) merged;
					hero.next();
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean doPickUp(Hero hero) {
		parent = null;
		if (mergeWithMisc(hero)) {
			return true;
		}
		return super.doPickUp(hero);
	}

	@Override
	public boolean doEquip(Hero hero) {
		if (parent != null) {
			return parent.doEquip(hero);
		} else {
			return super.doEquip(hero);
		}
	}

	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public String info() {

		String info = desc();
		
		info += "\n\n" + Messages.get( MissileWeapon.class, "stats",
				Math.round(augment.damageFactor(min())),
				Math.round(augment.damageFactor(max())));

		if (enchantment != null && (cursedKnown || !enchantment.curse())) {
			info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
			info += " " + Messages.get(enchantment, "desc");
		}

		info += "\n\n" + Messages.get(MissileWeapon.class, "distance");
		
		info += "\n\n" + Messages.get(this, "durability");
		
		if (durabilityPerUse() > 0){
			info += " " + Messages.get(this, "uses_left",
					(int)Math.ceil(durability/durabilityPerUse()),
					(int)Math.ceil(MAX_DURABILITY/durabilityPerUse()));
		} else {
			info += " " + Messages.get(this, "unlimited_uses");
		}
		
		
		return info + equipableItemDesc();
	}
	
	@Override
	public int price() {
		return 20 * quantity * (level() + 1);
	}
	
	private static final String DURABILITY = "durability";
	
	@Override
	public void storeInBundle(  Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DURABILITY, durability);
	}
	
	private static boolean bundleRestoring = false;
	
	@Override
	public void restoreFromBundle(  Bundle bundle) {
		bundleRestoring = true;
		super.restoreFromBundle(bundle);
		bundleRestoring = false;
		durability = bundle.getInt(DURABILITY);
	}
}

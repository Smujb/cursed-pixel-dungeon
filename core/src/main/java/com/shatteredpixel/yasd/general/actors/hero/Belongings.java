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

import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Berserk;
import com.shatteredpixel.yasd.general.actors.buffs.Fury;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.EquipableItem;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindOfWeapon;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.items.armor.Armor;
import com.shatteredpixel.yasd.general.items.artifacts.CapeOfThorns;
import com.shatteredpixel.yasd.general.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.general.items.bags.Bag;
import com.shatteredpixel.yasd.general.items.keys.Key;
import com.shatteredpixel.yasd.general.items.rings.RingOfFuror;
import com.shatteredpixel.yasd.general.items.rings.RingOfHaste;
import com.shatteredpixel.yasd.general.items.rings.RingOfTenacity;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.yasd.general.items.shield.Shield;
import com.shatteredpixel.yasd.general.items.wands.Wand;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;

public class Belongings implements Iterable<Item> {

	public static final int BACKPACK_SIZE	= 40;
	
	private Char owner;
	
	public Bag backpack;

	public KindofMisc[] miscs;

	public Belongings( Char owner ) {
		this.owner = owner;

		backpack = new Bag() {
			{
				name = Messages.get(Bag.class, "name");
			}
			public int capacity(){
				int cap = super.capacity();
				for (Item item : items){
					if (item instanceof Bag){
						cap++;
					}
				}
				return cap;
			}
		};
		backpack.owner = owner;

		miscs = new KindofMisc[owner.miscSlots()];
	}

	//##############################################################################################
	//########################## Stuff for handling chars with belongings ##########################
	//##############################################################################################

	public boolean canEquip(int slot) {//Use for setting specific properties for each slot.
		return miscs[slot] == null;
	}

	public boolean canEquip() {//Use for setting specific properties for each slot.
		for (int i = 0; i < miscs.length; i++) {
			if (canEquip(i)) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<KindofMisc> getMiscsOfType(Class type ) {//Find equipped items of a certain kind
		ArrayList<KindofMisc> items = new ArrayList<>();
		for (KindofMisc misc : miscs) {
			if (type.isInstance(misc)) {
				items.add(misc);
			}
		}
		return items;
	}

	@Nullable
	public KindOfWeapon getWeapon() {
		ArrayList<KindofMisc> weps = getMiscsOfType(KindOfWeapon.class);
		return weps.size() > 0 ? (KindOfWeapon) weps.get(0) : null;
	}
	
	@Nullable
	public Armor getArmor() {
		ArrayList<KindofMisc> armors = getMiscsOfType(Armor.class);
		return armors.size() > 0 ? (Armor) armors.get(0) : null;
	}

	public float accuracyFactor(float accuracy, Char target) {
		KindOfWeapon wep = getWeapon();
		//accuracy *= RingOfPerception.accuracyMultiplier(owner);
		if (wep instanceof MissileWeapon) {
			if (Dungeon.level.adjacent(owner.pos, target.pos)) {
				accuracy *= 0.5f;
			} else {
				accuracy *= 1.5f;
			}
		}

		if (wep != null) {
			accuracy *= wep.accuracyFactor(owner);
		}
		return accuracy;
	}

	public int damageRoll() {
		int dmg;
		KindOfWeapon wep = getWeapon();
		if (wep != null) {
			dmg = wep.damageRoll(owner);
			//if (!(wep instanceof MissileWeapon)) dmg += RingOfForce.armedDamageBonus(owner);
		} else {
			int level = 0;
			if (owner instanceof Mob) {
				level = ((Mob) owner).EXP;
			} else if (owner instanceof Hero) {
				level = ((Hero)owner).lvl/2;
			}
			dmg = Random.Int(1, level);
			//dmg = RingOfForce.damageRoll(owner);
		}
		if (dmg < 0) dmg = 0;

		Berserk berserk = owner.buff(Berserk.class);
		if (berserk != null) dmg = berserk.damageFactor(dmg);

		return owner.buff(Fury.class) != null ? (int) (dmg * 1.5f) : dmg;
	}

	public int defenseProc(Char enemy, int damage) {
		return damage;
	}

	public int attackProc(Char enemy, int damage) {
		return damage;
	}

	public int magicalAttackProc(Char enemy, int damage) {
		return damage;
	}

	public int magicalDefenseProc(Char enemy, int damage) {
		return damage;
	}

	public boolean canSurpriseAttack() {
		KindOfWeapon curWep = getWeapon();
		if (!(curWep instanceof Weapon)) return true;
		if (!curWep.canTypicallyUse(owner)) return false;
		return curWep.canSurpriseAttack;
	}

	public int affectDamage(int damage, Object src) {
		if (owner.buff(TimekeepersHourglass.timeStasis.class) != null) {
			return 0;
		}
		CapeOfThorns.Thorns thorns = owner.buff(CapeOfThorns.Thorns.class);
		if (thorns != null) {
			damage = thorns.proc(damage, (src instanceof Char ? (Char) src : null), owner);
		}

		damage = (int) Math.ceil(damage * RingOfTenacity.damageMultiplier(owner));

		return damage;
	}

	public float attackDelay() {
		float multiplier = 1f;
		if (getWeapon() != null) {
			return getWeapon().speedFactor(owner) * multiplier;//Two getWeapon()s = 1/2 attack speed
		} else {
			//Normally putting furor speed on unarmed attacks would be unnecessary
			//But there's going to be that one guy who gets a furor+force ring combo
			//This is for that one guy, you shall get your fists of fury!
			return RingOfFuror.attackDelayMultiplier(owner);
		}
	}


	public float affectEvasion(float evasion) {
		return evasion;
	}

	public float affectSpeed(float speed) {
		return speed * RingOfHaste.speedMultiplier(owner);
	}

	public float affectStealth(float stealth) {
		return stealth;
	}

	public float affectPerception(float perception) {
		return perception;
	}

	//##############################################################################################
	//####################### End of stuff for handling chars with belongings ######################
	//##############################################################################################

	//legacy
	private static final String ARMOR		= "armor";
	private static final String WEAPON		= "weapon";


	private static final String MISC          = "misc";
	private static final String ARMOR_PREVIEW = "preview";


	public void storeInBundle( Bundle bundle ) {

		backpack.storeInBundle( bundle );
		for (int i = 0; i < owner.miscSlots(); i++) {//Store all miscs
			bundle.put( MISC + i, miscs[i]);
		}
	}
	
	public void restoreFromBundle( Bundle bundle ) {
		
		backpack.clear();
		backpack.restoreFromBundle( bundle );
		miscs = new KindofMisc[owner.miscSlots()];
		for (int i = 0; i < owner.miscSlots(); i++) {//Restore all miscs
			miscs[i] = (KindofMisc) bundle.get(MISC + i);
			if (miscs[i] != null) {
				miscs[i].activate( owner );
			}
		}

		//Old saves
		if (bundle.contains(WEAPON)) {
			KindOfWeapon weapon = (KindOfWeapon) bundle.get(WEAPON);
			weapon.collect(backpack, owner);
		}

		if (bundle.contains(ARMOR)) {
			Shield shield = (Shield) Generator.random(Generator.Category.SHIELD);
			shield.collect(backpack, owner);
		}
	}
	
	@SuppressWarnings("unchecked")
	public<T extends Item> T getItem( Class<T> itemClass ) {

		for (Item item : this) {
			if (itemClass.isInstance( item )) {
				return (T)item;
			}
		}
		
		return null;
	}
	
	public boolean contains( Item contains ){
		
		for (Item item : this) {
			if (contains == item ) {
				return true;
			}
		}
		
		return false;
	}
	
	public Item getSimilar( Item similar ){
		
		for (Item item : this) {
			if (similar != item && similar.isSimilar(item)) {
				return item;
			}
		}
		
		return null;
	}
	
	public ArrayList<Item> getAllSimilar( Item similar ){
		ArrayList<Item> result = new ArrayList<>();
		
		for (Item item : this) {
			if (item != similar && similar.isSimilar(item)) {
				result.add(item);
			}
		}
		
		return result;
	}

	public ArrayList<Item> getAlchemyInputs( Item similar ){
		ArrayList<Item> result = new ArrayList<>();

		for (Item item : this) {
			Item alchemyVer = item.replaceForAlchemy();
			if (item != similar && alchemyVer != null && similar.isSimilar(alchemyVer)) {
				result.add(alchemyVer);
			}
		}

		return result;
	}
	
	public void identify() {
		for (Item item : this) {
			item.identify();
		}
	}
	
	public void observe() {

		for (int i = 0; i < owner.miscSlots(); i++) {
			if (miscs[i] != null) {
				miscs[i].identify();
				Badges.validateItemLevelAquired(miscs[i]);
			}
		}

		for (Item item : backpack) {
			if (item instanceof EquipableItem) {
				item.cursedKnown = true;
			}
		}
	}
	
	public void uncurseEquipped() {
		ScrollOfRemoveCurse.uncurse( owner, miscs[0], miscs[1], miscs[2]);
	}
	
	public Item randomUnequipped() {
		return Random.element( backpack.items );
	}
	
	public void resurrect(String levelKey) {

		for (Item item : backpack.items.toArray( new Item[0])) {
			if (item instanceof Key) {
				if (((Key) item).levelKey.equals(levelKey)) {
					item.detachAll( backpack );
				}
			} else if (item.unique) {
				item.detachAll(backpack);
				//you keep the bag itself, not its contents.
				if (item instanceof Bag){
					((Bag)item).resurrect();
				}
				item.collect();
			} else if (!item.isEquipped( owner )) {
				item.detachAll( backpack );
			}
		}

		for (int i = 0; i < owner.miscSlots(); i++) {//Restore all miscs
			if (miscs[i] != null) {
				miscs[i].cursed = false;
				miscs[i].activate( owner );
			}
		}
	}
	
	public int charge( float charge ) {
		
		int count = 0;
		
		for (Wand.Charger charger : owner.buffs(Wand.Charger.class)){
			charger.gainCharge(charge);
			count++;
		}
		
		return count;
	}

	@NotNull
	@Override
	public Iterator<Item> iterator() {
		return new ItemIterator();
	}
	
	private class ItemIterator implements Iterator<Item> {

		private int index = 0;
		
		private Iterator<Item> backpackIterator = backpack.iterator();
		
		private Item[] equipped = miscs.clone();
		private int backpackIndex = equipped.length;
		
		@Override
		public boolean hasNext() {
			
			for (int i=index; i < backpackIndex; i++) {
				if (equipped[i] != null) {
					return true;
				}
			}
			
			return backpackIterator.hasNext();
		}

		@Override
		public Item next() {
			
			while (index < backpackIndex) {
				Item item = equipped[index++];
				if (item != null) {
					return item;
				}
			}
			
			return backpackIterator.next();
		}

		@Override
		public void remove() {
			switch (index) {
				case 0:
					equipped[0] = miscs[0] = null;
					break;
				case 1:
					equipped[1] = miscs[1] = null;
					break;
				case 2:
					equipped[2] = miscs[2] = null;
					break;
				case 3:
					equipped[3] = miscs[3] = null;
					break;
				case 4:
					equipped[4] = miscs[4] = null;
					break;
				case 5:
					if (miscs.length > 5 && equipped.length > 5) {
						equipped[5] = miscs[5] = null;
					}
					break;
				default:
					backpackIterator.remove();
			}
		}
	}
}

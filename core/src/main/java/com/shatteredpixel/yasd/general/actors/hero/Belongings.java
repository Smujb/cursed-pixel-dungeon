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
import com.shatteredpixel.yasd.general.GamesInProgress;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Berserk;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Fury;
import com.shatteredpixel.yasd.general.actors.buffs.Invisibility;
import com.shatteredpixel.yasd.general.actors.buffs.Momentum;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.EquipableItem;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindOfWeapon;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.items.armor.Armor;
import com.shatteredpixel.yasd.general.items.armor.curses.Bulk;
import com.shatteredpixel.yasd.general.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.yasd.general.items.armor.glyphs.Brimstone;
import com.shatteredpixel.yasd.general.items.armor.glyphs.Flow;
import com.shatteredpixel.yasd.general.items.armor.glyphs.Obfuscation;
import com.shatteredpixel.yasd.general.items.armor.glyphs.Stone;
import com.shatteredpixel.yasd.general.items.armor.glyphs.Swiftness;
import com.shatteredpixel.yasd.general.items.artifacts.CapeOfThorns;
import com.shatteredpixel.yasd.general.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.general.items.bags.Bag;
import com.shatteredpixel.yasd.general.items.keys.Key;
import com.shatteredpixel.yasd.general.items.rings.RingOfFuror;
import com.shatteredpixel.yasd.general.items.rings.RingOfHaste;
import com.shatteredpixel.yasd.general.items.rings.RingOfTenacity;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.yasd.general.items.wands.Wand;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
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

	@Nullable
	private KindOfWeapon weapon;
	@Nullable
	public Armor armor;
	public KindofMisc[] miscs;

	public Belongings( Char owner ) {
		this.owner = owner;

		backpack = new Bag() {{
			name = Messages.get(Bag.class, "name");
			size = BACKPACK_SIZE;
		}};
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

	public boolean shoot(Char enemy, MissileWeapon wep) {

		//temporarily set the hero's weapon to the missile weapon being used
		KindOfWeapon equipped = weapon;
		weapon = wep;
		boolean hit = owner.attack(enemy);
		Invisibility.dispel();
		weapon = equipped;

		return hit;
	}

	@Nullable
	public KindOfWeapon getWeapon() {
	    return weapon;
	}

	public void setWeapon(@Nullable KindOfWeapon weapon) {
		this.weapon = weapon;
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

	public int magicalDR() {
		int dr = 0;
		int armDr = 0;
		if (armor != null) {
			 armDr = armor.magicalDRRoll();
		}
		if (owner.STR() < armor.STRReq()) {
			armDr -= 2 * (armor.STRReq() - owner.STR());
		}
		if (armDr > 0) dr += armDr;
		if (armor != null && armor.hasGlyph(AntiMagic.class, owner)) {
			dr += AntiMagic.drRoll(armor.level());
		}

		return dr;
	}

	public int drRoll() {
		int dr = 0;
		if (armor != null) {
			dr += armor.DRRoll();
			if (owner.STR() < armor.STRReq()) {
				dr -= 2 * (armor.STRReq() - owner.STR());
			}
		}

		if (weapon != null) {
			int wepDr = Random.NormalIntRange(0, weapon.defenseFactor(owner));
			if (weapon instanceof MeleeWeapon & owner.STR() < ((MeleeWeapon) weapon).STRReq()) {
				wepDr -= 2 * (((MeleeWeapon) weapon).STRReq() - owner.STR());
			}
			if (wepDr > 0) dr += wepDr;
		}

		return dr;
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
		if (armor != null) {
			damage = armor.proc(enemy, owner, damage);
		}
		return damage;
	}

	public int attackProc(Char enemy, int damage) {
		KindOfWeapon wep = getWeapon();

		if (wep != null) damage = wep.proc(owner, enemy, damage);
		return damage;
	}

	public int magicalAttackProc(Char enemy, int damage) {
		return damage;
	}

	public int magicalDefenseProc(Char enemy, int damage) {
		if (armor != null) {
			damage = armor.magicalProc(enemy, owner, damage);
		}
		return damage;
	}

	public boolean canSurpriseAttack() {
		KindOfWeapon curWep = getWeapon();
		if (!(curWep instanceof Weapon)) return true;
		if (owner.STR() < ((Weapon) curWep).STRReq()) return false;
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
		if (weapon != null) {
			return weapon.speedFactor(owner) * multiplier;//Two weapons = 1/2 attack speed
		} else {
			//Normally putting furor speed on unarmed attacks would be unnecessary
			//But there's going to be that one guy who gets a furor+force ring combo
			//This is for that one guy, you shall get your fists of fury!
			return RingOfFuror.attackDelayMultiplier(owner);
		}
	}


	public float affectEvasion(float evasion) {
		//evasion *= _Unused.evasionMultiplier(owner);
		Armor CurArmour = armor;
		//evasion *= CurArmour.evasionMultiplier(ownerID);
		if (CurArmour != null) {
			if (CurArmour.hasGlyph(Stone.class, owner) && !((Stone) CurArmour.glyph).testingEvasion()) {
				return 0;
			}
			int aEnc = CurArmour.STRReq() - owner.STR();
			if (aEnc > 0) evasion /= Math.pow(1.5, aEnc);

			Momentum momentum = owner.buff(Momentum.class);
			if (momentum != null) {
				evasion += momentum.evasionBonus(Math.max(0, -aEnc));

			}
			evasion += CurArmour.augment.evasionFactor(CurArmour.level());

			evasion *= CurArmour.EVA;
		}

		return evasion;
	}

	public float affectSpeed(float speed) {
		speed *= RingOfHaste.speedMultiplier(owner);
		Armor CurArmour = armor;
		//speed *= CurArmour.speedMultiplier(ownerID);
		if (armor != null) {
			int aEnc = CurArmour.STRReq() - owner.STR();
			if (aEnc > 0) speed /= Math.pow(1.2, aEnc);
			if (CurArmour.hasGlyph(Swiftness.class, owner)) {
				boolean enemyNear = false;
				for (Char ch : Actor.chars()) {
					if (Dungeon.level.adjacent(ch.pos, owner.pos) && owner.alignment != ch.alignment) {
						enemyNear = true;
						break;
					}
				}
				if (!enemyNear) speed *= (1.2f + 0.04f * CurArmour.level());
			} else if (CurArmour.hasGlyph(Flow.class, owner) && Dungeon.level.liquid(owner.pos)) {
				speed *= 2f;
			}

			if (CurArmour.hasGlyph(Bulk.class, owner) &&
					(Dungeon.level.getTerrain(owner.pos) == Terrain.DOOR
							|| Dungeon.level.getTerrain(owner.pos) == Terrain.OPEN_DOOR)) {
				speed /= 3f;
			}
			speed *= CurArmour.speedFactor;
		}


		return speed;
	}

	public float affectStealth(float stealth) {
		Armor CurArmour = armor;
		//sneakSkill *= CurArmour.stealthMultiplier(ownerID);
		if (CurArmour != null) {
			if (CurArmour.hasGlyph(Obfuscation.class, owner)) {
				stealth += 1 + CurArmour.level() / 3f;
			}
			int penalty = CurArmour.tier;


			if (owner instanceof Hero) {
				if (((Hero) owner).heroClass == HeroClass.ROGUE) {
					penalty /= 2;
				}
			}
			stealth -= penalty;

			stealth *= CurArmour.STE;
		}

		return stealth;
	}

	public float affectPerception(float perception) {
		return perception;
	}

	public boolean isImmune(Class effect) {
		return effect == Burning.class
				&& armor != null
				&& armor.hasGlyph(Brimstone.class, owner);
	}

	//##############################################################################################
	//####################### End of stuff for handling chars with belongings ######################
	//##############################################################################################

	private static final String ARMOR		= "armor";
	private static final String WEAPON		= "weapon";
	private static final String MISC        = "misc";


	public void storeInBundle( Bundle bundle ) {
		
		backpack.storeInBundle( bundle );
		for (int i = 0; i < owner.miscSlots(); i++) {//Store all miscs
			bundle.put( MISC + i, miscs[i]);
		}
		bundle.put(ARMOR, armor);
		bundle.put(WEAPON, weapon);
	}
	
	public void restoreFromBundle( Bundle bundle ) {
		
		backpack.clear();
		backpack.restoreFromBundle( bundle );
		armor = (Armor) bundle.get(ARMOR);
		weapon = (KindOfWeapon) bundle.get(WEAPON);
		if (weapon != null) {
			weapon.activate( owner );
		}
		if (armor != null) {
			armor.activate( owner );
		}
		miscs = new KindofMisc[owner.miscSlots()];
		for (int i = 0; i < owner.miscSlots(); i++) {//Restore all miscs
			miscs[i] = (KindofMisc) bundle.get(MISC + i);
			if (miscs[i] != null) {
				miscs[i].activate( owner );
			}
		}
	}
	
	static void preview(GamesInProgress.Info info, Bundle bundle) {
		if (bundle.contains( ARMOR )){
			info.armorTier = ((Armor)bundle.get( ARMOR )).tier;
		} else {
			info.armorTier = 0;
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
	
	public void identify() {
		for (Item item : this) {
			item.identify();
		}
	}
	
	public void observe() {

		if (weapon != null) {
			weapon.identify();
		}

		if (armor != null) {
			armor.identify();
		}

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
		
		private Item[] equipped = { weapon, armor, miscs[0], miscs[1],  miscs[2] };
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
					equipped[0] = weapon = null;
					break;
				case 1:
					equipped[1] = armor = null;
					break;
				case 2:
					equipped[2] = miscs[0] = null;
					break;
				case 3:
					equipped[3] = miscs[1] = null;
					break;
				case 4:
					equipped[4] = miscs[2] = null;
					break;
				default:
					backpackIterator.remove();
			}
		}
	}
}

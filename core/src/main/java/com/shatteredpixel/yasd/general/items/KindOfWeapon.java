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

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.utils.BArray;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

abstract public class KindOfWeapon extends EquipableItem {
	
	protected static final float TIME_TO_EQUIP = 1f;

	protected String hitSound = Assets.Sounds.HIT;
	protected float hitSoundPitch = 1f;

	public boolean canSurpriseAttack = true;

	public boolean breaksArmor(Char owner) {
		return false;
	}

	public int min(){
		return min(power());
	}

	public int max(){
		return max(power());
	}

	@Override
	public boolean doEquip( Hero hero ) {

		detachAll( hero.belongings.backpack );

		if (hero.belongings.getWeapon() == null || hero.belongings.getWeapon().doUnequip( hero, true )) {

			hero.belongings.setWeapon(this);
			activate( hero );

			updateQuickslot();

			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( Messages.get(KindOfWeapon.class, "equip_cursed") );
			}

			hero.spendAndNext( TIME_TO_EQUIP );
			return true;

		} else {

			collect( hero.belongings.backpack, hero );
			return false;
		}
	}

	@Override
	public boolean doUnequip(Char ch, boolean collect, boolean single ) {
		if (super.doUnequip( ch, collect, single )) {

			ch.belongings.setWeapon(null);
			return true;

		} else {

			return false;

		}
	}

	public boolean doAttack(Char attacker, Char enemy) {
		attacker.busy();
		if (attacker.sprite != null && (attacker.sprite.visible || enemy.sprite.visible)) {
			attacker.sprite.attack(enemy.pos, new Callback() {
				@Override
				public void call() {
					attack(attacker, enemy, false);
					attacker.next();
				}
			});
			attacker.spend( attacker.attackDelay() );
			return false;
		} else {
			attack(attacker, enemy, false);
			attacker.spend( attacker.attackDelay() );
			return true;
		}
	}

	public boolean attack(Char attacker, Char enemy, boolean guaranteed) {
		return attacker.attack(enemy, guaranteed, Char.AttackType.NORMAL);
	}

	@Override
	public boolean isEquipped(@NotNull Char owner) {
		return owner.belongings.getWeapon() == this;
	}

	abstract public int min(float lvl);
	abstract public int max(float lvl);

	public int damageRoll( Char owner ) {
		return Random.NormalIntRange(min(), max());
	}
	
	public float accuracyFactor( Char owner ) {
		return 1f;
	}
	
	public float speedFactor( Char owner ) {
		return 1f;
	}

	public int reachFactor( Char owner ){
		return 1;
	}
	
	public boolean canReach( Char owner, int target){
		if (Dungeon.level.distance( owner.pos, target ) > reachFactor(owner)){
			return false;
		} else {
			boolean[] passable = BArray.not(Dungeon.level.solid(), null);
			for (Char ch : Actor.chars()) {
				if (ch != owner) passable[ch.pos] = false;
			}
			
			PathFinder.buildDistanceMap(target, passable, reachFactor(owner));
			
			return PathFinder.distance[owner.pos] <= reachFactor(owner);
		}
	}

	public int defenseFactor( Char owner ) {
		return 0;
	}
	
	public int proc( Char attacker, Char defender, int damage ) {
		return damage;
	}

	public void hitSound( float pitch ){
		Sample.INSTANCE.play(hitSound, 1, pitch * hitSoundPitch);
	}
}

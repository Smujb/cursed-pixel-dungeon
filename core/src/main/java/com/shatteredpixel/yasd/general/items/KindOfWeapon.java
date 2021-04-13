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
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.CellSelector;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.utils.BArray;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

abstract public class KindOfWeapon extends KindofMisc {

	{
		defaultAction = AC_ATTACK;
	}

	protected String hitSound = Assets.Sounds.HIT;
	protected float hitSoundPitch = 1f;

	public boolean canSurpriseAttack = true;
	public boolean sneakBenefit = false;
	public boolean canBeParried = true;

	public boolean breaksArmor(Char owner) {
		return false;
	}

	public int min(){
		return min(power());
	}

	public int max(){
		return max(power());
	}

	private static final String AC_ATTACK = "attack";

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_ATTACK);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_ATTACK)) {
			if (isEquipped(curUser)) {
				GameScene.selectCell(ATTACK);
			} else {
				GLog.info(Messages.get(KindOfWeapon.this, "not_equipped"));
			}
		}
	}

	private final CellSelector.Listener ATTACK = new CellSelector.Listener() {

		@Override
		public void onSelect(Integer cell) {
			if (cell != null && curUser != null) {
				Char ch = Actor.findChar(cell);
				if (ch != null) {
					if (KindOfWeapon.this.canAttack(cell)) {
						KindOfWeapon.this.doAttack(curUser, ch);
					} else {
						GLog.info(Messages.get(KindOfWeapon.this, "out_of_range"));
					}
				} else {
					GLog.info(Messages.get(KindOfWeapon.this, "no_enemy"));
				}
			}
		}

		@Override
		public String prompt() {
			return Messages.get(KindOfWeapon.this, "select_cell");
		}
	};

	public boolean doAttack(Char attacker, Char enemy) {
		attacker.busy();
		if (attacker.sprite != null && (attacker.sprite.visible || enemy.sprite.visible)) {
			attacker.spend( this.speedFactor(attacker) );
			attacker.sprite.attack(enemy.pos, new Callback() {
				@Override
				public void call() {
					fx(attacker, enemy.pos, new Callback() {
						@Override
						public void call() {
							attack(attacker, enemy, false);
							attacker.next();
						}
					});
				}
			});
			return false;
		} else {
			attack(attacker, enemy, false);
			attacker.spend( this.speedFactor(attacker) );
			attacker.next();
			return true;
		}
	}

	public boolean attack(Char attacker, Char enemy, boolean guaranteed) {
		Char.DamageSrc src = new Char.DamageSrc(Element.PHYSICAL, this);
		if (breaksArmor(attacker)) src.ignoreDefense();
		if (!canBeParried) src.breakShields();

		int damage = damageRoll(attacker);
		damage = proc(attacker, enemy, damage);
		boolean attack = attacker.attack(enemy, guaranteed, damage, src);
		if (attack) {
			hitSound(Random.Float(0.87f, 1.15f));
		}
		return attack;
	}

	protected boolean canAttack(int pos) {
		return canReach(curUser, pos);
	}

	public void fx(Char attacker, int pos, Callback callback) {
		callback.call();
	}

	abstract public int min(float lvl);
	abstract public int max(float lvl);

	public int damageRoll( Char owner ) {
		return affectDamage(Random.NormalIntRange(min(), max()));
	}

	public int affectDamage(int damage) {
		if (sneakBenefit) {
			Char enemy = null;
			float bonus = 0;
			if (curUser instanceof Hero) {
				enemy = ((Hero) curUser).enemy();
			} else if (curUser instanceof Mob) {
				enemy = ((Mob) curUser).getEnemy();
			}
			if (enemy != null) {
				bonus = 1f-enemy.noticeChance(curUser);
			}
			if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(curUser) && curUser.canSurpriseAttack()) {
				damage *= (2 + bonus);
				if (damage < max()) {
					damage = max();
				}
			}
		}
		return damage;
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

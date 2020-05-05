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

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.MagicImmune;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Imp;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.general.sprites.GolemSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Golem extends Mob {
	
	{
		spriteClass = GolemSprite.class;


		healthFactor = 2f;
		damageFactor = 1.5f;

		attackDelay = 1.5f;
		
		EXP = 12;
		
		properties.add(Property.INORGANIC);
		properties.add(Property.LARGE);

		WANDERING = new Wandering();
		HUNTING = new Hunting();
	}
	
	@Override
	public void rollToDropLoot() {
		Imp.Quest.process( this );
		
		super.rollToDropLoot();
	}

	private boolean teleporting = false;
	private int selfTeleCooldown = 0;
	private int enemyTeleCooldown = 0;

	private static final String TELEPORTING = "vent_cooldown";
	private static final String SELF_COOLDOWN = "self_cooldown";
	private static final String ENEMY_COOLDOWN = "vent_cooldown";

	@Override
	public void storeInBundle( Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(TELEPORTING, teleporting);
		bundle.put(SELF_COOLDOWN, selfTeleCooldown);
		bundle.put(ENEMY_COOLDOWN, enemyTeleCooldown);
	}

	@Override
	public void restoreFromBundle( Bundle bundle) {
		super.restoreFromBundle(bundle);
		teleporting = bundle.getBoolean( TELEPORTING );
		selfTeleCooldown = bundle.getInt( SELF_COOLDOWN );
		enemyTeleCooldown = bundle.getInt( ENEMY_COOLDOWN );
	}

	@Override
	protected boolean act() {
		selfTeleCooldown--;
		enemyTeleCooldown--;
		if (teleporting){
			((GolemSprite)sprite).teleParticles(false);
			if (Actor.findChar(target) == null && Dungeon.level.openSpace(target)) {
				ScrollOfTeleportation.appear(this, target);
				selfTeleCooldown = 30;
			} else {
				target = Dungeon.level.randomDestination(this);
			}
			teleporting = false;
			spend(TICK);
			return true;
		}
		return super.act();
	}

	public void teleportEnemy(Char enemy){
		MagicMissile.boltFromChar(sprite.parent,
				MagicMissile.TOXIC_VENT,
				sprite,
				enemy.pos,
				new Callback() {
					@Override
					public void call() {
						spend(TICK);

						int bestPos = enemy.pos;
						for (int i : PathFinder.NEIGHBOURS8){
							if (Dungeon.level.passable(pos + i)
									&& Actor.findChar(pos+i) == null
									&& Dungeon.level.trueDistance(pos+i, enemy.pos) > Dungeon.level.trueDistance(bestPos, enemy.pos)){
								bestPos = pos+i;
							}
						}

						if (enemy.buff(MagicImmune.class) != null){
							bestPos = enemy.pos;
						}

						if (bestPos != enemy.pos){
							ScrollOfTeleportation.appear(enemy, bestPos);
							if (enemy instanceof Hero){
								((Hero) enemy).interrupt();
								Dungeon.observe();
							}
						}

						enemyTeleCooldown = 20;
					}
				});
	}

	private class Wandering extends Mob.Wandering{

		@Override
		protected boolean continueWandering() {
			enemySeen = false;

			int oldPos = pos;
			if (target != -1 && getCloser( target )) {
				spend( 1 / speed() );
				return moveSprite( oldPos, pos );
			} else if (target != -1 && target != pos && selfTeleCooldown <= 0) {
				((GolemSprite)sprite).teleParticles(true);
				teleporting = true;
				spend( 2*TICK );
			} else {
				target = Dungeon.level.randomDestination( Golem.this );
				spend( TICK );
			}

			return true;
		}
	}

	private class Hunting extends Mob.Hunting{

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			if (!enemyInFOV || canAttack(enemy)) {
				return super.act(enemyInFOV, justAlerted);
			} else {
				enemySeen = true;
				target = enemy.pos;

				int oldPos = pos;

				if (enemyTeleCooldown <= 0 && Random.Int(100/distance(enemy)) == 0
						&& !Char.hasProp(enemy, Property.IMMOVABLE)){
					if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
						sprite.zap( enemy.pos );
						return false;
					} else {
						teleportEnemy(enemy);
						return true;
					}

				} else if (getCloser( target )) {
					spend( 1 / speed() );
					return moveSprite( oldPos,  pos );

				} else if (enemyTeleCooldown <= 0 && !Char.hasProp(enemy, Property.IMMOVABLE)) {
					if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
						sprite.zap( enemy.pos );
						return false;
					} else {
						teleportEnemy(enemy);
						return true;
					}

				} else {
					spend( TICK );
					return true;
				}

			}
		}
	}
}

/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Cursed Pixel Dungeon
 *   Copyright (C) 2014-2020 Samuel Braithwaite
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Light;
import com.shatteredpixel.yasd.general.effects.Pushing;
import com.shatteredpixel.yasd.general.effects.TargetedCell;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.RipperSprite;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class RipperDemon extends Mob {

	{
		spriteClass = RipperSprite.class;
		viewDistance = Light.DISTANCE;

		healthFactor = 0.5f;
		damageFactor = 1.4f;
		accuracyFactor = 2f;
		attackDelay = 0.5f;
		evasionFactor = 0.5f;

        HUNTING = new Hunting();

		baseSpeed = 1f;
		properties.add(Property.DEMONIC);
		properties.add(Property.UNDEAD);
	}

	@Override
	public float spawningWeight() {
		return 0;
	}

    @Override
	public float attackDelay() {
		return super.attackDelay()*0.5f;
	}

	private static final String LAST_ENEMY_POS = "last_enemy_pos";
	private static final String LEAP_POS = "leap_pos";
	private static final String LEAP_CD = "leap_cd";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(LAST_ENEMY_POS, lastEnemyPos);
		bundle.put(LEAP_POS, leapPos);
		bundle.put(LEAP_CD, leapCooldown);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		//pre beta-5.0
		lastEnemyPos = bundle.getInt(LAST_ENEMY_POS);
		leapPos = bundle.getInt(LEAP_POS);
		leapCooldown = bundle.getFloat(LEAP_CD);
	}

	private int lastEnemyPos = -1;

	@Override
	protected boolean act() {
		AiState lastState = state;
		boolean result = super.act();
		if (paralysed <= 0) leapCooldown --;

		//if state changed from wandering to hunting, we haven't acted yet, don't update.
		if (!(lastState == WANDERING && state == HUNTING)) {
			if (enemy != null) {
				lastEnemyPos = enemy.pos;
			} else {
				lastEnemyPos = Dungeon.hero.pos;
			}
		}

		return result;
	}

	private int leapPos = -1;
	private float leapCooldown = 0;

	public class Hunting extends Mob.Hunting {

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {

			if (leapPos != -1){
				//do leap
				sprite.visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos];
				sprite.jump(pos, leapPos, new Callback() {
					@Override
					public void call() {

						Char ch = Actor.findChar(leapPos);
						if (ch != null){
							if (alignment != ch.alignment){
								Buff.affect(ch, Bleeding.class).set(0.75f*damageRoll());
								ch.sprite.flash();
								Sample.INSTANCE.play(Assets.Sounds.HIT);
							}
							//bounce to a random safe pos(if possible)
							int bouncepos = -1;
							for (int i : PathFinder.NEIGHBOURS8){
								if ((bouncepos == -1 || Dungeon.level.trueDistance(pos, leapPos+i) < Dungeon.level.trueDistance(pos, bouncepos))
										&& Actor.findChar(leapPos+i) == null && Dungeon.level.passable(leapPos+i)){
									bouncepos = leapPos+i;
								}
							}
							if (bouncepos != -1) {
								pos = bouncepos;
								Actor.addDelayed(new Pushing(RipperDemon.this, leapPos, bouncepos), -1);
							} else {
								pos = leapPos;
							}
						} else {
							pos = leapPos;
						}

						leapPos = -1;
						leapCooldown = Random.NormalIntRange(2, 4);
						sprite.idle();
						Dungeon.level.occupyCell(RipperDemon.this);
						next();
					}
				});
				return false;
			}

			enemySeen = enemyInFOV;
			if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

				return doAttack( enemy );

			} else {

				if (enemyInFOV) {
					target = enemy.pos;
				} else if (enemy == null) {
					state = WANDERING;
					target = Dungeon.level.randomDestination( RipperDemon.this );
					return true;
				}

				if (leapCooldown <= 0 && enemyInFOV && Dungeon.level.distance(pos, enemy.pos) >= 3) {

					int targetPos = enemy.pos;
					if (lastEnemyPos != enemy.pos){
						int closestIdx = 0;
						for (int i = 1; i < PathFinder.CIRCLE8.length; i++){
							if (Dungeon.level.trueDistance(lastEnemyPos, enemy.pos+PathFinder.CIRCLE8[i])
									< Dungeon.level.trueDistance(lastEnemyPos, enemy.pos+PathFinder.CIRCLE8[closestIdx])){
								closestIdx = i;
							}
						}
						targetPos = enemy.pos + PathFinder.CIRCLE8[(closestIdx+4)%8];
					}

					Ballistica b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_TERRAIN);
					//try aiming directly at hero if aiming near them doesn't work
					if (b.collisionPos != targetPos && targetPos != enemy.pos){
						targetPos = enemy.pos;
						b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_TERRAIN);
					}
					if (b.collisionPos == targetPos){
						//get ready to leap
						leapPos = targetPos;
						//don't want to overly punish players with slow move or attack speed
						spend(GameMath.gate(TICK, enemy.cooldown(), 3*TICK));
						if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos]){
							GLog.warning(Messages.get(RipperDemon.this, "leap"));
							sprite.parent.addToBack(new TargetedCell(leapPos, Constants.Colours.PURE_RED));
							Dungeon.hero.interrupt();
							((RipperSprite)sprite).leapPrep( leapPos );

						}
						return true;
					}
				}

				int oldPos = pos;
				if (target != -1 && getCloser( target )) {

					spend( 1 / speed() );
					return moveSprite( oldPos,  pos );

				} else {
					spend( TICK );
					if (!enemyInFOV) {
						sprite.showLost();
						state = WANDERING;
						target = Dungeon.level.randomDestination( RipperDemon.this );
					}
					return true;
				}
			}
		}

	}

}

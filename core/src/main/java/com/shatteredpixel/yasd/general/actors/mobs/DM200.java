/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Yet Another Shattered Dungeon
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

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.CorrosiveGas;
import com.shatteredpixel.yasd.general.actors.blobs.ToxicGas;
import com.shatteredpixel.yasd.general.actors.buffs.Corrosion;
import com.shatteredpixel.yasd.general.items.quest.MetalShard;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.DM200Sprite;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class DM200 extends Mob {

	{
		spriteClass = DM200Sprite.class;

		evasionFactor = 0.6f;
		damageFactor = 1.5f;
		healthFactor = 2f;

		EXP = 9;
		maxLvl = 17;

		//TODO loot?

		properties.add(Property.INORGANIC);
		properties.add(Property.LARGE);

		HUNTING = new Hunting();
	}

	@Override
	public Element elementalType() {
		return beside(enemy) ? Element.PHYSICAL : Element.TOXIC;
	}

	private int ventCooldown = 0;

	private static final String VENT_COOLDOWN = "vent_cooldown";

	@Override
	public void storeInBundle( Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(VENT_COOLDOWN, ventCooldown);
	}

	@Override
	public void restoreFromBundle( Bundle bundle) {
		super.restoreFromBundle(bundle);
		ventCooldown = bundle.getInt( VENT_COOLDOWN );
	}

	@Override
	protected boolean act() {
		ventCooldown--;
		return super.act();
	}

	@Override
	public boolean attack(Char enemy, boolean guaranteed) {
		if (beside(enemy)) {
			return super.attack(enemy, guaranteed);
		} else {
			return zap(enemy);
		}
	}

	private boolean beside(Char enemy) {
		return Dungeon.level.adjacent(enemy.pos, pos);
	}

	protected boolean zap(Char enemy){
		spend( TICK );
		ventCooldown = 30;

		Ballistica trajectory = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET);

		for (int i : trajectory.subPath(0, trajectory.dist)){
			GameScene.add(Blob.seed(i, 20, ToxicGas.class));
		}

		GLog.w(Messages.get(this, "vent"));
		GameScene.add(Blob.seed(trajectory.collisionPos, 100, ToxicGas.class));
		return true;
	}

	private boolean canZap() {
		return ventCooldown <= 0 && Ballistica.canHit(DM200.this, enemy, shotType);
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

				if (canZap() && Random.Int(100/distance(enemy)) == 0){
					if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
						sprite.attack( enemy.pos );
						return false;
					} else {
						attack(enemy);
						return true;
					}

				} else if (getCloser( target )) {
					spend( 1 / speed() );
					return moveSprite( oldPos,  pos );

				} else if (canZap()) {
					if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
						sprite.attack( enemy.pos );
						return false;
					} else {
						attack(enemy);
						return true;
					}

				} else {
					spend( TICK );
					return true;
				}

			}
		}
	}

	public static class DM201 extends DM200 {

		{
			spriteClass = DM200Sprite.DM201Sprite.class;

			healthFactor = 2f;

			properties.add(Property.IMMOVABLE);

			HUNTING = new Mob.Hunting();
		}

		private boolean threatened = false;

		@Override
		protected boolean act() {
			GameScene.add(Blob.seed(pos, 0, CorrosiveGas.class));
			if (state == HUNTING && enemy != null && enemySeen
					&& threatened && !Dungeon.level.adjacent(pos, enemy.pos)){
				enemySeen = enemy.isAlive() && fieldOfView[enemy.pos] && enemy.invisible <= 0;
				if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
					sprite.attack( enemy.pos );
					return false;
				} else {
					zap(enemy);
					return true;
				}
			}
			return super.act();
		}

		@Override
		public void damage(int dmg, DamageSrc src) {
			if ((src.getCause() instanceof Char && !Dungeon.level.adjacent(pos, ((Char)src.getCause()).pos))
					|| enemy == null || !Dungeon.level.adjacent(pos, enemy.pos)){
				threatened = true;
			}
			super.damage(dmg, src);
		}

		@Override
		protected boolean zap(Char enemy){
			threatened = false;
			spend(TICK);

			GLog.w(Messages.get(this, "vent"));
			GameScene.add(Blob.seed(enemy.pos, 15, CorrosiveGas.class).setStrength(Corrosion.defaultStrength(Dungeon.getScaleFactor())));
			for (int i : PathFinder.NEIGHBOURS8){
				if (!Dungeon.level.solid(enemy.pos+i)) {
					GameScene.add(Blob.seed(enemy.pos + i, 5, CorrosiveGas.class).setStrength(Corrosion.defaultStrength(Dungeon.getScaleFactor())));
				}
			}
			return true;
		}

		@Override
		protected boolean getCloser(int target) {
			return true;
		}

		@Override
		protected boolean getFurther(int target) {
			return true;
		}

		@Override
		public void rollToDropLoot() {
			if (Dungeon.hero.lvl > maxLvl + 2) return;

			super.rollToDropLoot();

			int ofs;
			do {
				ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
			} while (!Dungeon.level.passable(pos + ofs));
			Dungeon.level.drop( new MetalShard(), pos + ofs ).sprite.drop( pos );
		}

	}

}

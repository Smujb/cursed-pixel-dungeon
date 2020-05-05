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

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.effects.Beam;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.effects.particles.SparkParticle;
import com.shatteredpixel.yasd.general.items.artifacts.LloydsBeacon;
import com.shatteredpixel.yasd.general.items.keys.SkeletonKey;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.yasd.general.items.wands.WandOfBlastWave;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.MobSprite;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.shatteredpixel.yasd.general.ui.BossHealthBar;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TestBoss extends Mob {
	{
		spriteClass = TestBossSprite.class;

		HP = HT = 200;
		EXP = 10;
		defenseSkill = 5;

		viewDistance = 20;
		state = HUNTING;
		properties.add(Property.BOSS);
		properties.add(Property.INORGANIC);
	}

	private int time_to_summon = 0;
	private int MAX_COOLDOWN = 20;

	private boolean hint = true;

	public static ArrayList<Integer> towerPositions = new  ArrayList<>();

	private int numBolts() {
		return (int) (6 + (1 - (HP/(float)HT))*8);
	}

	private boolean canSummon() {
		return time_to_summon < 1;
	}

	@Override
	public int damageRoll() {
		return (int) (Random.NormalIntRange( 3, 12 ));
	}

	public boolean checkTowers() {
		for (Mob mob : Dungeon.level.mobs) {
			if (mob instanceof Tower) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int attackSkill( Char target ) {
		return 16;
	}

	@Override
	public void damage(int dmg,  DamageSrc src) {
		if (src.getCause() instanceof Tower) {
			super.damage(dmg, src);
			if (hint) {
				hint = false;
				GLog.p("The boss takes heavy damage from the disintegration rays!");
			}
		} else {
			if (HP > HT/2 || checkTowers()) {
				super.damage(Random.Int(dmg / 2), src);
			} else {
				super.damage(0, src);//Display that no damage is being done any more
			}
			if (Random.Int(10) == 0 || HP == HT) {
				GLog.n("The boss is too strong to be damaged significantly by your weapons...");
			}
		}
	}

	@Override
	protected boolean act() {
		time_to_summon--;
		if (canSummon()) {
			zap();
		}
		if (!checkTowers()) {
			sprite.add(CharSprite.State.BERSERK);
		} else {
			sprite.remove(CharSprite.State.BERSERK);
		}
		return super.act();
	}

	@Override
	public float speed() {
		float speed = super.speed();
		if (!checkTowers()) {
			speed *= 1.5f;
		}
		return speed;
	}

	@Override
	public void notice() {
		super.notice();
		BossHealthBar.assignBoss(this);
		yell( Messages.get(this, "notice") );
	}

	private static final String POSITIONS = "towerPositions";
	private static final String HINT = "hint";

	@Override
	public void storeInBundle( Bundle bundle) {
		super.storeInBundle(bundle);
		for (int i = 0; i < towerPositions.size(); i++) {
			bundle.put(POSITIONS+i, towerPositions.get(i));
		}
		bundle.put(HINT, hint);
	}

	@Override
	public void restoreFromBundle( Bundle bundle) {
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
		for (int i = 0; i < towerPositions.size(); i++) {
			towerPositions.set(i, bundle.getInt(POSITIONS+i));
		}
		hint = bundle.getBoolean(HINT);
	}


	public void zap() {
		time_to_summon = MAX_COOLDOWN;
		towerPositions.clear();
		int[] positions = new  int[numBolts()];
		for (int i = 0; i < positions.length; i++) {
			positions[i] = Dungeon.level.randomRespawnCell(this);
		}
		for (final int i : positions) {
			MagicMissile.boltFromChar( sprite.parent,
					MagicMissile.SHADOW,
					this.sprite,
					i,
					new  Callback() {
						@Override
						public void call() {
							Mob tower = Mob.spawnAt(Tower.class, i);
							if (tower != null) {
								tower.spend(5f);
							}
							TestBoss.towerPositions.add(i);
						}
					} );
			Sample.INSTANCE.play( Assets.SND_ZAP );
		}
	}

	@Override
	public void die( DamageSrc cause ) {

		super.die( cause );

		GameScene.bossSlain();
		Dungeon.level.drop( new  SkeletonKey( Dungeon.key ), pos ).sprite.drop();

		//60% chance of 2 shards, 30% chance of 3, 10% chance for 4. Average of 2.5
		int blobs = Random.chances(new  float[]{0, 0, 6, 3, 1});
		for (int i = 0; i < blobs; i++){
			int ofs;
			do {
				ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
			} while (!Dungeon.level.passable(pos + ofs));
			Dungeon.level.drop( new  ScrollOfUpgrade(), pos + ofs ).sprite.drop( pos );
		}


		Badges.validateBossSlain();

		LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
		if (beacon != null) {
			beacon.upgrade();
		}

		yell( Messages.get(this, "defeated") );
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (!checkTowers()) {
			//trace a ballistica to our target (which will also extend past them
			Ballistica trajectory = new  Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET);
			//trim it to just be the part that goes past them
			trajectory = new  Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
			//knock them back along that ballistica
			WandOfBlastWave.throwChar(enemy, trajectory, 2);
			damage *= 2;
		}
		return super.attackProc(enemy, damage);
	}

	static public class Tower extends Mob {
		{
			spriteClass = LitTowerSprite.class;

			HP = HT = 600;
			defenseSkill = 1000;

			EXP = 0;
			maxLvl = -2;
			state = PASSIVE;
			properties.add(Property.IMMOVABLE);
		}

		@Override
		public void beckon(int cell) {
			// Do nothing
		}


		@Override
		public int attackSkill(Char target) {
			return 100;
		}


		@Override
		public void damage(int dmg,  DamageSrc src) {
		}

		@Override
		protected boolean act() {
			for (int i : TestBoss.towerPositions) {
				if (i != pos) {
					zap(i);
				}
			}
			die(new DamageSrc(Element.META, null));
			return true;
		}

		private void zap(int cell) {
			sprite.parent.add(new  Beam.DeathRay(this.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
			Ballistica shot = new  Ballistica(this.pos, cell, Ballistica.STOP_TARGET);
			for (int c : shot.path) {
				Char ch = Actor.findChar(c);
				if (ch != null) {
					ch.damage(damageRoll(), this);
					CellEmitter.get(c).burst(SparkParticle.FACTORY, 2);
				}
			}
		}

		@Override
		public int damageRoll() {
			return Random.Int(10, 25);
		}

		@Override
		protected boolean doAttack(Char enemy) {
			return true;
		}

		@Override
		public String description() {
			return "The lightning shell crackles with electric power. "
					+ "It's powerful lightning attack is drawn to all living things in the lair. ";
		}

		@Override
		public void die( DamageSrc cause) {
			super.die(cause);
			sprite.emitter().burst(SparkParticle.FACTORY, 10);
		}

		@Override
		public void add(Buff buff) {
		}
	}
	public static class TestBossSprite extends MobSprite {

		public TestBossSprite() {
			super();

			texture(Assets.STATUE);

			TextureFilm frames = new  TextureFilm(texture, 12, 15);

			idle = new  Animation(2, true);
			idle.frames(frames, 0, 0, 0, 0, 0, 1, 1);

			run = new  Animation(15, true);
			run.frames(frames, 2, 3, 4, 5, 6, 7);

			attack = new  Animation(12, false);
			attack.frames(frames, 8, 9, 10);

			die = new  Animation(5, false);
			die.frames(frames, 11, 12, 13, 14, 15, 15);

			play(idle);
		}

		@Override
		public int blood() {
			return 0xFFcdcdb7;
		}
	}

		static public class LitTowerSprite extends MobSprite {

			public LitTowerSprite() {
			super();

			texture(Assets.LITTOWER);
			TextureFilm frames = new  TextureFilm(texture, 16, 16);

			idle = new  MovieClip.Animation(10, true);
			idle.frames(frames, 0, 0, 0, 0, 0, 0, 0, 0, 0);

			run = idle.clone();
			die = idle.clone();
			attack = idle.clone();

			zap = attack.clone();

			idle();
		}

		@Override
		public void play(MovieClip.Animation anim) {
			if (anim.equals(idle)) {
				emitter().burst(SparkParticle.FACTORY, 20);
			}
			super.play(anim);
		}
	}
}


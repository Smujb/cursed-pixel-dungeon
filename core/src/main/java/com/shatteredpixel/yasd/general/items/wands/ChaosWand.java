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

package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.LevelHandler;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.ConfusionGas;
import com.shatteredpixel.yasd.general.actors.blobs.Fire;
import com.shatteredpixel.yasd.general.actors.blobs.ParalyticGas;
import com.shatteredpixel.yasd.general.actors.blobs.Regrowth;
import com.shatteredpixel.yasd.general.actors.blobs.ToxicGas;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Frost;
import com.shatteredpixel.yasd.general.actors.buffs.Recharging;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.GoldenMimic;
import com.shatteredpixel.yasd.general.actors.mobs.Mimic;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Sheep;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Flare;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.effects.SpellSprite;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.general.items.bombs.Bomb;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.general.items.weapon.melee.hybrid.MagesStaff;
import com.shatteredpixel.yasd.general.levels.terrain.KindOfTerrain;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.traps.CursingTrap;
import com.shatteredpixel.yasd.general.levels.traps.ShockingTrap;
import com.shatteredpixel.yasd.general.levels.traps.SummoningTrap;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Languages;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.plants.Plant;
import com.shatteredpixel.yasd.general.plants.Swiftthistle;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.ui.TargetHealthIndicator;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.io.IOException;
import java.util.ArrayList;

//helper class to contain all the cursed wand zapping logic, so the main wand class doesn't get huge.
public class ChaosWand extends Wand {
	{
		element = Element.CHAOS;

		image = ItemSpriteSheet.Wands.WARPING;
	}

	private static float COMMON_CHANCE = 0.6f;
	private static float UNCOMMON_CHANCE = 0.3f;
	private static float RARE_CHANCE = 0.09f;
	private static float VERY_RARE_CHANCE = 0.01f;

	public static void cursedZap(final Item origin, final Char user, final Ballistica bolt, final Callback afterZap){
		switch (Random.chances(new float[]{COMMON_CHANCE, UNCOMMON_CHANCE, RARE_CHANCE, VERY_RARE_CHANCE})){
			case 0:
			default:
				commonEffect(origin, user, bolt, afterZap);
				break;
			case 1:
				uncommonEffect(origin, user, bolt, afterZap);
				break;
			case 2:
				rareEffect(origin, user, bolt, afterZap);
				break;
			case 3:
				veryRareEffect(origin, user, bolt, afterZap);
				break;
		}
	}

	private static void commonEffect(final Item origin, final Char user, final Ballistica bolt, final Callback afterZap){
		switch(Random.Int(4)){

			//anti-entropy
			case 0:
				cursedFX(user, bolt, new Callback() {
						public void call() {
							Char target = Actor.findChar(bolt.collisionPos);
							switch (Random.Int(2)){
								case 0:
									if (target != null)
										Buff.affect(target, Burning.class).reignite(target);
									Buff.affect(user, Frost.class, Frost.DURATION);
									break;
								case 1:
									Buff.affect(user, Burning.class).reignite(user);
									if (target != null)
										Buff.affect(target, Frost.class, Frost.DURATION);
									break;
							}
							afterZap.call();
						}
					});
				break;

			//spawns some regrowth
			case 1:
				cursedFX(user, bolt, new Callback() {
					public void call() {
						GameScene.add( Blob.seed(bolt.collisionPos, 30, Regrowth.class));
						afterZap.call();
					}
				});
				break;

			//random teleportation
			case 2:
				switch(Random.Int(2)){
					case 0:
						ScrollOfTeleportation.teleportUser(user);
						afterZap.call();
						break;
					case 1:
						cursedFX(user, bolt, new Callback() {
							public void call() {
								Char ch = Actor.findChar( bolt.collisionPos );
								if (ch != null && !ch.properties().contains(Char.Property.IMMOVABLE)) {
									ScrollOfTeleportation.teleportUser(user);
								}
								afterZap.call();
							}
						});
						break;
				}
				break;

			//random gas at location
			case 3:
				cursedFX(user, bolt, new Callback() {
					public void call() {
						switch (Random.Int(3)) {
							case 0:
								GameScene.add( Blob.seed( bolt.collisionPos, 800, ConfusionGas.class ) );
								break;
							case 1:
								GameScene.add( Blob.seed( bolt.collisionPos, 500, ToxicGas.class ) );
								break;
							case 2:
								GameScene.add( Blob.seed( bolt.collisionPos, 200, ParalyticGas.class ) );
								break;
						}
						Sample.INSTANCE.play( Assets.Sounds.GAS );
						afterZap.call();
					}
				});
				break;
		}

	}

	private static void uncommonEffect(final Item origin, final Char user, final Ballistica bolt, final Callback afterZap){
		switch(Random.Int(4)){

			//Random plant
			case 0:
				cursedFX(user, bolt, new Callback() {
					public void call() {
						int pos = bolt.collisionPos;
						//place the plant infront of an enemy so they walk into it.
						if (Actor.findChar(pos) != null && bolt.dist > 1) {
							pos = bolt.path.get(bolt.dist - 1);
						}
						KindOfTerrain terrain = Dungeon.level.getTerrain(pos);

						if (terrain == Terrain.EMPTY ||
								terrain == Terrain.EMBERS ||
								terrain == Terrain.EMPTY_DECO ||
								terrain == Terrain.GRASS ||
								terrain == Terrain.HIGH_GRASS ||
								terrain == Terrain.FURROWED_GRASS) {
							Dungeon.level.plant((Plant.Seed) Generator.randomUsingDefaults(Generator.Category.SEED), pos);
						}
						afterZap.call();
					}
				});
				break;

			//Health transfer
			case 1:
				final Char target = Actor.findChar( bolt.collisionPos );
				if (target != null) {
					cursedFX(user, bolt, new Callback() {
						public void call() {
							int damage;
							if (user instanceof Hero) {
								damage = ((Hero)user).lvl * 2;
							} else {
								damage = Dungeon.getScaling() * 2;
							}
							if (Random.Int(2) == 0) {
								user.HP = Math.min(user.HT, user.HP + damage);
								user.sprite.emitter().burst(Speck.factory(Speck.HEALING), 3);
								target.damage(damage, new Char.DamageSrc(Element.SHADOW, ChaosWand.class));
								target.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
							} else {
								user.damage(damage, new Char.DamageSrc(Element.SHADOW, ChaosWand.class));
								user.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
								target.HP = Math.min(target.HT, target.HP + damage);
								target.sprite.emitter().burst(Speck.factory(Speck.HEALING), 3);
								Sample.INSTANCE.play(Assets.Sounds.CURSED);
								if (!user.isAlive() && origin != null) {
									Dungeon.fail(origin.getClass());
									GLog.negative(Messages.get(ChaosWand.class, "ondeath", origin.name()));
								}
							}
							afterZap.call();
						}
					});
				} else {
					GLog.info(Messages.get(ChaosWand.class, "nothing"));
					afterZap.call();
				}
				break;

			//Bomb explosion
			case 2:
				cursedFX(user, bolt, new Callback() {
					public void call() {
						new Bomb().explode(bolt.collisionPos);
						afterZap.call();
					}
				});
				break;

			//shock and recharge
			case 3:
				new ShockingTrap().set( user.pos ).activate();
				Buff.prolong(user, Recharging.class, Recharging.DURATION);
				ScrollOfRecharging.charge(user);
				SpellSprite.show(user, SpellSprite.CHARGE);
				afterZap.call();
				break;
		}

	}

	private static void rareEffect(final Item origin, final Char user, final Ballistica bolt, final Callback afterZap){
		switch(Random.Int(4)){

			//sheep transformation
			case 0:
				if (user != Dungeon.hero){
					cursedZap(origin, user, bolt, afterZap);
					return;
				}
				cursedFX(user, bolt, new Callback() {
					public void call() {
						Char ch = Actor.findChar( bolt.collisionPos );

						if (ch != null && ch != user
								&& !ch.properties().contains(Char.Property.BOSS)
								&& !ch.properties().contains(Char.Property.MINIBOSS)){
							Sheep sheep = new Sheep();
							sheep.lifespan = 10;
							sheep.pos = ch.pos;
							ch.destroy();
							ch.sprite.killAndErase();
							Dungeon.level.mobs.remove(ch);
							TargetHealthIndicator.instance.target(null);
							GameScene.add(sheep);
							CellEmitter.get(sheep.pos).burst(Speck.factory(Speck.WOOL), 4);
						} else {
							GLog.info(Messages.get(ChaosWand.class, "nothing"));
						}
						afterZap.call();
					}
				});
				break;

			//curses!
			case 1:
				CursingTrap.curse(user);
				afterZap.call();
				break;

			//inter-level teleportation
			case 2:
				if (Dungeon.depth > 1 && !Dungeon.bossLevel()) {

					//each depth has 1 more weight than the previous depth.
					float[] depths = new float[Dungeon.depth -1];
					for (int i = 1; i < Dungeon.depth; i++) depths[i-1] = i;
					int depth = 1+Random.chances(depths);

					Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
					if (buff != null) buff.detach();
					
					buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
					if (buff != null) buff.detach();

					LevelHandler.returnTo(depth, -1);

				} else {
					ScrollOfTeleportation.teleportUser(user);

				}
				afterZap.call();
				break;

			//summon monsters
			case 3:
				new SummoningTrap().set( user.pos ).activate();
				afterZap.call();
				break;
		}
	}

	private static void veryRareEffect(final Item origin, final Char user, final Ballistica bolt, final Callback afterZap){
		switch(Random.Int(4)){

			//great forest fire!
			case 0:
				for (int i = 0; i < Dungeon.level.length(); i++){
					GameScene.add( Blob.seed(i, 15, Regrowth.class));
				}
				do {
					GameScene.add(Blob.seed(Dungeon.level.randomDestination(null), 10, Fire.class));
				} while (Random.Int(5) != 0);
				new Flare(8, 32).color(0xFFFF66, true).show(user.sprite, 2f);
				Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
				GLog.positive(Messages.get(ChaosWand.class, "grass"));
				GLog.warning(Messages.get(ChaosWand.class, "fire"));
				afterZap.call();
				break;


			//golden mimic
			case 1:
				cursedFX(user, bolt, new Callback() {
					public void call() {
						Mimic mimic = Mimic.spawnAt(bolt.collisionPos, new ArrayList<>(), GoldenMimic.class, Dungeon.level);
						if (mimic != null) {
							mimic.stopHiding();
							mimic.alignment = Char.Alignment.ENEMY;
							mimic.HP = mimic.HT;
							Item reward;
							do {
								reward = Generator.random(Random.oneOf(Generator.Category.WEAPON, Generator.Category.SHIELD,
										Generator.Category.RING, Generator.Category.WAND));
							} while (reward.level() < 1);
							//play vfx/sfx manually as mimic isn't in the scene yet
							Sample.INSTANCE.play(Assets.Sounds.MIMIC, 1, 0.85f);
							CellEmitter.get(mimic.pos).burst(Speck.factory(Speck.STAR), 10);
							mimic.items.clear();
							mimic.items.add(reward);
							GameScene.add(mimic);
						} else {
							GLog.info(Messages.get(ChaosWand.class, "nothing"));
						}
						
						afterZap.call();
					}
				});
				break;

			//crashes the game, yes, really.
			case 2:

				if (user != Dungeon.hero){
					cursedZap(origin, user, bolt, afterZap);
					return;
				}

				try {
					Dungeon.saveAll();
					if(Messages.lang() != Languages.ENGLISH){
						//Don't bother doing this joke to none-english speakers, I doubt it would translate.
						GLog.info(Messages.get(ChaosWand.class, "nothing"));
						afterZap.call();
					} else {
						GameScene.show(
								new WndOptions("CURSED WAND ERROR", "this application will now self-destruct", "abort", "retry", "fail") {
									
									@Override
									protected void onSelect(int index) {
										Game.instance.finish();
									}
									
									@Override
									public void onBackPressed() {
										//do nothing
									}
								}
						);
					}
				} catch(IOException e){
					CPDGame.reportException(e);
					//oookay maybe don't kill the game if the save failed.
					GLog.info(Messages.get(ChaosWand.class, "nothing"));
					afterZap.call();
				}
				break;

			//random transmogrification
			case 3:
				//skips this effect if there is no item to transmogrify
				if (origin == null || user != Dungeon.hero || !Dungeon.hero.belongings.contains(origin)){
					cursedZap(origin, user, bolt, afterZap);
					return;
				}
				origin.detach(user.belongings.backpack);
				Item result;
				do {
					result = Generator.random(Random.oneOf(Generator.Category.WEAPON, Generator.Category.SHIELD,
							Generator.Category.RING, Generator.Category.RELIC, Generator.Category.WAND, Generator.Category.RANGED));
				} while (result.cursed());
				result.cursedKnown = true;
				result.curse();
				if (origin instanceof Wand){
					GLog.warning( Messages.get(ChaosWand.class, "transmogrify_wand") );
				} else {
					GLog.warning( Messages.get(ChaosWand.class, "transmogrify_other") );
				}
				Dungeon.level.drop(result, user.pos).sprite.drop();
				afterZap.call();
				break;
		}
	}

	private static void cursedFX(final Char user, final Ballistica bolt, final Callback callback){
		MagicMissile.boltFromChar( user.sprite.parent,
				MagicMissile.RAINBOW,
				user.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
	}

	@Override
	public void onZap(Ballistica attack) {
		cursedZap(this, curUser, attack, new Callback() {
			@Override
			public void call() {}
		});
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		element.attackProc(damage, attacker, defender);
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		cursedFX(curUser, bolt, callback);
	}
}

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

package com.shatteredpixel.yasd.general.actors.mobs.npcs;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.mobs.FetidRat;
import com.shatteredpixel.yasd.general.actors.mobs.GnollTrickster;
import com.shatteredpixel.yasd.general.actors.mobs.GreatCrab;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.armor.Armor;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.journal.Notes;
import com.shatteredpixel.yasd.general.levels.SewerLevel;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.GhostSprite;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndQuest;
import com.shatteredpixel.yasd.general.windows.WndSadGhost;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Ghost extends NPC {

	{
		spriteClass = GhostSprite.class;
		
		flying = true;
		
		state = WANDERING;
	}
	
	public Ghost() {
		super();

		Sample.INSTANCE.load( Assets.SND_GHOST );
	}

	@Override
	protected boolean act() {
		if (Quest.processed())
			target = Dungeon.hero.pos;
		return super.act();
	}

	@Override
	public int defenseSkill( Char enemy ) {
		return INFINITE_EVASION;
	}
	
	@Override
	public float speed() {
		return Quest.processed() ? 2f : 0.5f;
	}
	
	@Override
	protected Char chooseEnemy() {
		return null;
	}
	
	@Override
	public void damage(int dmg,  DamageSrc src) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}

	public boolean interact(Char c) {
		sprite.turnTo( pos, c.pos );
		
		Sample.INSTANCE.play( Assets.SND_GHOST );

		if (c != Dungeon.hero){
			return super.interact(c);
		}
		
		if (Quest.given) {
			if (Quest.weapon != null) {
				if (Quest.processed) {
					Game.runOnRenderThread(new Callback() {
						@Override
						public void call() {
							GameScene.show(new WndSadGhost(Ghost.this, Quest.type));
						}
					});
				} else {
					Game.runOnRenderThread(new Callback() {
						@Override
						public void call() {
							switch (Quest.type) {
								case 1:
								default:
									GameScene.show(new WndQuest(Ghost.this, Messages.get(Ghost.this, "rat_2")));
									break;
								case 2:
									GameScene.show(new WndQuest(Ghost.this, Messages.get(Ghost.this, "gnoll_2")));
									break;
								case 3:
									GameScene.show(new WndQuest(Ghost.this, Messages.get(Ghost.this, "crab_2")));
									break;
							}
						}
					});

					int newPos = -1;
					for (int i = 0; i < 10; i++) {
						newPos = Dungeon.level.randomRespawnCell(this);
						if (newPos != -1) {
							break;
						}
					}
					if (newPos != -1) {

						CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
						pos = newPos;
						sprite.place(pos);
						sprite.visible = Dungeon.level.heroFOV[pos];
					}
				}
			}
		} else {
			Mob questBoss;
			String txt_quest;

			switch (Quest.type){
				case 1: default:
					questBoss = Mob.create(FetidRat.class);
					txt_quest = Messages.get(this, "rat_1", Dungeon.hero.name()); break;
				case 2:
					questBoss = Mob.create(GnollTrickster.class);
					txt_quest = Messages.get(this, "gnoll_1", Dungeon.hero.name()); break;
				case 3:
					questBoss = Mob.create(GreatCrab.class);
					txt_quest = Messages.get(this, "crab_1", Dungeon.hero.name()); break;
			}

			questBoss.pos = Dungeon.level.randomRespawnCell(this);

			if (questBoss.pos != -1) {
				GameScene.add(questBoss);
				Quest.given = true;
				Notes.add( Notes.Landmark.GHOST );
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show( new WndQuest( Ghost.this, txt_quest ) );
					}
				});
			}

		}

		return true;
	}

	public static class Quest {
		
		private static boolean spawned;

		private static int type;

		private static boolean given;
		private static boolean processed;
		
		private static int depth;
		
		public static Weapon weapon;
		public static Armor armor;
		
		public static void reset() {
			spawned = false;
			
			weapon = null;
			armor = null;
		}
		
		private static final String NODE		= "sadGhost";
		
		private static final String SPAWNED		= "spawned";
		private static final String TYPE        = "type";
		private static final String GIVEN		= "given";
		private static final String PROCESSED	= "processed";
		private static final String DEPTH		= "depth";
		private static final String WEAPON		= "getWeapons";
		private static final String ARMOR		= "getArmors";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				
				node.put( TYPE, type );
				
				node.put( GIVEN, given );
				node.put( DEPTH, depth );
				node.put( PROCESSED, processed);
				
				node.put( WEAPON, weapon );
				node.put( ARMOR, armor );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {
			
			Bundle node = bundle.getBundle( NODE );

			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {

				type = node.getInt(TYPE);
				given	= node.getBoolean( GIVEN );
				processed = node.getBoolean( PROCESSED );

				depth	= node.getInt( DEPTH );
				
				weapon	= (Weapon)node.get( WEAPON );
				armor	= (Armor)node.get( ARMOR );
			} else {
				reset();
			}
		}
		
		public static void spawn( SewerLevel level ) {
			if (!spawned && Dungeon.depth > 1 && Random.Int( 5 - Dungeon.depth) == 0) {
				
				Ghost ghost = new Ghost();
				do {
					ghost.pos = level.randomRespawnCell(ghost);
				} while (ghost.pos == -1);
				level.mobs.add( ghost );
				
				spawned = true;
				//dungeon depth determines type of quest.
				//depth2=fetid rat, 3=gnoll trickster, 4=great crab
				type = Dungeon.depth -1;
				
				given = false;
				processed = false;
				depth = Dungeon.depth;

				//50%:tier2, 30%:tier3, 15%:tier4, 5%:tier5
				float itemTierRoll = Random.Float();
				int tier;

				if (itemTierRoll < 0.5f) {
					tier = 2;
				} else if (itemTierRoll < 0.8f) {
					tier = 3;
				} else if (itemTierRoll < 0.95f) {
					tier = 4;
				} else {
					tier = 5;
				}
				armor = Generator.randomArmor();
				armor.setTier(tier);
				weapon = Generator.randomWeapon();
				((MeleeWeapon) weapon).setTier(tier);


				//50%:+0, 30%:+1, 15%:+2, 5%:+3
				float itemLevelRoll = Random.Float();
				int itemLevel;
				if (itemLevelRoll < 0.5f){
					itemLevel = 0;
				} else if (itemLevelRoll < 0.8f){
					itemLevel = 1;
				} else if (itemLevelRoll < 0.95f){
					itemLevel = 2;
				} else {
					itemLevel = 3;
				}
				weapon.level(itemLevel);
				armor.level(itemLevel);

				if (Random.Int(4) == 0 || weapon.cursed || armor.cursed){
					weapon.cursed = armor.cursed = false;
					weapon.enchant();
					armor.inscribe();
				}

			}
		}
		
		public static void process() {
			if (spawned && given && !processed && (depth == Dungeon.depth)) {
				GLog.n( Messages.get(Ghost.class, "find_me") );
				Sample.INSTANCE.play( Assets.SND_GHOST );
				processed = true;
			}
		}
		
		public static void complete() {
			weapon = null;
			armor = null;
			
			Notes.remove( Notes.Landmark.GHOST );
		}

		public static boolean processed(){
			return spawned && processed;
		}
		
		public static boolean completed(){
			return processed() && weapon == null && armor == null;
		}
	}
}

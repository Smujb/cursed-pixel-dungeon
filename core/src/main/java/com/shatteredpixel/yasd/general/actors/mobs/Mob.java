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

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Challenges;
import com.shatteredpixel.yasd.general.Difficulty;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.Statistics;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.DarkGas;
import com.shatteredpixel.yasd.general.actors.blobs.DemonGas;
import com.shatteredpixel.yasd.general.actors.buffs.Adrenaline;
import com.shatteredpixel.yasd.general.actors.buffs.Aggression;
import com.shatteredpixel.yasd.general.actors.buffs.Amok;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Charm;
import com.shatteredpixel.yasd.general.actors.buffs.Corrosion;
import com.shatteredpixel.yasd.general.actors.buffs.Corruption;
import com.shatteredpixel.yasd.general.actors.buffs.DeferredDeath;
import com.shatteredpixel.yasd.general.actors.buffs.Hunger;
import com.shatteredpixel.yasd.general.actors.buffs.ParryBuff;
import com.shatteredpixel.yasd.general.actors.buffs.Sleep;
import com.shatteredpixel.yasd.general.actors.buffs.SoulMark;
import com.shatteredpixel.yasd.general.actors.buffs.StaminaRegen;
import com.shatteredpixel.yasd.general.actors.buffs.Terror;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroSubClass;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.allies.Soul;
import com.shatteredpixel.yasd.general.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.general.items.powers.LuckyBadge;
import com.shatteredpixel.yasd.general.items.relics.CupOfSuffering;
import com.shatteredpixel.yasd.general.items.rings.Ring;
import com.shatteredpixel.yasd.general.items.rings.RingOfWealth;
import com.shatteredpixel.yasd.general.items.unused.missiles.MissileWeapon;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Lucky;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.plants.Swiftthistle;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public abstract class Mob extends Char {

	{
		actPriority = MOB_PRIO;
		
		alignment = Alignment.ENEMY;
		immunities.add(DemonGas.class);
	}

	protected int level = 0;

	@Override
	public int level() {
		return level;
	}

	public float damageFactor = 1f;
	public float healthFactor = 1f;
	public float drFactor = 1f;
	public float elementaldrFactor = 0f;
	public float attackDelay = 1f;
	public float accuracyFactor = 1f;
	public float evasionFactor = 1f;
	public float perceptionFactor = 1f;
	public float stealthFactor = 1f;

	public int range = 1;
	public boolean hasMeleeAttack = true;
	public boolean hasSupport = false;
	public int shotType = Ballistica.PROJECTILE;

	public AiState SLEEPING     = new  Sleeping();
	public AiState HUNTING		= new  Hunting();
	public AiState WANDERING	= new  Wandering();
	public AiState FLEEING		= new  Fleeing();
	public AiState PASSIVE		= new  Passive();
	public AiState state = SLEEPING;
	
	public Class<? extends CharSprite> spriteClass;
	
	protected int target = -1;


	//Bosses give 10x exp
	public int experience(int lvl) {
		if (lvl + 1 < Dungeon.hero.lvl) return 0;
		return ((lvl + 1)/2) * (properties().contains(Property.BOSS) ? 10 : 1);
	}

	public final int experience() {
		return experience(level);
	}

	protected Char enemy;
	public boolean enemySeen;
	protected boolean alerted = false;

	protected static final float TIME_TO_WAKE_UP = 1f;
	
	private static final String STATE	= "state";
	private static final String SEEN	= "seen";
	private static final String TARGET	= "target";
	private static final String LEVEL	= "level";
	private static final String SUS	= "suspicion";

	@Override
	public void storeInBundle( Bundle bundle ) {
		
		super.storeInBundle( bundle );

		if (state == SLEEPING) {
			bundle.put( STATE, Sleeping.TAG );
		} else if (state == WANDERING) {
			bundle.put( STATE, Wandering.TAG );
		} else if (state == HUNTING) {
			bundle.put( STATE, Hunting.TAG );
		} else if (state == FLEEING) {
			bundle.put( STATE, Fleeing.TAG );
		} else if (state == PASSIVE) {
			bundle.put( STATE, Passive.TAG );
		}
		bundle.put( SEEN, enemySeen );
		bundle.put( TARGET, target );
		bundle.put( LEVEL, level );
		bundle.put(SUS, suspicion);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		
		super.restoreFromBundle( bundle );

		String state = bundle.getString( STATE );
		switch (state) {
			case Sleeping.TAG:
				this.state = SLEEPING;
				break;
			case Wandering.TAG:
				this.state = WANDERING;
				break;
			case Hunting.TAG:
				this.state = HUNTING;
				break;
			case Fleeing.TAG:
				this.state = FLEEING;
				break;
			case Passive.TAG:
				this.state = PASSIVE;
				break;
		}

		enemySeen = bundle.getBoolean( SEEN );

		target = bundle.getInt( TARGET );

		level = bundle.getInt( LEVEL );

		suspicion = bundle.getFloat(SUS);
	}

	public Mob() {
		level = Dungeon.getScaling();
		updateHT(true);
	}

	protected boolean canParry() {
		return false;
	}

	@Override
	public void updateHT(boolean boostHP) {
		HP = HT = (int) (normalHP(level) * healthFactor);
		//Bosses (obviously) have higher HP
		if (properties().contains(Property.BOSS)) {
			HP = HT *= 5;
		} else if (properties().contains(Property.MINIBOSS)) {
			HP = HT *= 2;
		}
	}

	protected static int normalHP(int level) {
		return Math.round(16 * Item.calcMobPower(level));
	}

	protected static int normalNoticeSkill(int level) {
		return 5 + (level);
	}

	protected static int normalSneakSkill(int level) {
		return 5 + (level);
	}

	protected static int normalCritSkill(int level) {
		return Math.round(5 + 0.5f*level);
	}

	protected static int normalCritDef(int level) {
		return 10 + level;
	}

	protected static int normalDamageRoll(int level) {
		return Random.NormalIntRange(normalMin(level), normalMax(level));
	}

	protected static int normalMax(int level) {
		return Math.round(Item.calcMobPower(level) * 16);
	}

	protected static int normalMin(int level) {
		return Math.round(Item.calcMobPower(level) * 12);
	}

	//Allows mobs to specify difficulty overrides (such as guardians on grind depth)
	protected Difficulty difficulty() {
		return Dungeon.difficulty;
	}

	protected float maxParryDefenseFactor = 1f;

	protected static int normalMaxDefense(int level) {
		return Math.round(Item.calcMobPower(level) * 36);
	}

	int findClosest(Char enemy, int pos) {
		int closest = -1;
		boolean[] passable = Dungeon.level.passable();

		for (int n : PathFinder.NEIGHBOURS9) {
			int c = pos + n;
			if (passable[c] && Actor.findChar( c ) == null
					&& (closest == -1 || (Dungeon.level.trueDistance(c, enemy.pos) < (Dungeon.level.trueDistance(closest, enemy.pos))))) {
				closest = c;
			}
		}
		return closest;
	}

	public static <T extends Mob> T create(Class<T> type, int level) {
		T mob;
		try {
			mob = type.newInstance();
		} catch (InstantiationException e) {
			CPDGame.reportException(e);
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			CPDGame.reportException(e);
			throw new RuntimeException(e);
		}
		mob.setLevel(level);
		mob.onCreate();
		if (mob.numTypes > 1) {
			mob.type = Random.Int(mob.numTypes);
		} else {
			mob.type = 0;
		}
		return mob;
	}

	public static <T extends Mob> T create(Class<T> type, Level level) {
		return create(type, level.getScaleFactor());
	}

	public static <T extends Mob> T create(Class<T> type) {
		return create(type, Dungeon.level);
	}

	public void setLevel(int lvl) {
		level = lvl;
		updateHT(true);
	}

	protected void onCreate() {};

	@Override
	public int damageRoll() {
		if (hasBelongings()) {
			return super.damageRoll();
		} else {
			return affectDamageRoll( (int) (normalDamageRoll(level) * damageFactor));
		}
	}

	@Override
	public float sneakSkill(Char enemy) {
		return affectSneakSkill(normalSneakSkill(level) * stealthFactor);
	}

	@Override
	public float noticeSkill(Char enemy) {
		return affectNoticeSkill(normalNoticeSkill(level) * perceptionFactor);
	}


	@Override
	public int critSkill() {
		return affectCritDef(normalCritSkill(level));
	}

	@Override
	public int critDef() {
		return affectCritDef(normalCritDef(level));
	}


	public int maxDefense() {
		return (int) (normalMaxDefense(level) * maxParryDefenseFactor);
	}

	public int defense() {
		return Math.round(maxDefense() * (stamina/maxStamina()));
	}

	public CharSprite sprite() {
		return Reflection.newInstance(spriteClass);
	}
	
	@Override
	protected boolean act() {
		for (ParryBuff buff : buffs(ParryBuff.class)) buff.detach();
		StaminaRegen.toggleRegen(this, true);
		super.act();
		
		if (Dungeon.level.liquid(pos) && elementalType() == Element.WATER) {
			heal(HT/10);
		}
		
		if (paralysed > 0) {
			enemySeen = false;
			spend( TICK );
			return true;
		}
		
		enemy = chooseEnemy();

		boolean enemyInFOV = enemy != null && Dungeon.level.distance(pos, enemy.pos) < viewDistance && (properties().contains(Property.IGNORES_INVISIBLE) | enemy.invisible <= 0);

		return state.act( enemyInFOV, checkEnemy(enemy) );
	}

	@Override
	public int maxStamina() {
		return 100 + 5*level();
	}

	//FIXME this is sort of a band-aid correction for allies needing more intelligent behaviour
	protected boolean intelligentAlly = false;

	private Char chooseAlly() {
		ArrayList<Char> targets = new ArrayList<>();
		for (Char ch : Actor.chars()) {
			if (ch.alignment == this.alignment && (fieldOfView[ch.pos] || threshold())) {
				targets.add(ch);
			}
		}
		if (targets.size() > 0) {
			return Random.element(targets);
		}
		return null;
	}
	
	protected Char chooseEnemy() {

		Terror terror = buff( Terror.class );
		if (terror != null) {
			Char source = (Char)Actor.findById( terror.object );
			if (source != null) {
				return source;
			}
		}
		
		//if we are an enemy, and have no target or current target isn't affected by aggression
		//then auto-prioritize a target that is affected by aggression, even another enemy
		if (alignment == Alignment.ENEMY
				&& (enemy == null || enemy.buff(Aggression.class) == null)) {
			for (Char ch : Actor.chars()) {
				if (ch != this && fieldOfView[ch.pos] &&
						ch.buff(Aggression.class) != null) {
					return ch;
				}
			}
		}

		//find a new  enemy if..
		boolean newEnemy = false;
		//we have no enemy, or the current one is dead/missing
		if ( enemy == null || !enemy.isAlive() || !Actor.chars().contains(enemy) || state == WANDERING) {
			newEnemy = true;
			//We are an ally, and current enemy is another ally.
		} else if (alignment == Alignment.ALLY && enemy.alignment == Alignment.ALLY) {
			newEnemy = true;
			//We are amoked and current enemy is the hero
		} else if (buff( Amok.class ) != null && enemy == Dungeon.hero) {
			newEnemy = true;
			//We are charmed and current enemy is what charmed us
		} else if (buff(Charm.class) != null && buff(Charm.class).object == enemy.id()) {
			newEnemy = true;

		}
		if ( newEnemy ) {

			HashSet<Char> enemies = new  HashSet<>();

			//if the mob is amoked...
			if ( buff(Amok.class) != null) {
				//try to find an enemy mob to attack first.
				for (Mob mob : Dungeon.level.mobs)
					if (mob.alignment == Alignment.ENEMY && mob != this
							&& mob.invisible <= 0) {
						enemies.add(mob);
					}
				
				if (enemies.isEmpty()) {
					//try to find ally mobs to attack second.
					for (Mob mob : Dungeon.level.mobs)
						if (mob.alignment == Alignment.ALLY && mob != this
								&& mob.invisible <= 0) {
							enemies.add(mob);
						}
					
					if (enemies.isEmpty()) {
						//try to find the hero third
						if (Dungeon.hero.invisible <= 0) {
							enemies.add(Dungeon.hero);
						}
					}
				}
				
			//if the mob is an ally...
			} else if ( alignment == Alignment.ALLY ) {
				//look for hostile mobs to attack
				for (Mob mob : Dungeon.level.mobs)
					if (mob.alignment == Alignment.ENEMY
							&& mob.invisible <= 0 && !mob.isInvulnerable(getClass()))
						//intelligent allies do not target mobs which are passive, wandering, or asleep
						if (!intelligentAlly ||
								(mob.state != mob.SLEEPING && mob.state != mob.PASSIVE && mob.state != mob.WANDERING)) {
							enemies.add(mob);
						}
				
			//if the mob is an enemy...
			} else if (alignment == Alignment.ENEMY) {
				//look for ally mobs to attack
				for (Mob mob : Dungeon.level.mobs) {
					if (mob.alignment == Alignment.ALLY && mob.invisible <= 0 && !mob.isInvulnerable(getClass())) {
						enemies.add(mob);
					}
				}
				if (Dungeon.hero.invisible <= 0 && !Dungeon.hero.isInvulnerable(getClass())) {
					enemies.add(Dungeon.hero);
				}
			}
			
			Charm charm = buff( Charm.class );
			if (charm != null){
				Char source = (Char)Actor.findById( charm.object );
				if (source != null && enemies.contains(source) && enemies.size() > 1){
					enemies.remove(source);
				}
			}
			
			//neutral characters in particular do not choose enemies.
			if (enemies.isEmpty()){
				return null;
			} else {
				//go after the closest potential enemy, preferring the hero if two are equidistant
				Char closest = null;
				for (Char curr : enemies){
					if ((closest == null
							|| Dungeon.level.distance(pos, curr.pos) < Dungeon.level.distance(pos, closest.pos)
							|| Dungeon.level.distance(pos, curr.pos) == Dungeon.level.distance(pos, closest.pos) && curr == Dungeon.hero )
							//&& notice(curr)){
					){
						closest = curr;
					}
				}
				return closest;
			}

		} else
			return enemy;
	}
	
	@Override
	public void add( Buff buff ) {
		super.add( buff );
		if (buff instanceof Amok || buff instanceof Corruption) {
			state = HUNTING;
		} else if (buff instanceof Terror) {
			state = FLEEING;
		} else if (buff instanceof Sleep) {
			state = SLEEPING;
			postpone( Sleep.SWS );
		}
	}
	
	@Override
	public void remove( Buff buff ) {
		super.remove( buff );
		if (buff instanceof Terror && sprite != null) {
			sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "rage") );
			state = HUNTING;
		}
	}

	public static void spawnAround4(Class<? extends Mob> type, int pos) {
		spawnAtList(type, pos, PathFinder.NEIGHBOURS4);
	}

	public static void spawnAround8(Class<? extends Mob> type, int pos) {
		spawnAtList(type, pos, PathFinder.NEIGHBOURS8);
	}

	public static void spawnAround9(Class<? extends Mob> type, int pos) {
		spawnAtList(type, pos, PathFinder.NEIGHBOURS9);
	}

	public static void spawnAtList(Class<? extends Mob> type, int pos, int[] relativePositions) {
		for (int n : relativePositions) {
			int cell = pos + n;
			if (Dungeon.level.passable(cell) && Actor.findChar(cell) == null) {
				Mob.spawnAt(type, cell);
			}
		}
	}

	public static <T extends Mob> T spawnAt(Class<T> type, int pos) {
		if (Dungeon.level.passable(pos) && Actor.findChar( pos ) == null) {

			T mob = Mob.create(type);
			mob.pos = pos;
			mob.state = mob.HUNTING;
			GameScene.add( mob );

			return mob;
		} else {
			return null;
		}
	}

	@Override
	public boolean canAttack(@NotNull Char enemy) {
		if (Dungeon.level.adjacent(this.pos, enemy.pos) && !hasMeleeAttack) {
			return false;
		}
		return (Dungeon.level.distance( pos, enemy.pos ) <= range | range < 0) & Ballistica.canHit(this, enemy, shotType);
	}
	
	protected boolean getCloser( int target ) {
		if (state == HUNTING && !hasMeleeAttack) {

			return enemySeen && getFurther( target );

		} else {

			if (rooted || target == pos) {
				return false;
			}

			int step = -1;

			if (Dungeon.level.adjacent(pos, target)) {

				path = null;

				if (Actor.findChar( target ) == null
						&& (Dungeon.level.passable(target) || (flying && Dungeon.level.avoid(target)))
						&& canOccupy(Dungeon.level, target)) {
					step = target;
				}

			} else {

				boolean newPath = false;
				//scrap the current xPos if it's empty, no longer connects to the current location
				//or if it's extremely inefficient and checking again may result in a much better xPos
				if (path == null || path.isEmpty()
						|| !Dungeon.level.adjacent(pos, path.getFirst())
						|| path.size() > 2 * Dungeon.level.distance(pos, target))
					newPath = true;
				else if (path.getLast() != target) {
					//if the new  target is adjacent to the end of the xPos, adjust for that
					//rather than scrapping the whole xPos.
					if (Dungeon.level.adjacent(target, path.getLast())) {
						int last = path.removeLast();

						if (path.isEmpty()) {

							//shorten for a closer one
							if (Dungeon.level.adjacent(target, pos)) {
								path.add(target);
							//extend the xPos for a further target
							} else {
								path.add(last);
								path.add(target);
							}

						} else {
							//if the new  target is simply 1 earlier in the xPos shorten the xPos
							if (path.getLast() == target) {

							//if the new  target is closer/same, need to modify end of xPos
							} else if (Dungeon.level.adjacent(target, path.getLast())) {
								path.add(target);

							//if the new  target is further away, need to extend the xPos
							} else {
								path.add(last);
								path.add(target);
							}
						}

					} else {
						newPath = true;
					}

				}

				//checks if the next cell along the current path can be stepped into
				if (!newPath) {
					int nextCell = path.removeFirst();
					if (!Dungeon.level.passable(nextCell)
							|| (!flying && Dungeon.level.avoid(nextCell))
							|| (Char.hasProp(this, Char.Property.LARGE) && !Dungeon.level.openSpace(nextCell))
							|| Actor.findChar(nextCell) != null) {

						newPath = true;
						//If the next cell on the path can't be moved into, see if there is another cell that could replace it
						if (!path.isEmpty()) {
							for (int i : PathFinder.NEIGHBOURS8) {
								if (Dungeon.level.adjacent(pos, nextCell + i) && Dungeon.level.adjacent(nextCell + i, path.getFirst())) {
									if (Dungeon.level.passable(nextCell+i)
											&& (flying || !Dungeon.level.avoid(nextCell+i))
											&& (!Char.hasProp(this, Char.Property.LARGE) || Dungeon.level.openSpace(nextCell+i))
											&& Actor.findChar(nextCell+i) == null){
										path.addFirst(nextCell+i);
										newPath = false;
										break;
									}
								}
							}
						}
					} else {
						path.addFirst(nextCell);
					}
				}

				if (newPath) {
					//Create a new variable to prevent slowdown as it's used a lot here.
					boolean[] passable = Dungeon.level.passable();
					//If we aren't hunting, always take a full path
					PathFinder.Path full = Dungeon.findPath(this, target, passable, fieldOfView, true);
					if (state != HUNTING){
						path = full;
					} else {
						//otherwise, check if other characters are forcing us to take a very slow route
						// and don't try to go around them yet in response, basically assume their blockage is temporary
						PathFinder.Path ignoreChars = Dungeon.findPath(this, target, passable, fieldOfView, false);
						if (ignoreChars != null && (full == null || full.size() > 2*ignoreChars.size())){
							//check if first cell of shorter path is valid. If it is, use new shorter path. Otherwise do nothing and wait.
							path = ignoreChars;
							if (!Dungeon.level.passable(ignoreChars.getFirst())
									|| (!flying && Dungeon.level.avoid(ignoreChars.getFirst()))
									|| (Char.hasProp(this, Char.Property.LARGE) && !Dungeon.level.openSpace(ignoreChars.getFirst()))
									|| Actor.findChar(ignoreChars.getFirst()) != null) {
								return false;
							}
						} else {
							path = full;
						}
					}
				}

				if (path != null) {
					step = path.removeFirst();
				} else {
					return false;
				}
			}
			if (step != -1) {
				move(step);
				return true;
			} else {
				return false;
			}
		}
	}
	
	protected boolean getFurther( int target ) {
		if (rooted || target == pos) {
			return false;
		}

		int step = Dungeon.flee( this, target, Dungeon.level.passable(), fieldOfView, true );
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void updateSpriteState() {
		super.updateSpriteState();
		if (Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class) != null
				|| Dungeon.hero.buff(Swiftthistle.TimeBubble.class) != null)
			sprite.add( CharSprite.State.PARALYSED );
	}
	@Override
    public float attackDelay() {
		float delay = 1f;
		if ( buff(Adrenaline.class) != null) delay /= 1.5f;
		return delay;
	}

	protected void doParry() {
		Buff.affect(this, Parry.class).setMob(this);
		spendAndNext(Actor.TICK);
	}
	
	protected boolean doAttack( Char enemy ) {

		if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
			sprite.attack( enemy.pos );
			spend( attackDelay() );
			return false;
		} else {
			attack( enemy);
			spend( attackDelay() );
			return true;
		}
	}
	
	@Override
	public void onAttackComplete() {
		attack( enemy);
		super.onAttackComplete();
	}
	
	@Override
	public int attackProc(Char enemy, int damage) {
		damage = super.attackProc(enemy, damage);
		if (enemy instanceof Mob && ((Mob)enemy).alignment == Alignment.ENEMY && Dungeon.hero.subClass == HeroSubClass.MEDIC) ((Mob)enemy).aggro(this);
		return damage;
	}

	public boolean canBeSurpriseAttacked( Char enemy ) {
		boolean seen = (enemySeen && enemy.invisible == 0);
		if (enemy == Dungeon.hero && !Dungeon.hero.canSurpriseAttack()) seen = true;
		return !seen;
	}
	
	protected boolean hitWithRanged = false;
	
	@Override
	public int defenseProc(Char enemy, int damage) {
		if (enemy instanceof Hero && ((Hero) enemy).belongings.getWeapon() instanceof MissileWeapon){
			hitWithRanged = true;
		}

		//if attacked by something else than current target, and that thing is closer, switch targets
		if (this.enemy == null
				|| (enemy != this.enemy && (Dungeon.level.distance(pos, enemy.pos) < Dungeon.level.distance(pos, this.enemy.pos)))) {
			aggro(enemy);
			target = enemy.pos;
		}

		if (buff(SoulMark.class) != null) {
			int restoration = Math.min(damage, HP);
			
			//physical damage that doesn't come from the hero is less effective
			if (enemy != Dungeon.hero){
				restoration = Math.round(restoration * 0.4f);
			}
			
			Buff.affect(Dungeon.hero, Hunger.class).satisfy(restoration);
			Dungeon.hero.HP = (int)Math.ceil(Math.min(Dungeon.hero.HT, Dungeon.hero.HP+(restoration*0.4f)));
			Dungeon.hero.sprite.emitter().burst( Speck.factory(Speck.HEALING), 1 );
		}

		return super.defenseProc(enemy, damage);
	}

	@Override
	public boolean surprisedBy(Char enemy) {
		return super.surprisedBy(enemy) || (!enemySeen && state != PASSIVE);
	}

	public Char getEnemy() {
		return enemy;
	}

	public void aggro(Char ch ) {
		enemy = ch;
		if (state != PASSIVE){
			state = HUNTING;
		}
	}
	
	public boolean isTargeting( Char ch){
		return enemy == ch;
	}

	@Override
	public void damage(int dmg,  DamageSrc src) {

		if (state == SLEEPING) {
			state = WANDERING;
		}
		if (state != HUNTING) {
			alerted = true;
		}

		if (Dungeon.isChallenged(Challenges.CORROSION)) {
			int amount = Math.min( (int) (100*((float)dmg/(float)HT)), 100);
			GameScene.add(Blob.seed(this.pos, amount, DemonGas.class).setStrength(Corrosion.defaultStrength(Dungeon.getScaling())));
		}
		
		super.damage( dmg, src);
	}
	
	
	@Override
	public void destroy() {
		
		super.destroy();
		
		Dungeon.level.mobs.remove( this );
		
		if (Dungeon.hero.isAlive()) {
			
			if (alignment == Alignment.ENEMY) {
				Statistics.enemiesSlain++;
				Badges.validateMonstersSlain();
				Statistics.qualifiedForNoKilling = false;
				
				int exp = Dungeon.hero.lvl <= level + 1 ? experience() : 0;
				Dungeon.hero.earnExp(exp, getClass());

				//If the hero is cursed, eliminate an enemy from the curse
				CupOfSuffering.Cursed.eliminateEnemy(Dungeon.hero);
			}
		}
	}
	
	@Override
	public void die( DamageSrc cause ) {

		if (hitWithRanged) {
			Statistics.thrownAssists++;
			Badges.validateHuntressUnlock();
		}

		if (alignment == Alignment.ENEMY && Dungeon.hero != null) {
			rollToDropLoot();
		}

		if (Dungeon.hero != null) {

			if (Dungeon.hero.isAlive() && !Dungeon.level.heroFOV[pos]) {
				GLog.info(Messages.get(this, "died"));
			}

			if (Dungeon.hero.subClass == HeroSubClass.NECROMANCER && Random.Int(3) == 0) {
				if (Wraith.spawnAt(pos) != null) {
					Dungeon.hero.sprite.emitter().burst(ShadowParticle.CURSE, 6);
				}
			}
		}

		DarkGas.MindBreak mindBreak = buff(DarkGas.MindBreak.class);
		if (mindBreak != null && !isImmune(Corruption.class)) {
			mindBreak.detach();
			updateHT(true);
			HP = HT;
			Buff.affect(this, DeferredDeath.class, DarkGas.MindBreak.MOB_LIFETIME);
			Buff.affect(this, Corruption.class);
			return;
		}

		super.die(cause);
	}

	protected boolean doSupport(Char ch) {
		if (sprite != null && (sprite.visible || ch.sprite.visible)) {
			sprite.operate(ch.pos, new Callback() {
				@Override
				public void call() {
					support(ch);
					next();
				}
			});
			spend( attackDelay() );
			return true;
		} else {
			support( ch );
			spend( attackDelay() );
			return true;
		}
	}

	/*public boolean canSupport(Char ch) {
		return Dungeon.level.adjacent(pos, ch.pos);
	}*/

	//By default heals char for it's damage roll
	public boolean support(Char ch) {
		int heal = damageRoll();
		ch.heal(heal, false);
		return true;
	}

	public float corruptionResistance() {
		//base enemy resistance is usually based on their exp, but in special cases it is based on other criteria
		float enemyResist = 1 + this.level;
		if (this instanceof Mimic || this instanceof Statue || this instanceof Wraith){
			enemyResist = 3 + level * 2;
		} else if (this instanceof Piranha || this instanceof Bee) {
			enemyResist = 1 + level / 2f;
		}  else if (this.properties().contains(Property.BOSS)) {
			enemyResist = 4 + level * 3;
		} else if (this instanceof Yog.Larva || this instanceof King.Undead){
			enemyResist = 1 + 5;
		} else if (this instanceof Swarm){
			//child swarms don't give exp, so we force this here.
			enemyResist = 1 + new Swarm().experience();
		}
		//100% health: 5x resist   75%: 3.25x resist   50%: 2x resist   25%: 1.25x resist
		enemyResist *= 1 + 4*Math.pow(HP/(float)HT, 2);
		return enemyResist;
	}
	
	public void rollToDropLoot(){
		if (experience() == 0) return;
		
		float lootChance = this.lootChance;
		lootChance *= RingOfWealth.dropChanceMultiplier();
		
		if (Random.Float() < lootChance) {
			Item loot = createLoot();
			if (loot != null) {
				Dungeon.level.drop(loot, pos).sprite.drop();
			}
		}
		
		//ring of wealth logic
		if (Ring.hasRing(Dungeon.hero, RingOfWealth.Wealth.class)) {
			int rolls = 1;
			if (properties.contains(Property.BOSS)) rolls = 15;
			else if (properties.contains(Property.MINIBOSS)) rolls = 5;
			ArrayList<Item> bonus = RingOfWealth.tryForBonusDrop(Dungeon.hero, rolls);
			if (bonus != null && !bonus.isEmpty()) {
				for (Item b : bonus) Dungeon.level.drop(b, pos).sprite.drop();
				RingOfWealth.showFlareForBonusDrop(sprite);
			}
		}
		
		//lucky enchant logic
		if (Dungeon.hero.lvl <= Dungeon.getScaling() + 1 && buff(Lucky.LuckProc.class) != null){
			Dungeon.level.drop(Lucky.genLoot(), pos).sprite.drop();
			Lucky.showFlare(sprite);
		}
		//Cultist subclass logic
		if (Dungeon.hero.subClass == HeroSubClass.CULTIST && Random.Int(3) == 0 && !(this.properties().contains(Property.BOSS) || this.properties().contains(Property.INORGANIC))) {
			Dungeon.level.drop(new Soul().setMob(this), pos).sprite.drop();
		}

		LuckyBadge.LuckBuff buff = Dungeon.hero.buff(LuckyBadge.LuckBuff.class);
		//Lucky badge logic
		if (buff != null) {
			LuckyBadge.dropItem(this, buff.nDrops());
		}
	}
	
	protected Object loot = null;
	protected float lootChance = 0;
	
	@SuppressWarnings("unchecked")
	protected Item createLoot() {
		Item item;
		if (loot instanceof Generator.Category) {

			item = Generator.random( (Generator.Category)loot );

		} else if (loot instanceof Class<?>) {

			item = Generator.random( (Class<? extends Item>)loot );

		} else {

			item = (Item)loot;

		}
		return item;
	}

	//how many mobs this one should count as when determining spawning totals
	public float spawningWeight(){
		return 1;
	}
	
	public boolean reset() {
		return false;
	}
	
	public void beckon( int cell ) {
		
		notice();
		
		if (state != HUNTING) {
			state = WANDERING;
		}
		target = cell;
	}
	
	public String description() {
		return Messages.get(this, "desc");// + Messages.get(Mob.class, "info", elementalType().label());
	}

	public boolean following(Char follow) {
		if (alignment == follow.alignment) {
			Char targetChar = Actor.findChar(this.target);
			return targetChar == follow;
		}
		return false;

	}
	
	public void yell( String str ) {
		GLog.newLine();
		GLog.negative( "%s: \"%s\" ", Messages.titleCase(name()), str );
	}

	public static class Parry extends ParryBuff {

		private Mob mob;

		@Override
		protected void emptyCharge() {
			mob.useStamina(mob.stamina);
		}

		private static final String MOB_ID = "mob-id";

		public void setMob(Mob mob) {
			this.mob = mob;
		}

		@Override
		public int absorbDamage(DamageSrc src, int damage) {
			if (mob == null) return damage;
			parried = true;
			int defense = mob.defense();
			if (defense >= damage) {
				float chargeToLose = (damage / (float) defense) * 120;
				mob.useStamina(chargeToLose);
				return 0;
			} else {
				damage -= defense;
				emptyCharge();
				return damage;
			}
		}

		@Override
		public void affectEnemy(Char enemy, boolean parry) {
			if (mob == null) return;
			int damage = (int) (mob.damageRoll()/(2*mob.attackDelay));
			if (parry) damage *= 2;
			enemy.damage(damage, mob);
		}

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(MOB_ID, mob.id());
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			mob = (Mob) Actor.findById(bundle.getInt(MOB_ID));
		}
	}

	public interface AiState {
		boolean act( boolean enemyInFOV, boolean justAlerted );
		float noticeFactor();
	}

	private float suspicion = 1;
	private static final int MAX_SUSPICION = 6;
	private static final int SUSPICION_THRESHOLD = 3;

	public void notice() {
		enemySeen = true;
		for (Mob mob : Dungeon.level.mobs.toArray( new  Mob[0] )) {
			//Mobs get less suspicion increase drop-off distance on swarm intelligence.
			float increase = SUSPICION_THRESHOLD - Dungeon.level.distance(pos, mob.pos)/(Dungeon.isChallenged(Challenges.SWARM_INTELLIGENCE) ? 5f : 3f);
			if (mob.alignment == alignment && mob != this) {
				mob.increaseSuspicion(increase);
			}
		}
	}

	private void increaseSuspicion(float amount) {
		suspicion += amount;
		if (suspicion > MAX_SUSPICION) {
			suspicion = MAX_SUSPICION;
		}
		if (Math.round(suspicion) == SUSPICION_THRESHOLD && sprite != null) {
			sprite.showAlert();
		}
	}

	private void decreaseSuspicion(float amount) {
		suspicion -= amount;
		if (suspicion < 0) {
			suspicion = 0;
		}
		if (Math.round(suspicion) == SUSPICION_THRESHOLD && sprite != null) {
			sprite.showLost();
		}
	}

	private boolean checkEnemy(Char enemy) {
		if (enemy == null) {
			return false;
		}
		if (notice(enemy)) {
			increaseSuspicion(1);
			if (suspicion <= SUSPICION_THRESHOLD) checkEnemy(enemy);
			return true;
		} else {
			if (!fieldOfView(enemy.pos)) {
				//Swarm Intelligence causes mobs to forget you slower.
				decreaseSuspicion(Dungeon.isChallenged(Challenges.SWARM_INTELLIGENCE) ? 2 / 3f : 1.5f);
			}
			return false;
		}
	}

	private boolean threshold() {
		return suspicion > SUSPICION_THRESHOLD;
	}

	protected class Sleeping implements AiState {

		public static final String TAG	= "SLEEPING";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if (enemyInFOV && threshold()) {

				enemySeen = true;

				notice();
				state = HUNTING;
				target = enemy.pos;

				if (Dungeon.isChallenged( Challenges.SWARM_INTELLIGENCE )) {
					for (Mob mob : Dungeon.level.mobs.toArray( new  Mob[0] )) {
						if (Dungeon.level.distance(pos, mob.pos) <= 8 && mob.state != mob.HUNTING) {
							mob.beckon( target );
						}
					}
				}

				spend( TIME_TO_WAKE_UP );

			} else {

				enemySeen = false;

				spend( TICK );

			}
			return true;
		}

		@Override
		public float noticeFactor() {
			return 3;
		}
	}

	public class Wandering implements AiState {

		public static final String TAG	= "WANDERING";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if (enemyInFOV && threshold()) {

				return noticeEnemy();

			} else {

				return continueWandering();
			}
		}

		@Override
		public float noticeFactor() {
			return 4;
		}

		protected boolean noticeEnemy(){
			enemySeen = true;

			notice();
			alerted = true;
			state = HUNTING;
			target = enemy.pos;

			if (Dungeon.isChallenged( Challenges.SWARM_INTELLIGENCE )) {
				for (Mob mob : Dungeon.level.mobs) {
					if (Dungeon.level.distance(pos, mob.pos) <= 8 && mob.state != mob.HUNTING) {
						mob.beckon( target );
					}
				}

			}

			return true;
		}

		protected boolean continueWandering(){
			enemySeen = false;

			int oldPos = pos;
			if (target != -1 && getCloser( target )) {
				spend( 1 / speed() );
				return moveSprite( oldPos, pos );
			} else {
				target = Dungeon.level.randomDestination(Mob.this);
				spend( TICK );
			}

			return true;
		}
	}

	public class Following extends Wandering implements AiState {

		private Char toFollow(Char start) {
			Char toFollow = start;
			boolean[] passable = Dungeon.level.passable();
			PathFinder.buildDistanceMap(pos, passable, Integer.MAX_VALUE);//No limit on distance
			for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
				if (mob.alignment == alignment && PathFinder.distance[toFollow.pos] > PathFinder.distance[mob.pos] && mob.following(toFollow)) {
					toFollow = toFollow(mob);//If we find a mob already following the target, ensure there is not a mob already following them. This allows even massive chains of allies to traverse corridors correctly.
				}
			}
			return toFollow;
		}

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {

			//Ensure there is direct line of sight from ally to enemy, and the distance is small. This is enforced so that allies don't end up trailing behind when following hero.
			if ( enemyInFOV && threshold() && Dungeon.level.distance(pos, enemy.pos) < range + 5) {

				enemySeen = true;

				notice();
				alerted = true;

				state = HUNTING;
				target = enemy.pos;

			} else {

				enemySeen = false;
				Char toFollow = toFollow(Dungeon.hero);
				int oldPos = pos;
				//always move towards the target when wandering
				if (getCloser( target = toFollow.pos )) {
					if (!Dungeon.level.adjacent(toFollow.pos, pos) && Actor.findChar(pos) == null) {
						getCloser( target = toFollow.pos );
					}
					spend( 1 / speed() );
					return moveSprite( oldPos, pos );
				} else {
					spend( TICK );
				}

			}
			return true;
		}

	}

	protected class Hunting implements AiState {

		public static final String TAG	= "HUNTING";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;

			if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy ) && threshold()) {

				target = enemy.pos;

				if (Dungeon.level.adjacent(pos, enemy.pos) && canParry()) {
					doParry();
					return true;
				}
				return doAttack( enemy );

			} else {

				if (enemyInFOV && threshold()) {
					target = enemy.pos;
				} else {
					sprite.showLost();
					state = WANDERING;
					enemy = null;
					target = Dungeon.level.randomDestination(Mob.this);
					return true;
				}
				
				int oldPos = pos;
				if (target != -1 && getCloser( target )) {
					
					spend( 1 / speed() );
					return moveSprite( oldPos,  pos );

				} else {
					//if moving towards an enemy isn't possible, try to switch targets to another enemy that is closer
					Char newEnemy = chooseEnemy();
					if (newEnemy != null && enemy != newEnemy){
						enemy = newEnemy;
						return act(enemyInFOV, justAlerted);
					}

					spend( TICK );
					if (enemy == null) {
						sprite.showLost();
						state = WANDERING;
						target = Dungeon.level.randomDestination(Mob.this);
					}
					return true;
				}
			}
		}

		@Override
		public float noticeFactor() {
			return 4;
		}
	}

	//FIXME this works fairly well but is coded poorly. Should refactor
	protected class Fleeing implements AiState {

		public static final String TAG	= "FLEEING";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;
			//loses target when 0-dist rolls a 6 or greater.
			if (enemy == null || !enemyInFOV && 1 + Random.Int(Dungeon.level.distance(pos, target)) >= 6){
				target = -1;
			
			//if enemy isn't in FOV, keep running from their previous position.
			} else if (enemyInFOV) {
				target = enemy.pos;
			}

			int oldPos = pos;
			if (target != -1 && getFurther( target )) {

				spend( 1 / speed() );
				return moveSprite( oldPos, pos );

			} else {

				spend( TICK );
				nowhereToRun();

				return true;
			}
		}

		@Override
		public float noticeFactor() {
			return 3;
		}

		protected void nowhereToRun() {
		}
	}

	protected class Passive implements AiState {

		public static final String TAG	= "PASSIVE";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = false;
			spend( TICK );
			return true;
		}

		@Override
		public float noticeFactor() {
			return 0;
		}
	}
	
	
	private static ArrayList<Mob> heldMobs = new ArrayList<>();
	
	public static void holdMobs( Level level ) {
		heldMobs.clear();
		for (Mob mob : level.mobs.toArray( new  Mob[0] )) {
			//preserve intelligent allies if they are near the hero
			if (mob.alignment == Alignment.ALLY
					&& mob.intelligentAlly
					&& Dungeon.level.distance(Dungeon.hero.pos, mob.pos) <= 3){
				level.mobs.remove( mob );
				heldMobs.add(mob);
			} /*else if (mob.properties().contains(Property.BOSS)
					|| (mob.properties().contains(Property.MINIBOSS)
					&& level.distance(Dungeon.hero.pos, mob.pos) < 5)) {
				level.mobs.remove( mob );
				heldMobs.add(mob);
			}*/
		}
	}
	
	public static void restoreMobs( Level level, int pos ){
		if (!heldMobs.isEmpty()){
			
			ArrayList<Integer> candidatePositions = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS8) {
				if (!Dungeon.level.solid(i+pos) && level.findMob(i+pos) == null){
					candidatePositions.add(i+pos);
				}
			}
			Collections.shuffle(candidatePositions);
			
			for (Mob ally : heldMobs) {
				level.mobs.add(ally);
				ally.state = ally.WANDERING;
				
				if (!candidatePositions.isEmpty()){
					ally.pos = candidatePositions.remove(0);
				} else {
					ally.pos = pos;
				}
				
			}
		}
		heldMobs.clear();
	}
	
	public static void clearHeldAllies(){
		heldMobs.clear();
	}
}


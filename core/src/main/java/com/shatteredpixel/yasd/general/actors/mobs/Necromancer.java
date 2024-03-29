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

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Adrenaline;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Corruption;
import com.shatteredpixel.yasd.general.effects.Beam;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Pushing;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.potions.PotionOfRestoration;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.NecromancerSprite;
import com.shatteredpixel.yasd.general.sprites.SkeletonSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

public class Necromancer extends Mob {
	
	{
		spriteClass = NecromancerSprite.class;

		healthFactor = 0.8f;

        loot = new PotionOfRestoration();
		lootChance = 0.2f;

		
		HUNTING = new Hunting();
	}
	
	public boolean summoning = false;
	private Emitter summoningEmitter = null;
	private int summoningPos = -1;
	
	private boolean firstSummon = true;
	
	private NecroSkeleton mySkeleton;
	private int storedSkeletonID = -1;
	
	@Override
	public void updateSpriteState() {
		super.updateSpriteState();
		
		if (summoning && summoningEmitter == null){
			summoningEmitter = CellEmitter.get( summoningPos );
			summoningEmitter.pour(Speck.factory(Speck.RATTLE), 0.2f);
			sprite.zap( summoningPos );
		}
	}
	
	@Override
	public void die( DamageSrc cause) {
		if (storedSkeletonID != -1){
			Actor ch = Actor.findById(storedSkeletonID);
			storedSkeletonID = -1;
			if (ch instanceof NecroSkeleton){
				mySkeleton = (NecroSkeleton) ch;
			}
		}
		
		if (mySkeleton != null && mySkeleton.isAlive()){
			mySkeleton.die(new DamageSrc(Element.META, null));
		}
		
		if (summoningEmitter != null){
			summoningEmitter.killAndErase();
			summoningEmitter = null;
		}
		
		super.die(cause);
	}
	
	private static final String SUMMONING = "summoning";
	private static final String FIRST_SUMMON = "first_summon";
	private static final String SUMMONING_POS = "summoning_pos";
	private static final String MY_SKELETON = "my_skeleton";
	
	@Override
	public void storeInBundle( Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( SUMMONING, summoning );
		bundle.put( FIRST_SUMMON, firstSummon );
		if (summoning){
			bundle.put( SUMMONING_POS, summoningPos);
		}
		if (mySkeleton != null){
			bundle.put( MY_SKELETON, mySkeleton.id() );
		}
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle) {
		super.restoreFromBundle(bundle);
		summoning = bundle.getBoolean( SUMMONING );
		if (bundle.contains(FIRST_SUMMON)) firstSummon = bundle.getBoolean(FIRST_SUMMON);
		if (summoning){
			summoningPos = bundle.getInt( SUMMONING_POS );
		}
		if (bundle.contains( MY_SKELETON )){
			storedSkeletonID = bundle.getInt( MY_SKELETON );
		}
	}

	@Override
	public boolean support(Char ch) {
		if (ch == null || ch.sprite == null || !ch.isAlive() || !Ballistica.canHit(this, ch, shotType)) {
			return false;
		}

		//heal skeleton first
		if (sprite.visible || ch.sprite.visible) {
			sprite.parent.add(new Beam.HealthRay(sprite.center(), ch.sprite.center()));
		}
		if (ch.HP < ch.HT){
			ch.heal(ch.HT/3, false);

		} else {
			Buff.prolong(ch, Adrenaline.class, 3f);
		}
		return true;
	}
	
	private class Hunting extends Mob.Hunting{
		
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			enemySeen = enemyInFOV;
			
			if (storedSkeletonID != -1){
				Actor ch = Actor.findById(storedSkeletonID);
				storedSkeletonID = -1;
				if (ch instanceof NecroSkeleton){
					mySkeleton = (NecroSkeleton) ch;
				}
			}
			
			if (summoning){
				
				//push anything on summoning spot away, to the furthest valid cell
				if (Actor.findChar(summoningPos) != null) {
					int pushPos = pos;
					for (int c : PathFinder.NEIGHBOURS8) {
						if (Actor.findChar(summoningPos + c) == null
								&& Dungeon.level.passable(summoningPos + c)
								&& Dungeon.level.trueDistance(pos, summoningPos + c) > Dungeon.level.trueDistance(pos, pushPos)) {
							pushPos = summoningPos + c;
						}
					}
					
					//push enemy, or wait a turn if there is no valid pushing position
					if (pushPos != pos) {
						Char ch = Actor.findChar(summoningPos);
						Actor.addDelayed( new  Pushing( ch, ch.pos, pushPos ), -1 );
						
						ch.pos = pushPos;
						Dungeon.level.occupyCell(ch );
						
					} else {
						spend(TICK);
						return true;
					}
				}
				
				summoning = firstSummon = false;
				
				mySkeleton = Mob.create(NecroSkeleton.class);
				mySkeleton.pos = summoningPos;
				GameScene.add( mySkeleton );
				Dungeon.level.occupyCell( mySkeleton );
				Sample.INSTANCE.play(Assets.Sounds.BONES);
				summoningEmitter.burst( Speck.factory( Speck.RATTLE ), 5 );
				sprite.idle();
				
				if (buff(Corruption.class) != null){
					Buff.affect(mySkeleton, Corruption.class);
				}
				
				spend(TICK);
				return true;
			}
			
			if (mySkeleton != null &&
					(!mySkeleton.isAlive()
					|| !Dungeon.level.mobs.contains(mySkeleton)
					|| mySkeleton.alignment != alignment)){
				mySkeleton = null;
			}
			
			//if enemy is seen, and enemy is within range, and we haven no skeleton, summon a skeleton!
			if (enemySeen && Dungeon.level.distance(pos, enemy.pos) <= 4 && mySkeleton == null){
				
				summoningPos = -1;
				for (int c : PathFinder.NEIGHBOURS8){
					if (Actor.findChar(enemy.pos+c) == null
							&& Dungeon.level.passable(enemy.pos+c)
							&& fieldOfView[enemy.pos+c]
							&& Dungeon.level.trueDistance(pos, enemy.pos+c) < Dungeon.level.trueDistance(pos, summoningPos)){
						summoningPos = enemy.pos+c;
					}
				}
				
				if (summoningPos != -1){
					
					summoning = true;
					summoningEmitter = CellEmitter.get(summoningPos);
					summoningEmitter.pour(Speck.factory(Speck.RATTLE), 0.2f);
					
					//sprite.zap( summoningPos );
					((NecromancerSprite)sprite).charge();
					
					spend( firstSummon ? TICK*2 : 3*TICK );
				} else {
					//wait for a turn
					spend(TICK);
				}
				
				return true;
			//otherwise, if enemy is seen, and we have a skeleton...
			} else if (enemySeen && mySkeleton != null){
				
				target = enemy.pos;
				spend(TICK);
				
				if (!fieldOfView[mySkeleton.pos]){
					
					//if the skeleton is not next to the enemy
					//teleport them to the closest spot next to the enemy that can be seen
					if (!Dungeon.level.adjacent(mySkeleton.pos, enemy.pos)){
						int telePos = -1;
						for (int c : PathFinder.NEIGHBOURS8){
							if (Actor.findChar(enemy.pos+c) == null
									&& Dungeon.level.passable(enemy.pos+c)
									&& fieldOfView[enemy.pos+c]
									&& Dungeon.level.trueDistance(pos, enemy.pos+c) < Dungeon.level.trueDistance(pos, telePos)){
								telePos = enemy.pos+c;
							}
						}
						
						if (telePos != -1){
							
							ScrollOfTeleportation.appear(mySkeleton, telePos);
							mySkeleton.teleportSpend();
						}
					}
					
					return true;
					
				} else {
					//zap skeleton
					doSupport(mySkeleton);
				}
				
				return true;
				
			//otherwise, default to regular hunting behaviour
			} else {
				return super.act(enemyInFOV, justAlerted);
			}
		}
	}
	
	public static class NecroSkeleton extends Skeleton {
		
		{
			state = WANDERING;
			
			spriteClass = NecroSkeletonSprite.class;
			
			//no loot or exp

            damageFactor = 1.2f;

			lootChance = 0f;
			
			//Less HP than Shattered, more damage
			healthFactor = 0.7f;
		}

		@Override
		public Element elementalType() {
			return Element.PHYSICAL;
		}

		@Override
		public float spawningWeight() {
			return 0;
		}

		private void teleportSpend(){
			spend(TICK);
		}

        public static class NecroSkeletonSprite extends SkeletonSprite{
			
			public NecroSkeletonSprite(){
				super();
				brightness(0.75f);
			}
			
			@Override
			public void resetColor() {
				super.resetColor();
				brightness(0.75f);
			}
		}
		
	}
}

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
import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.LockedFloor;
import com.shatteredpixel.yasd.general.actors.hero.HeroSubClass;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.TomeOfMastery;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.chapters.prison.OldPrisonBossLevel;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.traps.GrippingTrap;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.TenguSprite;
import com.shatteredpixel.yasd.general.ui.BossHealthBar;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

//Exists to support pre-0.7.5 saves
public class OldTengu extends Boss {
	
	{
		spriteClass = TenguSprite.class;
		
		HP = HT = 120;

		HUNTING = new  Hunting();

		flying = true; //doesn't literally fly, but he is fleet-of-foot enough to avoid hazards

		properties.add(Property.BOSS);
	}
	
	@Override
	protected void onAdd() {
		//when he's removed and re-added to the fight, his time is always set to now.
		spend(-cooldown());
		super.onAdd();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 6, 20 );
	}

    @Override
	public void damage(int dmg,  DamageSrc src) {
		
		OldPrisonBossLevel.State state = ((OldPrisonBossLevel)Dungeon.level).state();
		
		int hpBracket;
		if (state == OldPrisonBossLevel.State.FIGHT_START){
			hpBracket = 12;
		} else {
			hpBracket = 20;
		}

		int beforeHitHP = HP;
		super.damage(dmg, src);
		dmg = beforeHitHP - HP;

		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null) {
			int multiple = state == OldPrisonBossLevel.State.FIGHT_START ? 1 : 4;
			lock.addTime(dmg*multiple);
		}

		//phase 2 of the fight is over
		if (HP == 0 && state == OldPrisonBossLevel.State.FIGHT_ARENA) {
			//let full attack action complete first
			Actor.add(new  Actor() {
				
				{
					actPriority = VFX_PRIO;
				}
				
				@Override
				protected boolean act() {
					Actor.remove(this);
					((OldPrisonBossLevel)Dungeon.level).progress();
					return true;
				}
			});
			return;
		}
		
		//phase 1 of the fight is over
		if (state == OldPrisonBossLevel.State.FIGHT_START && HP <= HT/2){
			HP = (HT/2)-1;
			yell(Messages.get(this, "interesting"));
			((OldPrisonBossLevel)Dungeon.level).progress();
			BossHealthBar.bleed(true);

		//if tengu has lost a certain amount of hp, jump
		} else if (beforeHitHP / hpBracket != HP / hpBracket) {
			jump();
		}
	}

	@Override
	public boolean isAlive() {
		return Dungeon.level.mobs.contains(this); //Tengu has special death rules, see prisonbosslevel.progress()
	}

	@Override
	public void die( DamageSrc cause ) {
		
		if (Dungeon.hero.subClass == HeroSubClass.NONE) {
			Dungeon.level.drop( new  TomeOfMastery(), pos ).sprite.drop();
		}
		
		GameScene.bossSlain();
		super.die( cause );
		
		Badges.validateBossSlain();
		
		yell( Messages.get(this, "defeated") );
	}

	@Override
    public boolean canAttack(@NotNull Char enemy) {
		return new  Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos;
	}

	//tengu's attack is always visible
	@Override
	protected boolean doAttack(Char enemy) {
		sprite.attack( enemy.pos );
		spend( attackDelay() );
		return false;
	}

	private void jump() {
		
		Level level = Dungeon.level;
		
		//incase tengu hasn't had a chance to act yet
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new  boolean[Dungeon.level.length()];
			Dungeon.level.updateFieldOfView( this, fieldOfView );
		}
		
		if (enemy == null) enemy = chooseEnemy();
		if (enemy == null) return;

		int newPos;
		//if we're in phase 1, want to warp around within the room
		if (((OldPrisonBossLevel)Dungeon.level).state() == OldPrisonBossLevel.State.FIGHT_START) {
			
			//place new  traps
			int tries;
			for (int i=0; i < 4; i++) {
				int trapPos;
				tries = 15;
				do {
					trapPos = Random.Int( level.length() );
				} while (tries-- > 0 && !level.hasTrap(pos));
				
				if (level.getTerrain(trapPos) == Terrain.EMPTY && (level.trap(trapPos) == null || !level.trap(trapPos).active)) {
					level.setTrap( new  GrippingTrap().reveal(), trapPos );
					ScrollOfMagicMapping.discover( trapPos );
				}
			}
			
			tries = 50;
			do {
				newPos = Random.IntRange(3, 7) + 32*Random.IntRange(26, 30);
			} while ( (level.adjacent(newPos, enemy.pos) || Actor.findChar(newPos) != null)
					&& --tries > 0);
			if (tries <= 0) return;

		//otherwise go wherever, as long as it's a little bit away
		} else {
			do {
				newPos = Random.Int(level.length());
			} while (
					level.solid(newPos) ||
					level.distance(newPos, enemy.pos) < 8 ||
					Actor.findChar(newPos) != null);
		}
		
		if (level.heroFOV[pos]) CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );

		sprite.move( pos, newPos );
		move( newPos );
		
		if (level.heroFOV[newPos]) CellEmitter.get( newPos ).burst( Speck.factory( Speck.WOOL ), 6 );
		Sample.INSTANCE.play( Assets.Sounds.PUFF );
		
		spend( 1 / speed() );
	}
	
	@Override
	public void notice() {
		super.notice();
		if (!BossHealthBar.isAssigned()) {
			BossHealthBar.assignBoss(this);
			if (HP <= HT/2) BossHealthBar.bleed(true);
			if (HP == HT) {
				yell(Messages.get(this, "notice_mine", Dungeon.hero.name()));
			} else {
				yell(Messages.get(this, "notice_face", Dungeon.hero.name()));
			}
		}
	}

	@Override
	public void restoreFromBundle( Bundle bundle) {
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
		if (HP <= HT/2) BossHealthBar.bleed(true);
	}

	//tengu is always hunting
	private class Hunting extends Mob.Hunting{

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			enemySeen = enemyInFOV;
			if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

				return doAttack( enemy );

			} else {

				if (enemyInFOV) {
					target = enemy.pos;
				} else {
					chooseEnemy();
					if (enemy != null) {
						target = enemy.pos;
					}
				}

				spend( TICK );
				return true;

			}
		}
	}
}

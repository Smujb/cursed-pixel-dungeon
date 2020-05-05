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
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.Fire;
import com.shatteredpixel.yasd.general.actors.blobs.ToxicGas;
import com.shatteredpixel.yasd.general.actors.buffs.Amok;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Charm;
import com.shatteredpixel.yasd.general.actors.buffs.LockedFloor;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.actors.buffs.Poison;
import com.shatteredpixel.yasd.general.actors.buffs.Sleep;
import com.shatteredpixel.yasd.general.actors.buffs.Terror;
import com.shatteredpixel.yasd.general.actors.buffs.Vertigo;
import com.shatteredpixel.yasd.general.effects.Pushing;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.general.items.artifacts.DriedRose;
import com.shatteredpixel.yasd.general.items.keys.SkeletonKey;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Grim;
import com.shatteredpixel.yasd.general.levels.traps.GrimTrap;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.FistSprite;
import com.shatteredpixel.yasd.general.sprites.LarvaSprite;
import com.shatteredpixel.yasd.general.sprites.YogSprite;
import com.shatteredpixel.yasd.general.ui.BossHealthBar;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class Yog extends Mob {
	
	{
		spriteClass = YogSprite.class;

		//HP = HT = 600;
		
		EXP = 50;
		
		state = PASSIVE;

		properties.add(Property.BOSS);
		properties.add(Property.IMMOVABLE);
		properties.add(Property.DEMONIC);
	}
	
	public Yog() {
		super();
	}
	
	public void spawnFists() {
		RottingFist fist1 = Mob.create(RottingFist.class);
		BurningFist fist2 = Mob.create(BurningFist.class);
		
		do {
			fist1.pos = pos + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
			fist2.pos = pos + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
		} while (!Dungeon.level.passable(fist1.pos) || !Dungeon.level.passable(fist2.pos) || fist1.pos == fist2.pos);
		
		GameScene.add( fist1 );
		GameScene.add( fist2 );

		notice();
	}

	@Override
	protected boolean act() {
		//heals 10 health per turn
		HP = Math.min( HT, HP + 10 );

		return super.act();
	}

	@Override
	public void damage(int dmg,  DamageSrc src) {

		HashSet<Mob> fists = new HashSet<>();

		for (Mob mob : Dungeon.level.mobs)
			if (mob instanceof RottingFist || mob instanceof BurningFist)
				fists.add( mob );

		dmg >>= fists.size();
		
		super.damage( dmg, src);

		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null) lock.addTime(dmg*0.5f);

		ArrayList<Integer> spawnPoints = new ArrayList<>();

		for (int i=0; i < PathFinder.NEIGHBOURS8.length; i++) {
			int p = pos + PathFinder.NEIGHBOURS8[i];
			if (Actor.findChar( p ) == null && (Dungeon.level.passable(p) || Dungeon.level.avoid(p))) {
				spawnPoints.add( p );
			}
		}

		if (spawnPoints.size() > 0) {
			//Larva larva = Mob.create(Larva.class);
			//larva.pos = Random.element( spawnPoints );
			Larva larva = Mob.spawnAt(Larva.class, Random.element(spawnPoints));
			//GameScene.add( larva );
			Actor.addDelayed( new Pushing( larva, pos, larva.pos ), -1 );
		}

		for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
			if ((mob instanceof BurningFist || mob instanceof RottingFist || mob instanceof Larva) & enemy != null) {
				mob.aggro( enemy );
			}
		}

	}
	
	@Override
	public void beckon( int cell ) {
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void die( DamageSrc cause ) {

		for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
			if (mob instanceof BurningFist || mob instanceof RottingFist) {
				mob.die( cause );
			}
		}
		
		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey( Dungeon.key ), pos ).sprite.drop();
		super.die( cause );
		
		yell( Messages.get(this, "defeated") );
	}
	
	@Override
	public void notice() {
		super.notice();
		if (!BossHealthBar.isAssigned()) {
			BossHealthBar.assignBoss(this);
			yell(Messages.get(this, "notice"));
			for (Char ch : Actor.chars()){
				if (ch instanceof DriedRose.GhostHero){

					((DriedRose.GhostHero) ch).sayBoss();
				}
			}
		}
	}
	
	{
		immunities.add( Grim.class );
		immunities.add( GrimTrap.class );
		immunities.add( Terror.class );
		immunities.add( Amok.class );
		immunities.add( Charm.class );
		immunities.add( Sleep.class );
		immunities.add( Burning.class );
		immunities.add( ToxicGas.class );
		immunities.add( ScrollOfRetribution.class );
		immunities.add( ScrollOfPsionicBlast.class );
		immunities.add( Vertigo.class );
	}

	@Override
	public void restoreFromBundle( Bundle bundle) {
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
	}

	public static class RottingFist extends Mob {
	
		private static final int REGENERATION = 4;
		
		{
			spriteClass = FistSprite.Rotting.class;


			healthFactor = 0.5f;
			//HP = HT = 300;
			//defenseSkill = 25;
			
			EXP = 0;
			
			state = WANDERING;

			properties.add(Property.MINIBOSS);
			properties.add(Property.DEMONIC);
			properties.add(Property.ACIDIC);
		}

		@Override
		public Element elementalType() {
			return Element.ACID;
		}
		
		@Override
		public boolean act() {
			
			if (Dungeon.level.liquid(pos) && HP < HT) {
				sprite.emitter().burst( ShadowParticle.UP, 2 );
				heal(REGENERATION);
			}
			
			return super.act();
		}

		@Override
		public void damage(int dmg,  DamageSrc src) {
			super.damage(dmg, src);
			LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
			if (lock != null) lock.addTime(dmg*0.5f);
		}
		
		{
			immunities.add( Paralysis.class );
			immunities.add( Amok.class );
			immunities.add( Sleep.class );
			immunities.add( Terror.class );
			immunities.add( Poison.class );
			immunities.add( Vertigo.class );
		}
	}
	
	public static class BurningFist extends Mob {
		
		{
			spriteClass = FistSprite.Burning.class;

			healthFactor = 1/3f;
			damageFactor = 1.5f;
			drFactor = 0.75f;
			elementaldrFactor = 1.5f;
			
			EXP = 0;
			
			state = WANDERING;

			properties.add(Property.MINIBOSS);
			properties.add(Property.DEMONIC);
			properties.add(Property.FIERY);
		}

		@Override
		public Element elementalType() {
			return Element.FIRE;
		}

		@Override
		public boolean act() {
			
			for (int i=0; i < PathFinder.NEIGHBOURS9.length; i++) {
				GameScene.add( Blob.seed( pos + PathFinder.NEIGHBOURS9[i], 2, Fire.class ) );
			}
			
			return super.act();
		}

		@Override
		public void damage(int dmg,  DamageSrc src) {
			super.damage(dmg, src);
			LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
			if (lock != null) lock.addTime(dmg*0.5f);
		}
		
		{
			immunities.add( Amok.class );
			immunities.add( Sleep.class );
			immunities.add( Terror.class );
			immunities.add( Vertigo.class );
		}
	}
	
	public static class Larva extends Mob {
		
		{
			spriteClass = LarvaSprite.class;

			healthFactor = 0.25f;
			baseSpeed = 2f;
			damageFactor = 2f;
			
			EXP = 0;
			maxLvl = -2;
			
			state = HUNTING;

			properties.add(Property.DEMONIC);
		}
		
		/*@Override
		public int attackSkill( Char target ) {
			return 30;
		}
		
		@Override
		public int drRoll(Element element) {
			return Random.NormalIntRange(0, 8);
		}*/
	}
}

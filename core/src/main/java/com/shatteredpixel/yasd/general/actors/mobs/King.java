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
import com.shatteredpixel.yasd.general.actors.buffs.Blindness;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.LockedFloor;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.actors.buffs.Terror;
import com.shatteredpixel.yasd.general.actors.buffs.Vertigo;
import com.shatteredpixel.yasd.general.effects.Flare;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.Torch;
import com.shatteredpixel.yasd.general.items.artifacts.DriedRose;
import com.shatteredpixel.yasd.general.items.artifacts.LloydsBeacon;
import com.shatteredpixel.yasd.general.items.keys.SkeletonKey;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Grim;
import com.shatteredpixel.yasd.general.levels.OldCityBossLevel;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.KingSprite;
import com.shatteredpixel.yasd.general.sprites.UndeadSprite;
import com.shatteredpixel.yasd.general.ui.BossHealthBar;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

public class King extends Mob {
	
	private static final int MAX_ARMY_SIZE = 5;
	
	{
		spriteClass = KingSprite.class;

		damageFactor = 1.3f;

		healthFactor = 3f;

		EXP = 40;
		
		Undead.count = 0;

		properties.add(Property.BOSS);
		properties.add(Property.UNDEAD);
	}
	
	private boolean nextPedestal = true;
	
	private static final String PEDESTAL = "pedestal";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( PEDESTAL, nextPedestal );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		nextPedestal = bundle.getBoolean( PEDESTAL );
		BossHealthBar.assignBoss(this);
	}
	
	@Override
	protected boolean getCloser( int target ) {
		return canTryToSummon() ?
			super.getCloser( ((OldCityBossLevel)Dungeon.level).pedestal( nextPedestal ) ) :
			super.getCloser( target );
	}
	
	@Override
    public boolean canAttack(@NotNull Char enemy) {
		return canTryToSummon() ?
				pos == ((OldCityBossLevel)Dungeon.level).pedestal( nextPedestal ) :
				Dungeon.level.adjacent( pos, enemy.pos );
	}
	
	protected boolean canTryToSummon() {
		if (paralysed <= 0 && Undead.count < maxArmySize()) {
			Char ch = Actor.findChar( ((OldCityBossLevel)Dungeon.level).pedestal( nextPedestal ) );
			return ch == this || ch == null;
		} else {
			return false;
		}
	}
	
	@Override
	protected boolean act() {
		if (canTryToSummon() && pos == ((OldCityBossLevel)Dungeon.level).pedestal( nextPedestal )) {
			summon();
			return true;
		} else {
			if (enemy != null && canTryToSummon() && Actor.findChar( ((OldCityBossLevel)Dungeon.level).pedestal( nextPedestal ) ) == enemy) {
				nextPedestal = !nextPedestal;
			}
			return super.act();
		}
	}

	@Override
	public void damage(int dmg,  DamageSrc src) {
		super.damage(dmg, src);
		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null) lock.addTime(dmg);
	}
	
	@Override
	public void die( DamageSrc cause ) {

		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey( Dungeon.key ), pos ).sprite.drop();

		Dungeon.level.drop(new Torch(), ((OldCityBossLevel)Dungeon.level).pedestal( nextPedestal )).sprite.drop(pos);
		
		super.die( cause );
		
		Badges.validateBossSlain();

		LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
		if (beacon != null) {
			beacon.upgrade();
		}

		yell( Messages.get(this, "defeated", Dungeon.hero.name()) );
	}

	@Override
	public void aggro(Char ch) {
		super.aggro(ch);
		for (Mob mob : Dungeon.level.mobs){
			if (mob instanceof Undead){
				mob.aggro(ch);
			}
		}
	}

	protected int maxArmySize() {
		return 1 + MAX_ARMY_SIZE * (HT - HP) / HT;
	}
	
	private void summon() {

		nextPedestal = !nextPedestal;
		
		sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.4f, 2 );
		Sample.INSTANCE.play( Assets.SND_CHALLENGE );
		
		boolean[] passable = Dungeon.level.passable();
		for (Char c : Actor.chars()) {
			passable[c.pos] = false;
		}
		
		int undeadsToSummon = maxArmySize() - Undead.count;

		PathFinder.buildDistanceMap( pos, passable, undeadsToSummon );
		PathFinder.distance[pos] = Integer.MAX_VALUE;
		int dist = 1;
		
	undeadLabel:
		for (int i=0; i < undeadsToSummon; i++) {
			do {
				for (int j=0; j < Dungeon.level.length(); j++) {
					if (PathFinder.distance[j] == dist) {
						
						Undead undead = Mob.create( Undead.class );
						undead.pos = j;
						GameScene.add( undead );
						
						ScrollOfTeleportation.appear( undead, j );
						new Flare( 3, 32 ).color( 0x000000, false ).show( undead.sprite, 2f ) ;
						
						PathFinder.distance[j] = Integer.MAX_VALUE;
						
						continue undeadLabel;
					}
				}
				dist++;
			} while (dist < undeadsToSummon);
		}
		
		yell( Messages.get(this, "arise") );
		spend( TICK );
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
		resistances.put( Element.DESTRUCTION, 2/3f );
		resistances.put( Element.TOXIC, 0.5f );
		resistances.put( Element.FIRE, 0.5f );
		resistances.put( Element.SHOCK, 0.5f );
		immunities.add( Paralysis.class );
		immunities.add( Vertigo.class );
		immunities.add( Blindness.class );
		immunities.add( Terror.class );
	}
	
	public static class Undead extends Mob {
		
		public static int count = 0;
		
		{
			spriteClass = UndeadSprite.class;


			healthFactor = 0.25f;
			damageFactor = 0.75f;
			//HP = HT = 28;
			//defenseSkill = 15;
			
			maxLvl = -2;
			EXP = 0;
			
			state = WANDERING;

			properties.add(Property.UNDEAD);
			properties.add(Property.INORGANIC);
		}
		
		@Override
		protected void onAdd() {
			count++;
			super.onAdd();
		}
		
		@Override
		protected void onRemove() {
			count--;
			super.onRemove();
		}
		
		/*@Override
		public int damageRoll() {
			return Random.NormalIntRange( 15, 25 );
		}
		
		@Override
		public int attackSkill( Char target ) {
			return 16;
		}*/
		
		@Override
		public int attackProc( Char enemy, int damage ) {
			damage = super.attackProc( enemy, damage );
			if (Random.Int( MAX_ARMY_SIZE ) == 0) {
				Buff.prolong( enemy, Paralysis.class, 1 );
			}
			
			return damage;
		}
		
		@Override
		public void die( DamageSrc cause ) {
			super.die( cause );
			
			if (Dungeon.level.heroFOV[pos]) {
				Sample.INSTANCE.play( Assets.SND_BONES );
			}
		}
		
		@Override
		public int drRoll(Element element) {
			return Random.NormalIntRange(0, 5);
		}

		{
			immunities.add( Grim.class );
			immunities.add( Paralysis.class );
		}
	}
}

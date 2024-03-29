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
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.GooWarn;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.LockedFloor;
import com.shatteredpixel.yasd.general.actors.buffs.Ooze;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.effects.particles.ElmoParticle;
import com.shatteredpixel.yasd.general.items.keys.SkeletonKey;
import com.shatteredpixel.yasd.general.items.quest.GooBlob;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.GooSprite;
import com.shatteredpixel.yasd.general.ui.BossHealthBar;
import com.shatteredpixel.yasd.general.utils.BArray;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

public class Goo extends Boss {

	{

        damageFactor = 1.5f;
		healthFactor = 0.8f;
		spriteClass = GooSprite.BossGoo.class;

		properties.add(Property.BOSS);
		properties.add(Property.DEMONIC);
		properties.add(Property.ACIDIC);

		rematchLevel = "goo";
	}

	private int pumpedUp = 0;

	@Override
	public int damageRoll() {
		//int min = 1;
		//int max = (HP*2 <= HT) ? 12 : 8;
		if (pumpedUp > 0) {
			pumpedUp = 0;
			PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid(), null ), 2 );
			for (int i = 0; i < PathFinder.distance.length; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE)
					CellEmitter.get(i).burst(ElmoParticle.FACTORY, 10);
			}
			Sample.INSTANCE.play( Assets.Sounds.BURNING );
			//return Random.NormalIntRange( min*3, max*3 );
			return super.damageRoll() * 3;
		} else {
			return super.damageRoll();
			//return Random.NormalIntRange( min, max );
		}
	}

	@Override
	public boolean act() {

		//ensures goo warning blob acts at the correct times
		//as normally blobs act one extra time when initialized if they normally act before
		//whatever spawned them
		GameScene.add(Blob.seed(pos, 0, GooWarn.class));

		if (Dungeon.level.liquid(pos) && HP < HT) {
			sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			heal(HT/30, false, true);
			if (HP*2 >= HT) {
				BossHealthBar.bleed(false);
				((GooSprite)sprite).spray(false);
			}
		}
		
		if (state != SLEEPING){
			Dungeon.level.seal();
		}

		//prevents goo pump animation from persisting when it shouldn't
		sprite.idle();

		return super.act();
	}

	@Override
    public boolean canAttack(@NotNull Char enemy) {
		return (pumpedUp > 0) ? distance( enemy ) <= 2 : super.canAttack(enemy);
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (Random.Int( 3 ) == 0) {
			Buff.affect( enemy, Ooze.class ).set( Ooze.DURATION );
			enemy.sprite.burst( 0x000000, 5 );
		}

		if (pumpedUp > 0) {
			Camera.main.shake( 3, 0.2f );
		}

		return damage;
	}

	@Override
	protected boolean doAttack( Char enemy ) {
		if (pumpedUp == 1) {
			((GooSprite)sprite).pumpUp();
			PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid(), null ), 2 );
			for (int i = 0; i < PathFinder.distance.length; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE)
					GameScene.add(Blob.seed(i, 1, GooWarn.class));
			}
			pumpedUp++;
			Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );

			spend( attackDelay() );

			return true;
		} else if (pumpedUp >= 2 || Random.Int( (HP*2 <= HT) ? 2 : 5 ) > 0) {

			boolean visible = Dungeon.level.heroFOV[pos];

			if (visible) {
				if (pumpedUp >= 2) {
					((GooSprite) sprite).pumpAttack();
				}
				else
					sprite.attack( enemy.pos );
			} else {
				attack( enemy);
			}

			spend( attackDelay() );

			return !visible;

		} else {

			pumpedUp++;

			((GooSprite)sprite).pumpUp();

			for (int i=0; i < PathFinder.NEIGHBOURS9.length; i++) {
				int j = pos + PathFinder.NEIGHBOURS9[i];
				if (!Dungeon.level.solid(j)) {
					GameScene.add(Blob.seed(j, 1, GooWarn.class));
				}
			}

			if (Dungeon.level.heroFOV[pos]) {
				sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "!!!") );
				GLog.negative( Messages.get(this, "pumpup") );
				Sample.INSTANCE.play( Assets.Sounds.CHARGEUP, 1f, 0.8f );
			}

			spend( attackDelay() );

			return true;
		}
	}

	@Override
	public boolean attack(Char enemy, boolean guaranteed, int dmg, DamageSrc src) {
		boolean result = super.attack( enemy, guaranteed, dmg, src);
		pumpedUp = 0;
		return result;
	}

	@Override
	protected boolean getCloser( int target ) {
		pumpedUp = 0;
		return super.getCloser( target );
	}

	@Override
	public void damage(int dmg,  DamageSrc src) {
		if (!BossHealthBar.isAssigned()){
			BossHealthBar.assignBoss( this );
		}
		boolean bleeding = (HP*2 <= HT);
		super.damage(dmg, src);
		if ((HP*2 <= HT) && !bleeding){
			BossHealthBar.bleed(true);
			sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "enraged"));
			((GooSprite)sprite).spray(true);
			yell(Messages.get(this, "gluuurp"));
		}
		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null) lock.addTime(dmg*2);
	}

	@Override
	public void die( DamageSrc cause ) {
		if (!isRematch()) Dungeon.level.drop( new SkeletonKey( Dungeon.key ), pos ).sprite.drop();
		
		super.die( cause );
		
		Dungeon.level.unseal();
		
		GameScene.bossSlain();
		
		//60% chance of 2 blobs, 30% chance of 3, 10% chance for 4. Average of 2.5
		int blobs = Random.chances(new  float[]{0, 0, 6, 3, 1});
		for (int i = 0; i < blobs; i++){
			int ofs;
			do {
				ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
			} while (!Dungeon.level.passable(pos + ofs));
			Dungeon.level.drop( new  GooBlob(), pos + ofs ).sprite.drop( pos );
		}
		
		Badges.validateBossSlain();
		
		yell( Messages.get(this, "defeated") );
	}
	
	@Override
	public void notice() {
		super.notice();
		if (!BossHealthBar.isAssigned()) {
			BossHealthBar.assignBoss(this);
			yell(Messages.get(this, "notice"));
		}
	}

	private final String PUMPEDUP = "pumpedup";

	@Override
	public void storeInBundle( Bundle bundle ) {

		super.storeInBundle( bundle );

		bundle.put( PUMPEDUP , pumpedUp );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {

		super.restoreFromBundle( bundle );

		pumpedUp = bundle.getInt( PUMPEDUP );
		if (state != SLEEPING) BossHealthBar.assignBoss(this);
		if (HP*2 <= HT) BossHealthBar.bleed(true);

	}
	
}

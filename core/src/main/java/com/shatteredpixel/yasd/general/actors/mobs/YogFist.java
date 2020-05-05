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
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.Fire;
import com.shatteredpixel.yasd.general.actors.blobs.ToxicGas;
import com.shatteredpixel.yasd.general.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.general.actors.buffs.Blindness;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Light;
import com.shatteredpixel.yasd.general.actors.buffs.Ooze;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.effects.particles.LeafParticle;
import com.shatteredpixel.yasd.general.items.armor.glyphs.Viscosity;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.FistSprite;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

public abstract class YogFist extends Mob {

	{
		//for doomed resistance
		EXP = 25;
		maxLvl = -2;

		state = HUNTING;

		viewDistance = Light.DISTANCE;

		properties.add(Property.MINIBOSS);
		properties.add(Property.DEMONIC);
	}

	private float rangedCooldown;

	protected void incrementRangedCooldown(){
		rangedCooldown += Random.NormalFloat(8, 12);
	}

	@Override
	protected boolean act() {
		if (paralysed <= 0 && rangedCooldown > 0) rangedCooldown--;
		return super.act();
	}

	@Override
	public boolean canAttack(@NotNull Char enemy) {
		if (rangedCooldown <= 0){
			return Ballistica.canHit(this, enemy, this.shotType);
		} else {
			return super.canAttack(enemy);
		}
	}

	@Override
	public boolean attack(Char enemy, boolean guaranteed) {
		if (!Dungeon.level.adjacent(pos, enemy.pos)) {
			incrementRangedCooldown();
		}
		return super.attack(enemy, guaranteed);
	}

	boolean invulnWarned = false;

	protected boolean isNearYog(){
		int yogPos = Dungeon.level.getExitPos() + 3*Dungeon.level.width();
		return Dungeon.level.distance(pos, yogPos) <= 4;
	}

	@Override
	public boolean isInvulnerable(Class effect) {
		return isNearYog();
	}

	@Override
	public void damage(int dmg, DamageSrc src) {
		if (isInvulnerable(src.getClass())){
			if (!invulnWarned){
				invulnWarned = true;
				GLog.w(Messages.get(this, "invuln_warn"));
			}
		}
		super.damage(dmg, src);
	}

	@Override
	public String description() {
		return Messages.get(YogFist.class, "desc") + "\n\n" + Messages.get(this, "desc");
	}

	private static final String RANGED_COOLDOWN = "ranged_cooldown";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(RANGED_COOLDOWN, rangedCooldown);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		rangedCooldown = bundle.getFloat(RANGED_COOLDOWN);
	}

	public static class Burning extends YogFist {

		{
			spriteClass = FistSprite.Burning.class;

			properties.add(Property.FIERY);
		}

		@Override
		public boolean act() {

			boolean result = super.act();

			if (Dungeon.level.map[pos] == Terrain.WATER){
				Dungeon.level.set( pos, Terrain.EMPTY);
				GameScene.updateMap( pos );
				CellEmitter.get( pos ).burst( Speck.factory( Speck.STEAM ), 10 );
			}

			int cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
			if (Dungeon.level.map[cell] == Terrain.WATER){
				Dungeon.level.set( cell, Terrain.EMPTY);
				GameScene.updateMap( cell );
				CellEmitter.get( cell ).burst( Speck.factory( Speck.STEAM ), 10 );
			}

			for (int i : PathFinder.NEIGHBOURS9) {
				int vol = Fire.volumeAt(pos+i, Fire.class);
				if (vol < 4 && !Dungeon.level.liquid(pos + i) && !Dungeon.level.solid(pos + i)){
					GameScene.add( Blob.seed( pos + i, 4 - vol, Fire.class ) );
				}
			}

			return result;
		}

		@Override
		public Element elementalType() {
			return Element.FIRE;
		}
	}

	public static class Soiled extends YogFist {

		{
			spriteClass = FistSprite.Soiled.class;
		}

		@Override
		public Element elementalType() {
			return Element.EARTH;
		}

		@Override
		public boolean act() {

			boolean result = super.act();

			int cell = pos + PathFinder.NEIGHBOURS9[Random.Int(9)];
			if (Dungeon.level.map[cell] == Terrain.GRASS){
				Dungeon.level.set( cell, Terrain.FURROWED_GRASS);
				GameScene.updateMap( cell );
				CellEmitter.get( cell ).burst( LeafParticle.GENERAL, 10 );
			}

			Dungeon.observe();

			for (int i : PathFinder.NEIGHBOURS9) {
				cell = pos + i;
				if (canSpreadGrass(cell)){
					Dungeon.level.set(pos+i, Terrain.GRASS);
					GameScene.updateMap( pos + i );
				}
			}

			return result;
		}

		@Override
		public void damage(int dmg, DamageSrc src) {
			int grassCells = 0;
			for (int i : PathFinder.NEIGHBOURS9) {
				if (Dungeon.level.map[pos+i] == Terrain.FURROWED_GRASS
						|| Dungeon.level.map[pos+i] == Terrain.HIGH_GRASS){
					grassCells++;
				}
			}
			if (grassCells > 0) dmg = Math.round(dmg * (6-grassCells)/6f);

			super.damage(dmg, src);
		}

		/*@Override
		protected void zap() {
			spend( 1f );

			if (hit( this, enemy, true )) {

				int dmg = damageRoll()/2;
				enemy.damage( dmg, this );
				Buff.affect( enemy, Roots.class, 3f );

				if (!enemy.isAlive() && enemy == Dungeon.hero) {
					Dungeon.fail( getClass() );
					GLog.n( Messages.get(Char.class, "kill", name()) );
				}

			} else {

				enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
			}

			for (int i : PathFinder.NEIGHBOURS9){
				int cell = enemy.pos + i;
				if (canSpreadGrass(cell)){
					if (Random.Int(5) == 0){
						Level.set(cell, Terrain.FURROWED_GRASS);
						GameScene.updateMap( cell );
					} else {
						Level.set(cell, Terrain.GRASS);
						GameScene.updateMap( cell );
					}
					CellEmitter.get( cell ).burst( LeafParticle.GENERAL, 10 );
				}
			}
			Dungeon.observe();

		}*/

		private boolean canSpreadGrass(int cell){
			int yogPos = Dungeon.level.getExitPos() + Dungeon.level.width()*3;
			return Dungeon.level.distance(cell, yogPos) > 4 && !Dungeon.level.solid(cell)
					&& !(Dungeon.level.map[cell] == Terrain.FURROWED_GRASS || Dungeon.level.map[cell] == Terrain.HIGH_GRASS);
		}

	}

	public static class Rotting extends YogFist {

		{
			spriteClass = FistSprite.Rotting.class;

			properties.add(Property.ACIDIC);
		}

		@Override
		public Element elementalType() {
			return Element.TOXIC;
		}

		@Override
		protected boolean act() {
			//ensures toxic gas acts at the appropriate time when added
			GameScene.add(Blob.seed(pos, 0, ToxicGas.class));

			if (Dungeon.level.liquid(pos) && HP < HT) {
				sprite.emitter().burst( Speck.factory(Speck.HEALING), 3 );
				heal(HT/100);
			}

			return super.act();
		}

		@Override
		public void damage(int dmg, DamageSrc src) {
			if (!isInvulnerable(src.getCause().getClass()) && !(src.getCause() instanceof Bleeding)){
				Bleeding b = buff(Bleeding.class);
				int curlvl = 0;
				if (b == null){
					b = new Bleeding();
				} else {
					curlvl = (int) b.level();
				}
				b.announced = false;
				b.set((dmg/2f) + curlvl);
				b.attachTo(this);
				sprite.showStatus(CharSprite.WARNING, b.toString() + " " + (int)b.level());
			} else{
				super.damage(dmg, src);
			}
		}

		@Override
		public int attackProc( Char enemy, int damage ) {
			damage = super.attackProc( enemy, damage );
			if (Dungeon.level.adjacent(this.pos, enemy.pos)) {
				if (Random.Int(2) == 0) {
					Buff.affect(enemy, Ooze.class).set(20f);
					enemy.sprite.burst(0xFF000000, 5);
				}
			} else {
				Blob.seed(enemy.pos, 300, ToxicGas.class);
			}

			return damage;
		}

		{
			immunities.add(ToxicGas.class);
		}

	}

	public static class Rusted extends YogFist {

		{
			spriteClass = FistSprite.Rusted.class;

			damageFactor = 1.3f;

			properties.add(Property.LARGE);
			properties.add(Property.INORGANIC);
		}

		@Override
		public Element elementalType() {
			return Element.STONE;
		}

		@Override
		public void damage(int dmg, DamageSrc src) {
			if (!isInvulnerable(src.getCause().getClass()) && !(src.getCause() instanceof Viscosity.DeferedDamage)){
				Buff.affect(this, Viscosity.DeferedDamage.class).prolong(dmg);
				sprite.showStatus(CharSprite.WARNING, Messages.get(Viscosity.class, "deferred", dmg));
			} else {
				super.damage(dmg, src);
			}
		}
	}

	public static class Bright extends YogFist {

		{
			spriteClass = FistSprite.Bright.class;

			properties.add(Property.ELECTRIC);

			hasMeleeAttack = false;

			damageFactor = 1.5f;

			healthFactor = 0.5f;
		}

		@Override
		public Element elementalType() {
			return Element.LIGHT;
		}

		@Override
		protected void incrementRangedCooldown() {
			//ranged attack has no cooldown
		}

		/*@Override
		protected void zap() {
			spend( 1f );

			if (hit( this, enemy, true )) {

				int dmg = damageRoll()/2;
				enemy.damage( dmg, this );
				Buff.prolong( enemy, Blindness.class, 5f );

				if (!enemy.isAlive() && enemy == Dungeon.hero) {
					Dungeon.fail( getClass() );
					GLog.n( Messages.get(Char.class, "kill", name()) );
				}

			} else {

				enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
			}

		}*/

		@Override
		public void damage(int dmg, DamageSrc src) {
			int beforeHP = HP;
			super.damage(dmg, src);
			if (enemy == null) {
				enemy = Dungeon.hero;
			}
			if (beforeHP > HT/2 && HP < HT/2){
				HP = HT/2;
				Buff.prolong( enemy, Blindness.class, 50f );
				int i;
				do {
					i = Random.Int(Dungeon.level.length());
				} while (Dungeon.level.heroFOV[i] || Dungeon.level.solid(i) || Actor.findChar(i) != null);
				ScrollOfTeleportation.appear(this, i);
				state = WANDERING;
				GameScene.flash(0xFFFFFF);
			} else if (!isAlive()){
				Buff.prolong( enemy, Blindness.class, 50f );
			}
		}

	}

	public static class Dark extends YogFist {

		{
			spriteClass = FistSprite.Dark.class;

			hasMeleeAttack = false;

			damageFactor = 1.3f;

			healthFactor = 0.5f;
		}

		@Override
		public Element elementalType() {
			return Element.SPIRIT;
		}

		@Override
		public void damage(int dmg, DamageSrc src) {
			int beforeHP = HP;
			super.damage(dmg, src);
			if (beforeHP > HT/2 && HP < HT/2){
				HP = HT/2;
				Light l = Dungeon.hero.buff(Light.class);
				if (l != null){
					l.detach();
				}
				int i;
				do {
					i = Random.Int(Dungeon.level.length());
				} while (Dungeon.level.heroFOV[i] || Dungeon.level.solid(i) || Actor.findChar(i) != null);
				ScrollOfTeleportation.appear(this, i);
				state = WANDERING;
				GameScene.flash(0, false);
			} else if (!isAlive()){
				Light l = Dungeon.hero.buff(Light.class);
				if (l != null){
					l.detach();
				}
				GameScene.flash(0, false);
			}
		}

	}

}

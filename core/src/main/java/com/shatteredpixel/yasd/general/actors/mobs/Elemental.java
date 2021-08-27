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

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.Inferno;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Chill;
import com.shatteredpixel.yasd.general.actors.buffs.Corruption;
import com.shatteredpixel.yasd.general.actors.buffs.DeferredDeath;
import com.shatteredpixel.yasd.general.actors.buffs.Poison;
import com.shatteredpixel.yasd.general.effects.Pushing;
import com.shatteredpixel.yasd.general.items.potions.PotionOfFrost;
import com.shatteredpixel.yasd.general.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.yasd.general.items.quest.Embers;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.yasd.general.items.wands.CursedWand;
import com.shatteredpixel.yasd.general.levels.features.Door;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ElementalSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class Elemental extends Mob {

	{
		spriteClass = ElementalSprite.class;

		healthFactor = 0.7f;
		damageFactor = 1.3f;

        flying = true;

		loot = Reflection.newInstance(PotionOfLiquidFlame.class);
		lootChance = 0.1f;

		properties.add(Property.FIERY);
	}

	@Override
	public Element elementalType() {
		return Element.FIRE;
	}

	private int rangedCooldown = Random.NormalIntRange(3, 5);

	@Override
	protected boolean act() {
		if (state == HUNTING) {
			rangedCooldown--;
		}

		return super.act();
	}

	@Override
	public boolean canAttack(@NotNull Char enemy) {
		if (rangedCooldown <= 0) {
			boolean canAttack = new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
			if (canAttack) {
				rangedCooldown = Random.NormalIntRange( 3, 5 );
			}
			return canAttack;
		} else {
			return super.canAttack(enemy);
		}
	}

    @Override
	public void add(Buff buff) {
		if (harmfulBuffs.contains(buff.getClass())) {
			damage(Random.NormalIntRange(HT / 2, HT * 3 / 5), buff);
		} else {
			super.add(buff);
		}
	}

	protected ArrayList<Class<? extends Buff>> harmfulBuffs = new ArrayList<>();

	private static final String COOLDOWN = "cooldown";

	@Override
	public void storeInBundle( Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(COOLDOWN, rangedCooldown);
	}

	@Override
	public void restoreFromBundle( Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(COOLDOWN)) {
			rangedCooldown = bundle.getInt(COOLDOWN);
		}
	}
	public static class Fire extends Elemental {

		{
			spriteClass = ElementalSprite.Fire.class;

			loot = new PotionOfLiquidFlame();
			lootChance = 1/8f;

			properties.add( Property.FIERY );

			harmfulBuffs.add( com.shatteredpixel.yasd.general.actors.buffs.Frost.class );
			harmfulBuffs.add( Chill.class );
		}

		@Override
		public Element elementalType() {
			return Element.FIRE;
		}

		public static class Adult extends Fire {
			{
				spriteClass = ElementalSprite.AdultFire.class;
			}

			@Override
			protected boolean act() {
				GameScene.add(Blob.seed(pos, 20, Inferno.class));
				return super.act();
			}
		}
	}

	public static class NewbornFire extends Fire {

		{
			spriteClass = ElementalSprite.NewbornFire.class;

			HP = HT/4; //32

            loot = new Embers();
			lootChance = 1f;

			properties.add(Property.MINIBOSS);
		}

		@Override
		public boolean reset() {
			return true;
		}

	}

	public static class Frost extends Elemental {

		{
			spriteClass = ElementalSprite.Frost.class;

			loot = new PotionOfFrost();
			lootChance = 1 / 8f;

			properties.add(Property.ICY);

			harmfulBuffs.add(Burning.class);
		}

		@Override
		public Element elementalType() {
			return Element.COLD;
		}
	}

	public static class Shock extends Elemental {

		{
			spriteClass = ElementalSprite.Shock.class;

			loot = new ScrollOfRecharging();
			lootChance = 1 / 4f;

			properties.add(Property.ELECTRIC);
		}

		@Override
		public Element elementalType() {
			return Element.SHOCK;
		}
	}

	public static class Chaos extends Elemental {

		{
			spriteClass = ElementalSprite.Chaos.class;

			loot = new ScrollOfTransmutation();
			lootChance = 1f;
		}

		@Override
		public int attackProc(Char enemy, int damage) {
			CursedWand.cursedZap( null, this, new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT ), new Callback() {
				@Override
				public void call() {
					next();
				}
			} );
			return super.attackProc(enemy, damage);
		}
	}

	public static class Water extends Elemental {
		{
			spriteClass = ElementalSprite.Water.class;

            damageFactor = 0.7f;
			healthFactor = 2f;

			//TODO loot?
		}

		private int generation	= 0;

		private static final String GENERATION	= "generation";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(GENERATION, generation);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			generation = bundle.getInt(GENERATION);
		}

		@Override
		public Element elementalType() {
			return Element.WATER;
		}

		@Override
		public boolean act() {

			if (Dungeon.level.liquid(pos) && HP < HT) {
				heal(HT/10, false, true);
			}

			return super.act();
		}

		@Override
		public int defenseProc(Char enemy, int damage ) {

			if (HP >= damage + 2) {
				ArrayList<Integer> candidates = new ArrayList<>();
				boolean[] solid = Dungeon.level.solid();

				int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
				for (int n : neighbours) {
					if (!solid[n] && Actor.findChar( n ) == null) {
						candidates.add( n );
					}
				}

				if (candidates.size() > 0) {

					Elemental.Water clone = split();
					clone.HP = (HP - damage) / 2;
					clone.pos = Random.element( candidates );
					clone.state = clone.HUNTING;

					if (Dungeon.level.getTerrain(clone.pos) == Terrain.DOOR) {
						Door.enter( clone.pos );
					}

					GameScene.add( clone, Swarm.SPLIT_DELAY );
					Actor.addDelayed( new Pushing( clone, pos, clone.pos ), -1 );

					HP -= clone.HP;
				}
			}

			return super.defenseProc(enemy, damage);
		}

		private Elemental.Water split() {
			Elemental.Water clone = new Elemental.Water();
			clone.generation = generation + 1;
            if (buff( Burning.class ) != null) {
				Buff.affect( clone, Burning.class ).reignite( clone );
			}
			if (buff( Poison.class ) != null) {
				Buff.affect( clone, Poison.class ).set(2);
			}
			if (buff(Corruption.class ) != null) {
				Buff.affect( clone, Corruption.class);
			}
			if (buff(DeferredDeath.class) != null) {
				clone.die(new DamageSrc(Element.SHADOW, buff(DeferredDeath.class)));
			}
			return clone;
		}
	}

	public static Class<? extends Elemental> random(){
		if (Random.Int( 25 ) == 0){
			return Chaos.class;
		}

		float roll = Random.Float();
		if (roll < 0.4f){
			return Fire.class;
		} else if (roll < 0.8f){
			return Frost.class;
		} else {
			return Shock.class;
		}
	}
}

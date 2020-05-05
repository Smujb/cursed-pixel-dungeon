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

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Chill;
import com.shatteredpixel.yasd.general.items.potions.PotionOfFrost;
import com.shatteredpixel.yasd.general.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.yasd.general.items.quest.Embers;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.yasd.general.items.wands.CursedWand;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
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
		damageFactor = 0.8f;

		EXP = 10;

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
	public int drRoll(Element element) {
		if (element == Element.WATER) {
			return -super.drRoll(element);
		} else {
			return super.drRoll(element);
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
	}

	public static class NewbornFire extends Fire {

		{
			spriteClass = ElementalSprite.NewbornFire.class;

			HP = HT/4; //32

			defenseSkill = 12;

			EXP = 7;

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

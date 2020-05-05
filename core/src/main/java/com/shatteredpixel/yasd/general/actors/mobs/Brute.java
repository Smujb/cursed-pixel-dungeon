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
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.actors.buffs.ShieldBuff;
import com.shatteredpixel.yasd.general.actors.buffs.Terror;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Gold;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Grim;
import com.shatteredpixel.yasd.general.levels.features.Chasm;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.BruteSprite;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.ShieldedSprite;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Brute extends Mob {
	
	{
		spriteClass = BruteSprite.class;

		damageFactor = 1.2f;
		stealthFactor = 2/3f;
		evasionFactor = 0.75f;
		
		EXP = 8;
		
		loot = Gold.class;
		lootChance = 0.5f;
	}


	protected boolean hasRaged = false;

	private static final String HAS_RAGED = "has_raged";

	@Override
	public void storeInBundle( Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(HAS_RAGED, hasRaged);
	}

	@Override
	public void restoreFromBundle( Bundle bundle) {
		super.restoreFromBundle(bundle);
		hasRaged = bundle.getBoolean(HAS_RAGED);
	}
	
	@Override
	public int damageRoll() {
		return buff(BruteRage.class) != null ?
			super.damageRoll()*3 :
			super.damageRoll();
	}


	public synchronized boolean isAlive() {
		if (HP > 0){
			return  true;
		} else {
			if (!hasRaged){
				triggerEnrage();
			}
			return buff(BruteRage.class) != null;
		}
	}

	protected void triggerEnrage(){
		Buff.affect(this, BruteRage.class).setShield(HT/2 + 4);
		if (Dungeon.level.heroFOV[pos]) {
			sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "enraged") );
		}
		spend( TICK );
		hasRaged = true;
	}

	@Override
	public void die( DamageSrc cause) {
		super.die(cause);

		if (cause.getCause() == Chasm.class){
			hasRaged = true; //don't let enrage trigger for chasm deaths
		}
	}

	public static class BruteRage extends ShieldBuff {

		{
			type = buffType.POSITIVE;
		}

		@Override
		public boolean act() {

			if (target.HP > 0){
				detach();
				return true;
			}

			absorbDamage( 4 );

			if (shielding() <= 0){
				target.die(new DamageSrc(Element.META));
			}

			spend( TICK );

			return true;
		}

		@Override
		public int icon () {
			return BuffIndicator.FURY;
		}

		@Override
		public String toString () {
			return Messages.get(this, "name");
		}

		@Override
		public String desc () {
			return Messages.get(this, "desc", shielding());
		}

	}
	
	{
		immunities.add( Terror.class );
		immunities.add( Paralysis.class );
		immunities.add( Grim.class);
	}

	public static class ArmoredBrute extends Brute {

		{
			spriteClass = ShieldedSprite.class;
			drFactor = 2f;

			lootChance = 0.33f;
			//HP = HT = 60;
		}
		@Override
		protected void triggerEnrage () {
			Buff.affect(this, ArmoredRage.class).setShield(HT/2 + 1);
			if (Dungeon.level.heroFOV[pos]) {
				sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "enraged") );
			}
			spend( TICK );
			hasRaged = true;
		}

		@Override
		protected Item createLoot () {
			return Generator.randomArmor().setTier(4);
		}

		//similar to regular brute rate, but deteriorates much slower. 60 turns to death total.
		public static class ArmoredRage extends Brute.BruteRage {

			@Override
			public boolean act() {

				if (target.HP > 0){
					detach();
					return true;
				}

				absorbDamage( 1 );

				if (shielding() <= 0){
					target.die(new DamageSrc(Element.META));
				}

				spend( 3*TICK );

				return true;
			}

		}
	}
}

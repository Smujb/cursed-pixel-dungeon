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

package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class MeleeWeapon extends Weapon {
	{
		statScaling = new ArrayList<>(Arrays.asList(Hero.HeroStat.EXECUTION));
	}

	public float damageMultiplier = 1f;
	public float defenseMultiplier = 0f;

	public boolean sneakBenefit = false;

	@Override
	public int min(float lvl) {
		return  Math.round(1 +  //base
				lvl);    //level scaling
	}

	@Override
	public int max(float lvl) {
		return (int) ((20 +    //base
				lvl*10)*damageMultiplier);   //level scaling
	}

	@Override
	public int defenseFactor(Char owner) {
		return (int) ((max()/2)*defenseMultiplier);
	}

	@Override
	public int damageRoll(Char owner) {

		int damage = augment.damageFactor(super.damageRoll( owner ));

		if (sneakBenefit) {
			Char enemy = null;
			float bonus = 0;
			if (curUser instanceof Hero) {
				enemy = ((Hero) curUser).enemy();
			} else if (curUser instanceof Mob) {
				enemy = ((Mob) curUser).getEnemy();
			}
			if (enemy != null) {
				bonus = 1f-enemy.noticeChance(curUser, 3);
			}
			if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(curUser) && curUser.canSurpriseAttack()) {
				damage *= (2 + bonus);
				if (damage < max()) {
					damage = max();
				}
			}
		}
		return damage;
	}

	@Override
	public Item randomHigh() {
		super.randomHigh();
		if (Random.Int(2) == 0) {
			enchant();
		}
		return this;
	}

	@Override
	public String info() {

		String info = desc();

		if (levelKnown) {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", augment.damageFactor(min()), augment.damageFactor(max()), statReq());
		} else {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", min(0), max(0), statReq(0));
		}

		if (DLY != 1f | ACC != 1f | RCH != 1 | breaksArmor(Dungeon.hero) | !canSurpriseAttack | defenseFactor(Dungeon.hero) > 0 | sneakBenefit) {

			info += "\n";

			if (DLY > 1f) {
				info += "\n" + Messages.get(MeleeWeapon.class, "delay_increase", Math.round((DLY-1f)*100));
			} else if (DLY < 1f) {
				info += "\n" + Messages.get(MeleeWeapon.class, "delay_decrease", Math.round((1f-DLY)*100));
			}

			if (ACC > 1f) {
				info += "\n" + Messages.get(MeleeWeapon.class, "acc_increase", Math.round((ACC-1f)*100));
			} else if (ACC < 1f) {
				info += "\n" + Messages.get(MeleeWeapon.class, "acc_decrease", Math.round((1f-ACC)*100));
			}

			if (RCH > 1) {
				info += "\n" + Messages.get(MeleeWeapon.class, "reach_increase", RCH - 1);
			}

			if (breaksArmor(curUser)) {
				info += "\n" + Messages.get(MeleeWeapon.class, "breaks_armour");
			}

			if (!canSurpriseAttack) {
				info += "\n" + Messages.get(MeleeWeapon.class, "cant_surprise_attk");
			}

			if (defenseFactor(curUser) > 0) {
				info += "\n" + Messages.get(MeleeWeapon.class, "blocks", 0,  defenseFactor(Dungeon.hero));
			}

			if (sneakBenefit) {
				info += "\n" + Messages.get(MeleeWeapon.class, "sneak_benefit");
			}
		}

		switch (augment) {
			case SPEED:
				info += "\n\n" + Messages.get(Weapon.class, "faster");
				break;
			case DAMAGE:
				info += "\n\n" + Messages.get(Weapon.class, "stronger");
				break;
			case NONE:
		}

		if (enchantment != null && cursedKnown) {
			info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
			info += " " + Messages.get(enchantment, "desc");
		}

		if (Dungeon.hero != null && cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed");
		} else if (!isIdentified() && cursedKnown){
			info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
		}
		
		return info + statsReqDesc();
	}

	@Override
	public int price() {
		int price = 100;
		if (hasGoodEnchant()) {
			price *= 1.5;
		}
		if (cursedKnown && (cursed || hasCurseEnchant())) {
			price /= 2;
		}
		if (levelKnown && level() > 0) {
			price *= (power() + 1);
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}
}

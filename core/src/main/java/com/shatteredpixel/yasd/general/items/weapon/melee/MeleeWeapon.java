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

import com.shatteredpixel.yasd.general.Challenges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.Attackable;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class MeleeWeapon extends Weapon implements Attackable {
	{
		statScaling = new ArrayList<>(Arrays.asList(Hero.HeroStat.EXECUTION));
	}

	public float damageFactor = 1f;
	public float defenseMultiplier = 0f;

	@Override
	public int min(float lvl) {
		return Math.round(10 * lvl * damageFactor);   //level scaling
	}

	@Override
	public int max(float lvl) {
		return Math.round(24 * lvl * damageFactor);   //level scaling
	}

	@Override
	public boolean use(Char enemy) {
		if (curUser != null && isEquipped(curUser) && canReach(curUser, enemy.pos)) {
			doAttack(curUser, enemy);
			return true;
		}
		return false;
	}

	@Override
	public boolean canUse(Char enemy) {
		return canReach(curUser, enemy.pos);
	}

	@Override
	public int defenseFactor(Char owner) {
		return (int) ((max()/2)*defenseMultiplier);
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (Dungeon.isChallenged(Challenges.BLOODLUST) && attacker instanceof Hero) {
			attacker.heal(Math.round((attacker.HT/10f)*DLY), true, true);
		}
		return super.proc(attacker, defender, damage);
	}

	@Override
	public int damageRoll(Char owner) {
		return augment.damageFactor(super.damageRoll( owner ));
	}

	@Override
	public Item randomHigh() {
		super.randomHigh();
		if (Random.Int(2) == 0) {
			enchant();
		}
		return this;
	}

	protected String propsDesc() {
		String info = "";
		if (DLY > 1f) {
			info += "\n" + Messages.get(MeleeWeapon.class, "delay_increase", Math.round((DLY - 1f) * 100));
		} else if (DLY < 1f) {
			info += "\n" + Messages.get(MeleeWeapon.class, "delay_decrease", Math.round((1f - DLY) * 100));
		}

		if (ACC > 1f) {
			info += "\n" + Messages.get(MeleeWeapon.class, "acc_increase", Math.round((ACC - 1f) * 100));
		} else if (ACC < 1f) {
			info += "\n" + Messages.get(MeleeWeapon.class, "acc_decrease", Math.round((1f - ACC) * 100));
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
			info += "\n" + Messages.get(MeleeWeapon.class, "blocks", 0, defenseFactor(Dungeon.hero));
		}

		if (sneakBenefit) {
			info += "\n" + Messages.get(MeleeWeapon.class, "sneak_benefit");
		}

		if (!canBeParried) info += "\n" + Messages.get(MeleeWeapon.class, "cant_be_parried");

		if (slotsUsed > 1) info += "\n" + Messages.get(KindofMisc.class, "requires_slots", slotsUsed);

		return info;
	}

	@Override
	public String info() {

		String info = desc();

		if (levelKnown) {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", augment.damageFactor(min()), augment.damageFactor(max()));
		} else {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", min(1f), max(1f));
		}

		String props = propsDesc();
		if (!props.isEmpty()) {
			info += "\n" + props;
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
		
		return info + upgradableItemDesc();
	}

	@Override
	public boolean cursed() {
		return super.cursed() || hasCurseEnchant();
	}
}

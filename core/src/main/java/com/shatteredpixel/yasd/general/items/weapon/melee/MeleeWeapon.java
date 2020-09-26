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
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.Attackable;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.CellSelector;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class MeleeWeapon extends Weapon implements Attackable {
	{
		statScaling = new ArrayList<>(Arrays.asList(Hero.HeroStat.EXECUTION));

		usesTargeting = true;
		defaultAction = AC_ATTACK;
	}

	public float damageMultiplier = 1f;
	public float defenseMultiplier = 0f;

	public boolean sneakBenefit = false;

	private static final String AC_ATTACK = "attack";

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_ATTACK);
		return actions;
	}

	@Override
	public int min(float lvl) {
		return Math.round(2 * lvl * damageMultiplier);   //level scaling
	}

	@Override
	public int max(float lvl) {
		return Math.round(5 * lvl * damageMultiplier);   //level scaling
	}

	@Override
	public int image() {
		return ItemSpriteSheet.adjustForTier(image, 1 + Math.round(GameMath.gate(0, level()/10f, 4)));
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_ATTACK)) {
			if (isEquipped(curUser)) {
				GameScene.selectCell(ATTACK);
			} else {
				GLog.info(Messages.get(MeleeWeapon.this, "not_equipped"));
			}
		}
	}

	private final CellSelector.Listener ATTACK = new CellSelector.Listener() {

		@Override
		public void onSelect(Integer cell) {
			if (cell != null && curUser != null) {
				Char ch = Actor.findChar(cell);
				if (ch != null) {
					if (MeleeWeapon.this.canReach(curUser, cell)) {
						MeleeWeapon.this.doAttack(curUser, ch);
					} else {
						GLog.info(Messages.get(MeleeWeapon.this, "out_of_range"));
					}
 				} else {
					GLog.info(Messages.get(MeleeWeapon.this, "no_enemy"));
				}
			}
		}

		@Override
		public String prompt() {
			return Messages.get(MeleeWeapon.this, "select_cell");
		}
	};

	@Override
	public boolean use(Char enemy) {
		if (curUser != null && isEquipped(curUser) && canReach(curUser, enemy.pos)) {
			doAttack(curUser, enemy);
			return true;
		}
		return false;
	}

	@Override
	public boolean canAttack(Char enemy) {
		return canReach(curUser, enemy.pos);
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
				bonus = 1f-enemy.noticeChance(curUser);
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

	protected boolean hasProperties() {
		return DLY != 1f | ACC != 1f | RCH != 1 | breaksArmor(curUser) | !canSurpriseAttack | defenseFactor(curUser) > 0 | sneakBenefit;
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

		if (hasProperties()) {
			info += "\n" + propsDesc();
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

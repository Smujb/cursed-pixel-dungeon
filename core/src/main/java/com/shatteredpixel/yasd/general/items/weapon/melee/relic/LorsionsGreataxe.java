/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.yasd.general.items.weapon.melee.relic;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.Damning;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.RelicEnchantment;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class LorsionsGreataxe extends RelicMeleeWeapon {

	{
		image = ItemSpriteSheet.LORSIONSGREATAXE;

		ACC = 1.33f;
		damageMultiplier = 1.2f;
		chargeToAdd = 1f;
	}

	private boolean heavyAttack = false;

	@Override
	public int damageRoll(Char owner) {
		float multiplier = 1f;
		if (heavyAttack) {
			multiplier = 1/owner.hpPercent();
		}
		heavyAttack = false;
		return (int) (super.damageRoll(owner) * multiplier);
	}

	public void prepare() {
		heavyAttack = true;
	}

	@Override
	public RelicEnchantment enchantment() {
		return new Damning();
	}

	@Override
	public int statReq(int level) {
		return super.statReq(level)+2;
	}
}

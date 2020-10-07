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

package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.effects.SpellSprite;
import com.shatteredpixel.yasd.general.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class WandOfMagicMissile extends DamageWand {

	{
		image = ItemSpriteSheet.Wands.MAGIC_MISSILE;

		element = Element.MAGICAL;

		damageMultiplier = 2/3f;
	}

	@Override
	public float min(float lvl){
		return max(lvl);
	}

	@Override
	public int damageRoll(float lvl) {
		return (int) max(lvl);
	}

	@Override
	public void onZap(Ballistica bolt) {
		Char ch = Actor.findChar( bolt.collisionPos );
		if (ch != null) {

			processSoulMark(ch, chargesPerCast());
			hit(ch);

			ch.sprite.burst(0xFFFFFFFF, power() / 2 + 2);

		} else {
			Dungeon.level.pressCell(bolt.collisionPos);
		}
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		SpellSprite.show(attacker, SpellSprite.CHARGE);
		for (Wand.Charger c : attacker.buffs(Wand.Charger.class)){
			if (c.wand() != this){
				c.gainCharge(1/3f);
			}
		}
	}
	
	protected int initialCharges() {
		return 5;
	}
}

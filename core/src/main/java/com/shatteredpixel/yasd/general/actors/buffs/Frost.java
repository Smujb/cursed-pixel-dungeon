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

package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.food.ChargrilledMeat;
import com.shatteredpixel.yasd.general.items.food.FrozenCarpaccio;
import com.shatteredpixel.yasd.general.items.food.MysteryMeat;
import com.shatteredpixel.yasd.general.items.potions.Potion;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Frost extends FlavourBuff {

	public static final float DURATION	= 10f;

	{
		type = buffType.NEGATIVE;
		announced = true;
	}
	
	@Override
	public boolean attachTo(@NotNull Char target ) {
		if (super.attachTo( target )) {
			
			target.paralysed++;
			Buff.detach( target, Burning.class );
			Buff.detach( target, Chill.class );

			if (target instanceof Hero) {

				Hero hero = (Hero) target;
				ArrayList<Item> freezable = new ArrayList<>();
				//does not reach inside of containers
				for (Item i : hero.belongings.backpack.items){
					if ((i instanceof Potion)
						|| i instanceof MysteryMeat || i instanceof ChargrilledMeat){
						freezable.add(i);
					}
				}
				
				if (!freezable.isEmpty()){
					Item toFreeze = Random.element(freezable).detach( hero.belongings.backpack );
					GLog.warning( Messages.get(this, "freezes", toFreeze.toString()) );
					if (toFreeze instanceof Potion){
						((Potion) toFreeze).shatter(hero.pos);
					} else if (toFreeze instanceof MysteryMeat){
						FrozenCarpaccio carpaccio = new FrozenCarpaccio();
						if (!carpaccio.collect( hero.belongings.backpack, target)) {
							Dungeon.level.drop( carpaccio, target.pos ).sprite.drop();
						}
					} else if (toFreeze instanceof ChargrilledMeat) {
						MysteryMeat meat = new MysteryMeat();
						if (!meat.collect(hero.belongings.backpack, target)) {
							Dungeon.level.drop(meat, hero.pos).sprite.drop();
						}
					}
				}
				
			}

			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		super.detach();
		if (target.paralysed > 0)
			target.paralysed--;
		if (Dungeon.level.liquid(target.pos))
			Buff.prolong(target, Chill.class, Chill.DURATION/2f);
	}
	
	@Override
	public int icon() {
		return BuffIndicator.FROST;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(0f, 0.75f, 1f);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - visualcooldown()) / DURATION);
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add(CharSprite.State.FROZEN);
		else target.sprite.remove(CharSprite.State.FROZEN);
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}
}

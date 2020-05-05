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

package com.shatteredpixel.yasd.general.items;

import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndOptions;

import org.jetbrains.annotations.NotNull;


public abstract class KindofMisc extends EquipableItem {

	private static final float TIME_TO_EQUIP = 1f;

	@Override
	public boolean doEquip(final Hero hero) {
		final KindofMisc[] miscs = hero.belongings.miscs;
		if (!hero.belongings.canEquip(this)) {


			GameScene.show(
					new WndOptions(Messages.get(KindofMisc.class, "unequip_title"),
							Messages.get(KindofMisc.class, "unequip_message"),
							Messages.titleCase(miscs[0].toString()),
							Messages.titleCase(miscs[1].toString()),
							Messages.titleCase(miscs[2].toString()),
							Messages.titleCase(miscs[3].toString()),
							Messages.titleCase(miscs[4].toString())) {

						@Override
						protected void onSelect(int index) {

							KindofMisc equipped = null;

							for (int i = 0; i < miscs.length; i++) {
								if (index == i) {
									equipped = miscs[i];
									break;
								}
							}
							if (equipped == null) return;

							//temporarily give 1 extra backpack spot to support swapping with a full inventory
							hero.belongings.backpack.size++;
							if (equipped.doUnequip(hero, true, false)) {
								//fully re-execute rather than just call doEquip as we want to preserve quickslot
								execute(hero, AC_EQUIP);
							}
							hero.belongings.backpack.size--;
						}
					});

			return false;

		} else {

			for (int i = 0; i < miscs.length; i++) {
				if (hero.belongings.canEquip(this, i)) {
					hero.belongings.miscs[i] = this;
					break;
				}
			}


			detach( hero.belongings.backpack );

			activate( hero );

			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( Messages.get(this, "equip_cursed", this) );
			}

			hero.spendAndNext( TIME_TO_EQUIP );
			return true;

		}

	}

	@Override
	public boolean doUnequip(Char hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){

			for (int i = 0; i < Constants.MISC_SLOTS; i++) {
				if (hero.belongings.miscs[i] == this) {
					hero.belongings.miscs[i] = null;
				}
			}

			return true;

		} else {

			return false;

		}
	}

	@Override
	public boolean isEquipped( Char owner ) {
		for (int i = 0; i < Constants.MISC_SLOTS; i++) {
			if (owner.belongings.miscs[i] == this) {
				return true;
			}
		}
		return false;
	}

}



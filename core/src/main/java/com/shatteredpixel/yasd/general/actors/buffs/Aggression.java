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

package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.utils.Bundle;


public class Aggression extends FlavourBuff {

    public static final float DURATION = 20f;

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public void storeInBundle( Bundle bundle) {
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle( Bundle bundle) {
        super.restoreFromBundle(bundle);
    }

    @Override
    public void detach() {
        //if our target is an enemy, reset the aggro of any enemies targeting it
        if (target.isAlive()) {
            if (target.alignment == Char.Alignment.ENEMY) {
                for (Mob m : Dungeon.level.mobs) {
                    if (m.alignment == Char.Alignment.ENEMY && m.isTargeting(target)) {
                        m.aggro(null);
                    }
                }
            }
        }
        super.detach();

    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

}

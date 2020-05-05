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

package com.shatteredpixel.yasd.general.items.alcohol;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Drunk;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.Item;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Alcohol extends Item {
    {
        cursed = false;
        stackable = true;
    }
    public static final String AC_DRINK = "DRINK";
    private static final float TIME_TO_DRINK = 1f;
    float MoraleGain;
    float drunkTurns;

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_DRINK );
        return actions;
    }

    @Override
    public void execute(final Hero hero, String action ) {

        super.execute( hero, action );

         if (action.equals( AC_DRINK )) {

           drink(hero);

        }
    }
    public void drink(Hero hero) {

        detach(hero.belongings.backpack);

        hero.spend(TIME_TO_DRINK);
        hero.busy();
        hero.gainMorale(MoraleGain);

        Buff.affect(hero, Drunk.class, drunkTurns);

        Sample.INSTANCE.play(Assets.SND_DRINK);

        hero.sprite.operate(hero.pos);

    }

    @Override
    public boolean isIdentified() {
        return true;
    }
}

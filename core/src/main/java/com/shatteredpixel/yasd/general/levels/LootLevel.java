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

package com.shatteredpixel.yasd.general.levels;

import com.shatteredpixel.yasd.general.actors.mobs.Eye;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.potions.PotionOfExperience;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfUpgrade;

public class LootLevel extends SewerLevel {

    @Override
    protected void createItems() {
        for (int i = 1; i < 5; i++) {
            addItemToSpawn(new ScrollOfUpgrade());
            addItemToSpawn(new PotionOfExperience());
            addItemToSpawn(Generator.randomWeapon().identify());
            addItemToSpawn(Generator.randomArmor().identify());
            addItemToSpawn(Generator.randomMissile());
            addItemToSpawn(Generator.random(Generator.Category.SCROLL).identify());
            addItemToSpawn(Generator.random(Generator.Category.STONE).identify());
            addItemToSpawn(Generator.random(Generator.Category.SEED).identify());
            addItemToSpawn(Generator.random(Generator.Category.POTION).identify());
            addItemToSpawn(Generator.random().identify());
        }
        super.createItems();
    }

    @Override
    public Class<?>[] mobClasses() {
        return new Class[] {Eye.class};
    }

    @Override
    public float[] mobChances() {
        return new float[] {1};
    }

    @Override
    public float respawnTime() {
        return super.respawnTime()/5;
    }

    @Override
    protected int standardRooms() {
        return 20;
    }

    @Override
    protected int specialRooms() {
        return 9;
    }
}

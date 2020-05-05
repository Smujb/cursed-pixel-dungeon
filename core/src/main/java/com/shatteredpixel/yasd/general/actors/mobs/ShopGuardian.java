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

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.buffs.Aggression;
import com.shatteredpixel.yasd.general.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.actors.buffs.Poison;
import com.shatteredpixel.yasd.general.actors.buffs.Terror;
import com.shatteredpixel.yasd.general.effects.particles.ElmoParticle;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.StatueSprite;
import com.watabou.utils.PathFinder;

public class ShopGuardian extends Statue {

    private static final float SPAWN_DELAY	= 2f;
    {
        lootChance = 0.33f;//Rarer in Shop Guardians, as 4 spawn at a time. Average 1.33 Stones per shop rob
        spriteClass = ShopGuardianSprite.class;
        immunities.add(Aggression.class);
        immunities.add(Terror.class);
        immunities.add(Paralysis.class);
        immunities.add(Bleeding.class);
        immunities.add(Poison.class);
        resistances.put(Element.COLD, 0.5f);
    }

    public ShopGuardian() {
        super();
        ankhs = 0;
    }

    public void spawnAround( int pos ) {
        for (int n : PathFinder.NEIGHBOURS4) {
            int cell = pos + n;
            if (Dungeon.level.passable(cell) && Actor.findChar( cell ) == null) {
                spawnAt( cell );
            }
        }
    }

    public static ShopGuardian spawnAt( int pos ) {
        if (Dungeon.level.passable(pos) && Actor.findChar( pos ) == null) {

            ShopGuardian shopGuardian = new  ShopGuardian();
            shopGuardian.pos = pos;
            shopGuardian.state = shopGuardian.HUNTING;
            GameScene.add( shopGuardian, SPAWN_DELAY );

            shopGuardian.sprite.emitter().burst(ElmoParticle.FACTORY, 5 );
            shopGuardian.next();
            return shopGuardian;
        } else {
            return null;
        }
    }

    @Override
    public void dropGear() {//Doesn't drop it's equipment
    }

    public static class ShopGuardianSprite extends StatueSprite {

        public ShopGuardianSprite(){
            super();
            tint(1, 1, 0, 0.2f);
        }

        @Override
        public void resetColor() {
            super.resetColor();
            tint(1, 1, 0, 0.2f);
        }
    }
}

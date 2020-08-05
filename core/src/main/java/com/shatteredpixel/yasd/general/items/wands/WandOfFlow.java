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

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Slow;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Chilling;
import com.shatteredpixel.yasd.general.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.mechanics.ConeAOE;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfFlow extends DamageWand {
    {
        image = ItemSpriteSheet.WAND_FLOW;

        collisionProperties = Ballistica.STOP_TERRAIN;

        element = Element.WATER;
    }

    ConeAOE cone;

    @Override
    public float min(float lvl) {
        return 1 + lvl;
    }

    @Override
    public float max(float lvl) {
        return 6 + lvl*5;
    }

    @Override
    public void onZap(Ballistica bolt) {
        ArrayList<Char> affectedChars = new ArrayList<>();
        for( int cell : cone.cells ){

            //ignore caster cell
            if (cell == bolt.sourcePos){
                continue;
            }

            Char ch = Actor.findChar( cell );
            if (ch != null) {
                affectedChars.add(ch);
            }
        }

        for ( Char ch : affectedChars.toArray( new Char[0] ) ) {
            try {
                processSoulMark(ch, chargesPerCast());
                hit(ch);
                if (Random.Int(2) == 0) {
                    Buff.affect(ch, Slow.class, Slow.DURATION / 3);
                }
                Ballistica trajectory = new Ballistica(ch.pos, bolt.path.get(bolt.dist + 1), Ballistica.MAGIC_BOLT);
                WandOfBlastWave.throwChar(ch, trajectory, 2);
                //Buff.affect(ch, Wet.class, Wet.DURATION);
            } catch (Exception e) {
                CPDGame.reportException(e);
            }
        }
    }
    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        //need to perform flame spread logic here so we can determine what cells to put flames in.

        // 4/6/8 distance
        int maxDist = 8;
        int dist = Math.min(bolt.dist, maxDist);

        cone = new ConeAOE( bolt.sourcePos, bolt.path.get(dist),
                maxDist,
                30 + 20*chargesPerCast(),
                collisionProperties | Ballistica.STOP_TARGET);

        //cast to cells at the tip, rather than all cells, better performance.
        for (Ballistica ray : cone.rays){
            //this way we only get the cells at the tip, much better performance.
            ((MagicMissile)curUser.sprite.parent.recycle( MagicMissile.class )).reset(
                    MagicMissile.WATER_CONE,
                    curUser.sprite,
                    ray.path.get(ray.dist),
                    null
            );
        }

        //final zap at half distance, for timing of the actual wand effect
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.WATER_CONE,
                curUser.sprite,
                bolt.path.get(dist/2),
                callback );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        new Chilling().proc(staff, attacker, defender, damage);
    }
}

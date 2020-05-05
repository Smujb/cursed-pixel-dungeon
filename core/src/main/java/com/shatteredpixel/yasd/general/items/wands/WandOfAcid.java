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

package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.AcidPool;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Corrosion;
import com.shatteredpixel.yasd.general.actors.buffs.Ooze;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

import static com.shatteredpixel.yasd.general.actors.blobs.Blob.seed;

public class WandOfAcid extends DamageWand {
    {
        image = ItemSpriteSheet.WAND_ACID;
        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_TERRAIN;

        element = Element.ACID;
    }
    @Override
    public float min(float lvl) {
        return 2 + lvl;
    }

    @Override
    public float max(float lvl) {
        return 8 + lvl * 4;
    }

    @Override
    public void onZap(Ballistica attack) {
        Char ch = Actor.findChar( attack.collisionPos );
        int pos = attack.collisionPos;
        if (ch != null) {

            hit(ch);

            ch.sprite.emitter().burst( Speck.factory(Speck.BUBBLE_GREEN), 3 );

            Buff.affect(ch, Ooze.class).set( 20f );

        } else {
            GameScene.add( seed( pos, 1, AcidPool.class ).setStrength((int)(damageRoll()*1.5f)));
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        Buff.affect(defender, Corrosion.class).set(2f, 1 + staff.level()/2);
    }

    @Override
    protected int initialCharges() {
        return 4;
    }
}

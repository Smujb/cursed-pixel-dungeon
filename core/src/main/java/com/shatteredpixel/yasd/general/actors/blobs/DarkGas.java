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

package com.shatteredpixel.yasd.general.actors.blobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.FlavourBuff;
import com.shatteredpixel.yasd.general.effects.BlobEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class DarkGas extends Blob {

    private int strength = 0;
    private int ownerID = 0;

    @Override
    protected void evolve() {
        super.evolve();

        if (volume == 0){
            strength = 0;
        } else {
            Char ch;
            int cell;
            Level l = Dungeon.level;
            for (int i = area.left; i < area.right; i++){
                for (int j = area.top; j < area.bottom; j++){
                    cell = i + j*l.width();
                    if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null) {
                        if (!ch.isImmune(this.getClass())) {
                            ch.damage(Random.Int(Math.max(1,strength/2), strength+2), new Char.DamageSrc(Element.SHADOW, this).ignoreDefense() );
                            Buff.affect(ch, MindBreak.class, 3);
                        }
                    }
                }
            }
        }
    }

    public DarkGas setStrength(int str){
        if (str > strength) {
            strength = str;
        }
        return this;
    }

    public DarkGas setOwner(Char entity) {
        this.ownerID = entity.id();
        return this;
    }

    private static final String STRENGTH = "strength";
    private static final String OWNER = "ownerID";

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour( Speck.factory(Speck.SMOKE), 0.4f );
    }

    @Override
    public void restoreFromBundle( Bundle bundle) {
        super.restoreFromBundle(bundle);
        strength = bundle.getInt( STRENGTH );
        ownerID = bundle.getInt( OWNER );
    }

    @Override
    public void storeInBundle( Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( STRENGTH, strength );
        bundle.put( OWNER, ownerID);
    }



    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }

    public static class MindBreak extends FlavourBuff {

        {
            type = buffType.NEGATIVE;
        }

        public static final float MOB_LIFETIME = 15f;

        @Override
        public int icon() {
            return BuffIndicator.VERTIGO;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(1f, 0.8f, 0f);
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
}

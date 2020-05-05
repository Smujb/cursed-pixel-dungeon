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

package com.shatteredpixel.yasd.general.actors.blobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Ooze;
import com.shatteredpixel.yasd.general.actors.buffs.Vertigo;
import com.shatteredpixel.yasd.general.actors.buffs.Weakness;
import com.shatteredpixel.yasd.general.effects.BlobEmitter;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Splash;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.GooSprite;
import com.watabou.utils.Random;

public class Miasma extends Blob {
    {
        //acts after mobs, to give them a chance to resist paralysis
        actPriority = MOB_PRIO - 1;
    }

    @Override
    protected void evolve() {
        super.evolve();

        Char ch;
        int cell;
        int buff = 0;

        for (int i = area.left; i < area.right; i++) {
            for (int j = area.top; j < area.bottom; j++) {
                cell = i + j * Dungeon.level.width();
                if (cur[cell] > 0 && (ch = Actor.findChar(cell)) != null) {
                    if (!ch.isImmune(this.getClass())) {
                        buff = Random.Int(3);
                        if (Dungeon.level.heroFOV[ch.pos]) {
                            CellEmitter.get(ch.pos).burst(ShadowParticle.UP, 5);
                        }
                        switch (buff) {//Has random chaotic effects
                            default:
                                Buff.prolong(ch, Vertigo.class, Vertigo.DURATION / 2f);
                            case 1:
                                Buff.affect(ch, Ooze.class).set(20f);
                                Splash.at(ch.pos, 0x000000, 5);
                                break;
                            case 2:
                                Buff.prolong(ch, Weakness.class, Weakness.DURATION / 2f);
                                break;
                            case 3:
                                ch.damage((int) (ch.HT / 10f), new Char.DamageSrc(Element.CONFUSION, this).ignoreDefense());
                                break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour(GooSprite.GooParticle.FACTORY, 0.03f );
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}

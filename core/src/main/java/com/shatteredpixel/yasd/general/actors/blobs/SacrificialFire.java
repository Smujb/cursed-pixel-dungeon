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

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.FlavourBuff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.effects.BlobEmitter;
import com.shatteredpixel.yasd.general.effects.Flare;
import com.shatteredpixel.yasd.general.effects.Wound;
import com.shatteredpixel.yasd.general.effects.particles.SacrificialParticle;
import com.shatteredpixel.yasd.general.items.Ankh;
import com.shatteredpixel.yasd.general.journal.Notes;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class SacrificialFire extends Blob {

    protected int pos;

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        if (cur != null && cur.length > 0) {
            for (int i = 0; i < cur.length; i++) {
                if (cur[i] > 0) {
                    pos = i;
                    break;
                }
            }
        }
    }

    @Override
    protected void evolve() {
        volume = off[pos] = cur[pos];
        Char ch = Actor.findChar( pos );
        if (ch != null) {
            if (Dungeon.hero.fieldOfView[pos] && ch.buff( Marked.class ) == null) {
                ch.sprite.emitter().burst( SacrificialParticle.FACTORY, 20 );
                Sample.INSTANCE.play( Assets.SND_BURNING );
            }
            Buff.prolong( ch, Marked.class, Marked.DURATION );
        }
        if (Dungeon.level.heroFOV[pos]) {
            Notes.add( Notes.Landmark.SACRIFICIAL_FIRE );
        }
    }

    @Override
    public void seed(Level level, int cell, int amount ) {
        if (cur == null) cur = new int[level.length()];
        if (off == null) off = new int[cur.length];

        cur[pos] = 0;
        pos = cell;
        volume = cur[pos] = amount;

        area.union(cell%level.width(), cell/level.width());

    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour( SacrificialParticle.FACTORY, 0.04f );
    }

    public static void sacrifice( Char ch ) {

        Wound.hit( ch );

        SacrificialFire fire = (SacrificialFire)Dungeon.level.blobs.get( SacrificialFire.class );
        if (fire != null) {

            int exp = 0;
            if (ch instanceof Mob) {
                exp = ((Mob)ch).EXP * Random.IntRange( 1, 3 );
            } else if (ch instanceof Hero) {
                exp = ((Hero)ch).maxExp();
            }

            if (exp > 0) {

                int volume = fire.volume - exp;
                if (volume > 0) {
                    fire.seed( Dungeon.level, fire.pos, volume );
                    GLog.w(Messages.get(SacrificialFire.class, "worthy"));
                } else {
                    fire.seed( Dungeon.level, fire.pos, 0 );
                    Notes.remove( Notes.Landmark.SACRIFICIAL_FIRE );

                    GLog.p(Messages.get(SacrificialFire.class, "reward"));
                    GameScene.effect( new Flare( 7, 32 ).color( 0x66FFFF, true ).show( ch.sprite.parent, DungeonTilemap.tileCenterToWorld( fire.pos ), 2f ) );
                    Dungeon.level.drop( new Ankh(), fire.pos ).sprite.drop();
                }
            } else {

                GLog.n(Messages.get(SacrificialFire.class, "unworthy"));

            }
        }
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }

    public static class Marked extends FlavourBuff {
        {
            announced = true;
        }

        public static final float DURATION	= 5f;

        @Override
        public int icon() {
            return BuffIndicator.SACRIFICE;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns());
        }

        @Override
        public void detach() {
            if (!target.isAlive()) {
                sacrifice( target );
            }
            super.detach();
        }
    }

}

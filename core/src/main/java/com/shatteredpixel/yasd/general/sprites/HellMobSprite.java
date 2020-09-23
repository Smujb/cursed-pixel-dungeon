package com.shatteredpixel.yasd.general.sprites;

import com.shatteredpixel.yasd.general.Assets;
import com.watabou.noosa.TextureFilm;

public abstract class HellMobSprite extends MobSprite {

    public HellMobSprite(int offset) {
        super();

        texture( Assets.Sprites.HELL_MOBS );

        TextureFilm frames = new TextureFilm( texture, 16, 18 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0+offset, 3+offset );

        run = new Animation( 12, true );
        run.frames( frames, 0+offset, 1+offset, 2+offset, 3+offset, 4+offset );

        attack = new Animation( 8, false );
        attack.frames( frames, 11+offset, 12+offset, 13+offset );
        zap = attack.clone();

        die = new Animation( 8, false );
        die.frames( frames, 5+offset, 6+offset, 7+offset, 8+offset, 9+offset, 10+offset );

        play( idle );
    }
}

package com.shatteredpixel.yasd.general.sprites;

import com.shatteredpixel.yasd.general.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class PoisonDragonSprite extends MobSprite {
    public PoisonDragonSprite() {
        super();

        texture( Assets.DRAGONS_1 );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation( 2, true );
        idle.frames( frames, 32, 32, 32, 33 );

        run = new MovieClip.Animation( 10, true );
        run.frames( frames, 38, 39, 40, 41, 42 );

        attack = new MovieClip.Animation( 15, false );
        attack.frames( frames, 34, 35, 36, 37, 32 );

        die = new MovieClip.Animation( 10, false );
        die.frames( frames, 43, 44, 45, 46 );

        play( idle );
    }
}

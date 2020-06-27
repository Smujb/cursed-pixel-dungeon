package com.shatteredpixel.yasd.general.sprites;

import com.shatteredpixel.yasd.general.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class IceDragonSprite extends MobSprite {
    public IceDragonSprite() {
        super();

        texture( Assets.DRAGONS_1 );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation( 2, true );
        idle.frames( frames, 16, 16, 16, 17 );

        run = new MovieClip.Animation( 10, true );
        run.frames( frames, 22, 23, 24, 25, 26 );

        attack = new MovieClip.Animation( 15, false );
        attack.frames( frames, 18, 19, 20, 21 );

        die = new MovieClip.Animation( 10, false );
        die.frames( frames, 27, 28, 29, 30 );

        play( idle );
    }
}

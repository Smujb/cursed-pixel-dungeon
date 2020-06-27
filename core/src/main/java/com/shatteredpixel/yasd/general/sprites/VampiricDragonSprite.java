package com.shatteredpixel.yasd.general.sprites;

import com.shatteredpixel.yasd.general.Assets;
import com.watabou.noosa.TextureFilm;

public class VampiricDragonSprite extends MobSprite {
    public VampiricDragonSprite() {
        super();

        texture( Assets.DRAGONS_1 );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation(2, true);
        idle.frames(frames, 48, 49, 50, 51);

        run = new Animation(8, true);
        run.frames(frames, 52, 53, 54, 55);

        attack = new Animation(8, false);
        attack.frames(frames, 56, 57, 58, 59);

        zap = attack.clone();

        die = new Animation(8, false);
        die.frames(frames, 60, 61, 62, 63);

        play( idle );
    }
}
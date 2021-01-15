package com.shatteredpixel.yasd.general.sprites;

import com.shatteredpixel.yasd.general.Assets;
import com.watabou.noosa.TextureFilm;

public class KupuaSprite extends MobSprite {
    public KupuaSprite() {
        super();

        texture(Assets.Sprites.KUPUA);

        TextureFilm frames = new TextureFilm(texture, 12, 16);

        idle = new Animation(12, true);
        idle.frames(frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1);

        run = new Animation(15, true);
        run.frames(frames, 0, 2, 3, 4);

        attack = new Animation(15, false);
        attack.frames(frames, 5, 6, 6);

        die = new Animation(12, false);
        die.frames(frames, 7, 8, 9, 10);

        play(idle);
    }
}

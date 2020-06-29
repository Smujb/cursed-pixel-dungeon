package com.shatteredpixel.yasd.general.sprites;

import com.shatteredpixel.yasd.general.Assets;
import com.watabou.noosa.TextureFilm;

public class WaterDragonSprite extends MobSprite {
	public WaterDragonSprite() {
		super();

		texture( Assets.DRAGONS_2 );

		TextureFilm frames = new TextureFilm( texture, 16, 16 );

		idle = new Animation( 2, true );
		idle.frames( frames, 16, 16, 16, 17 );

		run = new Animation( 10, true );
		run.frames( frames, 22, 23, 24, 25, 26 );

		attack = new Animation( 15, false );
		attack.frames( frames, 18, 19, 20, 21 );

		die = new Animation( 10, false );
		die.frames( frames, 27, 28, 29, 30 );

		play( idle );
	}
}

package com.shatteredpixel.yasd.general.sprites;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.HeroNPC;
import com.watabou.noosa.TextureFilm;

public class HeroNPCSprite extends MobSprite {

	public HeroNPCSprite() {
		super();
	}

	@Override
	public void link(Char ch) {
		texture( ((HeroNPC)ch).heroClass().spritesheet() );
		update();
		super.link(ch);
	}

	public void update() {
		TextureFilm film = new TextureFilm( HeroSprite.tiers(), 6, HeroSprite.FRAME_WIDTH, HeroSprite.FRAME_HEIGHT );
		idle = new Animation( 1, true );
		idle.frames( film, 0, 0, 0, 1, 0, 0, 1, 1 );

		run = new Animation( HeroSprite.RUN_FRAMERATE, true );
		run.frames( film, 2, 3, 4, 5, 6, 7 );

		die = new Animation( 20, false );
		die.frames( film, 8, 9, 10, 11, 12, 11 );

		attack = new Animation( 15, false );
		attack.frames( film, 13, 14, 15, 0 );

		zap = attack.clone();

		operate = new Animation( 8, false );
		operate.frames( film, 16, 17, 16, 17 );

		idle();
	}
}

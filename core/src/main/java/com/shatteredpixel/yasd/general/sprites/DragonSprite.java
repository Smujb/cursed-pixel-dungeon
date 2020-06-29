package com.shatteredpixel.yasd.general.sprites;

import com.shatteredpixel.yasd.general.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public abstract class DragonSprite extends MobSprite {

	private static final int SIZE = 16;

	private DragonSprite(int layer) {
		super();

		texture( Assets.DRAGONS );

		TextureFilm frames = new TextureFilm( texture, SIZE, SIZE );

		int texturesPerLayer = texture.width/SIZE;

		int offset = layer*texturesPerLayer;

		idle = new MovieClip.Animation( 2, true );
		idle.frames( frames, offset, offset, offset, 1+offset );

		attack = new MovieClip.Animation( 10, true );
		attack.frames( frames, 6+offset, 7+offset, 8+offset, 9+offset, 10+offset );

		run = new MovieClip.Animation( 15, false );
		run.frames( frames, 2+offset, 3+offset, 4+offset, 5+offset, offset);

		die = new MovieClip.Animation( 10, false );
		die.frames( frames, 11+offset, 12+offset, 13+offset, 14+offset );

		play( idle );
	}

	public static class Fire extends DragonSprite {
		public Fire() {
			super(0);
		}
	}

	public static class Ice extends DragonSprite {
		public Ice() {
			super(1);
		}
	}

	public static class Poison extends DragonSprite {
		public Poison() {
			super(2);
		}
	}

	public static class Vampiric extends DragonSprite {
		public Vampiric() {
			super(3);
		}
	}

	public static class Stone extends DragonSprite {
		public Stone() {
			super(4);
		}
	}

	public static class Earth extends DragonSprite {
		public Earth() {
			super(5);
		}
	}

	public static class Dark extends DragonSprite {
		public Dark() {
			super(6);
		}
	}

	public static class Water extends DragonSprite {
		public Water() {
			super(7);
		}
	}

	public static class Grass extends DragonSprite {
		public Grass() {
			super(8);
		}
	}

	public static class Spirit extends DragonSprite {
		public Spirit() {
			super(9);
		}
	}
}

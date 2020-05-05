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

package com.shatteredpixel.yasd.general.sprites;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.effects.particles.ElmoParticle;
import com.shatteredpixel.yasd.general.effects.particles.FlameParticle;
import com.shatteredpixel.yasd.general.effects.particles.RainbowParticle;
import com.shatteredpixel.yasd.general.effects.particles.SparkParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public abstract class ElementalSprite extends MobSprite {

	int boltType;

	protected abstract int texOffset();

	private Emitter particles;
	protected abstract Emitter createEmitter();
	
	public ElementalSprite() {
		super();

		int c = texOffset();

		texture( Assets.ELEMENTAL );
		
		TextureFilm frames = new TextureFilm( texture, 12, 14 );
		
		idle = new Animation( 10, true );
		idle.frames( frames, c+0, c+1, c+2 );
		
		run = new Animation( 12, true );
		run.frames( frames, c+0, c+1, c+3 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, c+4, c+5, c+6 );

		zap = attack.clone();
		
		die = new Animation( 15, false );
		die.frames( frames, c+7, c+8, c+9, c+10, c+11, c+12, c+13, c+12 );
		
		play( idle );
	}
	
	@Override
	public void link( Char ch ) {
		super.link( ch );
		if (particles == null) {
			particles = createEmitter();
		}
	}

	@Override
	public void update() {
		super.update();

		if (particles != null) {
			particles.visible = visible;
		}
	}

	
	@Override
	public void die() {
		super.die();
		if (particles != null){
			particles.on = false;
		}
	}

	@Override
	public void kill() {
		super.kill();
		if (particles != null){
			particles.killAndErase();
		}
	}
	
	@Override
	public int blood() {
		return 0xFFFF7D13;
	}

	public static class Fire extends ElementalSprite {

		{
			boltType = MagicMissile.FIRE;
		}

		@Override
		protected int texOffset() {
			return 0;
		}

		@Override
		protected Emitter createEmitter() {
			Emitter emitter = emitter();
			emitter.pour( FlameParticle.FACTORY, 0.06f );
			return emitter;
		}

		@Override
		public int blood() {
			return 0xFFFFBB33;
		}
	}

	public static class NewbornFire extends ElementalSprite {

		{
			boltType = MagicMissile.FIRE;
		}

		@Override
		protected int texOffset() {
			return 14;
		}

		@Override
		protected Emitter createEmitter() {
			Emitter emitter = emitter();
			emitter.pour( ElmoParticle.FACTORY, 0.06f );
			return emitter;
		}

		@Override
		public int blood() {
			return 0xFF85FFC8;
		}
	}

	public static class Frost extends ElementalSprite {

		{
			boltType = MagicMissile.FROST;
		}

		@Override
		protected int texOffset() {
			return 28;
		}

		@Override
		protected Emitter createEmitter() {
			Emitter emitter = emitter();
			emitter.pour( MagicMissile.MagicParticle.FACTORY, 0.06f );
			return emitter;
		}

		@Override
		public int blood() {
			return 0xFF8EE3FF;
		}
	}

	public static class Shock extends ElementalSprite {

		@Override
		protected int texOffset() {
			return 42;
		}

		@Override
		protected Emitter createEmitter() {
			Emitter emitter = emitter();
			emitter.pour( SparkParticle.STATIC, 0.06f );
			return emitter;
		}

		@Override
		public int blood() {
			return 0xFFFFFF85;
		}
	}

	public static class Chaos extends ElementalSprite {

		@Override
		protected int texOffset() {
			return 56;
		}

		@Override
		protected Emitter createEmitter() {
			Emitter emitter = emitter();
			emitter.pour(RainbowParticle.BURST, 0.025f);
			return emitter;
		}

		@Override
		public int blood() {
			return 0xFFE3E3E3;
		}
	}
}

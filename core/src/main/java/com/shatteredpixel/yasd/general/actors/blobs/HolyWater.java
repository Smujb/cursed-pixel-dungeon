package com.shatteredpixel.yasd.general.actors.blobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.effects.BlobEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;

public class HolyWater extends Gas {

	@Override
	public void affectCell(int cell) {
		int damage = 1 + Dungeon.getScaling()/6;
		Char ch = Actor.findChar( cell );
		if (ch != null && !ch.isImmune(this.getClass())) {
			ch.heal( damage, true, true );
		}
	}

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );

		emitter.pour( Speck.factory( Speck.HOLY_WATER ), 0.4f );
	}
}

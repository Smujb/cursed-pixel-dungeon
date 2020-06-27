package com.shatteredpixel.yasd.general.actors.blobs;

import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.effects.BlobEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;

public class DemonGas extends Gas {
	@Override
	public void affectCell(int cell) {
		Char ch = Actor.findChar(cell);
		if (ch != null && !ch.isImmune(getClass())) {
			new ConfusionGas().affectCell(cell);
			new CorrosiveGas().affectCell(cell);
		}
	}

	@Override
	public void use( BlobEmitter emitter ) {
		this.emitter = emitter;

		emitter.pour( Speck.factory(Speck.DEMON_GAS), 0.4f );
	}
}

package com.shatteredpixel.yasd.general.effects;

import com.shatteredpixel.yasd.general.CPDSettings;
import com.watabou.noosa.particles.Emitter;

public class CPDEmitter extends Emitter {

	public CPDEmitter(boolean importantEmitter) {
		this.importantEmitter = importantEmitter;
	}

	public CPDEmitter() {
		this(false);
	}

	//If the emitter is important (such as a blob emitter) turning it off would break the game. Instead, it simply halves the quantity.
	protected boolean importantEmitter;

	@Override
	public void start(Factory factory, float interval, int quantity) {
		float factor = CPDSettings.particles();
		if (factor <= 0 && importantEmitter) {
			factor = 0.5f;
		}
		if (quantity != 0) {
			quantity *= factor;
			if (quantity <= 0) {
				quantity = 1;
			}
		}
		if (interval != 0 && factor != 0) {
			interval *= 1/factor;
		}
		super.start(factory, interval, quantity);
	}
}

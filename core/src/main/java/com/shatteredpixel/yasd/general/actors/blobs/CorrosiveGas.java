/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Cursed Pixel Dungeon
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

package com.shatteredpixel.yasd.general.actors.blobs;

import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Corrosion;
import com.shatteredpixel.yasd.general.effects.BlobEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.watabou.utils.Bundle;

public class CorrosiveGas extends Gas {

	//FIXME should have strength per-cell
	private float strength = 0;

	@Override
	protected void evolve() {
		super.evolve();

		if (volume == 0){
			strength = 0;
		}
	}

	@Override
	public void affectCell(int cell) {
		Char ch = Actor.findChar(cell);
		if (ch != null &&!ch.isImmune(this.getClass())) Buff.affect(ch, Corrosion.class).set(2f, strength);
	}

	public CorrosiveGas setStrength(float str){
		if (str > strength) {
			strength = str;
		}
		return this;
	}

	private static final String STRENGTH = "strength";

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );

		emitter.pour( Speck.factory(Speck.CORROSION), 0.4f );
	}

	@Override
	public void restoreFromBundle( Bundle bundle) {
		super.restoreFromBundle(bundle);
		strength = bundle.getInt( STRENGTH );
	}

	@Override
	public void storeInBundle( Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( STRENGTH, strength );
	}
}

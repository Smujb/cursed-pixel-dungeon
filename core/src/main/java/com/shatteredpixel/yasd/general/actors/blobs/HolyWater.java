package com.shatteredpixel.yasd.general.actors.blobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.effects.BlobEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.messages.Messages;

public class HolyWater extends Blob {
	@Override
	protected void evolve() {
		super.evolve();

		int damage = 1 + Dungeon.getScaleFactor()/6;

		Char ch;
		int cell;

		for (int i = area.left; i < area.right; i++){
			for (int j = area.top; j < area.bottom; j++){
				cell = i + j* Dungeon.level.width();
				if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null) {
					ch.heal( damage, false, true );
				}
			}
		}
	}

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );

		emitter.pour( Speck.factory( Speck.HOLY_WATER ), 0.4f );
	}

	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}

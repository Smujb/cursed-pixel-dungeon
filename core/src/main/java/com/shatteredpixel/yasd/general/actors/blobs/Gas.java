package com.shatteredpixel.yasd.general.actors.blobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.messages.Messages;

public abstract class Gas extends Blob {

	@Override
	protected void evolve() {
		super.evolve();
		int cell;

		for (int i = area.left; i < area.right; i++){
			for (int j = area.top; j < area.bottom; j++){
				cell = i + j*Dungeon.level.width();
				if (cur[cell] > 0) {
					affectCell(cell);
				}
			}
		}
	}

	public abstract void affectCell(int cell);

	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}

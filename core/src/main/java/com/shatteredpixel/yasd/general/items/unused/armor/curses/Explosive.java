package com.shatteredpixel.yasd.general.items.unused.armor.curses;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.items.unused.armor.Armor;
import com.shatteredpixel.yasd.general.items.bombs.ShrapnelBomb;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Explosive extends Armor.Glyph {

	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		if (Random.Int(5) == 0) {
			new ShrapnelBomb().explode(attacker.pos);
		}
		return damage;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}

	@Override
	public boolean curse() {
		return true;
	}
}

package com.shatteredpixel.yasd.general.items.weapon.curses;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.items.Enchantable;
import com.shatteredpixel.yasd.general.items.armor.glyphs.Viscosity;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;

public class Sapping extends Weapon.Enchantment {

    private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );

    @Override
    public int proc(Enchantable weapon, Char attacker, Char defender, int damage) {
        Buff.affect(defender, Viscosity.DeferedDamage.class).prolong((int) (damage*1.5f));
        return 0;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }
}

package com.shatteredpixel.yasd.general.items;

import com.shatteredpixel.yasd.general.actors.Char;

public interface Attackable {
    boolean use(Char enemy);
    boolean canUse(Char enemy);
}

package com.shatteredpixel.yasd.general.items.weapon.ranged;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class OldSunCrossbow extends Crossbow {

    {
        image = ItemSpriteSheet.Ranged.SUN_CROSSBOW;

        DLY = 2f;
        damageFactor = 1.5f;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        Buff.affect(defender, Burning.class).reignite(defender);
        return super.proc(attacker, defender, damage);
    }
}

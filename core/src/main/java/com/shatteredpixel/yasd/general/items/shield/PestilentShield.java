package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Poison;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class PestilentShield extends Shield {

    {
        image = ItemSpriteSheet.Shields.PESTILENT;

        damageFactor = 0.7f;
        defenseMultiplier = 0.7f;
    }

    @Override
    public int proc(Char attacker, Char enemy, int damage, boolean parry) {
        if (parry) Buff.affect(enemy, Poison.class).set(3 + Dungeon.getScaleFactor());
        return super.proc(attacker, enemy, damage, parry);
    }
}

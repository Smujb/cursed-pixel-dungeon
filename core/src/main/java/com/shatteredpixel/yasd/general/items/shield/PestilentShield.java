package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Poison;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class PestilentShield extends Shield {

    {
        image = ItemSpriteSheet.Shields.PESTILENT;

        defenseMultiplier = 0.7f;
    }

    @Override
    public int proc(Char attacker, Char enemy, int damage, boolean parry) {
        if (parry) Buff.affect(enemy, Poison.class).set(Poison.defaultStrength(Dungeon.getScaling()));
        return super.proc(attacker, enemy, damage, parry);
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "causes_poison");
    }
}

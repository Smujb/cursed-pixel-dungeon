package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.actors.buffs.FlavourBuff;
import com.shatteredpixel.yasd.general.messages.Messages;

public class SwiftCrossbow extends Crossbow {

    {
        damageMultiplier = 0.5f;
    }

    @Override
    public String desc() {
        return Messages.get(Crossbow.class,"desc") + "\n\n" + Messages.get(this,"desc");
    }

    public static class SwiftDartAttack extends FlavourBuff {}
}

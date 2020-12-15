package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;

public class TitanShield extends Shield {

    {
        //TODO image

        defenseMultiplier = 1.5f;
        damageFactor = 1.5f;
        chargePerTurn = 3f;
    }

    @Override
    public void doParry(Char ch) {
        ch.spend(Actor.TICK);
        super.doParry(ch);
    }
}

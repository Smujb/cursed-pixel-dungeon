package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class TitanShield extends Shield {

    {
        image = ItemSpriteSheet.Shields.TITAN;

        defenseMultiplier = 1.5f;
        damageFactor = 1.5f;
        chargePerTurn = 6f;
    }

    @Override
    public void doParry(Char ch) {
        ch.spend(Actor.TICK);
        super.doParry(ch);
    }
}

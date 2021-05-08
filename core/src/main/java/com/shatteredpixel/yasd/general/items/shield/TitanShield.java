package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.messages.Messages;
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

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "slow_parry");
    }
}

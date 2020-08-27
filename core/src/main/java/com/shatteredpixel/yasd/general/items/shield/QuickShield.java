package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class QuickShield extends Shield {

    {
        image = ItemSpriteSheet.Shields.QUICK;

        defenseMultiplier = 1/3f;
        chargePerTurn = 10f;
    }

    @Override
    protected float parryTime() {
        return super.parryTime()/2f;
    }
}

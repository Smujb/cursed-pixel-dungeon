package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class RunicShield extends Shield {
    {
        image = ItemSpriteSheet.Shields.RUNIC;

        defenseMultiplier = 0.7f;
    }

    @Override
    public int enchPower() {
        return 2 + level() * 2;
    }
}

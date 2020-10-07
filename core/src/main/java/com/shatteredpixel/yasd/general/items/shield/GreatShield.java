package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class GreatShield extends Shield {

    {
        image = ItemSpriteSheet.Shields.GREAT;

        chargePerTurn = 0.5f;

        defenseMultiplier = 1.75f;
        damageFactor = 1.5f;
    }
}

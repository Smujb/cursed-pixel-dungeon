package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class ReinforcedShield extends Shield {
    {
        image = ItemSpriteSheet.Shields.REINFORCED;

        damageFactor = 0.5f;
        chargePerTurn = 1f;
        defenseMultiplier = 2f;
    }
}

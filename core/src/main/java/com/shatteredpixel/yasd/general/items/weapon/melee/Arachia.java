package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class Arachia extends Scimitar {

    {
        image = ItemSpriteSheet.Weapons.ARACHIA;

        attackDelay = 0.8f;
        damageFactor = 1.2f;
        reach = 2;
        critModifier = 1f;
        slotsUsed = 2;
    }
}

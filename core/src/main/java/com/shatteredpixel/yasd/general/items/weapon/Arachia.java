package com.shatteredpixel.yasd.general.items.weapon;

import com.shatteredpixel.yasd.general.items.weapon.melee.Scimitar;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class Arachia extends Scimitar {

    {
        image = ItemSpriteSheet.Weapons.ARACHIA;

        DLY = 0.8f;
        damageFactor = 1.2f;
        RCH = 2;
        ACC = 0.8f;
        slotsUsed = 2;
    }
}

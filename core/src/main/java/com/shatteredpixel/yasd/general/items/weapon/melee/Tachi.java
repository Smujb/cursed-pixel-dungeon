package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class Tachi extends Katana {

    {
        image = ItemSpriteSheet.Weapons.TACHI;

        attackDelay = 1.5f;
        damageFactor = 1.2f;
        reach = 2;
    }
}

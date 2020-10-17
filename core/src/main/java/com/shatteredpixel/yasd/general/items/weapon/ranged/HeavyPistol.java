package com.shatteredpixel.yasd.general.items.weapon.ranged;

import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class HeavyPistol extends Firearm {

    {
        image = ItemSpriteSheet.Ranged.HEAVY_PISTOL;

        curAmmo = MAX_AMMO = 2;
        damageMultiplier = 1.4f;
        reloadTime = 0.5f;
    }
}

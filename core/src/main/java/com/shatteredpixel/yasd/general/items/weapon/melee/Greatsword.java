package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class Greatsword extends MeleeWeapon {
    {
        image = ItemSpriteSheet.Weapons.GREATSWORD;

        damageFactor = 1.3f;
        staminaConsumption = 35;
        critModifier = 1.4f;
    }
}

package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class Pitchfork extends MeleeWeapon {

    {
        image = ItemSpriteSheet.Weapons.PITCHFORK;
        hitSound = Assets.Sounds.HIT_STAB;

        canBeParried = false;
        RCH = 2;
        damageFactor = 0.7f;
    }
}

package com.shatteredpixel.yasd.general.items.weapon.ranged;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.items.weapon.ranged.ammo.Ammo;
import com.shatteredpixel.yasd.general.items.weapon.ranged.ammo.Bolt;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class Crossbow extends RangedWeapon {

    {
        image = ItemSpriteSheet.Ranged.CROSSBOW;

        hitSound = Assets.Sounds.HIT_ARROW;
        hitSoundPitch = 0.7f;
        speed = 400f;
    }

    @Override
    public Class<? extends Ammo> ammoClass() {
        return Bolt.class;
    }
}

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.DemonGas;
import com.shatteredpixel.yasd.general.sprites.KupuaSprite;

public class Kupua extends Mob {
    {
        spriteClass = KupuaSprite.class;
        healthFactor = 1.5f;
        damageFactor = 0.5f;
        immunities.add(DemonGas.class);
    }

    @Override
    protected boolean act() {
        Blob.seed(pos, 10, DemonGas.class);
        return super.act();
    }
}

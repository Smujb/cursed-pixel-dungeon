package com.shatteredpixel.yasd.general.items.relics;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class GarbOfRetribution extends Relic {

    {
        image = ItemSpriteSheet.ARTIFACT_CAPE;
    }

    @Override
    protected boolean critCondition(Char enemy) {
        return false;
    }
}

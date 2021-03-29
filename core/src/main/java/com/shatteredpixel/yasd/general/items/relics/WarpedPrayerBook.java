package com.shatteredpixel.yasd.general.items.relics;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class WarpedPrayerBook extends Relic {

    {
        image = ItemSpriteSheet.ARTIFACT_SPELLBOOK;
    }

    @Override
    protected boolean critCondition(Char enemy) {
        return false;
    }
}

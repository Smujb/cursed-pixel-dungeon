package com.shatteredpixel.yasd.general.items.armor;

import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class IceArmor extends Armor {

    {
        image = ItemSpriteSheet.ARMOR_MAIL;
    }

    @Override
    public int appearance() {
        return 3;
    }
}

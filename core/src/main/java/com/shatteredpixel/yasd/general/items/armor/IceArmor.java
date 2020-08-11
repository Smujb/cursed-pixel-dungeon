package com.shatteredpixel.yasd.general.items.armor;

import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class IceArmor extends Armor {

    {
        image = ItemSpriteSheet.ARMOR_MAIL;

        statScaling.add(Hero.HeroStat.FOCUS);
    }

    @Override
    public int appearance() {
        return 3;
    }
}

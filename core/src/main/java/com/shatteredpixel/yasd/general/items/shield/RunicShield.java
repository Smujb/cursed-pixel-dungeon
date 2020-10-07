package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class RunicShield extends Shield {
    {
        image = ItemSpriteSheet.Shields.RUNIC;

        defenseMultiplier = 0.7f;

        statScaling.add(Hero.HeroStat.FOCUS);
    }

    @Override
    public int enchPower() {
        return 2 + level() * 2;
    }
}

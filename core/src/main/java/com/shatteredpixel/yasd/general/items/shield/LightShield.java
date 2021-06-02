package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class LightShield extends Shield {

    {
        image = ItemSpriteSheet.Shields.LIGHT;

        defenseMultiplier = 0.6f;
        chargePerTurn = 10f;

        statScaling.add(Hero.HeroStat.ASSAULT);
    }
}

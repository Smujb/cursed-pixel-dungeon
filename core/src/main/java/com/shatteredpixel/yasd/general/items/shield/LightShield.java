package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class LightShield extends Shield {

    {
        image = ItemSpriteSheet.Shields.LIGHT;

        defenseMultiplier = 1/3f;
        chargePerTurn = 10f;

        statScaling.add(Hero.HeroStat.ASSAULT);
    }

    @Override
    protected float parryTime() {
        return super.parryTime()/2f;
    }
}

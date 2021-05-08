package com.shatteredpixel.yasd.general.items.weapon.ranged;

import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class PrecisionRifle extends Firearm {

    {
        image = ItemSpriteSheet.Ranged.PRESCISON_RIFLE;

        damageFactor = 0.7f;
        range = 12;
        sneakBenefit = true;

        statScaling.add(Hero.HeroStat.EXECUTION);
    }
}

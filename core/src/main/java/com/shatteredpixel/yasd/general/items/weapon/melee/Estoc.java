package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class Estoc extends Rapier {

    {
        image = ItemSpriteSheet.Weapons.ESTOC;

        RCH = 2;
        damageFactor = 0.9f;
        slotsUsed = 2;

        statScaling.add(Hero.HeroStat.ASSAULT);
    }
}

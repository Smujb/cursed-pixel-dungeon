package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class Wakizashi extends Katana {
    {
        image = ItemSpriteSheet.Weapons.WAKIZASHI;

        slotsUsed = 1;
        attackDelay = 0.7f;
        damageFactor = 0.5f;

        statScaling.remove(Hero.HeroStat.RESILIENCE);
    }
}

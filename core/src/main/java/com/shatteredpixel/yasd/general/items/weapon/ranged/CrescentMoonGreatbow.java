package com.shatteredpixel.yasd.general.items.weapon.ranged;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class CrescentMoonGreatbow extends Greatbow {

    {
        image = ItemSpriteSheet.Ranged.CRESCENT_GREATBOW;

        damageFactor = 1.2f;
        statScaling.add(Hero.HeroStat.FOCUS);
    }

    @Override
    protected float damageFactor() {
        float bns = super.damageFactor();
        if (Dungeon.checkNight()) {
            //+50% damage at night
            bns = 1.5f;
        } else if (cursed()) {
            //Reaches the same bonus if cursed at 20 curse intensity
            bns = 1f + Math.min(0.5f, curseIntensity/40f);
        }
        return bns;
    }

    @Override
    public boolean canSpawn() {
        //Can only spawn at night
        return super.canSpawn() && Dungeon.checkNight();
    }
}

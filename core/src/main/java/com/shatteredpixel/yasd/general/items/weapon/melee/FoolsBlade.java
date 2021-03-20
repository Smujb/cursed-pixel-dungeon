package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

import java.util.Calendar;

public class FoolsBlade extends MeleeWeapon {

    {
        image = ItemSpriteSheet.Weapons.FOOLS_BLADE;
    }

    @Override
    public int max(float lvl) {
        return super.max(lvl) * getMultiplier();
    }

    @Override
    public int min(float lvl) {
        return super.min(lvl) * getMultiplier();
    }

    private int getMultiplier() {
        int multiplier = 0;
        final Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.MONTH) == Calendar.APRIL && calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            multiplier = 2;
        }
        return multiplier;
    }
}

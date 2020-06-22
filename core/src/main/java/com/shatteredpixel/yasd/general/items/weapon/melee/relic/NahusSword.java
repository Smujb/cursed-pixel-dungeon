package com.shatteredpixel.yasd.general.items.weapon.melee.relic;

import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.Drawing;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.RelicEnchantment;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class NahusSword extends RelicMeleeWeapon {
    {
        image = ItemSpriteSheet.NAHUSSWORD;
        chargeToAdd = 1f;
    }

    @Override
    public RelicEnchantment enchantment() {
        return new Drawing();
    }
}

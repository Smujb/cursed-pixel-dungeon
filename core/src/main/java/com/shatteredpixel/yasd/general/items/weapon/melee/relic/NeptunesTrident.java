package com.shatteredpixel.yasd.general.items.weapon.melee.relic;

import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.RelicEnchantment;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.Voltage;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class NeptunesTrident extends RelicMeleeWeapon {
    {
        image = ItemSpriteSheet.NEPTUNES_TRIDENT;
        DLY = 1.5f;
        ACC = 1.1f;
        damageMultiplier = 1.6f;
        RCH = 2;
        chargeToAdd = 0.5f;//200 turns to charge.
    }

    @Override
    public RelicEnchantment enchantment() {
        return new Voltage();
    }
}

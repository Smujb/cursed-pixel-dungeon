package com.shatteredpixel.yasd.general.items.weapon.melee.relic;

import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.RelicEnchantment;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.Voltage;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class NeptunesTrident extends RelicMeleeWeapon {
    {
        image = ItemSpriteSheet.NEPTUNES_TRIDENT;
        attackDelay = 1.5f;
        damageFactor = 1.6f;
        reach = 2;
        chargeToAdd = 0.5f;//200 turns to charge.
    }

    @Override
    public RelicEnchantment enchantment() {
        return new Voltage();
    }
}

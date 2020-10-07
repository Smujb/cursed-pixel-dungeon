package com.shatteredpixel.yasd.general.items.weapon.melee.relic;

import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.Inferno;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.RelicEnchantment;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class MaracarsBlades extends RelicMeleeWeapon {
    {
        damageFactor = 0.64f;
        ACC = 0.8f;
        DLY = 0.5f;
        image = ItemSpriteSheet.MARACARS_BLADES;
    }

    @Override
    public RelicEnchantment enchantment() {
        return new Inferno();
    }
}

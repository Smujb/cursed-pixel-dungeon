package com.shatteredpixel.yasd.general.items.weapon.melee.relic;

import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.Barrier;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.RelicEnchantment;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class LoturgosCrystal extends RelicMeleeWeapon {
    {
        image = ItemSpriteSheet.LOTURGOS_CRYSTAL;
        chargeToAdd = 1f;
        usesTargeting = true;
    }

    @Override
    public RelicEnchantment enchantment() {
        return new Barrier();
    }
}

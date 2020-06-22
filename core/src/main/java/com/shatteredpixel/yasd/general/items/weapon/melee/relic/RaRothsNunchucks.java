package com.shatteredpixel.yasd.general.items.weapon.melee.relic;

import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.Ethereal;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.RelicEnchantment;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class RaRothsNunchucks extends RelicMeleeWeapon {
    {
        image = ItemSpriteSheet.RA_ROTHS_NUNCHUCKS;
        ACC = 0.7f;
        RCH = 3;
        DLY = 0.5f;
        damageMultiplier = 0.5f;
    }

    @Override
    public RelicEnchantment enchantment() {
        return new Ethereal();
    }
}

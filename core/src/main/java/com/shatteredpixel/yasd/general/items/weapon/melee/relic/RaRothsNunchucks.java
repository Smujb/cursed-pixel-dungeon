package com.shatteredpixel.yasd.general.items.weapon.melee.relic;

import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.Ethereal;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.RelicEnchantment;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class RaRothsNunchucks extends RelicMeleeWeapon {
    {
        image = ItemSpriteSheet.RA_ROTHS_NUNCHUCKS;

        reach = 3;
        attackDelay = 0.5f;
        critModifier = 1.2f;
        damageFactor = 0.5f;
        canSurpriseAttack = false;
    }

    @Override
    public RelicEnchantment enchantment() {
        return new Ethereal();
    }
}

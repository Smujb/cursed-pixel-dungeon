package com.shatteredpixel.yasd.general.items.weapon.melee.relic;

import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.RelicEnchantment;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.Vicious;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class ThonothsAxe extends RelicMeleeWeapon {
    {
        image = ItemSpriteSheet.THONOTHS_AXE;
        ACC = 2f;
        damageFactor = 0.74f;
    }

    @Override
    public RelicEnchantment enchantment() {
        return new Vicious();
    }
}

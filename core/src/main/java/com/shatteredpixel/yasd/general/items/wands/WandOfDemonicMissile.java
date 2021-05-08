package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class WandOfDemonicMissile extends WandOfMagicMissile {

    {
        image = ItemSpriteSheet.Wands.DEMONIC_MISSILE;

        element = Element.SHADOW;

        damageFactor = 1.5f;
        slotsUsed = 2;
    }

    @Override
    public void onZap(Ballistica bolt) {
        super.onZap(bolt);
        curUser.damage(Math.round(curUser.HT*getPercentHPDamage()), new Char.DamageSrc(element, this));
    }

    //Damage increases slightly per missing charge
    private float getPercentHPDamage() {
        return 0.05f + 0.01f * (maxCharges-curCharges);
    }
}

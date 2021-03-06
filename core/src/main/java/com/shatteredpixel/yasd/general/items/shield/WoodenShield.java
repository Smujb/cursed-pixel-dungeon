package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class WoodenShield extends Shield {
    {
        image = ItemSpriteSheet.Shields.WOODEN;

        chargePerTurn = 8f;
        defenseMultiplier = 0.75f;
    }

    @Override
    public int absorbDamage(Char.DamageSrc src, int damage) {
        if (src.getElement() == Element.FIRE || src.getElement() == Element.DESTRUCTION) return damage;
        return super.absorbDamage(src, damage);
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "cant_block_fire");
    }
}

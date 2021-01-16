package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class MundaneSword extends MeleeWeapon {

    {
        image = ItemSpriteSheet.Weapons.PLAIN_SWORD;
        hitSound = Assets.Sounds.HIT_SLASH;

        damageFactor = 1.4f;
    }

    @Override
    public int enchPower() {
        return level()/2;
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n\n" + Messages.get(this, "enchant_reduce");
    }
}

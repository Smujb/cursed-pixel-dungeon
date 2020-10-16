package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.actors.buffs.FlavourBuff;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class SwiftCrossbow extends Crossbow {

    {
        image = ItemSpriteSheet.Ranged.SWIFT_CROSSBOW;

        damageFactor = 0.5f;
    }

    @Override
    public String desc() {
        return Messages.get(Crossbow.class,"desc") + "\n\n" + Messages.get(this,"swift");
    }

    public static class SwiftDartAttack extends FlavourBuff {}
}

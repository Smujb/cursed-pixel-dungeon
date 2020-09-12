package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class SwiftShield extends Shield {

    {
        image = ItemSpriteSheet.Shields.SWIFT;
    }

    @Override
    public void affectEnemy(Char enemy, boolean parry) {
        if (parry && curUser != null) {
            curUser.spend(-parryTime());
        }
        proc(curUser, enemy, 0, parry);
    }
}

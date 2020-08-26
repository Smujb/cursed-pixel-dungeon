package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class PanicShield extends Shield {

    {
        image = ItemSpriteSheet.Shields.PANIC;

        damageFactor = 0.5f;
    }

    @Override
    public void affectEnemy(Char enemy, boolean parry) {
        super.affectEnemy(enemy, parry);
        if (parry && curUser != null && curUser.hpPercent() <= 0.25f) {
            ScrollOfTeleportation.teleportUser(curUser);
        }
    }
}

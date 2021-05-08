package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class PanicShield extends Shield {

    {
        image = ItemSpriteSheet.Shields.PANIC;

        damageFactor = 0.5f;
    }

    @Override
    public int proc(Char attacker, Char enemy, int damage, boolean parry) {
        if (parry && curUser != null && curUser.hpPercent() <= 0.25f) {
            ScrollOfTeleportation.teleportUser(curUser);
        }
        return super.proc(attacker, enemy, damage, parry);
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "teleport_away");
    }
}

package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Falchion extends Scimitar {

    {
        image = ItemSpriteSheet.Weapons.FALCHION;

        attackDelay = 1f;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (Random.Int(3) == 0) {
            Buff.affect( defender, Bleeding.class ).set( damage/3f );
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "causes_bleed");
    }
}

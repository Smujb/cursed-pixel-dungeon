package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Cripple;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class ButchersKnife extends MeleeWeapon {
    {
        image = ItemSpriteSheet.Weapons.BUTCHER_KNIFE;

        hitSound = Assets.Sounds.HIT_CRUSH;
        DLY = 1.5f;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (Random.Int(2) == 0) {
            Buff.affect(defender, Bleeding.class).set(damage/2f);
            Buff.affect(defender, Cripple.class, Cripple.DURATION);
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    protected boolean hasProperties() {
        return true;
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "causes_bleed") + "\n" + Messages.get(this, "causes_cripple");
    }
}

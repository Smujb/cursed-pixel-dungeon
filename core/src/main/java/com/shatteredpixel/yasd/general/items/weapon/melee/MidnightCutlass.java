package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Terror;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.utils.Random;

public class MidnightCutlass extends Scimitar {
    {
        damageFactor = 0.5f;

        statScaling.add(Hero.HeroStat.FOCUS);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (Random.Int(5) == 0) Buff.affect(defender, Terror.class, 10f);
        return super.proc(attacker, defender, damage);
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + Messages.get(this, "causes_terror");
    }
}

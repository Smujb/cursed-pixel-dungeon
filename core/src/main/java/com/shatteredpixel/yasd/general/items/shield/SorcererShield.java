package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.MagicCharge;
import com.shatteredpixel.yasd.general.actors.hero.Hero;

public class SorcererShield extends Shield {
    {
        //TODO image

        defenseMultiplier = 0.8f;
        damageFactor = 0.5f;

        statScaling.add(Hero.HeroStat.FOCUS);
    }

    @Override
    public int proc(Char attacker, Char enemy, int damage, boolean parry) {
        if (parry) {
            Buff.affect(attacker, MagicCharge.class, MagicCharge.DURATION);
        }
        return super.proc(attacker, enemy, damage, parry);
    }
}

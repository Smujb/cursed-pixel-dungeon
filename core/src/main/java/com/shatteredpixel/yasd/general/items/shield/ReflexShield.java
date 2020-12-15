package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.watabou.utils.Callback;

public class ReflexShield extends Shield {

    {
        //TODO image

        defenseMultiplier = 0.8f;
        damageFactor = 0.6f;

        statScaling.add(Hero.HeroStat.ASSAULT);
    }

    @Override
    public void affectEnemy(Char enemy, boolean parry) {
        enemy.elementalType().FX(curUser, enemy.pos, new Callback() {
            @Override
            public void call() {
                ReflexShield.super.affectEnemy(enemy, parry);
            }
        });
    }

    @Override
    public boolean canAffectEnemy(Char enemy) {
        return true;
    }
}

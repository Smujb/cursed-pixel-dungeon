package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.utils.Callback;

public class ReflexShield extends Shield {

    {
        image = ItemSpriteSheet.Shields.REFLEX;

        defenseMultiplier = 0.7f;

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

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "deflects_projectiles");
    }
}

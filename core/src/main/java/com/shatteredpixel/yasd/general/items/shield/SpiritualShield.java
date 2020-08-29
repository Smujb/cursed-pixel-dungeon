package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Recharging;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class SpiritualShield extends Shield {

    {
        image = ItemSpriteSheet.Shields.SPIRITUAL;

        defenseMultiplier = 0.8f;
        damageFactor = 0.5f;

        statScaling.add(Hero.HeroStat.FOCUS);
    }

    @Override
    public void affectEnemy(Char enemy, boolean parry) {
        super.affectEnemy(enemy, parry);
        if (curUser != null && parry) {
            Buff.affect(curUser, Recharging.class, Recharging.DURATION);
        }
    }
}

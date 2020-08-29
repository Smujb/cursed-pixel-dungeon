package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class ParryShield extends Shield {
    {
        image = ItemSpriteSheet.Shields.PARRY;

        damageFactor = 2f;

        statScaling.add(Hero.HeroStat.EXECUTION);
    }

    @Override
    public void affectEnemy(Char enemy, boolean parry) {
        if (parry) {
            super.affectEnemy(enemy, true);
        }
    }

    @Override
    public int absorbDamage(Char.DamageSrc src, int damage) {
        if (!canParry(damage)) {
            setCharge(0);
            return damage;
        }
        return super.absorbDamage(src, damage);
    }
}

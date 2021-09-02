package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class ParryingDagger extends Shield {
    {
        image = ItemSpriteSheet.Shields.DAGGER;

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
            curUser.useStamina(curUser.stamina);
            return damage;
        }
        return super.absorbDamage(src, damage);
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this,"cant_block");
    }
}

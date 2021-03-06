package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class SwiftShield extends Shield {

    {
        image = ItemSpriteSheet.Shields.SWIFT;

        damageFactor = 0f;

        statScaling.add(Hero.HeroStat.ASSAULT);
    }

    @Override
    public void affectEnemy(Char enemy, boolean parry) {
        super.affectEnemy(enemy, parry);
        if (parry && curUser != null) {
            curUser.spend(-parryTime());
        }
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "free_turn");
    }
}

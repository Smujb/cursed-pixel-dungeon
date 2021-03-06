package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class LaceratingShield extends Shield {
    {
        image = ItemSpriteSheet.Shields.LACERATING;

        damageFactor = 0.8f;
        defenseMultiplier = 0.8f;

        statScaling.add(Hero.HeroStat.EXECUTION);
    }

    @Override
    public int proc(Char attacker, Char enemy, int damage, boolean parry) {
        Buff.affect(enemy, Bleeding.class).set(damage/2f);
        return super.proc(attacker, enemy, damage, parry);
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "causes_bleed");
    }
}

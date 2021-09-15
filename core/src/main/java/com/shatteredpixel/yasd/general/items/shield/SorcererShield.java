package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.MagicCharge;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class SorcererShield extends Shield {
    {
        image = ItemSpriteSheet.Shields.SORCERER;

        defenseMultiplier = 0.8f;
        damageFactor = 0.8f;

        statScaling.add(Hero.HeroStat.FOCUS);
    }

    @Override
    public int proc(Char attacker, Char enemy, int damage, boolean parry) {
        if (parry) {
            Buff.affect(attacker, MagicCharge.class, MagicCharge.DURATION).setLevel(3);
        }
        return super.proc(attacker, enemy, damage, parry);
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "boosts_wands_3");
    }
}

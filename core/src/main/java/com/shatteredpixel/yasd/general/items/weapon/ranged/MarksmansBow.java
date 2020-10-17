package com.shatteredpixel.yasd.general.items.weapon.ranged;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.FlavourBuff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class MarksmansBow extends Bow {

    {
        image = ItemSpriteSheet.Ranged.MARKSMAN_BOW;

        damageMultiplier = 0.7f;

        statScaling.add(Hero.HeroStat.SUPPORT);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        Buff.affect(defender, Marked.class, 50f);
        return super.proc(attacker, defender, damage);
    }

    public static class Marked extends FlavourBuff {}
}

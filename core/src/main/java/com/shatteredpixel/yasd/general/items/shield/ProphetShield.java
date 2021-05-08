package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class ProphetShield extends Shield {
    {
        image = ItemSpriteSheet.Shields.DEMONIC;

        damageFactor = 0.5f;
        defenseMultiplier = 0.8f;

        statScaling.add(Hero.HeroStat.SUPPORT);
    }

    @Override
    public int proc(Char attacker, Char enemy, int damage, boolean parry) {
        if (parry) {
            Buff.affect(attacker, ArtifactRecharge.class).set((int) (ArtifactRecharge.DURATION/4));
        }
        return super.proc(attacker, enemy, damage, parry);
    }
}

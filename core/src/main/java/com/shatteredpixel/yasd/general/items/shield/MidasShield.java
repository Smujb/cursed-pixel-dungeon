package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.Gold;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class MidasShield extends Shield {
    {
        image = ItemSpriteSheet.Shields.ROYAL;

        defenseMultiplier = 0.7f;

        statScaling.add(Hero.HeroStat.FOCUS);
    }

    @Override
    public int proc(Char attacker, Char enemy, int damage, boolean parry) {
        if (!enemy.isAlive()) Dungeon.level.drop(new Gold().random(4), enemy.pos).sprite.drop();
        return super.proc(attacker, enemy, damage, parry);
    }
}

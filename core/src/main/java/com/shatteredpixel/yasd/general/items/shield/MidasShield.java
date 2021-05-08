package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.Gold;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class MidasShield extends Shield {
    {
        image = ItemSpriteSheet.Shields.ROYAL;

        defenseMultiplier = 0.7f;

        statScaling.add(Hero.HeroStat.FOCUS);
    }

    @Override
    public int proc(Char attacker, Char enemy, int damage, boolean parry) {
        new Buff() {
            {
                actPriority = VFX_PRIO;
            }

            @Override
            public boolean act() {
                spend(1f);
                detach();
                return true;
            }

            @Override
            public void detach() {
                super.detach();
                if (!target.isAlive()) Dungeon.level.drop(new Gold().random(4), target.pos).sprite.drop();
            }
        }.attachTo(enemy);
        return super.proc(attacker, enemy, damage, parry);
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "drop_gold");
    }
}

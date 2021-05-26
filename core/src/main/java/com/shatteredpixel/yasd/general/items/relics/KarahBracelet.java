package com.shatteredpixel.yasd.general.items.relics;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Doom;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class KarahBracelet extends Relic {

    {
        image = ItemSpriteSheet.RELIC_BRACELET;

        chargePerUse = 50f;
    }

    @Override
    protected boolean critCondition(Char enemy) {
        return enemy.buff(Doom.class) != null;
    }

    @Override
    protected void doActivate() {
        super.doActivate();
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (Dungeon.hero.fieldOfView(mob.pos)) {
                Buff.affect(mob, Doom.class);
            }
        }
    }
}

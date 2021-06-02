package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;

public class TitanShield extends Shield {

    {
        image = ItemSpriteSheet.Shields.TITAN;

        defenseMultiplier = 1.5f;
        damageFactor = 1.5f;
    }

    @Override
    public void doParry(Char ch) {
        if (isEquipped(ch)) {
            new Buff() {
                @Override
                public boolean act() {
                    Buff.affect(ch, Parry.class).setShield(TitanShield.this);
                    detach();
                    return true;
                }
            }.attachTo(ch);
            ch.spendAndNext(Actor.TICK + parryTime());
            ch.spendAndNext(parryTime());
        } else {
            GLog.negative(Messages.get(this, "not_equipped"));
        }
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "slow_parry");
    }
}

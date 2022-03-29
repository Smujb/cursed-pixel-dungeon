package com.shatteredpixel.yasd.general.items.scrolls;

import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.effects.particles.SparkParticle;
import com.shatteredpixel.yasd.general.items.powers.LuckyBadge;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;

public class ScrollOfGreed extends Scroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_UPGRADE;

        mpCost = 4;
    }

    @Override
    public void doRead() {
        curUser.sprite.emitter().burst(SparkParticle.FACTORY, 10);
        Buff.affect(curUser, LuckyBadge.LuckBuff.class, 20f);
        GLog.negative(Messages.get(this, "luck_buff"));
    }

    @Override
    public int price() {
        return isKnown() ? 40 * quantity : super.price();
    }
}

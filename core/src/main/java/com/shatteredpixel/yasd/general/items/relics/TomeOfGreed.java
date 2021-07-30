package com.shatteredpixel.yasd.general.items.relics;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.Gold;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

import org.jetbrains.annotations.NotNull;

public class TomeOfGreed extends SelectorRelic {

    {
        image = ItemSpriteSheet.GREED;
    }

    @Override
    protected boolean critCondition(Char enemy) {
        return enemy.buff(GreedBuff.class) != null;
    }

    @Override
    protected void onCellSelected(int cell) {
        Char ch = Actor.findChar(cell);
        if (curUser != null && ch != null && Ballistica.canHit(curUser, cell, Ballistica.PROJECTILE)) {
            Buff.affect(ch, GreedBuff.class);
            ch.sprite.emitter().burst(Speck.factory(Speck.COIN), 10);
        }
    }

    public static class GreedBuff extends Buff {

        private float factor;

        private static final String FACTOR = "factor";

        {
            type = buffType.NEGATIVE;
        }

        @Override
        public boolean attachTo(@NotNull Char target) {
            factor = target.hpPercent();
            if (target.properties().contains(Char.Property.MINIBOSS)) {
                factor *= 2;
            } else if (target.properties().contains(Char.Property.BOSS)) {
                factor *= 5;
            }
            if (target.properties().contains(Char.Property.IMMOVABLE)) {
                factor = Math.min(factor, 1f);
            }
            return super.attachTo(target);
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(FACTOR, factor);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            factor = bundle.getFloat(FACTOR);
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public void detach() {
            Dungeon.level.drop(new Gold().random(4*factor), target.pos).sprite.drop();
            super.detach();
        }

        @Override
        public boolean act() {
            spend( TICK );
            return true;
        }
    }
}

package com.shatteredpixel.yasd.general.items.relics;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Healing;
import com.shatteredpixel.yasd.general.actors.buffs.Vulnerable;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class CupOfSuffering extends Relic {
    {
        image = ItemSpriteSheet.ARTIFACT_CHALICE1;

        damageFactor = 0.5f;

        //Uses 100% of charge to heal
        chargePerUse = MAX_CHARGE;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        Buff.affect(defender, Bleeding.class).set(damage/2f);
        return super.proc(attacker, defender, damage);
    }

    @Override
    protected void doActivate() {
        super.doActivate();
        if (curUser == null) return;
        Buff.affect(curUser, Healing.class).setHeal(healAmt(), 0.05f, 0);
        if (cursed()) {
            Buff.affect(curUser, Cursed.class);
        } else {
            Buff.prolong(curUser, Vulnerable.class, Vulnerable.DURATION);
        }
    }

    private int healAmt() {
        int amt = Math.round(power() * 40);
        if (cursed()) amt *= 1 + curseIntensity/3;
        return amt;
    }

    @Override
    protected boolean critCondition(Char enemy) {
        return false;
    }

    public static class Cursed extends Buff {

        {
            type = buffType.NEGATIVE;
        }

        private int enemies = 5;

        private static final String ENEMIES = "enemies";

        @Override
        public int icon() {
            return BuffIndicator.CORRUPT;
        }

        @Override
        public boolean act() {
            if (!active()) detach();
            spend(TICK);
            return true;
        }

        protected void eliminateEnemy() {
            enemies--;
        }

        protected boolean active() {
            return enemies > 0;
        }

        public static void eliminateEnemy(Char ch) {
            Cursed cursed = ch.buff(Cursed.class);
            if (cursed != null) cursed.eliminateEnemy();
        }

        public static boolean active(Char ch) {
            Cursed cursed = ch.buff(Cursed.class);
            if (cursed != null) return cursed.active();
            return false;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", enemies);
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(ENEMIES, enemies);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            enemies = bundle.getInt(ENEMIES);
        }
    }
}

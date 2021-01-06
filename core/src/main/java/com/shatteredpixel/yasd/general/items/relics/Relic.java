package com.shatteredpixel.yasd.general.items.relics;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.messages.Messages;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Relic extends KindofMisc {

    {
        statScaling = new ArrayList<>(Arrays.asList(Hero.HeroStat.SUPPORT));

        defaultAction = AC_ACTIVATE;
    }

    protected static final float MAX_CHARGE = 100;
    protected float charge = MAX_CHARGE;

    private static final String AC_ACTIVATE = "activate";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_ACTIVATE);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_ACTIVATE) && canActivate()) {
            doActivate();
        }
    }

    protected void doActivate() {
        useCharge(1);
    }

    protected boolean canActivate() {
        return curUser != null && isEquipped(curUser);
    }

    public final void useCharge(float amount) {
        charge -= amount;
        if (charge < 0) charge = 0;
        updateQuickslot();
    }

    public final void gainCharge(float amount) {
        charge += amount;
        if (charge > MAX_CHARGE) charge = MAX_CHARGE;
        updateQuickslot();
    }

    @Override
    public String status() {
        return Messages.format( "%d%%", Math.round(charge) );
    }

    @Override
    public void activate(Char ch) {
        super.activate(ch);
        new Charger().attachTo(ch);
    }

    public class Charger extends Buff {
        @Override
        public boolean act() {
            gainCharge(0.25f*power());
            spend(TICK);
            return true;
        }
    }
}

package com.shatteredpixel.yasd.general.items.relics;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.KindOfWeapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Relic extends KindOfWeapon {

    {
        statScaling = new ArrayList<>(Arrays.asList(Hero.HeroStat.SUPPORT));

        defaultAction = AC_INFO;
    }

    protected static final float MAX_CHARGE = 100;
    protected float charge = MAX_CHARGE;

    protected float damageFactor = 1f;
    protected float chargePerKill = 20f;
    protected float chargePerUse = 10f;

    protected static final String AC_ACTIVATE = "activate";

    private static final String CHARGE = "charge";

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

    protected int damageRoll() {
        return Random.NormalIntRange(min(), max());
    }

    @Override
    public final int min() {
        return Math.round(min(power()));
    }

    @Override
    public final int max(){
        return Math.round(max(power()));
    }

    @Override
    public int min(float lvl) {
        return (int) Math.max(0, (4 * lvl - damageReduction()) * damageFactor);   //level scaling
    }

    @Override
    public int max(float lvl) {
        return (int) Math.max(0, (10 * lvl - damageReduction()) * damageFactor);   //level scaling
    }

    final int defaultMin() {
        return min(1f);
    }

    final int defaultMax() {
        return max(1f);
    }

    protected abstract boolean critCondition(Char enemy);

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (critCondition(defender) || damage*2 >= defender.HP) damage *= 2;
        return super.proc(attacker, defender, damage);
    }

    public String statsDesc() {
        String desc = "";
        if (isIdentified()) {
            desc = Messages.get(this, "stats_desc", min(), max());
        } else {
            desc = Messages.get(this, "typical_stats_desc", defaultMin(), defaultMax());
        }
        String critCondition = Messages.get(this, "crit_condition");
        if (!(critCondition.equals("") || critCondition.contains("missed_string"))) desc += " " + critCondition;
        desc += " " + Messages.get(this, "charge_per_kill", (int) chargePerKill);
        if (chargePerUse > 0) desc += " " + Messages.get(this, "charge_per_use", (int) chargePerUse);
        String props = propsDesc();
        if (!props.equals("")) {
            desc += props;
        }
        return desc;
    }

    @Override
    protected void onEnemyDeath(Char ch) {
        super.onEnemyDeath(ch);
        gainCharge(chargePerKill);
    }

    public String propsDesc() {
        return "";
    }

    @Override
    public String desc() {
        return super.desc() + "\n\n" + statsDesc();
    }

    protected void doActivate() {
        useCharge(chargePerUse);
    }

    protected boolean canActivate() {
        return curUser != null && isEquipped(curUser) && charge >= chargePerUse;
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
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHARGE, charge);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        charge = bundle.getInt(CHARGE);
    }
}

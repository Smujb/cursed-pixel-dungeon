package com.shatteredpixel.yasd.general.items.relics;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Relic extends KindofMisc {

    {
        statScaling = new ArrayList<>(Arrays.asList(Hero.HeroStat.SUPPORT));

        defaultAction = AC_ACTIVATE;
    }

    protected static final float MAX_CHARGE = 100;
    protected float charge = MAX_CHARGE;

    protected float damageMultiplier = 1f;

    private static final String AC_ACTIVATE = "activate";
    private static final String AC_FINISHER = "finisher";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_ACTIVATE);
        actions.add(AC_FINISHER);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_ACTIVATE) && canActivate()) {
            doActivate();
        }
    }

    public boolean doAttack(Char attacker, Char defender) {
        attacker.busy();
        if (attacker.sprite != null && (attacker.sprite.visible || defender.sprite.visible)) {
            attacker.spend( 1f );
            attacker.sprite.attack(defender.pos, new Callback() {
                @Override
                public void call() {
                    attack(attacker, defender, false);
                    attacker.next();
                }
            });
            return false;
        } else {
            attack(attacker, defender, false);
            attacker.spend( 1f );
            attacker.next();
            return true;
        }
    }

    public boolean attack(Char attacker, Char enemy, boolean guaranteed) {
        Char.DamageSrc src = new Char.DamageSrc(Element.PHYSICAL, this);
        int damage = damageRoll();
        damage = proc(attacker, enemy, damage);
        return attacker.attack(enemy, guaranteed, damage, src);
    }

    protected int damageRoll(float lvl) {
        return Math.round(Random.NormalFloat(min(lvl), max(lvl)));
    }

    protected int damageRoll() {
        return Random.NormalIntRange(min(), max());
    }

    public final int min() {
        return Math.round(min(power()));
    }

    public final int max(){
        return Math.round(max(power()));
    }

    public float min(float lvl) {
        return 8 * lvl * damageMultiplier;    //level scaling
    }

    public float max(float lvl) {
        return 20 * lvl * damageMultiplier;   //level scaling
    }

    final int defaultMin() {
        return (int) min(1f);
    }

    final int defaultMax() {
        return (int) max(1f);
    }

    protected abstract boolean critCondition(Char enemy);

    protected int proc(Char attacker, Char defender, int damage) {
        if (critCondition(defender) || damage*2 >= defender.HP) damage *= 2;
        return damage;
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

package com.shatteredpixel.yasd.general.items.weapon.melee.relic;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.LockedFloor;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.RelicEnchantment;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public abstract class RelicMeleeWeapon extends MeleeWeapon {
    {
        tier = 6;
        defaultAction = AC_ACTIVATE;
    }

    public static final String AC_ACTIVATE = "activate";

    protected Buff passiveBuff;
    public int charge = 100;
    protected float partialCharge = 0;
    protected int chargeCap = 100;
    protected int cooldown = 0;
    protected float chargeToAdd = 1/5f;

    public RelicMeleeWeapon() {
        super();
        enchant(enchantment());
    }


    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions =  super.actions(hero);
        if (isEquipped(hero) & charge >= chargeCap) {
            actions.add(AC_ACTIVATE);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);
        if (action.equals(AC_ACTIVATE)) {
            specialAction(hero);
        }
    }

    public void specialAction(Hero hero) {
        if (isEquipped(hero) & charge >= chargeCap) {
            ((RelicEnchantment) enchantment).activate(this, hero);
        } else {
            GLog.i(Messages.get(RelicMeleeWeapon.class,"no_charge",name()));
        }
    }

    public void use() {
        use(charge);
    }

    public void use(int amount) {
        charge -= amount;
        if (charge < 0) {
            charge = 0;
        }
        updateQuickslot();
    }

    @Override
    public int max(float lvl) {
        return (int) (super.max(lvl)*0.7f);
    }

    public void activate(Char ch ) {
        passiveBuff = passiveBuff();
        if (passiveBuff != null) {
            passiveBuff.attachTo(ch);
        }
    }

    @Override
    public boolean doUnequip(Char hero, boolean collect, boolean single) {
        passiveBuff.detach();
        return super.doUnequip(hero, collect, single);
    }

    protected RelicMeleeWeaponBuff passiveBuff() {
        return new RelicMeleeWeaponBuff();
    }

    public abstract RelicEnchantment enchantment();

    @Override
    public Weapon enchant(Enchantment ench) {
        return super.enchant(enchantment());
    }

    @Override
    public String status() {
        if (!isIdentified()){
            return null;
        }
        //display as percent
        return Messages.format( "%d%%", charge );
    }
    private static final String CHARGE = "charge";
    private static final String PARTIALCHARGE = "partialcharge";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( CHARGE , charge );
        bundle.put( PARTIALCHARGE , partialCharge );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        charge = Math.min( chargeCap, bundle.getInt( CHARGE ));
        charge = bundle.getInt( CHARGE );
        partialCharge = bundle.getFloat( PARTIALCHARGE );
    }

    public class RelicMeleeWeaponBuff extends Buff {

        public int itemLevel() {
            return level();
        }

        public boolean isCursed() {
            return cursed;
        }

        @Override
        public boolean act() {
            LockedFloor lock = target.buff(LockedFloor.class);
            if (charge < chargeCap  && (lock == null || lock.regenOn())) {
                partialCharge += chargeToAdd;
                if (partialCharge > 1){
                    charge++;
                    updateQuickslot();
                    partialCharge--;
                    if (charge >= chargeCap){
                        partialCharge = 0f;
                        GLog.p( Messages.get(RelicMeleeWeapon.class, "charged"),name() );
                    }
                }
            }
            spend( TICK );
            return true;
        }
    }
}

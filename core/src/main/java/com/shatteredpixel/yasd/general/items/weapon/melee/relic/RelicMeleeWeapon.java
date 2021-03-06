package com.shatteredpixel.yasd.general.items.weapon.melee.relic;

import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.CPDSettings;
import com.shatteredpixel.yasd.general.Chrome;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.LockedFloor;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.bags.Bag;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants.RelicEnchantment;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.HeroSelectScene;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.ui.StyledButton;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndInfoItem;
import com.shatteredpixel.yasd.general.windows.WndTitledMessage;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Reflection;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class RelicMeleeWeapon extends MeleeWeapon {
    {
        defaultAction = AC_ACTIVATE;

        statScaling = new ArrayList<>(Arrays.asList(Hero.HeroStat.values()));

        unique = true;
        bones = false;
    }

    @Override
    public int image() {
        return image;
    }

    public static final Class<? extends RelicMeleeWeapon>[] weapons = new Class[] {
            //Placeholder for the first button
            null,
            LorsionsGreataxe.class,
            LoturgosCrystal.class,
            MaracarsBlades.class,
            NahusSword.class,
            NeptunesTrident.class,
            RaRothsNunchucks.class,
            ThonothsAxe.class
    };

    private static final String AC_ACTIVATE = "activate";

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

    public static Class<? extends RelicMeleeWeapon>[] unlockedRelicWeapons() {
        ArrayList<Class<? extends RelicMeleeWeapon>> wepClasses = new ArrayList<>();
        for (Class<? extends RelicMeleeWeapon> wepClass : weapons) {
            if (CPDSettings.unlockedRelicWep(wepClass) || DeviceCompat.isDebug()) {
                wepClasses.add(wepClass);
            }
        }
        return wepClasses.toArray(new Class[0]);
    }

    public static String[] unlockedWepNames() {
        Class<? extends RelicMeleeWeapon>[] wepClasses = unlockedRelicWeapons();
        ArrayList<String> names = new ArrayList<>();
        for (Class<? extends RelicMeleeWeapon> wepClass : wepClasses) {
            names.add(Messages.titleCase(Reflection.forceNewInstance(wepClass).name()));
        }
        return names.toArray(new String[0]);
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
    public boolean collect(Bag container, @NotNull Char ch) {
        //CPDSettings.unlockRelicWep(getClass(), true);
        return super.collect(container, ch);
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
            GLog.info(Messages.get(RelicMeleeWeapon.class,"no_charge",name()));
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
    public int min(float lvl) {
        return (int) (super.min(lvl)*0.6f);
    }

    @Override
    public int max(float lvl) {
        return (int) (super.max(lvl)*0.6f);
    }

    public void activate( Char ch ) {
        super.activate(ch);
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
                        GLog.positive( Messages.get(RelicMeleeWeapon.class, "charged"),name() );
                    }
                }
            }
            spend( TICK );
            return true;
        }
    }

    public static class BtnRelicWeapon extends StyledButton {

        public static int SIZE = 20;

        RelicMeleeWeapon weapon;

        public BtnRelicWeapon() {
            super(Chrome.Type.GREY_BUTTON_TR, "");
            icon(new ItemSprite(ItemSpriteSheet.WEAPON_HOLDER));
            this.weapon = null;
            setRect(0, 0, SIZE, SIZE);
        }

        public BtnRelicWeapon(RelicMeleeWeapon weapon) {
            super(Chrome.Type.GREY_BUTTON_TR, "");
            icon(new ItemSprite(weapon.image()));
            weapon.cursedKnown = true;
            this.weapon = weapon;
            setRect(0, 0, SIZE, SIZE);
            //Ensure it's clear whether it's unlocked
            alpha(1f);
        }

        @Override
        public void alpha(float value) {
            super.alpha(value);
            if (HeroSelectScene.curWeapon == weapon || (HeroSelectScene.curWeapon != null && HeroSelectScene.curWeapon.equals(weapon))) {
                icon.am = value;
            } else {
                icon.am = value*0.3f;
            }
        }

        @Override
        protected boolean onLongClick() {
            if (weapon == null) {
                CPDGame.scene().addToFront(new WndTitledMessage(new Image(icon), Messages.get(this, "relic_weapon"), Messages.get(this, "relic_weapon_desc")));
            } else {
                CPDGame.scene().addToFront(new WndInfoItem(weapon));
            }
            return true;
        }

        @Override
        protected void onClick() {
            super.onClick();
            if (HeroSelectScene.curWeapon == weapon) {
                 onLongClick();
            } else {
                HeroSelectScene.curWeapon = weapon;
            }
        }
    }
}

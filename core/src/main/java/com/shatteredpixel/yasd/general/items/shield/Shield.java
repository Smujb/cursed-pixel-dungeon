/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Yet Another Shattered Dungeon
 *   Copyright (C) 2014-2020 Samuel Braithwaite
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.MagicImmune;
import com.shatteredpixel.yasd.general.actors.buffs.ParryBuff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.BrokenSeal;
import com.shatteredpixel.yasd.general.items.Enchantable;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Shield extends KindofMisc implements Enchantable {

    {
        defaultAction = AC_PARRY;

        statScaling = new ArrayList<>(Arrays.asList(Hero.HeroStat.RESILIENCE));
    }

    public static final String AC_PARRY = "parry";
    protected static final String AC_DETACH       = "detach";

    private static final float PARRY_DURATION = 1f;

    public static final float MAX_CHARGE = 100f;
    private float charge = MAX_CHARGE;

    protected float chargePerTurn = 4f;
    protected float defenseMultiplier = 1f;
    protected float damageFactor = 1f;

    private BrokenSeal seal;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_PARRY);
        if (seal != null) actions.add(AC_DETACH);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_PARRY)) {
            doParry(hero);
        } else if (action.equals(AC_DETACH) && seal != null){
            GLog.info( Messages.get(this, "detach_seal") );
            hero.sprite.operate(hero.pos);
            if (!seal.collect()){
                Dungeon.level.drop(seal, hero.pos);
            }
            seal = null;
        }
    }

    public void doParry(Char ch) {
        if (isEquipped(ch)) {
            Buff.affect(ch, Parry.class).setShield(this);
            ch.spendAndNext(parryTime());
        } else {
            GLog.negative(Messages.get(this, "not_equipped"));
        }
    }

    protected float parryTime() {
        return PARRY_DURATION;
    }

    protected final void setCharge(float value) {
        charge = value;
        charge = GameMath.gate(0f, charge, MAX_CHARGE);
        updateQuickslot();
    }

    protected final void increaseCharge(float value) {
        setCharge(charge + value);
    }

    protected final void decreaseCharge(float value) {
        increaseCharge(-value);
    }

    public BrokenSeal checkSeal() {
        return seal;
    }

    public void affixSeal(BrokenSeal seal) {
        this.seal = seal;
    }

    @Override
    public void reset() {
        super.reset();
        seal = null;
    }

    @Override
    public void setupEmitters(ItemSprite sprite) {
        super.setupEmitters(sprite);
        if (seal != null) {
            Emitter emitter = emitter(sprite);
            emitter.move(ItemSpriteSheet.film.width(image) / 2f + 2f, ItemSpriteSheet.film.height(image) / 3f);
            emitter.fillTarget = false;
            emitter.pour(Speck.factory(Speck.RED_LIGHT), 0.6f);
        }
        Emitter emitter = emitter(sprite);
        if (enchantment != null && !enchantment.curse() && cursedKnown) {
            emitter.pour(Speck.factory(Speck.HALO), 0.15f);
        }
    }

    @Override
    public int level() {
        int lvl = super.level();
        if (seal != null) {
            lvl += seal.level();
        }
        return lvl;
    }

    @Override
    public Item upgrade() {
        if (seal != null && seal.isUpgradable()) {
            seal.upgrade();
            return this;
        }
        return super.upgrade();
    }

    private Charger charger = null;

    @Override
    public void activate(Char ch) {
        super.activate(ch);
        charger = new Charger();
        charger.attachTo(ch);
    }

    @Override
    public Item random() {
        //+0: 75% (3/4)
        //+1: 20% (4/20)
        //+2: 5%  (1/20)
        int n = Dungeon.getScaling()/2;
        if (Random.Int(4) == 0) {
            n++;
            if (Random.Int(5) == 0) {
                n++;
            }
        }
        level(n);

        //30% chance to be cursed
        //10% chance to be enchanted
        float effectRoll = Random.Float();
        if (effectRoll < 0.5f) {
            enchant(Weapon.Enchantment.randomCurse());
            curse();
        } else if (effectRoll >= 0.8f){
            enchant();
        }
        return this;
    }

    @Override
    public boolean doUnequip(Char hero, boolean collect, boolean single) {
        boolean unequip = super.doUnequip(hero, collect, single);
        if (unequip && charger != null) {
            charger.detach();
        }
        return unequip;
    }

    @Override
    public String info() {
        String info = "\n\n" + Messages.get(this, "stats_desc", maxDefense(power()), minDefense(power()), minDamage(power()), maxDamage(power()));
        String propsDesc = propsDesc();
        if (!propsDesc.equals("")) info = "\n" + propsDesc() + info;
        return desc() + info + upgradableItemDesc();
    }

    protected String propsDesc() {
        String props = "";
        if (parryTime() != Actor.TICK) props +=  "\n" + Messages.get(this, "parry_time", parryTime());
        if (slotsUsed > 1) props += "\n" + Messages.get(KindofMisc.class, "requires_slots", slotsUsed);
        return props;
    }

    public float chargePercent() {
        return charge/MAX_CHARGE;
    }

    public static float defaultMaxDefense(float lvl) {
        return Math.round(60 * lvl);
    }

    public int minDamage(float lvl) {
        return (int) Math.max(0, (12 * lvl - damageReduction()) * damageFactor);   //level scaling
    }

    public int maxDamage(float lvl) {
        return (int) Math.max(0, (16 * lvl - damageReduction()) * damageFactor);   //level scaling
    }

    public int maxDefense(float lvl) {
        return (int) Math.max(0, (defaultMaxDefense(lvl) - 2*damageReduction()) * defenseMultiplier);
    }

    public int minDefense(float lvl) {
        return (int) (defaultMaxDefense(lvl) * 0.1f);
    }

    public int curDefense(float lvl) {
        return Math.max(minDefense(lvl), Math.round(maxDefense(lvl) * chargePercent()));
    }

    public static void successfulParry(Char ch) {
        ch.sprite.showStatus( CharSprite.POSITIVE, Messages.get(Shield.class, "parried") );
        evoke(ch);
        Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1f, Random.Float(0.96f, 1.05f));
        CPDGame.shake(1f);
    }

    public void affectEnemy(Char enemy, boolean parry) {
        if (canAffectEnemy(enemy)) {
            if (damageFactor == 0f) return;
            int damage;
            if (parry) {
                damage = maxDamage(power());
            } else if (charge <= 0) {
                damage = minDamage(power());
            } else {
                damage = Random.NormalIntRange(minDamage(power()), maxDamage(power()));
            }
            damage = proc(curUser, enemy, damage, parry);
            enemy.damage(damage, new Char.DamageSrc(Element.PHYSICAL, this));
        }
    }

    public boolean canAffectEnemy(Char enemy) {
        return curUser != null && Dungeon.level.adjacent(curUser.pos, enemy.pos);
    }

    public int proc(Char attacker, Char enemy, int damage, boolean parry) {
        if (enchantment != null && attacker.buff(MagicImmune.class) == null) {
            damage = enchantment.proc(this, attacker, enemy, damage);
        }
        return damage;
    }

    public int absorbDamage(Char.DamageSrc src, int damage) {
        int defense = curDefense(power());
        if (defense >= damage) {
            float chargeToLose = (damage / (float) maxDefense(power())) * MAX_CHARGE;
            decreaseCharge(chargeToLose);
            Item.updateQuickslot();
            return 0;
        } else {
            damage -= defense;
            charge = 0;
            Item.updateQuickslot();
            return damage;
        }
    }

    protected boolean canParry(int damage) {
        float chargeToLose = (damage / (float) maxDefense(power())) * MAX_CHARGE;
        return charge >= chargeToLose;
    }

    @Override
    public String status() {
        if (!isIdentified()){
            return null;
        }
        return Messages.format( "%d%%", Math.round(charge) );
    }

    @Override
    public int price() {
        int price = 100;
        if (cursedKnown && cursed()) {
            price /= 2;
        }
        if (levelKnown && level() > 0) {
            price *= power();
        }
        if (price < 1) {
            price = 1;
        }
        return price;
    }

    private static final String SEAL            = "seal";
    private static final String CHARGE            = "charge";
    private static final String ENCHANTMENT            = "enchantment";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SEAL, seal);
        bundle.put(CHARGE, charge);
        bundle.put(ENCHANTMENT, enchantment);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        seal = (BrokenSeal) bundle.get(SEAL);
        charge = bundle.getFloat(CHARGE);
        enchantment = (Weapon.Enchantment) bundle.get(ENCHANTMENT);
    }

    private Weapon.Enchantment enchantment = null;

    @Override
    public Item enchant(Weapon.Enchantment enchantment) {
        this.enchantment = enchantment;
        updateQuickslot();
        return this;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return enchantment != null && cursedKnown ? enchantment.glowing() : null;
    }

    @Override
    public Item enchant() {

        Class<? extends Weapon.Enchantment> oldEnchantment = enchantment != null ? enchantment.getClass() : null;
        Weapon.Enchantment ench = Weapon.Enchantment.random( oldEnchantment );

        return enchant( ench );
    }

    @Override
    public boolean hasEnchant(Class<? extends Weapon.Enchantment> type, Char owner) {
        return enchantment != null && enchantment.getClass() == type && owner.buff(MagicImmune.class) == null;
    }

    @Override
    public boolean hasGoodEnchant(){
        return enchantment != null && !enchantment.curse();
    }

    @Override
    public boolean hasCurseEnchant(){
        return enchantment != null && enchantment.curse();
    }

    @Override
    public Weapon.Enchantment getEnchantment() {
        return enchantment;
    }

    @Override
    public int enchPower() {
        return cursed() ? curseIntensity : level();
    }

    @Override
    public boolean isSimilar(Item item) {
        boolean similar = super.isSimilar(item);
        if (similar && item instanceof Shield) {
            if (enchantment == null && ((Shield) item).enchantment == null) {
                return true;
            } else if (enchantment != null && ((Shield)item).enchantment != null) {
                return enchantment.getClass() == ((Shield) item).enchantment.getClass();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String name() {
        //return enchantment != null && (cursedKnown || !enchantment.curse()) ? enchantment.name( super.name() ) : super.name();
        return Weapon.Enchantment.getName(this.getClass(), enchantment, cursedKnown);
    }

    public static class Parry extends ParryBuff {

        private Shield shield;

        @Override
        protected void emptyCharge() {
            shield.setCharge(0);
        }

        private static final String SHIELD = "shield";

        public void setShield(Shield shield) {
            this.shield = shield;
        }

        public int absorbDamage(Char.DamageSrc src, int damage) {
            parried = true;
            detach();
            if (shield == null || target == null || !shield.isEquipped(target)) {
                return damage;
            } else {
                return shield.absorbDamage(src, damage);
            }
        }

        public void affectEnemy(Char enemy, boolean parry) {
            if (shield != null) {
                shield.affectEnemy(enemy, parry);
            }
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(SHIELD, shield);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            shield = (Shield) bundle.get(SHIELD);
        }
    }

    public class Charger extends Buff {

        @Override
        public boolean act() {
            increaseCharge(chargePerTurn);
            spend(TICK);
            updateQuickslot();
            return true;
        }
    }
}

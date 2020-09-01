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
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.BrokenSeal;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Shield extends KindofMisc {

    {
        defaultAction = AC_PARRY;

        statScaling = new ArrayList<>(Arrays.asList(Hero.HeroStat.RESILIENCE));
    }

    public static final String AC_PARRY = "parry";
    protected static final String AC_DETACH       = "detach";

    private static final float PARRY_DURATION = 1f;

    public static final float MAX_CHARGE = 100f;
    private float charge = MAX_CHARGE;

    protected float chargePerTurn = 2f;
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
            Buff.affect(hero, Parry.class).setShield(this);
            hero.spendAndNext(parryTime());
        } else if (action.equals(AC_DETACH) && seal != null){
            GLog.info( Messages.get(this, "detach_seal") );
            hero.sprite.operate(hero.pos);
            if (!seal.collect()){
                Dungeon.level.drop(seal, hero.pos);
            }
            seal = null;
        }
    }

    protected float parryTime() {
        return PARRY_DURATION;
    }

    protected final void setCharge(float value) {
        charge = value;
        charge = GameMath.gate(0f, charge, MAX_CHARGE);
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
    public boolean doUnequip(Char hero, boolean collect, boolean single) {
        boolean unequip = super.doUnequip(hero, collect, single);
        if (unequip && charger != null) {
            charger.detach();
        }
        return unequip;
    }

    @Override
    public String info() {
        return desc() + statsReqDesc();
    }

    public float chargePercent() {
        return charge/MAX_CHARGE;
    }

    public static float defaultMaxDefense(float lvl) {
        return Math.round(5 * lvl);
    }

    public int maxDefense(float lvl) {
        return Math.round(defaultMaxDefense(lvl) * defenseMultiplier);
    }

    public int curDefense(float lvl) {
        return Math.round(maxDefense(lvl) * chargePercent());
    }

    public static void successfulParry(Char ch) {
        ch.sprite.showStatus( CharSprite.POSITIVE, Messages.get(Shield.class, "parried") );
        Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1f, Random.Float(0.96f, 1.05f));
        CPDGame.shake(1f);
    }

    public void affectEnemy(Char enemy, boolean parry) {
        if (curUser != null && Dungeon.level.adjacent(curUser.pos, enemy.pos)) {
            int damage = Random.Int(Math.round(defaultMaxDefense(power())/2), Math.round(defaultMaxDefense(power())));
            enemy.damage(damage, new Char.DamageSrc(Element.PHYSICAL, this));
        }
    }

    public int absorbDamage(Char.DamageSrc src, int damage) {
        if (charge == 0) return damage;
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
        return damage > charge;
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
        if (cursedKnown && cursed) {
            price /= 2;
        }
        if (levelKnown && level() > 0) {
            price *= (power() + 1);
        }
        if (price < 1) {
            price = 1;
        }
        return price;
    }

    private static final String SEAL            = "seal";
    private static final String CHARGE            = "charge";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SEAL, seal);
        bundle.put(CHARGE, charge);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        seal = (BrokenSeal) bundle.get(SEAL);
        charge = bundle.getFloat(CHARGE);
    }

    public static class Parry extends Buff {

        private Shield shield;

        @Override
        public boolean act() {
            if (shield == null) {
                spend(PARRY_DURATION);
            } else {
                spend(shield.parryTime());
            }
            detach();
            return true;
        }

        public void setShield(Shield shield) {
            this.shield = shield;
        }

        public int absorbDamage(Char.DamageSrc src, int damage) {
            detach();
            if (shield == null) {
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
        public int icon() {
            return BuffIndicator.ARMOR;
        }

        @Override
        public void tintIcon(Image icon) {
            super.tintIcon(icon);
            icon.hardlight(0.5f, 0.5f, 0.5f);
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

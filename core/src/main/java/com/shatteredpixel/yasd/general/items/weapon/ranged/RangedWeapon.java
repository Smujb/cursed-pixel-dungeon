package com.shatteredpixel.yasd.general.items.weapon.ranged;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.Attackable;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.items.weapon.ranged.ammo.Ammo;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.MissileSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class RangedWeapon extends Weapon implements Attackable {

    {
        //Ranged weapons scale with Assault
        statScaling = new ArrayList<>(Arrays.asList(Hero.HeroStat.ASSAULT));
    }

    //Regular throw speed is 240f. Arrows, bolts and bullets should be faster than that.
    protected float speed = 300f;

    @Override
    public void fx(Char attacker, int pos, Callback callback) {
        //Rather than directly calling the callback, call it when the missile hits the enemy
        ((MissileSprite)attacker.sprite.parent.recycle( MissileSprite.class )).
                reset( attacker.pos, pos, Reflection.newInstance(ammoClass()), callback, speed);
    }

    protected float damageMultiplier = 1f;

    @Override
    public int min(float lvl) {
        return Math.round(6 * lvl * damageMultiplier);   //level scaling
    }

    @Override
    public int max(float lvl) {
        return Math.round(12 * lvl * damageMultiplier);   //level scaling
    }

    private int MAX_AMMO = 4;
    private int curAmmo = MAX_AMMO;

    public abstract Class<? extends Ammo> ammoClass();

    @Override
    public boolean canReach(Char owner, int target) {
        return Ballistica.canHit(owner, target, Ballistica.PROJECTILE);
    }

    @Override
    public boolean use(Char enemy) {
        if (curUser != null && isEquipped(curUser) && canReach(curUser, enemy.pos)) {
            doAttack(curUser, enemy);
            return true;
        }
        return false;
    }

    @Override
    public boolean canUse(Char enemy) {
        return canReach(curUser, enemy.pos);
    }

    public static final String AMMO = "ammo";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(AMMO, curAmmo);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        curAmmo = bundle.contains(AMMO) ? bundle.getInt(AMMO) : MAX_AMMO;
        //Since these will replace missile weapons, must manually set 1 quantity to ensure that this doesn't carry over
        quantity = 1;
    }
}

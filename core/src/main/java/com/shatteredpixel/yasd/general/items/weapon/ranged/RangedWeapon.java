package com.shatteredpixel.yasd.general.items.weapon.ranged;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.PinCushion;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.Attackable;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.items.weapon.ranged.ammo.Ammo;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.MissileSprite;
import com.shatteredpixel.yasd.general.windows.WndBag;
import com.watabou.noosa.audio.Sample;
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

    private static final String AC_RELOAD = "reload";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_RELOAD);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_RELOAD)) {
            curWeapon = this;
            GameScene.selectItem(selector, WndBag.Mode.RANGED_WEAPON_AMMO, Messages.get(this, "select_ammo"));
        }
    }

    public static RangedWeapon curWeapon = null;

    private final WndBag.Listener selector = new WndBag.Listener() {
        @Override
        public void onSelect(Item item) {
            curWeapon = null;
            int amount;
            int req = MAX_AMMO - curAmmo;
            if (req <= 0) return;
            //If it's another ranged weapon of the same type, take ammo from it
            if (RangedWeapon.this.getClass().isInstance(item)) {
                amount = Math.min(((RangedWeapon)item).curAmmo, req);
                ((RangedWeapon)item).loseAmmo(amount);
            //Otherwise, if it's the correct type of ammo, reduce it's quantity
            } else if (RangedWeapon.this.ammoClass().isInstance(item)) {
                amount = Math.min(item.quantity(), req);
                if (item.quantity() == amount) {
                    item.detachAll(Dungeon.hero.belongings.backpack);
                } else if (item.quantity() > amount) {
                    item.split(amount);
                } else {
                    return;
                }
            //If it's neither the correct type of ammo nor a ranged weapon of the same type, do nothing
            } else {
                return;
            }
            doReload(amount);
        }
    };

    private void doReload(int amount) {
        curUser.spend(reloadTime);
        curUser.busy();

        Sample.INSTANCE.play(Assets.Sounds.TRAP);
        curUser.sprite.operate(curUser.pos);
        gainAmmo(amount);
    }

    @Override
    public String info() {
        String info = "\n\n";
        if (isIdentified()) {
            info += Messages.get(this, "stats_known", min(), max(), range, Math.round(reloadTime));
        } else {
            info += Messages.get(this, "stats_unknown", min(1), max(1), range, Math.round(reloadTime));
        }
        return desc() + info + upgradableItemDesc();
    }

    //Regular throw speed is 240f. Arrows, bolts and bullets should be faster than that.
    protected float speed = 300f;

    @Override
    public void fx(Char attacker, int pos, Callback callback) {
        //Rather than directly calling the callback, call it when the missile hits the enemy
        ((MissileSprite)attacker.sprite.parent.recycle( MissileSprite.class )).
                reset( attacker.pos, pos, Reflection.newInstance(ammoClass()), callback);
    }

    @Override
    protected boolean canAttack(int pos) {
        return super.canAttack(pos) && curAmmo > 0;
    }

    @Override
    public boolean doAttack(Char attacker, Char enemy) {
        loseAmmo(1);
        return super.doAttack(attacker, enemy);
    }

    @Override
    public boolean attack(Char attacker, Char enemy, boolean guaranteed) {
        PinCushion p = Buff.affect(enemy, PinCushion.class);
        Ammo projectile = Reflection.forceNewInstance(ammoClass());
        boolean attack = super.attack(attacker, enemy, guaranteed);
        if (attack && enemy.isAlive()) {
            p.stick(projectile);
        } else {
            Dungeon.level.drop(projectile, enemy.pos).sprite.drop();
        }
        return attack;
    }

    protected int range = 10;
    protected float damageMultiplier = 1f;

    protected float reloadTime = 1f;

    @Override
    public int min(float lvl) {
        return Math.round(8 * lvl * damageMultiplier);   //level scaling
    }

    @Override
    public int max(float lvl) {
        return Math.round(16 * lvl * damageMultiplier);   //level scaling
    }

    protected int MAX_AMMO = 8;
    protected int curAmmo = MAX_AMMO;

    public abstract Class<? extends Ammo> ammoClass();

    public void gainAmmo(int amt) {
        curAmmo += amt;
        updateQuickslot();
    }

    public void  loseAmmo(int amt) {
        curAmmo -= amt;
        updateQuickslot();
    }

    @Override
    public String status() {
        if (levelKnown) {
            return curAmmo + "/" + MAX_AMMO;
        } else {
            return null;
        }
    }

    @Override
    public boolean canReach(Char owner, int target) {
        return Ballistica.canHit(owner, target, Ballistica.PROJECTILE) && Dungeon.level.distance(owner.pos, target) <= range;
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
        return canReach(curUser, enemy.pos) && curAmmo > 0;
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

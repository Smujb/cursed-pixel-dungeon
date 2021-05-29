package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.shield.Shield;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

import org.jetbrains.annotations.NotNull;

public abstract class ParryBuff extends Buff {

    protected boolean parried = false;

    @Override
    public boolean act() {
        //See Hero.ready(), detaches the buff when the hero can move again.
        spend(1f);
        return true;
    }

    @Override
    public void detach() {
        super.detach();
        if (!parried) emptyCharge();
    }

    protected abstract void emptyCharge();

    @Override
    public boolean attachTo(@NotNull Char target) {
        if (target instanceof Mob && target.sprite != null) target.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(Shield.class, "parry_prep"));
        return super.attachTo(target);
    }

    public abstract int absorbDamage(Char.DamageSrc src, int damage);

    public abstract void affectEnemy(Char enemy, boolean parry);

    @Override
    public int icon() {
        return BuffIndicator.ARMOR;
    }

    @Override
    public void tintIcon(Image icon) {
        super.tintIcon(icon);
        icon.hardlight(0.5f, 0.5f, 0.5f);
    }

    public static final String PARRIED = "parried";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PARRIED, parried);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        parried = bundle.getBoolean(PARRIED);
    }
}

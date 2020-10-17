package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.watabou.noosa.Image;

public abstract class ParryBuff extends Buff {

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
}

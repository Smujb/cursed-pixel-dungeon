package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.shield.Shield;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.watabou.noosa.Image;

import org.jetbrains.annotations.NotNull;

public abstract class ParryBuff extends Buff {

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
}

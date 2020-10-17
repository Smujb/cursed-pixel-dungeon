package com.shatteredpixel.yasd.general.items.weapon.ranged;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.items.weapon.ranged.ammo.Ammo;
import com.shatteredpixel.yasd.general.items.weapon.ranged.ammo.Arrow;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.Callback;

public class Bow extends RangedWeapon {

    {
        image = ItemSpriteSheet.Ranged.BOW;

        hitSound = Assets.Sounds.HIT_ARROW;
    }

    @Override
    public void fx(Char attacker, int pos, Callback callback) {
        Sample.INSTANCE.play(Assets.Sounds.BOW_STRETCH);
        Game.scene().add(new Delayer(0.25f) {
            @Override
            protected void updateValues(float progress) {
                if (progress >= 1f) {
                    Bow.super.fx(attacker, pos, callback);
                }
            }
        });
    }

    @Override
    public Class<? extends Ammo> ammoClass() {
        return Arrow.class;
    }
}

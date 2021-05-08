package com.shatteredpixel.yasd.general.items.weapon.ranged;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.items.weapon.ranged.ammo.Ammo;
import com.shatteredpixel.yasd.general.items.weapon.ranged.ammo.Bolt;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class Crossbow extends RangedWeapon {

    {
        image = ItemSpriteSheet.Ranged.CROSSBOW;

        hitSound = Assets.Sounds.HIT_ARROW;
        hitSoundPitch = 0.7f;
        speed = 400f;

        reloadTime = 3f;
        curAmmo = MAX_AMMO = 6;
        damageFactor = 1.5f;
    }

    @Override
    public void fx(Char attacker, int pos, Callback callback) {
        Sample.INSTANCE.play(Assets.Sounds.ATK_CROSSBOW, 1f, 0.7f);
        super.fx(attacker, pos, callback);
    }

    @Override
    public Class<? extends Ammo> ammoClass() {
        return Bolt.class;
    }
}

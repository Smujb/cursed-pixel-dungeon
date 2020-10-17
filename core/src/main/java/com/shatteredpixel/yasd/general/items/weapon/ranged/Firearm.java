package com.shatteredpixel.yasd.general.items.weapon.ranged;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.effects.particles.SmokeParticle;
import com.shatteredpixel.yasd.general.items.weapon.ranged.ammo.Ammo;
import com.shatteredpixel.yasd.general.items.weapon.ranged.ammo.Bullet;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class Firearm extends RangedWeapon {

    {
        image = ItemSpriteSheet.Ranged.RIFLE;

        //Bullets are super fast.
        speed = 600f;
        range = 16;

        curAmmo = MAX_AMMO = 4;
        damageMultiplier = 1.25f;
    }

    @Override
    public void fx(Char attacker, int pos, Callback callback) {
        Sample.INSTANCE.play(Assets.Sounds.TRAP);
        attacker.sprite.emitter().burst(SmokeParticle.FACTORY, 4);
        super.fx(attacker, pos, callback);
    }

    @Override
    public Class<? extends Ammo> ammoClass() {
        return Bullet.class;
    }
}

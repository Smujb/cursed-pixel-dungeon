package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Sickle extends MeleeWeapon {

    {
        image = ItemSpriteSheet.Weapons.SICKLE;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1.2f;

        damageFactor = 0.70f;

        critModifier = 1.6f;

        statScaling.add(Hero.HeroStat.SUPPORT);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (Random.Int(2) == 0) {
            Buff.affect(defender, Bleeding.class).set(damage);
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "causes_bleed");
    }
}

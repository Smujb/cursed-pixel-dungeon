package com.shatteredpixel.yasd.general.items.weapon.ranged;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class ShredderCrossbow extends Crossbow {

    {
        image = ItemSpriteSheet.Ranged.SHREDDER_CROSSBOW;

        damageFactor = 0.2f;

        DLY = 0.4f;

        curAmmo = MAX_AMMO = 10;

        statScaling.add(Hero.HeroStat.SUPPORT);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        Buff.affect(defender, Bleeding.class).set(damage*1.5f);
        return super.proc(attacker, defender, damage);
    }
}

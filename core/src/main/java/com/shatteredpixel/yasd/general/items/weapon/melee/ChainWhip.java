package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class ChainWhip extends Whip {

    {
        image = ItemSpriteSheet.Weapons.CHAIN_WHIP;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1.5f;

        ACC = 0.8f;
        DLY = 1.5f;

        statScaling.add(Hero.HeroStat.RESILIENCE);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (Random.Int(2) == 0 && defender.buff(Paralysis.class) == null) {
            Buff.affect(defender, Paralysis.class, Paralysis.DURATION/2f);
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    protected boolean hasProperties() {
        return true;
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "causes_paralysis");
    }
}

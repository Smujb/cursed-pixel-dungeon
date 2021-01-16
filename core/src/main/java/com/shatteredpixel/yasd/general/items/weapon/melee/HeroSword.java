package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Bless;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class HeroSword extends MeleeWeapon {

    {
        image = ItemSpriteSheet.Weapons.HERO_SWORD;

        damageFactor = 2/3f;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (attacker.buff(Bless.class) != null) damage *= 2;
        return super.proc(attacker, defender, damage);
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + Messages.get(this, "bless_bonus");
    }
}

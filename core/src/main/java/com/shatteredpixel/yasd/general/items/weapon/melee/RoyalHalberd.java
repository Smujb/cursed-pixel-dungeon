package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class RoyalHalberd extends MeleeWeapon {

    {
        image = ItemSpriteSheet.Weapons.ROYAL_HALBERD;
        hitSound = Assets.Sounds.HIT_STAB;

        RCH = 2;
        damageFactor = 2/3f;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (!Dungeon.level.adjacent(attacker.pos, defender.pos)) {
            damage *= 2;
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "damage_range");
    }
}
